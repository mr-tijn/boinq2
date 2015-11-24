package com.genohm.boinq.tools.fileformats;


import htsjdk.samtools.AlignmentBlock;
import htsjdk.samtools.SAMRecord;
import htsjdk.tribble.annotation.Strand;
import htsjdk.tribble.bed.BEDFeature;
import htsjdk.tribble.bed.FullBEDFeature.Exon;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.GenotypeType;
import htsjdk.variant.variantcontext.GenotypesContext;
import htsjdk.variant.variantcontext.VariantContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.faldo.FaldoFeature;
import com.genohm.boinq.generated.vocabularies.BoinqVocab;
import com.genohm.boinq.generated.vocabularies.FaldoVocab;
import com.genohm.boinq.generated.vocabularies.GfvoVocab;
import com.genohm.boinq.generated.vocabularies.SioVocab;
import com.genohm.boinq.generated.vocabularies.SoVocab;
import com.genohm.boinq.service.TripleGeneratorService;

import static com.genohm.boinq.generated.vocabularies.FaldoVocab.*;
import static org.apache.jena.datatypes.xsd.XSDDatatype.*;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.datatypes.xsd.impl.XSDDouble;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import edu.unc.genomics.BedEntry;
//import edu.unc.genomics.BedEntry;
import edu.unc.genomics.GFFEntry;
import edu.unc.genomics.ValuedInterval;

@Service
public class TripleConverter {
	// genohm.com / bed-gff-bedgraph??? / feature ------> datasource 

	// all variable names that are not included in the valuedInterval variables keep their original variable names
	// the variable names that are included in the valuedInterval are given a more generic name

	// vInt static finals
	public static final String FEATUREBASEURI ="/feature#";
	public static final String FEATUREBEGINURI = "/feature_begin#";
	public static final String FEATUREENDURI = "/feature_end#";
	public static final String FEATUREPOSURI = "/position#";
	public static final String CHRBASEURI = "http://www.genohm.com/chr";
	public static final String VALUEURI = "http://www.genohm.com/value";

	
	// todo: manage these terms through ontology
	// gff static finals
	public static final String SOURCEURI = "http://www.genohm.com/datasource/source";
	public static final String ATTRIBUTEURI ="http://www.genohm.com/datasource/attribute";
	public static final String FRAMEURI = "http://www.genohm.com/datasource/frame";
	public static final String TYPEURI = "http://www.genohm.com/datasource/type";

	// bed static finals
	public static final String NAMEURI = "http://www.genohm.com/datasource/name";
	public static final String THICKSTARTURI = "http://www.genohm.com/datasource/thickstart";
	public static final String THICKENDURI = "http://www.genohm.com/datasource/thickend";
	public static final String ITEMRBGURI = "http://www.genohm.com/datasource/itemrbg";
	public static final String BLOCKBASEURI = "http://www.genohm.com/datasource/block#";
	public static final String BLOCKURI = "http://www.genohm.com/datasource/block";
	public static final String BLOCKCOUNTURI = "http://www.genohm.com/datasource/blockcount";
	public static final String BLOCKSIZEURI = "http://www.genohm.com/datasource/blocksize";
	public static final String BLOCKSTARTURI = "http://www.genohm.com/datasource/blockstart";
	private static int blockCounter = 0;
	private static String chrValue;
	private Map<String, Node> attributeNodes;
	private Map<String, XSDDatatype> attributeTypeNodes;
	private Map<String, Node> featureTypeNodes;
	
	@Inject
	TripleGeneratorService tripleGenerator;
	
	public TripleConverter() {
		List<String> typeList = new ArrayList<String>();
		
		attributeNodes = new HashMap<>();
		attributeNodes.put("AA", GfvoVocab.Ancestral_Sequence.asNode());
		attributeNodes.put("AC", GfvoVocab.Allele_Count.asNode());
		attributeNodes.put("AF", GfvoVocab.Allele_Frequency.asNode());
		attributeNodes.put("AN", GfvoVocab.Total_Number_of_Alleles.asNode());
		attributeNodes.put("BaseQRankSum", BoinqVocab.Base_Quality_Rank_Sum.asNode());
		attributeNodes.put("BQ", GfvoVocab.Base_Quality.asNode());
		attributeNodes.put("CIGAR", GfvoVocab.Forward_Reference_Sequence_Frameshift.asNode());
		attributeNodes.put("ClippingRankSum", BoinqVocab.Clipping_Rank_Sum.asNode());
		attributeNodes.put("DB", GfvoVocab.External_Reference.asNode());
		attributeNodes.put("DP", GfvoVocab.Coverage.asNode());
		attributeNodes.put("FS", BoinqVocab.Fisher_Strand.asNode());
		attributeNodes.put("H2", GfvoVocab.External_Reference.asNode());
		attributeNodes.put("H3", GfvoVocab.External_Reference.asNode());
		attributeNodes.put("H3", GfvoVocab.External_Reference.asNode());
		attributeNodes.put("MLEAC", BoinqVocab.Maximum_Likelihood_Expectation_Allele_Count.asNode());
		attributeNodes.put("MLEAF", BoinqVocab.Maximum_Likelihood_Expectation_Allele_Frequency.asNode());
		attributeNodes.put("MQ", GfvoVocab.Mapping_Quality.asNode());
		attributeNodes.put("MQ0", GfvoVocab.Number_of_Reads.asNode());
		attributeNodes.put("MQRankSum", BoinqVocab.Mapping_Quality_Rank_Sum.asNode());
		attributeNodes.put("NS", GfvoVocab.Sample_Count.asNode());
		attributeNodes.put("QD", BoinqVocab.Quality_By_Depth.asNode());
		attributeNodes.put("ReadPosRankSum", BoinqVocab.Read_Position_Rank_Sum.asNode());
		attributeNodes.put("SB", GfvoVocab.Note.asNode());
		attributeNodes.put("SOMATIC", GfvoVocab.Somatic_Cell.asNode());
		attributeNodes.put("VALIDATED", GfvoVocab.Experimental_Method.asNode());
		attributeNodes.put("1000G", GfvoVocab.External_Reference.asNode());
		
		attributeTypeNodes = new HashMap<>();
		attributeTypeNodes.put("AA", XSDstring);
		attributeTypeNodes.put("AC", XSDint);
		attributeTypeNodes.put("AF", XSDfloat);
		attributeTypeNodes.put("AN", XSDint);
		attributeTypeNodes.put("BaseQRankSum", XSDfloat);
		attributeTypeNodes.put("BQ", XSDfloat);
		attributeTypeNodes.put("CIGAR", XSDstring);
		attributeTypeNodes.put("ClippingRankSum", XSDfloat);
		attributeTypeNodes.put("DB", XSDboolean);
		attributeTypeNodes.put("DP", XSDint);
		attributeTypeNodes.put("FS", XSDfloat);
		attributeTypeNodes.put("H2", XSDboolean);
		attributeTypeNodes.put("H3", XSDboolean);
		attributeTypeNodes.put("MLEAC", XSDfloat);
		attributeTypeNodes.put("MLEAF", XSDfloat);
		attributeTypeNodes.put("MQ", XSDfloat);
		attributeTypeNodes.put("MQ0", XSDfloat);
		attributeTypeNodes.put("MQRankSum", XSDfloat);
		attributeTypeNodes.put("NS", XSDfloat);
		attributeTypeNodes.put("QD", XSDfloat);
		attributeTypeNodes.put("ReadPosRankSum", XSDfloat);
		attributeTypeNodes.put("SB", XSDstring);
		attributeTypeNodes.put("SOMATIC", XSDboolean);
		attributeTypeNodes.put("VALIDATED", XSDboolean);
		attributeTypeNodes.put("1000G", XSDboolean);
		
		featureTypeNodes = new HashMap<>();
		featureTypeNodes.put("CDS", SoVocab.CDS.asNode());
		featureTypeNodes.put("gene", SoVocab.gene.asNode());
		featureTypeNodes.put("mRNA", SoVocab.mRNA.asNode());
		featureTypeNodes.put("operon", SoVocab.operon.asNode());
		featureTypeNodes.put("exon", SoVocab.exon.asNode());
		featureTypeNodes.put("TF_binding_site", SoVocab.TF_binding_site.asNode());
		featureTypeNodes.put("intron", SoVocab.intron.asNode());
		featureTypeNodes.put("EST_match", SoVocab.EST_match.asNode());
		featureTypeNodes.put("translated_nucleotide_match", SoVocab.translated_nucleotide_match.asNode());
		featureTypeNodes.put("Three_prime_UTR", SoVocab.three_prime_UTR.asNode());
		featureTypeNodes.put("Five_prime_UTR", SoVocab.five_prime_UTR.asNode());
		featureTypeNodes.put("cDNA_match", SoVocab.cDNA_match.asNode());
		featureTypeNodes.put("match_part", SoVocab.match_part.asNode());
		featureTypeNodes.put("polypeptide", SoVocab.polypeptide.asNode());
		featureTypeNodes.put("intein", SoVocab.intein.asNode());
		featureTypeNodes.put("primary_transcript", SoVocab.primary_transcript.asNode());
		
	
		

	}
	
	private void addKeyValueTriples(Node feature, Map<String, Object> keyValues,List<Triple> triples) {
		for (String key: keyValues.keySet()) {
			if (attributeTypeNodes.get(key) ==XSDboolean) {
			triples.add(new Triple(feature, attributeNodes.get(key), NodeFactory.createLiteral(key,XSDstring)));	
			}
			else if (attributeNodes.get(key)!=null){
			triples.add(new Triple(feature, attributeNodes.get(key), NodeFactory.createLiteral(keyValues.get(key).toString(),attributeTypeNodes.get(key))));
			}
		}
	}
	private void addFormatTriples(Node feature, String featureName , Iterable<Genotype> iterable,List<Triple> triples){
		for (Genotype gt: iterable) {
			String SampleID = featureName + "_" + gt.getSampleName();
			Node Sample = tripleGenerator.generateURI(SampleID);
			triples.add(new Triple(feature, GfvoVocab.Sample.asNode(), Sample));		
			triples.add(new Triple(Sample, GfvoVocab.Identifier.asNode(), NodeFactory.createLiteral(gt.getSampleName(),XSDstring)));
			triples.add(new Triple(Sample, GfvoVocab.Coverage.asNode(), NodeFactory.createLiteral(Integer.toString(gt.getDP()),XSDint)));
			
//			triples.add(new Triple(Sample, GfvoVocab.Genotype.asNode(), NodeFactory.createLiteral(gt.getGenotypeString(),XSDstring)));
//			
//			triples.add(new Triple(Sample, BoinqVocab.Allelic_Balance.asNode(),NodeFactory.createLiteral(gt.getAB().toString(),XSDstring)));
	
			triples.add(new Triple(Sample, GfvoVocab.Conditional_Genotype_Quality.asNode(), NodeFactory.createLiteral(Integer.toString(gt.getGQ()),XSDfloat)));
		
			if (gt.getAD()!=null){
					triples.add(new Triple(Sample, BoinqVocab.Allelic_Depth_Reference.asNode(),NodeFactory.createLiteral(String.valueOf(gt.getAD()[0]),XSDint)));
					triples.add(new Triple(Sample, BoinqVocab.Allelic_Depth_Alternate.asNode(),NodeFactory.createLiteral(String.valueOf(gt.getAD()[1]),XSDint)));
			}
			
			if (gt.getPL()!=null){
				for (int i = 0; i < gt.getPL().length; ++i){
					triples.add(new Triple(Sample, GfvoVocab.Phred_Score.asNode(), NodeFactory.createLiteral(String.valueOf(gt.getPL()[i]),XSDint)));
			}
		
			
			switch (gt.getType()) {
			case HET:
				triples.add(new Triple(Sample, GfvoVocab.has_attribute.asNode(), GfvoVocab.Heterozygous.asNode()));
				break;
			case HOM_VAR:
			case HOM_REF:
				triples.add(new Triple(Sample, GfvoVocab.has_attribute.asNode(), GfvoVocab.Homozygous.asNode()));
				break;
			case MIXED:
			case NO_CALL:
			case UNAVAILABLE:
				break;
			}
			
	}
			
		}	
}


	
	private void addFaldoTriples(Node feature, Node reference, Long biologicalStartPosBase1, Long biologicalEndPosBase1, Boolean forwardStrand, List<Triple> triples) {
		triples.add(new Triple(feature, RDF.type.asNode(), FaldoVocab.Region.asNode()));
		Node featureBegin = tripleGenerator.generateURI(FEATUREBEGINURI + feature.getLocalName());
		Node featureEnd = tripleGenerator.generateURI(FEATUREENDURI + feature.getLocalName());
		triples.add(new Triple(feature, begin.asNode(), featureBegin));
		triples.add(new Triple(feature, end.asNode(), featureEnd));
		triples.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));
		triples.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));
		
		if (reference != null) {
			triples.add(new Triple(featureBegin, FaldoVocab.reference.asNode(), reference));
			triples.add(new Triple(featureEnd, FaldoVocab.reference.asNode(), reference));
		}
		triples.add(new Triple(featureBegin, FaldoVocab.position.asNode(), NodeFactory.createLiteral(biologicalStartPosBase1.toString(),XSDint)));
		triples.add(new Triple(featureEnd, FaldoVocab.position.asNode(), NodeFactory.createLiteral(biologicalEndPosBase1.toString(),XSDint)));
		if (forwardStrand != null) {
			triples.add(new Triple(featureBegin, RDF.type.asNode(), (forwardStrand?FaldoVocab.ForwardStrandPosition.asNode():FaldoVocab.ReverseStrandPosition.asNode())));
			triples.add(new Triple(featureEnd, RDF.type.asNode(), (forwardStrand?FaldoVocab.ForwardStrandPosition.asNode():FaldoVocab.ReverseStrandPosition.asNode())));
		}
		// for searching: location may have a reference; feature may have a location that has a position; feature may be a region that has a position
		
	}
	
	protected void addTypes(Node feature, List<Node> types, List<Triple> triples) {
		for (Node type: types) {
			triples.add(new Triple(feature, RDF.type.asNode(), type));
		}	
	}
	
	protected void addAlleleTriples(Node feature, VariantContext variant, List<Triple> triples) {
	
		if (variant.getReference() != null && variant.getReference().getBaseString() != null) {
		//	Node ref = NodeFactory.createBlankNode();
		/*	triples.add(new Triple(ref, RDF.type.asNode(), GfvoVocab.Reference_Sequence.asNode()));
			triples.add(new Triple(ref, GfvoVocab.has_value.asNode(), NodeFactory.createLiteral(variant.getReference().getBaseString()))); */
			triples.add(new Triple(feature, GfvoVocab.Reference_Sequence.asNode(), NodeFactory.createLiteral(variant.getReference().getBaseString())));
			//TODO: hoe in biointerchange ?
		}
		for (Allele all: variant.getAlternateAlleles()) {
			if (all.getBaseString() != null) {
				/*Node alt = NodeFactory.createBlankNode();
				triples.add(new Triple(alt, RDF.type.asNode(), GfvoVocab.Sequence.asNode())); */
				triples.add(new Triple(feature, GfvoVocab.Sequence.asNode(), NodeFactory.createLiteral(all.getBaseString())));
			}
		}
	}
	
	public List<Triple> convert(VariantContext record, Node reference, String id, int start,List<String> typeList) {
		List<Triple> triples = new LinkedList<Triple>();
		String featureName;
		String point = ".";
		
		if (!point.equals(id)){
		featureName = FEATUREBASEURI + id;
		}
		else {
		featureName = FEATUREBASEURI + "st" + start;	
		}
		Node feature = tripleGenerator.generateURI(featureName);
		triples.add(new Triple(feature, GfvoVocab.Identifier.asNode(), NodeFactory.createLiteral(id.toString(),XSDstring)));

		Boolean forwardStrand = true; 
		Long biologicalStartPosBase1 = new Long(record.getStart());
		Long biologicalEndPosBase1 = new Long(record.getEnd());
		addFaldoTriples(feature, reference, biologicalStartPosBase1, biologicalEndPosBase1, forwardStrand, triples);
    	addAlleleTriples(feature, record, triples);
		addKeyValueTriples(feature, record.getAttributes(), triples);
		addFormatTriples(feature, featureName, record.getGenotypes(), triples);
		triples.add(new Triple(feature, GfvoVocab.Phred_Score.asNode(), NodeFactory.createLiteral(Double.toString(record.getPhredScaledQual()), XSDfloat)));
		triples.add(new Triple(feature, GfvoVocab.Sample_Count.asNode(), NodeFactory.createLiteral(Integer.toString(record.getNSamples()),XSDint)));
		triples.add(new Triple(feature, GfvoVocab.Chromosome.asNode(), NodeFactory.createLiteral(record.getChr().toString(),XSDstring)));
		triples.add(new Triple(feature, GfvoVocab.Genome_Analysis.asNode(), NodeFactory.createLiteral(record.getFilters().toString(),XSDstring)));
		switch (record.getType()) {
		case INDEL:
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.indel.asNode()));
			break;
		case MIXED:
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.insertion.asNode()));
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.deletion.asNode()));
			break;
		case MNP:
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.MNP.asNode()));
			break;
		case NO_VARIATION:
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.nucleotide_match.asNode()));
			break;
		case SNP:
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.SNP.asNode()));
			break;
		case SYMBOLIC:
			break;
		default:
			//TODO: check if child of sequence_alteration from SO
			break;
		}
		
		return triples;
	}

	
	public List<Triple> convert(SAMRecord record, Node reference, String id, List<String> typeList) {
		//TODO: this is a draft
		List<Triple> result = new LinkedList<Triple>();
		String featureName = FEATUREBASEURI + id;
		Node feature = tripleGenerator.generateURI(featureName);
		Boolean forwardStrand = !record.getReadNegativeStrandFlag();
		Long biologicalStartPosBase1 = (forwardStrand?new Long(record.getStart()):new Long(record.getEnd()));
		Long biologicalEndPosBase1 = (forwardStrand?new Long(record.getEnd()):new Long(record.getStart()));
		addFaldoTriples(feature, reference, biologicalStartPosBase1, biologicalEndPosBase1, forwardStrand, result);
		
		// SAM is base 1 
		// TODO: type: samrecord ?
		//result.add(new Triple(feature, RDF.type.asNode(), ))

		int childno = 0;
		for (AlignmentBlock block : record.getAlignmentBlocks()) {
			String subFeatureId = featureName + childno++;
			Node subFeature = tripleGenerator.generateURI(FEATUREBASEURI + subFeatureId);
			result.add(new Triple(subFeature, SoVocab.part_of.asNode(), feature));
			Long start = new Long(block.getReferenceStart());
			Long end =  new Long(block.getReferenceStart()+block.getLength()-1);
			addFaldoTriples(subFeature, reference, (forwardStrand?start:end), (forwardStrand?end:start), forwardStrand, result);
		}
		
		
		return result;
	}
	
	
	public List<Triple> convert(ValuedInterval vInt, String id) {

		List<Triple> result = new LinkedList<Triple>();
		// feature is the subject of all following triples except the FALDO exact position triples
		String featureName = FEATUREBASEURI + id;
		Node feature = tripleGenerator.generateURI(featureName);

		// TODO: check chr input for "chr#", or just "#" (if chr.startwith("chr")){ 	}
		//Node chrURI = NodeFactory.createURI(CHRBASEURI);

		//if(vInt.getChr().startsWith("chr")){
		//	chrValue = vInt.getChr().substring(3);
		//}else{
		//	chrValue = vInt.getChr();
		//}
		//Node chr = NodeFactory.createLiteral(String.valueOf(chrValue), XSDstring);
		
		result.add(new Triple(feature, SoVocab.label.asNode(), NodeFactory.createLiteral(String.valueOf(vInt.getChr()), XSDstring)));

		Node featureBegin = tripleGenerator.generateURI(FEATUREBEGINURI + id);
		Node featureEnd = tripleGenerator.generateURI(FEATUREENDURI + id);
		result.add(new Triple(feature, FaldoVocab.begin.asNode(), featureBegin));
		result.add(new Triple(feature, FaldoVocab.end.asNode(), featureEnd));
		result.add(new Triple(featureBegin, FaldoVocab.position.asNode(),NodeFactory.createLiteral(String.valueOf(Math.min(vInt.getStart(),vInt.getStop())),XSDint)));
		result.add(new Triple(featureEnd, FaldoVocab.position.asNode(),NodeFactory.createLiteral(String.valueOf(Math.max(vInt.getStart(),vInt.getStop())),XSDint)));
		result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));
		result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));
		
		if (vInt.getStart() <= vInt.getStop()) {
			result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ForwardStrandPosition.asNode()));			
			result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ForwardStrandPosition.asNode()));			
		} else {
			result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ReverseStrandPosition.asNode()));
			result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ReverseStrandPosition.asNode()));

		}

		if (vInt.getValue()!=null){
			Node valueURI = NodeFactory.createURI(VALUEURI);
			Node value = NodeFactory.createLiteral(String.valueOf(vInt.getValue()));
			result.add(new Triple(feature, valueURI, value));
		}

		return result;
	}

	// BedGraph kan volledig geconverteerd worden via vInt

	//		public static List<Triple> convert(BedGraphEntry entry){
	//			
	//			List<Triple> result = convert((ValuedInterval) entry);
	//			Node feature = NodeFactory.createURI(FEATUREBASEURI + entry.getId());
	//			
	//			return result;
	//			
	//		}

	public List<Triple> convert(GFFEntry entry, Node reference, String id, List<String> typeList) {
		List<Triple> result = convert((ValuedInterval) entry, id);
		Node feature = tripleGenerator.generateURI(FEATUREBASEURI + id);

		if(entry.getSource() != null && !entry.getSource().isEmpty() && !entry.getSource().equalsIgnoreCase(".")){
			result.add(new Triple(feature, GfvoVocab.has_source.asNode(), NodeFactory.createLiteral(String.valueOf(entry.getSource()), XSDstring)));
		}
		if(entry.getFrame() != null && !entry.getFrame().isEmpty() && !entry.getFrame().equalsIgnoreCase(".")){
			result.add(new Triple(feature, SoVocab.reading_frame.asNode(),NodeFactory.createLiteral(String.valueOf(entry.getFrame()), XSDint)));
		}
		
		if(entry.getFeature()!=null && !entry.getFeature().isEmpty() && !entry.getFeature().equalsIgnoreCase(".")){
			//String type= "SoVocab."+entry.getFeature()+".asNode()";
			//Object x =Class.forName(type).newInstance();
			//result.add(new Triple(feature, RDF.type.asNode(), NodeFactory.createLiteral(String.valueOf(entry.getFeature()),XSDstring)));
			if (featureTypeNodes.containsKey(entry.getFeature())){
			result.add(new Triple(feature, RDF.type.asNode(), featureTypeNodes.get(entry.getFeature())));
			typeList.add(entry.getFeature());
			}
			else{
			result.add(new Triple(feature, RDF.type.asNode(), NodeFactory.createLiteral(String.valueOf(entry.getFeature()),XSDstring)));
			}
		


		// notes 
//		if (entry.getNotes() != null) {
//			List<String> noteValues;
//			String[] notes = entry.getNotes();
//			for (String noteKVpair : notes) {
//				noteValues = new ArrayList<String>();
//				String[] noteKeySplitValue = noteKVpair.split("=");
//				String noteKeys = (noteKeySplitValue[0]);
//				String[] noteValue = noteKeySplitValue[1].split(",");
//				Node attributeURIkey = NodeFactory.createURI(ATTRIBUTEURI+noteKeys);
//				for (String oneValue : noteValue) {
//					Node attributeValue = NodeFactory.createLiteral(oneValue);
//					noteValues.add(oneValue);
//					result.add(new Triple(feature, attributeURIkey, attributeValue));
//				}
//			}
//		}

		}
		return result;
	}

	
	public List<Triple> convert(BEDFeature entry, String id, Node reference, List<String> typeList) {
		List<Triple> result = new LinkedList<Triple>();
		Node feature = tripleGenerator.generateURI(FEATUREBASEURI + id);
		Float score = entry.getScore();
		if (score != null && score != 0) {
			result.add(new Triple(feature, SoVocab.score.asNode(), NodeFactory.createLiteral(score.toString(), XSDdouble)));
		}
		if (entry.getName() != null && entry.getName().length() > 0) {
			result.add(new Triple(feature, RDFS.label.asNode(), NodeFactory.createLiteral(entry.getName(), XSDstring)));
		}
		if (entry.getDescription() != null && entry.getDescription().length() > 0) {
			result.add(new Triple(feature, RDFS.comment.asNode(), NodeFactory.createLiteral(entry.getDescription(), XSDstring)));
		}
		if (entry.getChr().substring(0,2).equalsIgnoreCase("chr")){
		result.add(new Triple(feature, SoVocab.chromosome.asNode(), NodeFactory.createLiteral(entry.getChr().substring(3),XSDstring)));
		}
		else {
		result.add(new Triple(feature, SoVocab.chromosome.asNode(), NodeFactory.createLiteral(entry.getChr(),XSDstring)));		
		}
		addFaldoTriples(feature, reference, Long.valueOf(entry.getStart()), Long.valueOf(entry.getEnd()), entry.getStrand() == Strand.POSITIVE, result);
		
		int idx = 1;
		for (Exon exon :entry.getExons()) {
			String subFeatureName = FEATUREBASEURI + id +"_Exon_"+ idx++;
			Node subFeature = tripleGenerator.generateURI(subFeatureName);
			result.add(new Triple(subFeature, SoVocab.has_integral_part.asNode(), feature)); 
			result.add(new Triple(feature, SoVocab.has_part.asNode(),subFeature));
			result.add(new Triple(subFeature, RDF.type.asNode(), SoVocab.exon_region.asNode()));
			addFaldoTriples(subFeature, reference, Long.valueOf(exon.getCdStart()), Long.valueOf(exon.getCdEnd()), null, result);
		}
		return result;
	}
	
	public List<Triple> convert(BedEntry entry, String id) {
		List<Triple> result = convert((ValuedInterval) entry, id);
		Node feature = tripleGenerator.generateURI(FEATUREBASEURI + entry.getId());
		
		
		String scoreString = String.valueOf(entry.getValue());
		if(scoreString != null && !scoreString.isEmpty()){
			Node featureScoreURI = NodeFactory.createURI(feature + "Value");
			result.add(new Triple(feature, featureScoreURI, NodeFactory.createLiteral(String.valueOf(entry.getValue()), XSDdouble)));
		}

		if(entry.getId()!=null && !entry.getId().isEmpty()){
			Node nameURI = NodeFactory.createURI(NAMEURI);
			Node name = NodeFactory.createLiteral(String.valueOf(entry.getId()), XSDstring);
			result.add(new Triple(feature, nameURI, name));
		}
		if(String.valueOf(entry.getThickStart())!= null && !String.valueOf(entry.getThickStart()).isEmpty()){
			Node thickStartURI = NodeFactory.createURI(THICKSTARTURI);
			Node thickStart = NodeFactory.createLiteral(String.valueOf((entry.getThickStart())), XSDint);
			result.add(new Triple(feature, thickStartURI, thickStart));
		}
		if(String.valueOf(entry.getThickEnd())!=null && !String.valueOf(entry.getThickEnd()).isEmpty()){
			Node thickEndURI = NodeFactory.createURI(THICKENDURI);
			Node thickEnd = NodeFactory.createLiteral(String.valueOf(entry.getThickEnd()), XSDint);
			result.add(new Triple(feature, thickEndURI, thickEnd));
		}

		// BlokCount, BlockSizes, BlockStarts
		Node featureBlock = NodeFactory.createLiteral(BLOCKBASEURI + id);
		Node blockURI = NodeFactory.createLiteral(BLOCKURI);
		result.add(new Triple(feature, blockURI, featureBlock));

		if (String.valueOf(entry.getBlockCount()) != null
				&& !String.valueOf(entry.getBlockCount()).isEmpty()) {
			Node blockCountURI = NodeFactory.createURI(BLOCKCOUNTURI);
			Node blockCount = NodeFactory.createLiteral(String.valueOf(entry
					.getBlockCount()), XSDint);
			result.add(new Triple(featureBlock, blockCountURI, blockCount));
		}
		if (entry.getBlockSizes() != null
				&& !(entry.getBlockSizes().length == 0)
				&& entry.getBlockStarts() != null
				&& !(entry.getBlockStarts().length == 0)) {
			Node blockStartURI = NodeFactory.createURI(BLOCKSTARTURI);
			Node blockSizeURI = NodeFactory.createURI(BLOCKSIZEURI);
			for (blockCounter = 1; blockCounter < entry.getBlockCount(); blockCounter++) {
				int[] blockSizes = entry.getBlockSizes();
				Node blockSize = NodeFactory.createLiteral(
						String.valueOf(blockSizes[blockCounter]),
						XSDint);
				result.add(new Triple(featureBlock, blockSizeURI, blockSize));

				int[] blockStarts = entry.getBlockStarts();
				Node blockStart = NodeFactory.createLiteral(
						String.valueOf(blockStarts[blockCounter]),
						XSDint);
				result.add(new Triple(featureBlock, blockStartURI, blockStart));
			}

		}

		return result;
	}	
	
	public List<Triple> convert(FaldoFeature faldoFeature) {
		List<Triple> result = new LinkedList<Triple>();
		// feature is the subject of all following triples except the FALDO exact position triples
		String featureName = FEATUREBASEURI + faldoFeature.id;
		Node feature = NodeFactory.createURI(featureName);

		Node ref = NodeFactory.createURI(faldoFeature.assembly);

		Node featureBegin = NodeFactory.createURI(FEATUREBEGINURI + faldoFeature.id);
		Node featureEnd = NodeFactory.createURI(FEATUREENDURI + faldoFeature.id);
		result.add(new Triple(feature, RDFS.label.asNode(), NodeFactory.createLiteral(faldoFeature.id)));
		result.add(new Triple(feature, FaldoVocab.begin.asNode(), featureBegin));
		result.add(new Triple(feature, FaldoVocab.end.asNode(), featureEnd));
		result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));
		result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));
		result.add(new Triple(featureBegin, FaldoVocab.position.asNode(),NodeFactory.createLiteral(faldoFeature.start.toString(),XSDint)));
		result.add(new Triple(featureBegin, FaldoVocab.reference.asNode(), ref));
		result.add(new Triple(featureEnd, FaldoVocab.position.asNode(),NodeFactory.createLiteral(faldoFeature.end.toString(),XSDint)));
		result.add(new Triple(featureEnd, FaldoVocab.reference.asNode(), ref));


		if (faldoFeature.strand) {
			result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ForwardStrandPosition.asNode()));			
			result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ForwardStrandPosition.asNode()));			
		} else {
			result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ReverseStrandPosition.asNode()));
			result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ReverseStrandPosition.asNode()));

		}
		return result;

	}
	
	
}


/*
 * public class BEDToTripleConverter {
	private final boolean rdftype;
	private final boolean faldobegin;
	private final boolean faldoend;

	public BEDToTripleConverter(ValueFactory vf, URI... preds) {
		super();
		this.vf = vf;
		List<URI> predList = Arrays.asList(preds);
		boolean tempType = predList.contains(RDF.TYPE);
		boolean tempfaldobegin = predList.contains(FALDO.BEGIN_PREDICATE);
		boolean tempfaldoend = predList.contains(FALDO.END_PREDICATE);

		if (predList.isEmpty() || predList.contains(null)) {
			tempType = true;
			tempfaldobegin = true;
			tempfaldoend = true;
		}
		rdftype = tempType;
		faldobegin = tempfaldobegin;
		faldoend = tempfaldoend;
		// type = predList.contains(RDF.TYPE) || predList.isEmpty() ||
		// predList.contains(null);
	}

	private final ValueFactory vf;

	public List<Statement> convertLineToTriples(String filePath,
			Feature feature, long lineNo) {
		List<Statement> stats = new ArrayList<Statement>(28);
		String recordPath = filePath + '/' + lineNo;
		URI recordId = vf.createURI(recordPath);
		URI alignStartId = vf.createURI(recordPath + "#start");
		URI alignEndId = vf.createURI(recordPath + "#end");

		add(stats, recordId, BED.CHROMOSOME, feature.getChr());

		if (rdftype) {
			rdfTypesForFeature(stats, recordId, alignStartId, alignEndId);
		}
		if (faldobegin) {
			add(stats, recordId, FALDO.BEGIN_PREDICATE, alignStartId);
		}
		add(stats, alignStartId, FALDO.POSTION_PREDICATE, feature.getStart());
		add(stats, alignStartId, FALDO.REFERENCE_PREDICATE, feature.getChr());

		if (faldoend) {
			add(stats, recordId, FALDO.END_PREDICATE, alignEndId);
		}
		add(stats, alignEndId, FALDO.POSTION_PREDICATE, feature.getEnd());
		add(stats, alignEndId, FALDO.REFERENCE_PREDICATE, feature.getChr());
		if (feature instanceof BEDFeature) {
			stats.addAll(convertLineToTriples(filePath, (BEDFeature) feature,
					lineNo));
		}
		return stats;
	}

	protected void rdfTypesForFeature(List<Statement> stats, URI recordId,
			URI alignStartId, URI alignEndId) {
		add(stats, recordId, RDF.TYPE, BED.FEATURE_CLASS);
		add(stats, recordId, RDF.TYPE, FALDO.REGION_CLASS);
		add(stats, alignStartId, RDF.TYPE, FALDO.EXACT_POSITION_CLASS);
		add(stats, alignEndId, RDF.TYPE, FALDO.EXACT_POSITION_CLASS);
	}

	private List<Statement> convertLineToTriples(String filePath,
			BEDFeature feature, long lineNo) {
		List<Statement> stats = new ArrayList<Statement>(28);
		String recordPath = filePath + '/' + lineNo;
		URI recordId = vf.createURI(recordPath);
		if (feature.getName() != null) // name
			add(stats, recordId, RDFS.LABEL, feature.getName());
		if (feature.getScore() != Float.NaN) // score
			add(stats, recordId, BED.SCORE, feature.getScore());
		if (rdftype)
			addStrandedNessInformation(stats, feature, recordId);
		// we skip position 6,7 and 8 as these are colouring instructions

		for (Exon exon : feature.getExons()) {
			convertExon(feature, stats, recordPath, recordId, exon);
		}
		return stats;
	}

	protected void convertExon(BEDFeature feature, List<Statement> stats,
			String recordPath, URI recordId, Exon exon) {
		String exonPath = recordPath + "/exon/" + exon.getNumber();
		URI exonId = vf.createURI(exonPath);
		URI beginId = vf.createURI(exonPath + "/begin");
		URI endId = vf.createURI(exonPath + "/end");
		add(stats, recordId, BED.EXON, endId);
		if (rdftype) {
			add(stats, exonId, RDF.TYPE, FALDO.REGION_CLASS);
			add(stats, endId, RDF.TYPE, FALDO.EXACT_POSITION_CLASS);
		}
		if (faldobegin) {
			add(stats, exonId, FALDO.BEGIN_PREDICATE, beginId);
		}
		add(stats, beginId, RDF.TYPE, FALDO.EXACT_POSITION_CLASS);
		add(stats, beginId, FALDO.POSTION_PREDICATE, exon.getCdStart());
		add(stats, beginId, FALDO.REFERENCE_PREDICATE, feature.getChr());
		if (faldoend) {
			add(stats, exonId, FALDO.END_PREDICATE, endId);
		}
		add(stats, endId, FALDO.POSTION_PREDICATE, exon.getCdEnd());
		add(stats, endId, FALDO.REFERENCE_PREDICATE, feature.getChr());
	}

	protected void addStrandedNessInformation(List<Statement> statements,
			BEDFeature feature, URI alignEndId) {

		if (Strand.POSITIVE == feature.getStrand()) {
			add(statements, alignEndId, RDF.TYPE,
					FALDO.FORWARD_STRAND_POSITION_CLASS);
		} else if (Strand.NEGATIVE == feature.getStrand()) {
			add(statements, alignEndId, RDF.TYPE,
					FALDO.REVERSE_STRANDED_POSITION_CLASS);
		} else {
			add(statements, alignEndId, RDF.TYPE, FALDO.STRANDED_POSITION_CLASS);
		}

	}

	private void add(List<Statement> statements, URI subject, URI predicate,
			String string) {
		add(statements, subject, predicate, vf.createLiteral(string));

	}

	private void add(List<Statement> statements, URI subject, URI predicate,
			int string) {
		add(statements, subject, predicate, vf.createLiteral(string));

	}

	private void add(List<Statement> statements, URI subject, URI predicate,
			float string) {
		add(statements, subject, predicate, vf.createLiteral(string));

	}

	private void add(List<Statement> statements, Resource subject,
			URI predicate, Value object) {
		statements.add(vf.createStatement(subject, predicate, object));
	}
}
*/
