package org.boinq.tools.fileformats;

import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDboolean;
import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDdouble;
import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDfloat;
import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDint;
import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDstring;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.boinq.domain.jobs.TripleConversion.Metadata;
import org.boinq.generated.vocabularies.FormatVocab;
import org.boinq.generated.vocabularies.GfvoVocab;
import org.boinq.generated.vocabularies.SoVocab;
import org.boinq.service.TripleGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import htsjdk.tribble.FeatureCodecHeader;
import htsjdk.tribble.readers.AsciiLineReader;
import htsjdk.tribble.readers.AsciiLineReaderIterator;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFCodec;
import htsjdk.variant.vcf.VCFFilterHeaderLine;
import htsjdk.variant.vcf.VCFFormatHeaderLine;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFInfoHeaderLine;

public class VCFTripleIterator extends TripleBuilder implements Iterator<Triple>  {
	
	public static final String FILTERBASEURI = "/resource/filter#";
	public static final String SAMPLEBASEURI = "/resource/sample#";
	public static final String GENOTYPEBASEURI = "/resource/genotype#";
	public static final String INFOBASEURI = "/resource/info#";
	
	public static Map<String, Node> infoAttributeTypes;
	public static Map<String, XSDDatatype> infoAttributeValueTypes;

	private static Logger log = LoggerFactory.getLogger(VCFTripleIterator.class);
	private AsciiLineReaderIterator lineIterator;
	private List<Triple> currentTriples = new LinkedList<Triple>();
	private VCFCodec codec = new VCFCodec();
	private Metadata meta;
	
	public VCFTripleIterator(TripleGeneratorService tripleGenerator, File file, Metadata meta) throws FileNotFoundException, IOException {
		super(tripleGenerator);
		lineIterator = new AsciiLineReaderIterator(AsciiLineReader.from(new FileInputStream(file)));
		try {
			initializeVCFMetadata(currentTriples, meta);
		} catch (Exception e) {
			log.error("Malformed Header" , e);
		}
       this.meta = meta;
	}

	static {
		
		//common info fields from specification
		infoAttributeTypes = new HashMap<>();
		infoAttributeTypes.put("AA", GfvoVocab.Ancestral_Sequence.asNode());
		infoAttributeTypes.put("AC", GfvoVocab.Allele_Count.asNode());
		infoAttributeTypes.put("AF", GfvoVocab.Allele_Frequency.asNode());
		infoAttributeTypes.put("AN", GfvoVocab.Total_Number_of_Alleles.asNode());
		infoAttributeTypes.put("BQ", GfvoVocab.Base_Quality.asNode());
		infoAttributeTypes.put("CIGAR", GfvoVocab.Sequence_Alignment.asNode()); // elaborate
		infoAttributeTypes.put("DB", GfvoVocab.External_Reference.asNode());
		infoAttributeTypes.put("DP", GfvoVocab.Coverage.asNode());
		infoAttributeTypes.put("H2", GfvoVocab.External_Reference.asNode());
		infoAttributeTypes.put("H3", GfvoVocab.External_Reference.asNode());
		infoAttributeTypes.put("MQ", GfvoVocab.Mapping_Quality.asNode());
		infoAttributeTypes.put("MQ0", GfvoVocab.Number_of_Reads.asNode());
		infoAttributeTypes.put("NS", GfvoVocab.Sample_Count.asNode());
		infoAttributeTypes.put("SB", GfvoVocab.Note.asNode());
		infoAttributeTypes.put("SOMATIC", GfvoVocab.Somatic_Cell.asNode());
		infoAttributeTypes.put("VALIDATED", GfvoVocab.Experimental_Method.asNode());

		infoAttributeValueTypes = new HashMap<>();
		infoAttributeValueTypes.put("AA", XSDstring);
		infoAttributeValueTypes.put("AC", XSDint);
		infoAttributeValueTypes.put("AF", XSDfloat);
		infoAttributeValueTypes.put("AN", XSDint);
		infoAttributeValueTypes.put("BQ", XSDfloat);
		infoAttributeValueTypes.put("CIGAR", XSDstring);
		infoAttributeValueTypes.put("DB", XSDboolean);
		infoAttributeValueTypes.put("DP", XSDint);
		infoAttributeValueTypes.put("H2", XSDboolean);
		infoAttributeValueTypes.put("H3", XSDboolean);
		infoAttributeValueTypes.put("MQ", XSDfloat);
		infoAttributeValueTypes.put("MQ0", XSDfloat);
		infoAttributeValueTypes.put("NS", XSDfloat);
		infoAttributeValueTypes.put("SB", XSDstring);
		infoAttributeValueTypes.put("SOMATIC", XSDboolean);
		infoAttributeValueTypes.put("VALIDATED", XSDboolean);
		// END is missing
		
		// more can be found here:
		// https://software.broadinstitute.org/gatk/documentation/tooldocs/3.8-0/org_broadinstitute_gatk_tools_walkers_annotator_LowMQ.php#AnnotationModules
		// https://wiki.nci.nih.gov/display/TCGA/TCGA+Variant+Call+Format+%28VCF%29+Specification
	}
	
	
	public void initializeVCFMetadata(List<Triple> triples, Metadata meta) throws IOException {
		FeatureCodecHeader header = codec.readHeader(lineIterator);
		meta.vcfHeader = (VCFHeader) header.getHeaderValue();
		for (VCFFilterHeaderLine headerLine : meta.vcfHeader.getFilterLines()) {
			String filterName = FILTERBASEURI + headerLine.getID();
			Node filter = tripleGenerator.generateURI(filterName);
			meta.filterMap.put(headerLine.getID(), filter);
			triples.add(new Triple(filter, RDF.type.asNode(), GfvoVocab.Variant_Calling.asNode()));
			triples.add(new Triple(filter, RDFS.label.asNode(), NodeFactory.createLiteral(headerLine.getID())));
			if (headerLine.getDescription() != null) {
				triples.add(new Triple(filter, RDFS.comment.asNode(), NodeFactory.createLiteral(headerLine.getDescription())));
			}
		}
		for (String name: meta.vcfHeader.getSampleNamesInOrder()) {
			String sampleName = SAMPLEBASEURI + ++meta.sampleCount;
			Node sample = tripleGenerator.generateURI(sampleName);
			meta.sampleMap.put(name, sample);
			triples.add(new Triple(sample, RDF.type.asNode(), GfvoVocab.Biological_Entity.asNode()));
			triples.add(new Triple(sample, RDF.type.asNode(), GfvoVocab.Sample.asNode()));
			triples.add(new Triple(sample, RDFS.label.asNode(), NodeFactory.createLiteral(name)));
		}
		Map<String, XSDDatatype> typeMap = new HashMap<>();
		typeMap.put("Integer", XSDint);
		typeMap.put("Float", XSDdouble);
		typeMap.put("Character", XSDstring);
		typeMap.put("String", XSDstring);
		typeMap.put("Flag", XSDboolean); //check:
		for (VCFFormatHeaderLine formatHeader : meta.vcfHeader.getFormatHeaderLines()) {
			String genoTypeName = GENOTYPEBASEURI + formatHeader.getID();
			Node genotype = tripleGenerator.generateURI(genoTypeName);
			meta.genotypeMap.put(formatHeader.getID(), genotype);
			meta.genotypeTypeMap.put(formatHeader.getID(), typeMap.get(formatHeader.getType()));
			triples.add(new Triple(genotype, RDF.type.asNode(), GfvoVocab.Genotype.asNode()));
			triples.add(new Triple(genotype, RDFS.label.asNode(), NodeFactory.createLiteral(formatHeader.getID())));
			if (formatHeader.getDescription() != null) {
				triples.add(new Triple(genotype, RDFS.comment.asNode(), NodeFactory.createLiteral(formatHeader.getDescription())));
			}
		}
		for (VCFInfoHeaderLine infoHeader: meta.vcfHeader.getInfoHeaderLines()) {
			String infoName = INFOBASEURI + infoHeader.getID();
			Node info = tripleGenerator.generateURI(infoName);
			meta.infoMap.put(infoHeader.getID(), info);
			meta.infoTypeMap.put(infoHeader.getID(), typeMap.get(infoHeader.getType()));
			triples.add(new Triple(info, RDFS.label.asNode(), NodeFactory.createLiteral(infoHeader.getID())));
			if (infoHeader.getDescription() != null) {
				triples.add(new Triple(info, RDFS.comment.asNode(), NodeFactory.createLiteral(infoHeader.getDescription())));
			}
		}
	}
	
	public List<Triple> convert(VariantContext vcfEntry, int start, Metadata meta) {
		// TODO: CONTIG
		// TODO: ONTOLOGIES
		List<Triple> triples = new LinkedList<Triple>();
		int attributeCount = 1;
		// VCFentry
		String VCFentryName = ENTRYBASEURI + meta.entryCount;
		Node VCFentry = tripleGenerator.generateURI(VCFentryName);
		triples.add(new Triple(VCFentry, RDF.type.asNode(), FormatVocab.VCF_Entry.asNode()));

		// FEATURE
		String featureName = FEATUREBASEURI + ++meta.featureCount;
		Node feature = tripleGenerator.generateURI(featureName);
		triples.add(new Triple(VCFentry, FormatVocab.defines.asNode(), feature));
		triples.add(new Triple(feature, RDF.type.asNode(), GfvoVocab.Feature.asNode()));

		if (vcfEntry.getID() != ".") {
			idGenerator(triples, feature, featureName, vcfEntry.getID());
			triples.add(new Triple(feature, DCTerms.identifier.asNode(), NodeFactory.createLiteral(vcfEntry.getID(),XSDstring)));
		}
		
		for (String filter : vcfEntry.getFilters()) {
			filter = filter.trim();
			if (meta.filterMap.get(filter) == null) {
				String filterName = FILTERBASEURI + ++meta.filterCount;
				Node filterNode = tripleGenerator.generateURI(filterName);
				triples.add(new Triple(filterNode, RDF.type.asNode(), GfvoVocab.Variant_Calling.asNode()));
				triples.add(new Triple(filterNode, RDFS.label.asNode(), NodeFactory.createLiteral(filter, XSDstring)));
				meta.filterMap.put(filter, filterNode);
			}
			triples.add(new Triple(feature, GfvoVocab.is_refuted_by.asNode(), meta.filterMap.get(filter)));
		}
		
		attributeCount = addAlleleTriples(triples, feature, featureName, vcfEntry, attributeCount);
		attributeCount = addInfoTriples(triples, feature, featureName, vcfEntry.getAttributes(), attributeCount);
		addGenotypeTriples(triples, feature, featureName, vcfEntry.getGenotypes(), meta);
		
		switch (vcfEntry.getType()) {
		case INDEL:
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.indel.asNode()));
			meta.typeList.add(SoVocab.indel.asNode());
			break;
		case MIXED:
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.insertion.asNode()));
			meta.typeList.add(SoVocab.insertion.asNode());
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.deletion.asNode()));
			meta.typeList.add(SoVocab.deletion.asNode());
			break;
		case MNP:
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.MNP.asNode()));
			meta.typeList.add(SoVocab.MNP.asNode());
			break;
		case NO_VARIATION:
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.nucleotide_match.asNode()));
			meta.typeList.add(SoVocab.nucleotide_match.asNode());
			break;
		case SNP:
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.SNP.asNode()));
			meta.typeList.add(SoVocab.SNP.asNode());
			break;
		case SYMBOLIC:
			break;
		default:
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.SNP.asNode()));
			meta.typeList.add(SoVocab.SNP.asNode());
			break;
		}

		// LOCATION
		super.addFaldoTriples(feature, Long.valueOf(vcfEntry.getStart()), Long.valueOf(vcfEntry.getEnd()), vcfEntry.getContig(), true, triples, meta);

		return triples;
	}
	
	
	private void idGenerator(List<Triple> triples, Node feature, String featureName, String ID) {
		Node idNode = tripleGenerator.generateURI(featureName + "_ID");
		triples.add(new Triple(feature, GfvoVocab.has_identifier.asNode(), idNode));
		triples.add(new Triple(idNode, RDF.type.asNode(), GfvoVocab.Identifier.asNode()));
		triples.add(new Triple(idNode, RDF.value.asNode(), NodeFactory.createLiteral(ID)));
	}
	
	private int addAlleleTriples(List<Triple> triples, Node feature, String featureName, VariantContext variant,
			int attributeCount) {

		if (variant.getReference() != null && variant.getReference().getBaseString() != null) {
			generateAttribute(triples, feature, featureName, variant.getReference().getBaseString(), XSDstring,
					GfvoVocab.Reference_Sequence.asNode(), attributeCount++);
		}
		for (Allele all : variant.getAlternateAlleles()) {
			if (all.getBaseString() != null) {
				String attributeName = generateAttribute(triples, feature, featureName, all.getBaseString(),
						XSDstring, GfvoVocab.Sequence_Variant.asNode(), attributeCount++);
				Node attributeNameNode = tripleGenerator.generateURI(attributeName);
				generateAttribute(triples, attributeNameNode, attributeName,
						String.valueOf(variant.getPhredScaledQual()), XSDdouble, GfvoVocab.Phred_Score.asNode(), 1);

			}
		}
		return attributeCount;
	}

	private int addInfoTriples(List<Triple> triples, Node feature, String featureName, Map<String, Object> keyValues, int attributeCount) {
		for (String key : keyValues.keySet()) {
			String attributeName = featureName + "/attribute_" + attributeCount++;
			Node attributeNode = tripleGenerator.generateURI(attributeName);
			triples.add(new Triple(feature, GfvoVocab.has_attribute.asNode(), attributeNode));

			if (infoAttributeTypes.get(key) != null) {
				// reserved info fields
				Node attributeType = infoAttributeTypes.get(key);
				if (attributeType.equals(GfvoVocab.External_Reference.asNode())) {
					triples.add(new Triple(feature, RDFS.seeAlso.asNode(), NodeFactory.createLiteral(keyValues.get(key).toString())));
				}
				triples.add(new Triple(attributeNode, RDF.type.asNode(), attributeType));
				XSDDatatype attributeValueType = infoAttributeValueTypes.get(key);
				if (attributeValueType.equals(XSDboolean)) {
					// why is this ?
					triples.add(new Triple(attributeNode, RDF.value.asNode(), NodeFactory.createLiteral(key, XSDstring)));
				} else {
					triples.add(new Triple(attributeNode, RDF.value.asNode(), NodeFactory.createLiteral(keyValues.get(key).toString(), attributeValueType)));
				}
			} else if (meta.infoMap.get(key) != null) {
				// unknown info field but described in header
				Node attributeType = meta.infoMap.get(key);
				triples.add(new Triple(attributeNode, RDF.type.asNode(), attributeType));
				XSDDatatype attributeValueType = meta.infoTypeMap.get(key);
				if (attributeValueType.equals(XSDboolean)) {
					// why is this ?
					triples.add(new Triple(attributeNode, RDF.value.asNode(), NodeFactory.createLiteral(key, XSDstring)));
				} else {
					triples.add(new Triple(attributeNode, RDF.value.asNode(), NodeFactory.createLiteral(keyValues.get(key).toString(), attributeValueType)));
				}
			} else {
				// unknown info field, not described in header
				Node attributeType = NodeFactory.createBlankNode();
				triples.add(new Triple(attributeNode, RDF.value.asNode(), NodeFactory.createLiteral(keyValues.get(key).toString(), XSDstring)));
				triples.add(new Triple(attributeNode, RDF.type.asNode(), attributeType));
				triples.add(new Triple(attributeType, RDFS.label.asNode(), NodeFactory.createLiteral(key, XSDstring)));
			}
		}
		return attributeCount;
	}

	
	private void addGenotypeTriples(List<Triple> triples, Node feature, String featureName, Iterable<Genotype> iterable, Metadata meta) {
		int index = 1;
		for (Genotype evidence : iterable) {
			String evidenceName = featureName + "/evidence_" + index++;
			Node evidenceNode = tripleGenerator.generateURI(evidenceName);
			triples.add(new Triple(feature, GfvoVocab.has_evidence.asNode(), evidenceNode));
			if (meta.sampleMap.get(evidence.getSampleName()) != null) {
				triples.add(new Triple(evidenceNode, GfvoVocab.has_source.asNode(), meta.sampleMap.get(evidence.getSampleName())));
			}

			String genotypeName = evidenceName + "/genotype";
			Node genotype = tripleGenerator.generateURI(genotypeName);
			triples.add(new Triple(evidenceNode, GfvoVocab.has_attribute.asNode(), genotype));
			triples.add(new Triple(genotype, RDF.type.asNode(), GfvoVocab.Genotype.asNode()));

			switch (evidence.getType()) {
			case HET:
				triples.add(new Triple(genotype, GfvoVocab.has_quality.asNode(), GfvoVocab.Heterozygous.asNode()));
				break;
			case HOM_VAR:
			case HOM_REF:
				triples.add(new Triple(genotype, GfvoVocab.has_quality.asNode(), GfvoVocab.Homozygous.asNode()));
				break;
			case MIXED:
			case NO_CALL:
			case UNAVAILABLE:
				break;
			}
			int index1 = 1;

			if (evidence.getGQ() != -1) {
				generateAttribute(triples, genotype, genotypeName, String.valueOf(evidence.getGQ()), XSDint, GfvoVocab.Conditional_Genotype_Quality.asNode(), index1++);
			}
			if (evidence.getDP() != -1) {
				generateAttribute(triples, genotype, genotypeName, String.valueOf(evidence.getDP()), XSDint, GfvoVocab.Coverage.asNode(), index1++);
			}
			for (Allele allele: evidence.getAlleles()) {
				if (allele.isReference()) {
					generateAttribute(triples, genotype, genotypeName, allele.getBaseString(), XSDstring, GfvoVocab.Reference_Sequence.asNode(), index1++);
				} else {
					generateAttribute(triples, genotype, genotypeName, allele.getBaseString(), XSDstring, GfvoVocab.Sequence_Variant.asNode(), index1++);
				}
			}
			
			if (evidence.getFilters() != null) {
				for (String filter : evidence.getFilters().split(";")) {
					filter = filter.trim();
					if (meta.filterMap.get(filter) == null) {
						String filterName = FILTERBASEURI + ++meta.filterCount;
						Node filterNode = tripleGenerator.generateURI(filterName);
						triples.add(new Triple(filterNode, RDF.type.asNode(), GfvoVocab.Variant_Calling.asNode()));
						triples.add(new Triple(filterNode, RDFS.label.asNode(), NodeFactory.createLiteral(filter, XSDstring)));
						meta.filterMap.put(filter, filterNode);
					}
					triples.add(new Triple(feature, GfvoVocab.is_refuted_by.asNode(), meta.filterMap.get(filter)));
				}
			}

		}

	}

	@Override
	public boolean hasNext() {
		if (currentTriples == null) return false;
		if (currentTriples.isEmpty()){
			return lineIterator.hasNext();
		} else{
			return true;
		}
	}

	@Override
	public Triple next() {
		if (currentTriples.isEmpty()){
			
			VariantContext record = codec.decode(lineIterator.next());
			while (record==null){
				record = codec.decode(lineIterator.next());
			}
			meta.entryCount++;
			currentTriples.addAll(convert(record, record.getStart(), meta));
		}
		return currentTriples.remove(0);
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}

