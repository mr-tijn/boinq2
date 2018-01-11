package org.boinq.domain.jobs;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.boinq.domain.Datasource;
import org.boinq.domain.RawDataFile;
import org.boinq.domain.Track;
import org.boinq.domain.query.GraphTemplate;
import org.boinq.generated.vocabularies.SoVocab;
import org.boinq.repository.GraphTemplateRepository;
import org.boinq.repository.RawDataFileRepository;
import org.boinq.repository.TrackRepository;
import org.boinq.service.GraphTemplateBuilderService;
import org.boinq.service.MetaInfoService;
import org.boinq.service.MetadataGraphService;
import org.boinq.service.TripleUploadService;
import org.boinq.service.TripleUploadService.TripleUploader;
import org.boinq.tools.Counter;
import org.boinq.tools.fileformats.MetaTripleBuilder;
import org.boinq.tools.fileformats.TripleIteratorFactory;
import org.boinq.tools.queries.Prefixes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import htsjdk.samtools.SAMFileHeader;
import htsjdk.variant.vcf.VCFHeader;


public class TripleConversion implements AsynchronousJob {

	private Boolean interrupted = false;
	@Inject
	private TripleUploadService tripleUploadService;
	@Inject
	private TripleIteratorFactory tripleIteratorFactory;
	@Inject
	private RawDataFileRepository rawDataFileRepository;
	@Inject
	private GraphTemplateRepository graphTemplateRepository;
	@Inject
	MetadataGraphService metadataGraphService;
	@Inject 
	MetaInfoService metainfoservice;
    @Inject
    private TrackRepository trackRepository;
    @Inject
    private GraphTemplateBuilderService templateBuilder;
    @Inject
    private MetaTripleBuilder metaTripleBuilder;
    
	

	private int status = JOB_STATUS_UNKNOWN;
	private String name = "";
	private String description = "";
	private String errorDescription = "";
	private String mainType = "";
	private String subType = "";
	private String attributeType = "";
	private Date startDate = null;
	private Date endDate = null;
	
	private Track track;
	
	public static final String SUPPORTED_EXTENSIONS[] = {"GFF", "GFF3", "BED", "VCF","SAM","BAM"};
	
	private static Logger log = LoggerFactory.getLogger(TripleConversion.class);
	
	public TripleConversion(Track track, String mainType, String subType, String attributeType) {
		// only use setters !
		// some stuff is initialized upon job launch
		this.track = track;
		this.description = "Triple conversion of track "
				+ track.getId();
		this.name = this.description;
		this.mainType = mainType;
		this.subType = subType;
		this.attributeType = attributeType;
	}

	@Override
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public String getErrorDescription() {
		return errorDescription;
	}

	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	

	@Override
	@Transactional
	public void execute() {
		try {
			if (track == null) {
				throw new Exception("Track is null");
			}
			Datasource datasource = track.getDatasource();
			if (datasource == null) {
				throw new Exception("Datasource is null");				
			}
			if (Datasource.TYPE_LOCAL_FALDO != datasource.getType()) {
				throw new Exception("Datasource should be of type local faldo in order to support upload");
			}
			Counter attributeCounter = new Counter(0);
			List<Triple> metaData = new LinkedList<>();
			List<Node> types = new LinkedList<>();
			for (RawDataFile inputData: track.getRawDataFiles()) {
			
				if (inputData.getStatus() == RawDataFile.STATUS_COMPLETE) {
					continue;
				}
				File inputFile = new File(inputData.getFilePath());
				Metadata meta = new Metadata();
				meta.entryCount = 0;
				meta.featureCount = 0;
				meta.tripleCount = 0;
				meta.fileType = track.getFileType();
				meta.date = (new Date()).toString();
				meta.fileName = inputFile.getName();
				meta.file = inputFile.toString();
				//			meta.organismMapping = track.getSpecies().replace(" ","_").toLowerCase() +"/"+ track.getAssembly() +"/";
				meta.prefixLength = (track.getContigPrefix()==null)? 0:track.getContigPrefix().length();
				if("bed".equalsIgnoreCase(track.getFileType())){
					if (null == mainType || mainType.length() == 0) {
						meta.mainType = SoVocab.region.asNode();
					} else {
						meta.mainType= NodeFactory.createURI(mainType);
					}
					meta.typeList.add(meta.mainType);
					if (null != subType && subType.length() > 0){
						meta.subType = NodeFactory.createURI(subType);
						meta.typeList.add(meta.subType);
					}
					if (null == attributeType || attributeType.length() == 0) {
						meta.scoreType = NodeFactory.createURI(attributeType);
					} else {
						meta.scoreType = SoVocab.score.asNode();
					}
				}
				Iterator<Triple> tripleIterator = tripleIteratorFactory.getIterator(inputFile, track.getReferenceMap(), meta);
				TripleUploader uploader = tripleUploadService.getUploader(track, Prefixes.getCommonPrefixes());
				inputData.setStatus(RawDataFile.STATUS_LOADING);
				rawDataFileRepository.save(inputData);
				track.setStatus(Track.STATUS_PROCESSING);
				trackRepository.save(track);

				while (!interrupted && tripleIterator.hasNext()) {
					uploader.triple(tripleIterator.next());
					meta.tripleCount++;
				}
				if (interrupted) {
					inputData.setStatus(RawDataFile.STATUS_ERROR);
					throw new Exception("Triple conversion was interrupted by user while processing " + inputData.getFilePath());
				}
				uploader.finish();
				types.addAll(meta.typeList);
				metaData.addAll(metaTripleBuilder.createMetadata(inputFile.toString(), meta, track.getGraphName(), attributeCounter));
				track.setEntryCount(track.getEntryCount() + meta.entryCount);
				track.setFeatureCount(track.getFeatureCount() + meta.featureCount);
				track.setTripleCount(track.getTripleCount() + meta.tripleCount);
				//			metainfoservice.getSupportedFeatureTypes(track);
				inputData.setStatus(RawDataFile.STATUS_COMPLETE);
				rawDataFileRepository.save(inputData);
			}
			metaData.addAll(metaTripleBuilder.createGeneralMetadata(track, types, track.getGraphName(), attributeCounter));
			String metagraph = track.getDatasource().getMetaGraphName();
			String endpoint = track.getDatasource().getEndpointUpdateUrl();
			TripleUploader uploader = tripleUploadService.getUploader(endpoint, metagraph);
			for (Triple meta: metaData) {
				uploader.triple(meta);
			}
			uploader.finish();
			GraphTemplate template = templateBuilder.fromBed(mainType, subType, track.getAssembly());
			template.setGraphIri(track.getGraphName());
			template = graphTemplateRepository.save(template);
			track.setGraphTemplate(template);
			track.setStatus(Track.STATUS_DONE);
			trackRepository.save(track);
		} catch (Exception e) {
			setStatus(JOB_STATUS_ERROR);
			track.setStatus(Track.STATUS_ERROR);
			trackRepository.save(track);
			this.errorDescription = e.getMessage();
			log.error(errorDescription);
		}
	}
	
	
	

	
	
	@Override
	public void kill() {
		this.interrupted = true;
	}
	
	public static class Metadata{
		public int prefixLength;
		public long tripleCount;
		public long featureCount;
		public long entryCount;
		public long filterCount;
		public long sampleCount;
		public long readCount;
		public Node mainType;
		public Node subType;
		public Node scoreType;
		public String date = new String();
		public String user = new String();
		public String fileType = new String();
		public String file = new String();
		public String fileName = new String();
		public String organismMapping = new String();
		public List<String> gffHeader = new LinkedList<>();
		public List<String> bedHeader = new LinkedList<>();
		public Set<Node> typeList = new HashSet<>();
		public Map<String,Node>featureIDmap = new HashMap<>();
		public Map<String,Node> filterMap = new HashMap<>();
		public Map<String,Node> sampleMap = new HashMap<>();
		public Map<String,Node> readMap = new HashMap<>();
		public Map<String,Node> referenceMap = new HashMap<>();
		public Map<String,String> formatMap = new HashMap<>();
		public VCFHeader vcfHeader = new VCFHeader();
		public SAMFileHeader samHeader = new SAMFileHeader();
		public Boolean initialize = true;
	}

	@Override
	public Long getDuration() {
		return endDate.getTime() - startDate.getTime();	
	}
	

	
}
