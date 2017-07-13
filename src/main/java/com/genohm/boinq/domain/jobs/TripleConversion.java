package com.genohm.boinq.domain.jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.query.GraphTemplate;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.repository.GraphTemplateRepository;
import com.genohm.boinq.repository.RawDataFileRepository;
import com.genohm.boinq.repository.TrackRepository;
import com.genohm.boinq.service.GraphTemplateBuilderService;
import com.genohm.boinq.service.MetaInfoService;
import com.genohm.boinq.service.MetadataGraphService;
import com.genohm.boinq.service.TripleUploadService;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.fileformats.TripleConverter;
import com.genohm.boinq.tools.fileformats.TripleIteratorFactory;
import com.genohm.boinq.tools.queries.Prefixes;

import htsjdk.samtools.SAMFileHeader;
import htsjdk.variant.vcf.VCFHeader;


public class TripleConversion implements AsynchronousJob {

	private Boolean interrupted = false;
	@Inject
	private TripleUploadService tripleUploadService;
	@Inject
	private TripleIteratorFactory tripleIteratorFactory;
	@Inject
	private TripleConverter tripleconverter;
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
	

	private int status = JOB_STATUS_UNKNOWN;
	private String name = "";
	private String description = "";
	private String errorDescription = "";
	private String mainType = "";
	private String subType = "";
	private Date startDate = null;
	private Date endDate = null;
	
	private RawDataFile inputData;
	
	public static final String SUPPORTED_EXTENSIONS[] = {"GFF", "GFF3", "BED", "VCF","SAM","BAM"};
	
	private static Logger log = LoggerFactory.getLogger(TripleConversion.class);
	
	public TripleConversion(RawDataFile inputData, String mainType, String subType) {
		// only use setters !
		// some stuff is initialized upon job launch
		this.inputData = inputData;
		this.description = "Triple conversion of "
				+ inputData.getFilePath() + " into track "
				+ inputData.getTrack().getId();
		this.name = this.description;
		this.mainType = mainType;
		this.subType = subType;
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
			Track track = inputData.getTrack();
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
			if (inputData.getStatus() == RawDataFile.STATUS_COMPLETE) {
				throw new Exception("Data is already uploaded");
			}
			File inputFile = new File(inputData.getFilePath());
		
			Metadata meta = new Metadata();
//			meta.filterCount = metainfoservice.getFileAttributeCount(track, TrackVocab.FilterCount.asNode());
//			meta.sampleCount = metainfoservice.getFileAttributeCount(track, TrackVocab.SampleCount.asNode());
//			meta.readCount = metainfoservice.getFileAttributeCount(track, TrackVocab.ReadCount.asNode());
//			meta.sumFilterCount = meta.filterCount;
//			meta.sumSampleCount = meta.sampleCount;
//			meta.sumReadCount = meta.readCount;
//			track.setEntryCount(metainfoservice.getFileAttributeCount(track, TrackVocab.EntryCount.asNode()));
//			track.setFeatureCount(metainfoservice.getFileAttributeCount(track, TrackVocab.FeatureCount.asNode()));
//			track.setTripleCount(metainfoservice.getFileAttributeCount(track, TrackVocab.TripleCount.asNode()));
//			trackRepository.save(track);
			
			// data needed: featureType for the track; referencemapping for the track
//			meta.fileType = track.getFileType();
//			meta.date = (new Date()).toString();
//			meta.fileName = inputFile.getName();
//			meta.file = inputFile.toString();
//			meta.sumFeatureCount= track.getFeatureCount();
//			meta.sumEntryCount = track.getEntryCount();
//			meta.organismMapping = track.getSpecies().replace(" ","_").toLowerCase() +"/"+ track.getAssembly() +"/";
//			meta.prefixLength = (track.getContigPrefix()==null)? 0:track.getContigPrefix().length();
			if("bed".equalsIgnoreCase(track.getFileType())){
				meta.mainType= NodeFactory.createURI(mainType);
				meta.typeList.add(meta.mainType);
				if (null != subType){
					meta.subType = NodeFactory.createURI(subType);
					meta.typeList.add(meta.subType);
				}
			}
			Iterator<Triple> tripleIterator = tripleIteratorFactory.getIterator(inputFile, track.getReferenceMap(), meta);
			TripleUploader uploader = tripleUploadService.getUploader(track, Prefixes.getCommonPrefixes());
			inputData.setStatus(RawDataFile.STATUS_LOADING);
			track.setStatus(Track.STATUS_PROCESSING);
			trackRepository.save(track);
			
			while (!interrupted && tripleIterator.hasNext()) {
				uploader.triple(tripleIterator.next());
//				meta.tripleCount++;
			}
			if (interrupted) throw new Exception("Triple conversion was interrupted by user");
			uploader.finish();
//			meta.featureCount=meta.sumFeatureCount-track.getFeatureCount();
//			meta.entryCount=meta.sumEntryCount-track.getEntryCount();
//			String metagraph = track.getDatasource().getMetaGraphName();
//			String endpoint = track.getDatasource().getEndpointUpdateUrl();
//			List<Triple> metadata =tripleconverter.createMetadata(meta,track.getGraphName());
//			metadataGraphService.updateFileConversion(endpoint, metagraph, metadata);

//			track.setEntryCount(metainfoservice.getFileAttributeCount(track, TrackVocab.EntryCount.asNode()));
//			track.setFeatureCount(metainfoservice.getFileAttributeCount(track, TrackVocab.FeatureCount.asNode()));
//			track.setTripleCount(metainfoservice.getFileAttributeCount(track, TrackVocab.TripleCount.asNode()));
//			metainfoservice.getSupportedFeatureTypes(track);
			inputData.setStatus(RawDataFile.STATUS_COMPLETE);
			rawDataFileRepository.save(inputData);
			
			if (otherFilesReady()) {
				GraphTemplate template = templateBuilder.fromBed(mainType, subType, track.getAssembly());
				template.setGraphIri(track.getGraphName());
				template = graphTemplateRepository.save(template);
				track.setGraphTemplate(template);
				track.setStatus(Track.STATUS_DONE);
				trackRepository.save(track);
			}
		} catch (Exception e) {
			setStatus(JOB_STATUS_ERROR);
			inputData.setStatus(RawDataFile.STATUS_ERROR);
			rawDataFileRepository.save(inputData);
			inputData.getTrack().setStatus(Track.STATUS_ERROR);
			// TODO: on track error, show rawdata status
			trackRepository.save(inputData.getTrack());
			this.errorDescription = e.getMessage();
			log.error(errorDescription);
		}
	}
	
	
	private Boolean otherFilesReady() {
		Set<RawDataFile> otherFilesNotReady = inputData.getTrack().getRawDataFiles().stream().filter(el -> {return (!el.equals(inputData) && el.getStatus()!=RawDataFile.STATUS_COMPLETE); }).collect(Collectors.toSet());
		return otherFilesNotReady.isEmpty();
	}
	
	@Override
	public void kill() {
		this.interrupted = true;
	}
	
	public static class Metadata{
		public int prefixLength;
		public long tripleCount;
		public long entryCount;
		public long featureCount;
		public long filterCount;
		public long sampleCount;
		public long readCount;
		public long sumFeatureCount;
		public long sumEntryCount;
		public long sumFilterCount;
		public long sumSampleCount;
		public long sumReadCount;
		public Node mainType;
		public Node subType;
		public String date = new String();
		public String user = new String();
		public String fileType = new String();
		public String fileName = new String();
		public String file = new String();
		public String organismMapping = new String();
		public List<String> gffHeader = new ArrayList<String>();
		public List<String> bedHeader = new ArrayList<String>();
		public List<Node> typeList = new ArrayList<Node>();
		public Map<String,Node>featureIDmap = new HashMap<>();
		public Map<String,Node> filterMap = new HashMap<>();
		public Map<String,Node> sampleMap = new HashMap<>();
		public Map<String,Node> readMap = new HashMap<>();
		public Map<String,Node> referenceMap = new HashMap<>();
		public Map<String,String> formatMap = new HashMap<>();
		public VCFHeader vcfHeader = new VCFHeader();
		public SAMFileHeader samHeader = new SAMFileHeader();
		
	}

	@Override
	public Long getDuration() {
		return endDate.getTime() - startDate.getTime();	
	}
	

	
}
