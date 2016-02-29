package com.genohm.boinq.tools.fileformats;

import htsjdk.samtools.AlignmentBlock;
import htsjdk.samtools.SAMReadGroupRecord;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMRecord.SAMTagAndValue;
import htsjdk.tribble.annotation.Strand;
import htsjdk.tribble.bed.BEDFeature;
import htsjdk.tribble.bed.FullBEDFeature.Exon;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFilterHeaderLine;
import htsjdk.variant.vcf.VCFFormatHeaderLine;
import htsjdk.variant.vcf.VCFHeaderLine;
import htsjdk.variant.vcf.VCFInfoHeaderLine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.lang.Object;
import java.util.Set;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.faldo.FaldoFeature;
import com.genohm.boinq.domain.jobs.TripleConversion.Metadata;
import com.genohm.boinq.generated.vocabularies.BoinqVocab;
import com.genohm.boinq.generated.vocabularies.FaldoVocab;
import com.genohm.boinq.generated.vocabularies.FormatVocab;
import com.genohm.boinq.generated.vocabularies.GfvoVocab;
import com.genohm.boinq.generated.vocabularies.SoVocab;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.service.TripleGeneratorService;

import de.charite.compbio.jannovar.impl.parse.gff.Feature;

import static com.genohm.boinq.generated.vocabularies.FaldoVocab.*;
import static org.apache.jena.datatypes.xsd.XSDDatatype.*;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;


@Service
public class TripleConverter {
	// genohm.com / bed-gff-bedgraph??? / feature ------> datasource

	// all variable names that are not included in the valuedInterval variables
	// keep their original variable names
	// the variable names that are included in the valuedInterval are given a
	// more generic name

	public static final String FEATUREBASEURI = "/feature#";
	public static final String FEATUREBEGINURI = "/feature_begin#";
	public static final String FEATUREENDURI = "/feature_end#";
	public static final String FEATUREPOSURI = "/position#";
	public static final String FEATURELOCATIONURI = "/resource/contig:";

	private Map<String, Node> attributeNodes;
	private Map<String, XSDDatatype> attributeTypeNodes;
	private Map<String, Node> featureTypeNodes;


	@Inject
	TripleGeneratorService tripleGenerator;

	public TripleConverter() {
		attributeNodes = new HashMap<>();
		attributeNodes.put("AA", GfvoVocab.Ancestral_Sequence.asNode());
		attributeNodes.put("AC", GfvoVocab.Allele_Count.asNode());
		attributeNodes.put("AF", GfvoVocab.Allele_Frequency.asNode());
		attributeNodes.put("AN", GfvoVocab.Total_Number_of_Alleles.asNode());
		attributeNodes.put("BaseQRankSum", BoinqVocab.Base_Quality_Rank_Sum.asNode());
		attributeNodes.put("BQ", GfvoVocab.Base_Quality.asNode());
		attributeNodes.put("CIGAR", GfvoVocab.Forward_Reference_Sequence_Frameshift.asNode()); //TODO:WRONG
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

		featureTypeNodes = new HashMap<String, Node>();
		featureTypeNodes.put("CDS", SoVocab.CDS.asNode());
		featureTypeNodes.put("GENE", SoVocab.gene.asNode());
		featureTypeNodes.put("MRNA", SoVocab.mRNA.asNode());
		featureTypeNodes.put("OPERON", SoVocab.operon.asNode());
		featureTypeNodes.put("EXON", SoVocab.exon.asNode());
		featureTypeNodes.put("TF_BINDING_SITE", SoVocab.TF_binding_site.asNode());
		featureTypeNodes.put("INTRON", SoVocab.intron.asNode());
		featureTypeNodes.put("EST_MATCH", SoVocab.EST_match.asNode());
		featureTypeNodes.put("TRANSLATED_NUCLEOTIDE_MATCH", SoVocab.translated_nucleotide_match.asNode());
		featureTypeNodes.put("THREE_PRIME_UTR", SoVocab.three_prime_UTR.asNode());
		featureTypeNodes.put("FIVE_PRIME_UTR", SoVocab.five_prime_UTR.asNode());
		featureTypeNodes.put("CDNA_MATCH", SoVocab.cDNA_match.asNode());
		featureTypeNodes.put("MATCH_PART", SoVocab.match_part.asNode());
		featureTypeNodes.put("POLYPEPTIDE", SoVocab.polypeptide.asNode());
		featureTypeNodes.put("INTEIN", SoVocab.intein.asNode());
		featureTypeNodes.put("PRIMARY_TRANSCRIPT", SoVocab.primary_transcript.asNode());
		featureTypeNodes.put("STOP_CODON", SoVocab.stop_codon.asNode());
		featureTypeNodes.put("NCRNA", SoVocab.ncRNA.asNode());
		featureTypeNodes.put("REGION", SoVocab.region.asNode());
		featureTypeNodes.put("RRNA", SoVocab.rRNA.asNode());
		featureTypeNodes.put("START_CODON", SoVocab.start_codon.asNode());
		featureTypeNodes.put("TRANSCRIPT", SoVocab.transcript.asNode());
		featureTypeNodes.put("TRNA", SoVocab.tRNA.asNode());

	}

	private void addKeyValueTriples(Node feature, String featureName, Map<String, Object> keyValues, List<Triple> triples) {
		int count=1;
		for (String key : keyValues.keySet()) {
			String attributeFeatureName = featureName + "_Attribute_"+ count++;
			Node attributeFeature = tripleGenerator.generateURI(FEATUREBASEURI + attributeFeatureName);
			triples.add(new Triple(feature, GfvoVocab.has_attribute.asNode(), attributeFeature));
			triples.add(new Triple(attributeFeature, RDFS.label.asNode(), NodeFactory.createLiteral(key, XSDstring)));
			if(attributeNodes.get(key) != null) {
				triples.add(new Triple(attributeFeature, RDF.type.asNode(), attributeNodes.get(key)));
				triples.add(new Triple(attributeFeature, GfvoVocab.has_value.asNode(), NodeFactory.createLiteral(keyValues.get(key).toString(), attributeTypeNodes.get(key))));
			} else {
				triples.add(new Triple(attributeFeature, GfvoVocab.has_value.asNode(), NodeFactory.createLiteral(keyValues.get(key).toString(), XSDstring)));
				
			}
		}
	}

	private void addFormatTriples(Node feature, long sumFeatureCount, Iterable<Genotype> iterable, List<Triple> triples) {
		for (Genotype gt : iterable) {
			String SampleID = sumFeatureCount + "_" + gt.getSampleName();
			Node Sample = tripleGenerator.generateURI(FEATUREBASEURI+SampleID);
			triples.add(new Triple(feature, GfvoVocab.Sample.asNode(), Sample));
			triples.add(new Triple(Sample, GfvoVocab.Identifier.asNode(),
					NodeFactory.createLiteral(gt.getSampleName(), XSDstring)));
			triples.add(new Triple(Sample, GfvoVocab.Coverage.asNode(),
					NodeFactory.createLiteral(Integer.toString(gt.getDP()), XSDint)));
			// triples.add(new Triple(Sample, GfvoVocab.Genotype.asNode(),
			// NodeFactory.createLiteral(gt.getGenotypeString(),XSDstring)));

			// triples.add(new Triple(Sample,
			// BoinqVocab.Allelic_Balance.asNode(),NodeFactory.createLiteral(gt.getAB().toString(),XSDstring)));

			triples.add(new Triple(Sample, GfvoVocab.Conditional_Genotype_Quality.asNode(),
					NodeFactory.createLiteral(Integer.toString(gt.getGQ()), XSDfloat)));

			if (gt.getAD() != null) {
				triples.add(new Triple(Sample, BoinqVocab.Allelic_Depth_Reference.asNode(),
						NodeFactory.createLiteral(String.valueOf(gt.getAD()[0]), XSDint)));
				triples.add(new Triple(Sample, BoinqVocab.Allelic_Depth_Alternate.asNode(),
						NodeFactory.createLiteral(String.valueOf(gt.getAD()[1]), XSDint)));
			}

			if (gt.getPL() != null) {
				for (int i = 0; i < gt.getPL().length; ++i) {
					triples.add(new Triple(Sample, GfvoVocab.Phred_Score.asNode(),
							NodeFactory.createLiteral(String.valueOf(gt.getPL()[i]), XSDint)));
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


	protected void addAlleleTriples(Node feature, VariantContext variant, List<Triple> triples) {

		if (variant.getReference() != null && variant.getReference().getBaseString() != null) {
			triples.add(new Triple(feature, GfvoVocab.Reference_Sequence.asNode(),
					NodeFactory.createLiteral(variant.getReference().getBaseString())));
			// TODO: hoe in biointerchange ?
		}
		for (Allele all : variant.getAlternateAlleles()) {
			if (all.getBaseString() != null) {
				triples.add(new Triple(feature, GfvoVocab.Sequence.asNode(),
						NodeFactory.createLiteral(all.getBaseString())));
			}
		}
	}


	public List<Triple> convert(VariantContext entry, Node reference, int start, Metadata meta) {
		List<Triple> triples = new LinkedList<Triple>();

		SoVocab.dbsnp_variant_terms.asNode();
		String featureName = Long.toString(meta.sumFeatureCount);
		Node feature = tripleGenerator.generateURI(FEATUREBASEURI + featureName);
		Long biologicalStartPosBase1 = new Long(entry.getStart());
		Long biologicalEndPosBase1 = new Long(entry.getEnd());
		addFaldoTriples(feature, featureName, reference, biologicalStartPosBase1, biologicalEndPosBase1, entry.getContig(), true, triples, meta);
		addAlleleTriples(feature, entry, triples);
		addKeyValueTriples(feature, featureName, entry.getAttributes(), triples);
		addFormatTriples(feature, meta.sumFeatureCount, entry.getGenotypes(), triples);
		
		triples.add(new Triple(feature, GfvoVocab.Phred_Score.asNode(),
				NodeFactory.createLiteral(Double.toString(entry.getPhredScaledQual()), XSDfloat)));
		triples.add(new Triple(feature, GfvoVocab.Sample_Count.asNode(),
				NodeFactory.createLiteral(Integer.toString(entry.getNSamples()), XSDint)));
		triples.add(new Triple(feature, GfvoVocab.Genome_Analysis.asNode(),
				NodeFactory.createLiteral(entry.getFilters().toString(), XSDstring)));

		switch (entry.getType()) {
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
			// TODO: check if child of sequence_alteration from SO
			break;
		}

		return triples;
	}

	public List<Triple> convert(Feature entry, Node reference, Metadata meta) {
		List<Triple> triples = new LinkedList<Triple>();
		String featureName =  Long.toString(meta.sumFeatureCount);
		Node feature = tripleGenerator.generateURI( FEATUREBASEURI + featureName);
		Long biologicalStartPosBase1 = new Long(entry.getStart());
		Long biologicalEndPosBase1 = new Long(entry.getEnd());
		addFaldoTriples(feature, featureName, reference, biologicalStartPosBase1, biologicalEndPosBase1, entry.getSequenceID(), entry.getStrand(), triples, meta);
		triples.add(new Triple(feature, SoVocab.label.asNode(),
				NodeFactory.createLiteral(String.valueOf(entry.getSequenceID()), XSDstring)));
		if (entry.getPhase() != -1) {
			triples.add(new Triple(feature, SoVocab.reading_frame.asNode(),
					NodeFactory.createLiteral(String.valueOf(entry.getPhase()), XSDint)));
		}
		if (entry.getSource() != null) {
			triples.add(new Triple(feature, GfvoVocab.has_source.asNode(),
					NodeFactory.createLiteral(String.valueOf(entry.getSource()), XSDstring)));
		}
		//TODO SCORE NOT CORRECT ONTOLOGY
		if (entry.getScore() != 0.0) {
			String scoreString = String.valueOf(entry.getScore());
			triples.add(new Triple(feature, SoVocab.score.asNode(),
					NodeFactory.createLiteral(String.valueOf(scoreString), XSDdouble)));
		}
		if (entry.getType() != null) {
			if (featureTypeNodes.containsKey(entry.getType().toString())) {
				triples.add(new Triple(feature, RDF.type.asNode(), featureTypeNodes.get(entry.getType().toString())));
				meta.typeList.add(featureTypeNodes.get(entry.getType().toString()));
			} else {
				triples.add(new Triple(feature, RDF.type.asNode(),
						NodeFactory.createLiteral(String.valueOf(entry.getType()), XSDstring)));
				meta.typeList.add(NodeFactory.createLiteral(entry.getType().toString(), XSDstring));
			}
		}
		Iterator it = entry.getAttributes().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			triples.add(new Triple(feature, RDFS.comment.asNode(),
					NodeFactory.createLiteral(pair.getKey() + "=" + pair.getValue().toString())));
			it.remove();
		}

		return triples;
	}

	public List<Triple> convert(BEDFeature entry, Node reference, Metadata meta) {
		List<Triple> triples = new LinkedList<Triple>();
		
		String featureName =  Long.toString(meta.sumFeatureCount);
		Node feature = tripleGenerator.generateURI(FEATUREBASEURI +featureName);
		addFaldoTriples(feature, featureName, reference, Long.valueOf(entry.getStart()), Long.valueOf(entry.getEnd()), entry.getContig(),
				entry.getStrand() == Strand.POSITIVE, triples, meta);
		Float score = entry.getScore();
		if (score != null && score != 0) {
			triples.add(new Triple(feature, SoVocab.score.asNode(),
					NodeFactory.createLiteral(score.toString(), XSDdouble)));
		}
		if (entry.getName() != null && entry.getName().length() > 0) {
			triples.add(
					new Triple(feature, RDFS.label.asNode(), NodeFactory.createLiteral(entry.getName(), XSDstring)));
		}
		if (entry.getDescription() != null && entry.getDescription().length() > 0) {
			triples.add(new Triple(feature, RDFS.comment.asNode(),
					NodeFactory.createLiteral(entry.getDescription(), XSDstring)));
		}

		int idx = 1;
		for (Exon exon : entry.getExons()) {
			String subFeatureName = meta.sumFeatureCount + "_Exon_" + idx++;
			Node subFeature = tripleGenerator.generateURI(FEATUREBASEURI + subFeatureName);
			triples.add(new Triple(subFeature, SoVocab.has_integral_part.asNode(), feature));
			triples.add(new Triple(feature, SoVocab.has_part.asNode(), subFeature));
			triples.add(new Triple(subFeature, RDF.type.asNode(), SoVocab.exon_region.asNode()));
			addFaldoTriples(subFeature, subFeatureName, reference, Long.valueOf(exon.getCdStart()), Long.valueOf(exon.getCdEnd()), entry.getContig(), entry.getStrand() == Strand.POSITIVE,
					triples, meta);
		}
		return triples;
	}
	
	public List<Triple> convert(SAMRecord entry, Node reference, Metadata meta) {
		// TODO: this is a draft
		List<Triple> triples = new LinkedList<Triple>();
		String featureName = Long.toString(meta.sumFeatureCount);
		Node feature = tripleGenerator.generateURI(FEATUREBASEURI + featureName);
		
		Boolean forwardStrand = !entry.getReadNegativeStrandFlag();
		Long biologicalStartPosBase1 = (forwardStrand ? new Long(entry.getStart()) : new Long(entry.getEnd()));
		Long biologicalEndPosBase1 = (forwardStrand ? new Long(entry.getEnd()) : new Long(entry.getStart()));
		addFaldoTriples(feature, featureName, reference, biologicalStartPosBase1, biologicalEndPosBase1, entry.getContig(), forwardStrand, triples, meta);
		
		//TODO CREATE/FIND ONTOLOGIES
		//FIELD: QNAME: Query template name 
		String qName = entry.getReadName();
		triples.add(new Triple(feature, FormatVocab.hasQNAME.asNode(), NodeFactory.createLiteral(qName, XSDstring)));
		//FIELD: FLAG: each bit explains different description
		int Flags = entry.getFlags(); //AS SHOWN IN FILES
		triples.add(new Triple(feature, FormatVocab.hasFLAG.asNode(), NodeFactory.createLiteral(String.valueOf(Flags), XSDint)));
		//FIELD: RNAME: Reference sequence name of the alignment
		String rName = entry.getReferenceName();
		triples.add(new Triple(feature, FormatVocab.hasRNAME.asNode(), NodeFactory.createLiteral(rName, XSDstring)));
		//FIELD: MAPQ: mapping quality -10logBase
		int mapQ = entry.getMappingQuality(); //AS SHOWN IN FILES
		triples.add(new Triple(feature, FormatVocab.hasMAPQ.asNode(), NodeFactory.createLiteral(String.valueOf(mapQ), XSDint)));
		//CIGAR: CIGAR String
		String cigar = entry.getCigarString();
		triples.add(new Triple(feature, FormatVocab.hasCIGAR.asNode() , NodeFactory.createLiteral(cigar, XSDstring)));
		//RNEXT: Reference sequence name of the NEXT read in the template
		String rNext = entry.getMateReferenceName();
		triples.add(new Triple(feature, FormatVocab.hasRNEXT.asNode(), NodeFactory.createLiteral(rNext, XSDstring)));
		//PNEXT: Position of the primary allignment of the NEXT read. not necessary -> positions are iterated at end of function
		//TLEN: signed observed Template LENgth.
		int tLength = entry.getInferredInsertSize();
		triples.add(new Triple(feature, FormatVocab.hasTLEN.asNode(), NodeFactory.createLiteral(String.valueOf(tLength), XSDint)));
		//SEQ: segment Sequence
		String segmentSeq = entry.getReadString();
		triples.add(new Triple(feature, FormatVocab.hasSEQ.asNode(), NodeFactory.createLiteral(segmentSeq, XSDstring)));	
		//QUAL: base quality plus 33
		String bQualstr = entry.getBaseQualityString(); //Gives string as found back in file
		triples.add(new Triple(feature, FormatVocab.hasFLAG.asNode(), NodeFactory.createLiteral(bQualstr, XSDstring)));
		//ATTRIBUTES
		for (SAMTagAndValue attribute : entry.getAttributes()) {
			//TODO not implemented enough in htsjdk
			String key = attribute.tag;
			//String value = (String)attribute.value;
		}
		////////////////////////////////////////////
		//String rNext1 = entry.getMateReferenceName(); //ERROR

		
	
		// SAM is base 1
		// TODO: type: samrecord ?
		// result.add(new Triple(feature, RDF.type.asNode(), ))

		int childno = 1;
		for (AlignmentBlock block : entry.getAlignmentBlocks()) {
			String subFeatureName = featureName + "_Block_" + childno++;
			Node subFeature = tripleGenerator.generateURI(FEATUREBASEURI + subFeatureName);
			triples.add(new Triple(feature, SoVocab.has_part.asNode(), subFeature));
			triples.add(new Triple(subFeature, SoVocab.part_of.asNode(), feature));
			//POSITIONS
			Long start = new Long(block.getReferenceStart());
			Long end = new Long(block.getReferenceStart() + block.getLength() - 1);
			addFaldoTriples(subFeature, subFeatureName, reference, (forwardStrand ? start : end), (forwardStrand ? end : start), entry.getContig(),
					forwardStrand, triples, meta);
			
		}
		return triples;
	}
	

private void addFaldoTriples(Node feature, String featureName, Node reference, Long biologicalStartPosBase1, Long biologicalEndPosBase1, String contig,
	Boolean forwardStrand, List<Triple> triples, Metadata meta) {
	//TODO 
	if (contig.length()>=3){
	if (contig.substring(0, 3).equalsIgnoreCase("chr")) {
		contig = contig.substring(3);
	}}
	String featureLocationName = FEATURELOCATIONURI+contig+":"+biologicalStartPosBase1+"-"+biologicalEndPosBase1;
	String featureBeginName = FEATURELOCATIONURI+contig+":"+biologicalStartPosBase1;
	String featureEndName = FEATURELOCATIONURI+contig+":"+biologicalEndPosBase1;	
	if (forwardStrand != null){
	String add = (forwardStrand) ? ":1":"-1";
	featureLocationName += add;
	featureBeginName += add; 
	featureEndName += add;
	} 
	Node featureLocation = tripleGenerator.generateURI(featureLocationName);
	Node featureBegin = tripleGenerator.generateURI(featureBeginName);
	Node featureEnd = tripleGenerator.generateURI(featureEndName);	
	triples.add(new Triple(feature, location.asNode(), featureLocation));
	triples.add(new Triple(featureLocation, FaldoVocab.reference.asNode(),NodeFactory.createLiteral(contig, XSDstring)));
	triples.add(new Triple(featureLocation, RDF.type.asNode(), FaldoVocab.Region.asNode()));
	triples.add(new Triple(featureLocation, begin.asNode(), featureBegin));
	triples.add(new Triple(featureLocation, end.asNode(), featureEnd));
	triples.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));
	triples.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));

	/*if (reference != null) {
		triples.add(new Triple(featureBegin, FaldoVocab.reference.asNode(), reference));
		triples.add(new Triple(featureEnd, FaldoVocab.reference.asNode(), reference));
	}*/
	triples.add(new Triple(featureBegin, FaldoVocab.position.asNode(),
			NodeFactory.createLiteral(biologicalStartPosBase1.toString(), XSDint)));
	triples.add(new Triple(featureEnd, FaldoVocab.position.asNode(),
			NodeFactory.createLiteral(biologicalEndPosBase1.toString(), XSDint)));
	if (forwardStrand != null) {
		triples.add(new Triple(featureBegin, RDF.type.asNode(), (forwardStrand
				? FaldoVocab.ForwardStrandPosition.asNode() : FaldoVocab.ReverseStrandPosition.asNode())));
		triples.add(new Triple(featureEnd, RDF.type.asNode(), (forwardStrand
				? FaldoVocab.ForwardStrandPosition.asNode() : FaldoVocab.ReverseStrandPosition.asNode())));
	}
	// for searching: location may have a reference; feature may have a
	// location that has a position; feature may be a region that has a
	// position

}

	public List<Triple> createMetadata(Metadata metadata, String Graphname) {
		List<Triple> triples = new LinkedList<Triple>();
		Node Graph = NodeFactory.createURI(Graphname);
		String file = metadata.file.replace("\\", "/");
		Node fileNode = tripleGenerator.generateURI(file);
		triples.add(new Triple(Graph, TrackVocab.File.asNode(), fileNode));
		
		triples.add(new Triple(fileNode, TrackVocab.uploadedOn.asNode(), NodeFactory.createLiteral(metadata.date,XSDstring)));
		triples.add(new Triple(fileNode, TrackVocab.featureCount.asNode(),NodeFactory.createLiteral(Long.toString(metadata.featureCount), XSDint)));
		triples.add(new Triple(fileNode, TrackVocab.tripleCount.asNode(),NodeFactory.createLiteral(Long.toString(metadata.tripleCount), XSDint)));
		triples.add(new Triple(fileNode, TrackVocab.fileExtension.asNode(),NodeFactory.createLiteral(metadata.fileType, XSDstring)));
		triples.add(new Triple(fileNode, TrackVocab.fileName.asNode(),NodeFactory.createLiteral(metadata.fileName, XSDstring)));
	
		Set<Node> uniqueTypes = new HashSet<Node>(metadata.typeList);
		for (Node x : uniqueTypes) {
			triples.add(new Triple(Graph, TrackVocab.FeatureType.asNode(), x));
			triples.add(new Triple(fileNode, TrackVocab.FeatureType.asNode(),x));
		}
		if (metadata.fileType=="GFF3" && metadata.gffHeader!=null){
			Iterator<String> gffit = metadata.gffHeader.iterator();
			while (gffit.hasNext()) {
				triples.add(new Triple(fileNode, TrackVocab.gffHeader.asNode(),NodeFactory.createLiteral(gffit.next(),XSDstring)));
			}
		}
		if (metadata.fileType=="BED" && metadata.bedHeader!=null){
			Iterator<String> bedit = metadata.bedHeader.iterator();
			while (bedit.hasNext()) {
				triples.add(new Triple(fileNode, TrackVocab.bedHeader.asNode(),NodeFactory.createLiteral(bedit.next(),XSDstring)));
			}
		}
		if (metadata.fileType=="BAM" || metadata.fileType=="SAM" && metadata.samHeader!=null){
			//TODO Can be parsed further
			Iterator<SAMReadGroupRecord> samit = metadata.samHeader.getReadGroups().iterator();
			while (samit.hasNext()) {
				triples.add(new Triple(fileNode, TrackVocab.samHeader.asNode(), NodeFactory.createLiteral(samit.next().toString(),XSDstring)));
			}
		}
		if (metadata.fileType=="VCF" && metadata.vcfHeader!=null) {
			//TODO Can be parsed further
			Iterator<VCFFilterHeaderLine> filterIt = metadata.vcfHeader.getFilterLines().iterator();
			while (filterIt.hasNext()) {
				triples.add(new Triple(fileNode, TrackVocab.vcfHeaderFilter.asNode(), NodeFactory.createLiteral(filterIt.next().toString(), XSDstring)));
			}
			Iterator<VCFFormatHeaderLine> formatIt = metadata.vcfHeader.getFormatHeaderLines().iterator();
			while (formatIt.hasNext()) {
				triples.add(new Triple(fileNode, TrackVocab.vcfHeaderFormat.asNode(), NodeFactory.createLiteral(formatIt.next().toString(), XSDstring)));
			}
			Iterator<VCFInfoHeaderLine> infoIt = metadata.vcfHeader.getInfoHeaderLines().iterator();
			while (infoIt.hasNext()) {
				triples.add(new Triple(fileNode, TrackVocab.vcfHeaderInfo.asNode(), NodeFactory.createLiteral(infoIt.next().toString(), XSDstring)));
			}
			Iterator<VCFHeaderLine> metadataIt = metadata.vcfHeader.getOtherHeaderLines().iterator();
			while (metadataIt.hasNext()) {
				triples.add(new Triple(fileNode, TrackVocab.vcfHeaderMetadata.asNode(),NodeFactory.createLiteral(metadataIt.next().toString(), XSDstring)));
			}
		}
		return triples;
	}



	public List<Triple> convert(FaldoFeature faldoFeature) {
		List<Triple> result = new LinkedList<Triple>();
		// feature is the subject of all following triples except the FALDO
		// exact position triples
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
		result.add(new Triple(featureBegin, FaldoVocab.position.asNode(),
				NodeFactory.createLiteral(faldoFeature.start.toString(), XSDint)));
		result.add(new Triple(featureBegin, FaldoVocab.reference.asNode(), ref));
		result.add(new Triple(featureEnd, FaldoVocab.position.asNode(),
				NodeFactory.createLiteral(faldoFeature.end.toString(), XSDint)));
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
