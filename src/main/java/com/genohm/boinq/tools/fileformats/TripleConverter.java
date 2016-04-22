package com.genohm.boinq.tools.fileformats;

import htsjdk.samtools.AlignmentBlock;
import htsjdk.samtools.SAMProgramRecord;
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
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;

@Service
public class TripleConverter {
	// genohm.com / bed-gff-bedgraph??? / feature ------> datasource

	// all variable names that are not included in the valuedInterval variables
	// keep their original variable names
	// the variable names that are included in the valuedInterval are given a
	// more generic name

	public static final String ENTRYBASEURI = "/resource/entry#";
	public static final String LOCATIONBASEURI = "/resource/";
	public static final String FEATUREBASEURI = "/resource/feature#";
	public static final String FILTERBASEURI = "/resource/filter#";
	public static final String SAMPLEBASEURI = "/resource/sample#";
	public static final String READBASEURI = "/resource/read#";
	public static final String REFERENCEBASEURI = "/resource/reference#";
	public static final String ASSEMBLYBASEURI = "/resource/Assembly:";
	// public static final String

	private Map<String, Node> variantAttributeNodes;
	private Map<String, XSDDatatype> variantAttributeTypeNodes;
	private Map<String, Node> featureTypeNodes;
	private Map<String, Node> featureAttributeNodes;

	@Inject
	TripleGeneratorService tripleGenerator;

	public TripleConverter() {
		variantAttributeNodes = new HashMap<>();
		variantAttributeNodes.put("AA", GfvoVocab.Ancestral_Sequence.asNode());
		variantAttributeNodes.put("AC", GfvoVocab.Allele_Count.asNode());
		variantAttributeNodes.put("AF", GfvoVocab.Allele_Frequency.asNode());
		variantAttributeNodes.put("AN", GfvoVocab.Total_Number_of_Alleles.asNode());
		variantAttributeNodes.put("BaseQRankSum", BoinqVocab.Base_Quality_Rank_Sum.asNode());
		variantAttributeNodes.put("BQ", GfvoVocab.Base_Quality.asNode());
		// variantAttributeNodes.put("CIGAR",
		// GfvoVocab.Forward_Reference_Sequence_Frameshift.asNode());
		// //TODO:WRONG
		variantAttributeNodes.put("ClippingRankSum", BoinqVocab.Clipping_Rank_Sum.asNode());
		variantAttributeNodes.put("DB", GfvoVocab.External_Reference.asNode());
		variantAttributeNodes.put("DP", GfvoVocab.Coverage.asNode());
		variantAttributeNodes.put("FS", BoinqVocab.Fisher_Strand.asNode());
		variantAttributeNodes.put("H2", GfvoVocab.External_Reference.asNode());
		variantAttributeNodes.put("H3", GfvoVocab.External_Reference.asNode());
		variantAttributeNodes.put("MLEAC", BoinqVocab.Maximum_Likelihood_Expectation_Allele_Count.asNode());
		variantAttributeNodes.put("MLEAF", BoinqVocab.Maximum_Likelihood_Expectation_Allele_Frequency.asNode());
		variantAttributeNodes.put("MQ", GfvoVocab.Mapping_Quality.asNode());
		variantAttributeNodes.put("MQ0", GfvoVocab.Number_of_Reads.asNode());
		variantAttributeNodes.put("MQRankSum", BoinqVocab.Mapping_Quality_Rank_Sum.asNode());
		variantAttributeNodes.put("NS", GfvoVocab.Sample_Count.asNode());
		variantAttributeNodes.put("QD", BoinqVocab.Quality_By_Depth.asNode());
		variantAttributeNodes.put("ReadPosRankSum", BoinqVocab.Read_Position_Rank_Sum.asNode());
		variantAttributeNodes.put("SB", GfvoVocab.Note.asNode());
		variantAttributeNodes.put("SOMATIC", GfvoVocab.Somatic_Cell.asNode());
		variantAttributeNodes.put("VALIDATED", GfvoVocab.Experimental_Method.asNode());
		variantAttributeNodes.put("1000G", GfvoVocab.External_Reference.asNode());

		variantAttributeTypeNodes = new HashMap<>();
		variantAttributeTypeNodes.put("AA", XSDstring);
		variantAttributeTypeNodes.put("AC", XSDint);
		variantAttributeTypeNodes.put("AF", XSDfloat);
		variantAttributeTypeNodes.put("AN", XSDint);
		variantAttributeTypeNodes.put("BaseQRankSum", XSDfloat);
		variantAttributeTypeNodes.put("BQ", XSDfloat);
		variantAttributeTypeNodes.put("CIGAR", XSDstring);
		variantAttributeTypeNodes.put("ClippingRankSum", XSDfloat);
		variantAttributeTypeNodes.put("DB", XSDboolean);
		variantAttributeTypeNodes.put("DP", XSDint);
		variantAttributeTypeNodes.put("FS", XSDfloat);
		variantAttributeTypeNodes.put("H2", XSDboolean);
		variantAttributeTypeNodes.put("H3", XSDboolean);
		variantAttributeTypeNodes.put("MLEAC", XSDfloat);
		variantAttributeTypeNodes.put("MLEAF", XSDfloat);
		variantAttributeTypeNodes.put("MQ", XSDfloat);
		variantAttributeTypeNodes.put("MQ0", XSDfloat);
		variantAttributeTypeNodes.put("MQRankSum", XSDfloat);
		variantAttributeTypeNodes.put("NS", XSDfloat);
		variantAttributeTypeNodes.put("QD", XSDfloat);
		variantAttributeTypeNodes.put("ReadPosRankSum", XSDfloat);
		variantAttributeTypeNodes.put("SB", XSDstring);
		variantAttributeTypeNodes.put("SOMATIC", XSDboolean);
		variantAttributeTypeNodes.put("VALIDATED", XSDboolean);
		variantAttributeTypeNodes.put("1000G", XSDboolean);

		featureAttributeNodes = new HashMap<String, Node>();

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

	private int addVCFKeyValueTriples(List<Triple> triples, Node feature, String featureName,
			Map<String, Object> keyValues, int attributeCount) {
		for (String key : keyValues.keySet()) {
			String attributeFeatureName = featureName + "/attribute_" + attributeCount++;
			Node attributeFeature = tripleGenerator.generateURI(attributeFeatureName);
			triples.add(new Triple(feature, GfvoVocab.has_attribute.asNode(), attributeFeature));

			if (variantAttributeNodes.get(key) != null) {
				// TODO CREATE LINKS FOR EXTERNAL REFERENCES ->
				// GfvoVocab.refers_to.asNode()
				triples.add(new Triple(attributeFeature, RDF.type.asNode(), variantAttributeNodes.get(key)));
				if (variantAttributeTypeNodes.get(key) == XSDboolean) {
					triples.add(new Triple(attributeFeature, RDF.value.asNode(),
							NodeFactory.createLiteral(key, XSDstring)));
				} else {
					triples.add(new Triple(attributeFeature, RDF.value.asNode(), NodeFactory
							.createLiteral(keyValues.get(key).toString(), variantAttributeTypeNodes.get(key))));
				}
			} else {
				triples.add(
						new Triple(attributeFeature, RDFS.label.asNode(), NodeFactory.createLiteral(key, XSDstring)));
				triples.add(new Triple(attributeFeature, RDF.value.asNode(),
						NodeFactory.createLiteral(keyValues.get(key).toString(), XSDstring)));
			}
		}
		return attributeCount;
	}

	private int addGFFKeyValueTriples(List<Triple> triples, Node entry, String entryName, Map<String, String> keyValues,
			int attributeCount, Node feature, String featureName, Metadata meta) {
		for (String key : keyValues.keySet()) {
			String attributeFeatureName = entryName + "/attribute_" + attributeCount++;
			Node attributeFeature = tripleGenerator.generateURI(attributeFeatureName);
			triples.add(new Triple(entry, SoVocab.has_quality.asNode(), attributeFeature));

			if (featureAttributeNodes.get(key) != null) {

				triples.add(new Triple(attributeFeature, RDF.type.asNode(), featureAttributeNodes.get(key)));
				triples.add(new Triple(attributeFeature, RDF.value.asNode(),
						NodeFactory.createLiteral(keyValues.get(key).toString(), XSDstring)));

				// TODO implement CIGAR, Target,
			} else if (key.equalsIgnoreCase("Parent")) {
				String[] parents = keyValues.get(key).split(",");
				for (String parent : parents) {
					if (meta.featureIDmap.get(keyValues.get(key)) != null) {
						triples.add(new Triple(feature, SoVocab.has_part.asNode(), meta.featureIDmap.get(parent)));
						triples.add(new Triple(meta.featureIDmap.get(parent), SoVocab.part_of.asNode(), feature));
					}
				}
			} else if (key.equalsIgnoreCase("ID")) {
				triples.add(new Triple(feature, DCTerms.identifier.asNode(),
						NodeFactory.createLiteral(keyValues.get(key), XSDstring)));

				// idGenerator(triples,feature,featureName,entry.getAttributes().get("ID"));
				meta.featureIDmap.put(keyValues.get(key), feature);
			} else if (key.equalsIgnoreCase("Derives_from")) {
				if (meta.featureIDmap.get(keyValues.get(key)) != null) {
					triples.add(new Triple(feature, SoVocab.derives_from.asNode(), meta.featureIDmap.get(key)));
				}
			} else if (key.equalsIgnoreCase("Is_circular")) {
				triples.add(
						new Triple(attributeFeature, RDF.type.asNode(), (keyValues.get(key).equalsIgnoreCase("true"))
								? SoVocab.circular.asNode() : SoVocab.linear.asNode()));
			} else if (key.equalsIgnoreCase("Name")) {
				triples.add(new Triple(feature, RDFS.label.asNode(), NodeFactory.createLiteral(keyValues.get(key))));
			} else if (key.equalsIgnoreCase("Note")) {
				triples.add(new Triple(feature, RDFS.comment.asNode(), NodeFactory.createLiteral(keyValues.get(key))));
			} else if (key.equalsIgnoreCase("Alias")) {
				triples.add(new Triple(feature, SKOS.altLabel.asNode(), NodeFactory.createLiteral(keyValues.get(key))));
			} else if (key.equalsIgnoreCase("Dbxref")) {
				triples.add(new Triple(feature, RDFS.seeAlso.asNode(), NodeFactory.createLiteral(keyValues.get(key))));

			}

		}
		return attributeCount;
	}

	private void addFormatTriples(List<Triple> triples, Node feature, String featureName, Iterable<Genotype> iterable,
			Metadata meta) {
		int index = 1;
		for (Genotype evidence : iterable) {
			String sampleName = featureName + "/evidence_" + index++;
			Node sample = tripleGenerator.generateURI(sampleName);
			triples.add(new Triple(feature, GfvoVocab.has_evidence.asNode(), sample));
			// triples.add(new Triple(sample, RDF.type.asNode(),
			// GfvoVocab.Sample.asNode()));
			if (meta.sampleMap.get(evidence.getSampleName()) != null) {
				triples.add(new Triple(sample, GfvoVocab.has_source.asNode(),
						meta.sampleMap.get(evidence.getSampleName())));
			}

			String genotypeName = sampleName + "/genotype";
			Node genotype = tripleGenerator.generateURI(genotypeName);
			triples.add(new Triple(sample, GfvoVocab.has_attribute.asNode(), genotype));
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

			if (evidence.getGQ() != 0) {
				attributeGenerator(triples, genotype, genotypeName, String.valueOf(evidence.getGQ()), XSDint,
						GfvoVocab.Conditional_Genotype_Quality.asNode(), index1++);
			}
			if (evidence.getDP() != 0) {
				attributeGenerator(triples, genotype, genotypeName, String.valueOf(evidence.getDP()), XSDint,
						GfvoVocab.Coverage.asNode(), index1++);
			}
			for (int ad : evidence.getAD()) {
				// attributeGenerator(triples, genotype, genotypeName,
				// String.valueOf(ad), XSDint, GfvoVocab.Allelic_Depth.asNode(),
				// index1++);
			}
			for (int pl : evidence.getPL()) {
				// attributeGenerator(triples, genotype, genotypeName,
				// String.valueOf(pl), XSDint, GfvoVocab.Coverage.asNode(),
				// index1++);
			}

			Node firstpartGT = tripleGenerator.generateURI(genotypeName + "/first_part");
			Node lastpartGT = tripleGenerator.generateURI(genotypeName + "/last_part");
			triples.add(new Triple(genotype, GfvoVocab.has_first_part.asNode(), firstpartGT));
			triples.add(new Triple(genotype, GfvoVocab.has_last_part.asNode(), lastpartGT));
			if (evidence.getAllele(0).isReference()) {
				triples.add(new Triple(firstpartGT, RDF.type.asNode(), GfvoVocab.Reference_Sequence.asNode()));
			} else {
				triples.add(new Triple(firstpartGT, RDF.type.asNode(), GfvoVocab.Sequence_Variant.asNode()));
			}
			triples.add(new Triple(firstpartGT, RDF.value.asNode(),
					NodeFactory.createLiteral(evidence.getAllele(0).getBaseString(), XSDstring)));
			if (evidence.getAllele(1).isReference()) {
				triples.add(new Triple(lastpartGT, RDF.type.asNode(), GfvoVocab.Reference_Sequence.asNode()));
			} else {
				triples.add(new Triple(lastpartGT, RDF.type.asNode(), GfvoVocab.Sequence_Variant.asNode()));
			}
			triples.add(new Triple(lastpartGT, RDF.value.asNode(),
					NodeFactory.createLiteral(evidence.getAllele(0).getBaseString(), XSDstring)));

			String haplotypeName = genotypeName + "/haplotype";
			Node haplotype = tripleGenerator.generateURI(haplotypeName);
			triples.add(new Triple(genotype, GfvoVocab.has_attribute.asNode(), haplotype));
			triples.add(new Triple(haplotype, RDF.type.asNode(), GfvoVocab.Haplotype.asNode()));

			Node firstpartHT = tripleGenerator.generateURI(haplotypeName + "/first_part");
			Node lastpartHT = tripleGenerator.generateURI(haplotypeName + "/last_part");
			triples.add(new Triple(haplotype, GfvoVocab.has_first_part.asNode(), firstpartHT));
			triples.add(new Triple(haplotype, GfvoVocab.has_last_part.asNode(), lastpartHT));
			triples.add(new Triple(firstpartHT, RDF.type.asNode(), GfvoVocab.Phred_Score.asNode()));
			triples.add(new Triple(lastpartHT, RDF.type.asNode(), GfvoVocab.Phred_Score.asNode()));
			//
			// htsjdk kan HQ nog niet extraheren

		}

	}

	private int addAlleleTriples(List<Triple> triples, Node feature, String featureName, VariantContext variant,
			int attributeCount) {

		if (variant.getReference() != null && variant.getReference().getBaseString() != null) {
			attributeGenerator(triples, feature, featureName, variant.getReference().getBaseString(), XSDstring,
					GfvoVocab.Reference_Sequence.asNode(), attributeCount++);
		}
		for (Allele all : variant.getAlternateAlleles()) {
			if (all.getBaseString() != null) {
				String attributeName = attributeNGenerator(triples, feature, featureName, all.getBaseString(),
						XSDstring, GfvoVocab.Sequence_Variant.asNode(), attributeCount++);
				Node attributeNameNode = tripleGenerator.generateURI(attributeName);
				attributeGenerator(triples, attributeNameNode, attributeName,
						String.valueOf(variant.getPhredScaledQual()), XSDdouble, GfvoVocab.Phred_Score.asNode(), 1);

			}
		}
		return attributeCount;
	}

	public List<Triple> convert(VariantContext entry, int start, Metadata meta) {
		// TODO: CONTIG
		// TODO: ONTOLOGIES
		List<Triple> triples = new LinkedList<Triple>();
		int attributeCount = 1;
		if (meta.tripleCount == 0) {
			createVCFEntities(triples, entry, meta);
		}
		// VCFentry
		String VCFentryName = ENTRYBASEURI + meta.sumEntryCount;
		Node VCFentry = tripleGenerator.generateURI(VCFentryName);
		triples.add(new Triple(VCFentry, RDF.type.asNode(), FormatVocab.VCF_Entry.asNode()));

		// FEATURE
		String featureName = FEATUREBASEURI + ++meta.sumFeatureCount;
		Node feature = tripleGenerator.generateURI(featureName);
		triples.add(new Triple(VCFentry, FormatVocab.defines.asNode(), feature));
		triples.add(new Triple(feature, RDF.type.asNode(), GfvoVocab.Feature.asNode()));

		if (entry.getID() != ".") {
			idGenerator(triples, feature, featureName, entry.getID());
		}
		for (String filter : entry.getFilters()) {
			if (meta.readMap.get(filter) != null) {
				triples.add(new Triple(feature, GfvoVocab.is_refuted_by.asNode(), meta.readMap.get(filter)));
			} else {
				String filterNodeName = FILTERBASEURI + ++meta.sumFilterCount;
				Node filterNode = tripleGenerator.generateURI(filterNodeName);
				meta.readMap.put(filter, filterNode);
				triples.add(new Triple(filterNode, RDF.type.asNode(), GfvoVocab.Variant_Calling.asNode()));
				// attributeGenerator(triples, filterNode, filterNodeName,
				// filter, XSDstring, GfvoVocab.Label.asNode(), 1);
				triples.add(new Triple(filterNode, RDFS.label.asNode(), NodeFactory.createLiteral(filter)));
				triples.add(new Triple(feature, GfvoVocab.is_refuted_by.asNode(), meta.readMap.get(filter)));

			}

		}
		attributeCount = addAlleleTriples(triples, feature, featureName, entry, attributeCount);
		attributeCount = addVCFKeyValueTriples(triples, feature, featureName, entry.getAttributes(), attributeCount);
		addFormatTriples(triples, feature, featureName, entry.getGenotypes(), meta);

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
			triples.add(new Triple(feature, RDF.type.asNode(), SoVocab.SNP.asNode()));
			meta.typeList.add(SoVocab.SNP.asNode());
			break;
		}

		// LOCATION
		addFaldoTriples(feature, Long.valueOf(entry.getStart()), Long.valueOf(entry.getEnd()), entry.getContig(), true,
				triples, meta);

		return triples;
	}

	public List<Triple> convert(Feature entry, Metadata meta) {
		int attributeCount = 1;
		List<Triple> triples = new LinkedList<Triple>();

		// GFF_ENTRY
		String GFFentryName = (ENTRYBASEURI + meta.sumEntryCount);
		Node GFFentry = tripleGenerator.generateURI(GFFentryName);
		triples.add(new Triple(GFFentry, RDF.type.asNode(), FormatVocab.GFF_Entry.asNode()));

		if (entry.getAttributes().containsKey("Target")) {
			attributeGenerator(triples, GFFentry, GFFentryName, String.valueOf(entry.getAttributes().get("Target")),
					XSDstring, FormatVocab.Target.asNode(), attributeCount++);
			attributeGenerator(triples, GFFentry, GFFentryName, String.valueOf(entry.getAttributes().get("Gap")),
					XSDstring, FormatVocab.Gap.asNode(), attributeCount++);

		}
		if (entry.getSource() != null) {
			attributeGenerator(triples, GFFentry, GFFentryName, entry.getSource(), XSDstring,
					GfvoVocab.has_source.asNode(), attributeCount++);
		}

		// FEATURE
		attributeCount = 1;
		String featureName = FEATUREBASEURI + ++meta.sumFeatureCount;
		Node feature = tripleGenerator.generateURI(featureName);
		triples.add(new Triple(GFFentry, FormatVocab.defines.asNode(), feature));

		if (entry.getScore() != 0.0) {
			attributeGenerator(triples, feature, featureName, String.valueOf(entry.getScore()), XSDdouble,
					SoVocab.score.asNode(), attributeCount++);
		}

		if (entry.getPhase() != -1) {
			attributeGenerator(triples, feature, featureName, String.valueOf(entry.getPhase() + 1), XSDint,
					SoVocab.reading_frame.asNode(), attributeCount++);
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
		attributeCount = addGFFKeyValueTriples(triples, GFFentry, GFFentryName, entry.getAttributes(), attributeCount,
				feature, featureName, meta);

		// LOCATION
		addFaldoTriples(feature, Long.valueOf(entry.getStart()), Long.valueOf(entry.getEnd()), entry.getSequenceID(),
				entry.getStrand(), triples, meta);

		return triples;
	}

	public List<Triple> convert(BEDFeature entry, Metadata meta) {
		List<Triple> triples = new LinkedList<Triple>();
		int attributeCount = 1;
		// BEDentry
		String BEDentryName = ENTRYBASEURI + Long.toString(meta.sumEntryCount);
		Node BEDentry = tripleGenerator.generateURI(BEDentryName);
		triples.add(new Triple(BEDentry, RDF.type.asNode(), FormatVocab.BED_Entry.asNode()));

		if (entry.getColor() != null) {
			String attributeName = attributeNGenerator(triples, BEDentry, BEDentryName,
					String.valueOf(entry.getColor()), XSDstring, FormatVocab.RGBcolor.asNode(), attributeCount++);
			Node attributeNameNode = tripleGenerator.generateURI(attributeName);
			attributeGeneratorSO(triples, attributeNameNode, attributeName, String.valueOf(entry.getColor().getBlue()),
					XSDint, FormatVocab.RGBblue.asNode(), attributeCount++);
			attributeGeneratorSO(triples, attributeNameNode, attributeName, String.valueOf(entry.getColor().getGreen()),
					XSDint, FormatVocab.RGBgreen.asNode(), attributeCount++);
			attributeGeneratorSO(triples, attributeNameNode, attributeName, String.valueOf(entry.getColor().getRed()),
					XSDint, FormatVocab.RGBred.asNode(), attributeCount++);
		}
		// FEATURE
		attributeCount = 1;
		String featureName = FEATUREBASEURI + ++meta.sumFeatureCount;
		Node feature = tripleGenerator.generateURI(featureName);
		triples.add(new Triple(BEDentry, FormatVocab.defines.asNode(), feature));
		if (meta.mainType != null) {
			triples.add(new Triple(feature, RDF.type.asNode(), meta.mainType));
		}

		if (entry.getName() != null && entry.getName().length() > 0) {
			triples.add(
					new Triple(feature, RDFS.label.asNode(), NodeFactory.createLiteral(entry.getName(), XSDstring)));
		}

		if (entry.getDescription() != null && entry.getDescription().length() > 0) {
			triples.add(new Triple(feature, DCTerms.description.asNode(),
					NodeFactory.createLiteral(entry.getDescription(), XSDstring)));
		}
		Float score = entry.getScore();
		if (score != null && score != 0) {
			attributeGeneratorSO(triples, feature, featureName, score.toString(), XSDdouble, SoVocab.score.asNode(),
					attributeCount++);

		}

		addFaldoTriples(feature, Long.valueOf(entry.getStart()), Long.valueOf(entry.getEnd()), entry.getContig(),
				entry.getStrand() == Strand.POSITIVE, triples, meta);

		int idx = 1;
		for (Exon exon : entry.getExons()) {
			String subFeatureName = FEATUREBASEURI + ++meta.sumFeatureCount;
			Node subFeature = tripleGenerator.generateURI(subFeatureName);
			triples.add(new Triple(BEDentry, FormatVocab.defines.asNode(), subFeature));
			if (meta.subType != null) {
				triples.add(new Triple(subFeature, RDF.type.asNode(), meta.subType));
			}
			triples.add(new Triple(subFeature, SoVocab.has_integral_part.asNode(), feature));
			triples.add(new Triple(feature, SoVocab.has_part.asNode(), subFeature));
			triples.add(new Triple(subFeature, RDFS.label.asNode(),
					NodeFactory.createLiteral(entry.getName() + "_BLOCK#" + idx++, XSDstring)));
			addFaldoTriples(subFeature, Long.valueOf(exon.getCdStart()), Long.valueOf(exon.getCdEnd()),
					entry.getContig(), entry.getStrand() == Strand.POSITIVE, triples, meta);
			// BUG TICKBOXES HAVE TO SPAN WHOLE GENE AREA, OTHERWHISE REGIONS OF
			// EXONS ARE WRONG
		}
		return triples;
	}

	public List<Triple> convert(SAMRecord entry, Metadata meta) {
		int attributeCount = 1;
		List<Triple> triples = new LinkedList<Triple>();
		/*
		 * if (meta.tripleCount == 0){ createSAMEntities(triples, entry, meta);
		 * }
		 */
		// SAMEntry
		String SAMentryName = ENTRYBASEURI + Long.toString(meta.sumEntryCount);
		Node SAMentry = tripleGenerator.generateURI(SAMentryName);
		triples.add(new Triple(SAMentry, RDF.type.asNode(), FormatVocab.SAM_Entry.asNode()));

		// FLAG: each bit explains different description
		attributeGeneratorSO(triples, SAMentry, SAMentryName, String.valueOf(entry.getFlags()), XSDint,
				FormatVocab.FLAG.asNode(), attributeCount++);
		// RNAME: Reference sequence name of the alignment
		// attributeGeneratorSO(triples, SAMentry, SAMentryName,
		// entry.getReferenceName(), XSDstring, FormatVocab.RNAME.asNode(),
		// attributeCount++);
		// MAPQ: mapping quality -10logBase
		// attributeGeneratorSO(triples, SAMentry, SAMentryName,
		// String.valueOf(entry.getMappingQuality()), XSDint,
		// FormatVocab.MAPQ.asNode(), attributeCount++);
		// CIGAR: CIGAR String
		attributeGeneratorSO(triples, SAMentry, SAMentryName, entry.getCigarString(), XSDstring,
				FormatVocab.CIGAR.asNode(), attributeCount++);
		// RNEXT: Reference sequence name of the NEXT read in the template
		attributeGeneratorSO(triples, SAMentry, SAMentryName, entry.getMateReferenceName(), XSDstring,
				FormatVocab.RNEXT.asNode(), attributeCount++);
		// PNEXT: Position of the primary alignment of the NEXT read. not
		// necessary -> positions are iterated at end of function
		// TLEN: signed observed Template LENgth.
		attributeGeneratorSO(triples, SAMentry, SAMentryName, String.valueOf(entry.getInferredInsertSize()), XSDint,
				FormatVocab.TLEN.asNode(), attributeCount++);
		// SEQ: segment Sequence
		// attributeGeneratorSO(triples, SAMentry, SAMentryName,
		// entry.getReadString(), XSDstring, FormatVocab.SEQ.asNode(),
		// attributeCount++);
		// QUAL: base quality plus 33
		attributeGeneratorSO(triples, SAMentry, SAMentryName, String.valueOf(entry.getBaseQualityString()), XSDstring,
				FormatVocab.QUAL.asNode(), attributeCount++);
		// ATTRIBUTES
		for (SAMTagAndValue attribute : entry.getAttributes()) {
			// TODO not implemented enough in htsjdk
			// How to get class from object?
			// String key = attribute.tag;
			// String value = (String)attribute.value;
		}

		// FEATURE
		attributeCount = 1;
		String featureName = FEATUREBASEURI + ++meta.sumFeatureCount;
		Node feature = tripleGenerator.generateURI(featureName);
		triples.add(new Triple(SAMentry, FormatVocab.defines.asNode(), feature));
		String RName = entry.getReadName();
		if (meta.readMap.get(RName) != null && entry.getMateReferenceName() != null) {
			triples.add(new Triple(meta.readMap.get(RName), SoVocab.evidence_for_feature.asNode(), feature));
		} else {
			String readNodeName = READBASEURI + ++meta.sumReadCount;
			Node readNode = tripleGenerator.generateURI(readNodeName);
			meta.readMap.put(RName, readNode);
			triples.add(new Triple(readNode, RDF.type.asNode(), SoVocab.read.asNode()));
			triples.add(new Triple(readNode, RDFS.label.asNode(), NodeFactory.createLiteral(RName)));
			triples.add(new Triple(meta.readMap.get(RName), SoVocab.evidence_for_feature.asNode(), feature));

		}
		// triples.add(new Triple(feature, RDF.type.asNode(),
		// SoVocab.read.asNode()));
		triples.add(
				new Triple(feature, RDFS.label.asNode(), NodeFactory.createLiteral(entry.getReadName(), XSDstring)));

		if (entry.getMappingQuality() != 255) {
			attributeGeneratorSO(triples, feature, featureName, String.valueOf(entry.getMappingQuality() - 33), XSDint,
					FormatVocab.MAPQ.asNode(), attributeCount++);
		}
		if (entry.getReadString() != null) {
			String featureAttributeName = featureName + "/attribute_" + attributeCount++;
			Node featureAttribute = tripleGenerator.generateURI(featureAttributeName);
			triples.add(new Triple(feature, SoVocab.has_quality.asNode(), featureAttribute));
			triples.add(new Triple(featureAttribute, RDF.value.asNode(),
					NodeFactory.createLiteral(entry.getReadString(), XSDstring)));
			triples.add(new Triple(featureAttribute, RDF.type.asNode(), SoVocab.sequence_motif.asNode()));

			if (entry.getBaseQualityString() != "*") {
				triples.add(new Triple(featureAttribute, RDFS.comment.asNode(),
						NodeFactory.createLiteral(entry.getBaseQualityString(), XSDstring)));
			}
		}

		// LOCATION

		addFaldoTriples(feature, new Long(entry.getStart()), new Long(entry.getEnd()), entry.getContig(),
				!entry.getReadNegativeStrandFlag(), triples, meta);

		int childno = 1;
		for (AlignmentBlock block : entry.getAlignmentBlocks()) {
			String subFeatureName = featureName + "_Block_" + childno++;
			Node subFeature = tripleGenerator.generateURI(subFeatureName);
			triples.add(new Triple(subFeature, RDF.type.asNode(), SoVocab.nucleotide_match.asNode()));
			triples.add(new Triple(feature, SoVocab.evidence_for_feature.asNode(), subFeature));
			attributeGeneratorSO(triples, subFeature, subFeatureName,
					entry.getReadString().substring(block.getReadStart() - 1,
							block.getReadStart() - 1 + block.getLength()),
					XSDstring, SoVocab.sequence_motif.asNode(), 1);
					/*
					 * if(meta.referenceMap.get(entry.getReferenceName())!=null)
					 * { triples.add(new Triple(subFeature,
					 * SoVocab.part_of.asNode(),
					 * meta.referenceMap.get(entry.getReferenceName())));
					 * triples.add(new
					 * Triple(meta.referenceMap.get(entry.getReferenceName()),
					 * SoVocab.has_part.asNode(), subFeature)); }
					 */

			// POSITIONS
			addFaldoTriples(subFeature, new Long(block.getReferenceStart()),
					new Long(block.getReferenceStart() + block.getLength() - 1), entry.getContig(),
					!entry.getReadNegativeStrandFlag(), triples, meta);

		}
		return triples;
	}

	private void idGenerator(List<Triple> triples, Node feature, String featureName, String ID) {
		Node idNode = tripleGenerator.generateURI(featureName + "_ID");
		triples.add(new Triple(feature, GfvoVocab.has_identifier.asNode(), idNode));
		triples.add(new Triple(idNode, RDF.type.asNode(), GfvoVocab.Identifier.asNode()));
		triples.add(new Triple(idNode, RDF.value.asNode(), NodeFactory.createLiteral(ID)));

	}

	private String attributeNGenerator(List<Triple> triples, Node feature, String featureName, String value,
			XSDDatatype type, Node attributeType, int attributeCount) {
		String featureAttributeName = featureName + "/attribute_" + attributeCount;
		Node featureAttribute = tripleGenerator.generateURI(featureAttributeName);
		triples.add(new Triple(feature, GfvoVocab.has_attribute.asNode(), featureAttribute));
		triples.add(new Triple(featureAttribute, RDF.type.asNode(), attributeType));
		triples.add(new Triple(featureAttribute, RDF.value.asNode(), NodeFactory.createLiteral(value, type)));
		return featureAttributeName;
	}

	// TODO CHECK GFVO.HAS_VALUE.ASNODE() <-> RDF.VALUE.ASNODE()
	private void attributeGenerator(List<Triple> triples, Node feature, String featureName, String value,
			XSDDatatype type, Node attributeType, int attributeCount) {
		String featureAttributeName = featureName + "/attribute_" + attributeCount;
		Node featureAttribute = tripleGenerator.generateURI(featureAttributeName);
		triples.add(new Triple(feature, GfvoVocab.has_attribute.asNode(), featureAttribute));
		triples.add(new Triple(featureAttribute, RDF.type.asNode(), attributeType));
		triples.add(new Triple(featureAttribute, RDF.value.asNode(), NodeFactory.createLiteral(value, type)));
	}

	private void attributeGeneratorSO(List<Triple> triples, Node feature, String featureName, String value,
			XSDDatatype type, Node attributeType, int attributeCount) {
		String featureAttributeName = featureName + "/attribute_" + attributeCount;
		Node featureAttribute = tripleGenerator.generateURI(featureAttributeName);
		triples.add(new Triple(feature, SoVocab.has_quality.asNode(), featureAttribute));
		triples.add(new Triple(featureAttribute, RDF.type.asNode(), attributeType));
		triples.add(new Triple(featureAttribute, RDF.value.asNode(), NodeFactory.createLiteral(value, type)));
	}

	private void attributeGeneratorMD(List<Triple> triples, Node feature, String featureName, String value,
			XSDDatatype type, Node attributeType, int attributeCount) {
		String featureAttributeName = featureName + "/attribute_" + attributeCount;
		Node featureAttribute = tripleGenerator.generateURI(featureAttributeName);
		triples.add(new Triple(feature, TrackVocab.hasAttribute.asNode(), featureAttribute));
		triples.add(new Triple(featureAttribute, RDF.type.asNode(), attributeType));
		triples.add(new Triple(featureAttribute, RDF.value.asNode(), NodeFactory.createLiteral(value, type)));
	}

	private void addFaldoTriples(Node feature, Long biologicalStartPosBase1, Long biologicalEndPosBase1, String contig,
			Boolean forwardStrand, List<Triple> triples, Metadata meta) {
		if (meta.fileType.equalsIgnoreCase("BED") || meta.fileType.equalsIgnoreCase("BAM")) {
			biologicalStartPosBase1++;
		}
		// TODO Introduce Assembly
		contig = contig.substring(meta.prefixLength);
		String contigName = LOCATIONBASEURI + meta.organismMapping + contig;
		Node reference = tripleGenerator.generateURI(contigName);
		String featureLocationName = contigName + ":" + biologicalStartPosBase1 + "-" + biologicalEndPosBase1;
		String featureBeginName = contigName + ":" + biologicalStartPosBase1;
		String featureEndName = contigName + ":" + biologicalEndPosBase1;
		if (forwardStrand != null) {
			String add = (forwardStrand) ? ":1" : ":-1";
			featureLocationName += add;
			featureBeginName += add;
			featureEndName += add;
		}
		Node featureLocation = tripleGenerator.generateURI(featureLocationName);
		Node featureBegin = tripleGenerator.generateURI(featureBeginName);
		Node featureEnd = tripleGenerator.generateURI(featureEndName);
		triples.add(new Triple(feature, location.asNode(), featureLocation));
		triples.add(new Triple(featureLocation, FaldoVocab.reference.asNode(), reference));
		triples.add(new Triple(featureLocation, RDF.type.asNode(), FaldoVocab.Region.asNode()));
		triples.add(new Triple(featureLocation, begin.asNode(), featureBegin));
		triples.add(new Triple(featureLocation, end.asNode(), featureEnd));
		triples.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));
		triples.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));

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

	}

	public List<Triple> headerIterator(List<Triple> triples, Iterator<String> it, Node fileNode, Node attributeNode) {
		int index = 1;
		while (it.hasNext()) {
			String header = it.next();
			String headerNodeName = ("HL_" + index++);
			Node headerNode = tripleGenerator.generateURI(headerNodeName);
			triples.add(new Triple(fileNode, TrackVocab.hasAttribute.asNode(), headerNode));
			triples.add(new Triple(headerNode, RDF.type.asNode(), attributeNode));
			triples.add(new Triple(headerNode, RDF.value.asNode(), NodeFactory.createLiteral(header)));
		}
		return triples;
	}

	public List<Triple> createMetadata(Metadata metadata, String Graphname) {
		int attributeCount = 1;
		List<Triple> triples = new LinkedList<Triple>();
		Node Graph = NodeFactory.createURI(Graphname);
		String fileNodeName = metadata.file.replace("\\", "/").replace(" ", "_");
		Node fileNode = tripleGenerator.generateURI(fileNodeName);
		triples.add(new Triple(Graph, TrackVocab.holds.asNode(), fileNode));
		triples.add(new Triple(Graph, RDF.type.asNode(), TrackVocab.File.asNode()));

		attributeGeneratorMD(triples, fileNode, fileNodeName, metadata.date, XSDstring,
				TrackVocab.ConversionDate.asNode(), attributeCount++);
		attributeGeneratorMD(triples, fileNode, fileNodeName, Long.toString(metadata.entryCount), XSDint,
				TrackVocab.EntryCount.asNode(), attributeCount++);
		attributeGeneratorMD(triples, fileNode, fileNodeName, Long.toString(metadata.featureCount), XSDint,
				TrackVocab.FeatureCount.asNode(), attributeCount++);
		attributeGeneratorMD(triples, fileNode, fileNodeName, Long.toString(metadata.tripleCount), XSDint,
				TrackVocab.TripleCount.asNode(), attributeCount++);
		attributeGeneratorMD(triples, fileNode, fileNodeName,
				Long.toString(metadata.sumFilterCount - metadata.filterCount), XSDint, TrackVocab.FilterCount.asNode(),
				attributeCount++);
		attributeGeneratorMD(triples, fileNode, fileNodeName,
				Long.toString(metadata.sumSampleCount - metadata.sampleCount), XSDint, TrackVocab.SampleCount.asNode(),
				attributeCount++);
		attributeGeneratorMD(triples, fileNode, fileNodeName, Long.toString(metadata.sumReadCount - metadata.readCount),
				XSDint, TrackVocab.ReadCount.asNode(), attributeCount++);
		attributeGeneratorMD(triples, fileNode, fileNodeName, metadata.fileType, XSDstring,
				TrackVocab.FileExtension.asNode(), attributeCount++);
		attributeGeneratorMD(triples, fileNode, fileNodeName, metadata.fileName, XSDstring,
				TrackVocab.FileName.asNode(), attributeCount++);

		if ((metadata.fileType.equalsIgnoreCase("GFF3") || metadata.fileType.equalsIgnoreCase("GTF"))
				&& metadata.gffHeader != null) {
			Iterator<String> gffIt = metadata.gffHeader.iterator();
			headerIterator(triples, gffIt, fileNode, TrackVocab.HeaderGFF.asNode());
		}
		if (metadata.fileType.equalsIgnoreCase("BED") && metadata.bedHeader != null) {
			Iterator<String> bedIt = metadata.bedHeader.iterator();
			headerIterator(triples, bedIt, fileNode, TrackVocab.HeaderBED.asNode());

		}
		if ((metadata.fileType.equalsIgnoreCase("BAM") || metadata.fileType.equalsIgnoreCase("SAM"))
				&& metadata.samHeader != null) {
			metadata.typeList.add(SoVocab.read.asNode());
			metadata.typeList.add(SoVocab.nucleotide_match.asNode());

			// TODO Can be parsed further, ontologies needed
			Iterator<SAMProgramRecord> samit = metadata.samHeader.getProgramRecords().iterator();
			// headerIterator(triples, samit, fileNode);
		}

		if (metadata.fileType.equalsIgnoreCase("VCF") && metadata.vcfHeader != null) {
			int index = 1;
			Iterator<VCFFilterHeaderLine> filterIt = metadata.vcfHeader.getFilterLines().iterator();
			while (filterIt.hasNext()) {
				VCFFilterHeaderLine header = filterIt.next();
				String headerNodeName = (fileNodeName + "_FILTER_" + index++);
				Node headerNode = tripleGenerator.generateURI(headerNodeName);
				triples.add(new Triple(fileNode, GfvoVocab.has_member.asNode(), headerNode));
				triples.add(new Triple(headerNode, RDF.type.asNode(), GfvoVocab.Variant_Calling.asNode()));
				// attributeGenerator(triples, headerNode, headerNodeName,
				// header.getID(), XSDstring, GfvoVocab.Label.asNode(), 1);
				// attributeGenerator(triples, headerNode, headerNodeName,
				// header.getValue(), XSDstring, GfvoVocab.Comment.asNode(), 2);

				// triples.add(new Triple(headerNode, RDFS.label.asNode(),
				// NodeFactory.createLiteral(header.getDescription())));
				triples.add(
						new Triple(headerNode, RDFS.comment.asNode(), NodeFactory.createLiteral(header.toString())));

			}
			Iterator<VCFFormatHeaderLine> formatIt = metadata.vcfHeader.getFormatHeaderLines().iterator();
			index = 1;
			while (formatIt.hasNext()) {
				VCFFormatHeaderLine header = formatIt.next();
				String headerNodeName = (fileNodeName + "_FORMAT_" + index++);
				Node headerNode = tripleGenerator.generateURI(headerNodeName);
				triples.add(new Triple(fileNode, GfvoVocab.has_member.asNode(), headerNode));
				triples.add(new Triple(headerNode, RDF.type.asNode(), GfvoVocab.Comment.asNode()));
				triples.add(
						new Triple(headerNode, RDF.value.asNode(), NodeFactory.createLiteral(header.getDescription())));
				triples.add(new Triple(headerNode, RDFS.label.asNode(), NodeFactory.createLiteral(header.getID())));

				// attributeGenerator(triples, headerNode, headerNodeName,
				// header.getID(), XSDstring, GfvoVocab.Identifier.asNode(), 1);
			}
			Iterator<VCFInfoHeaderLine> infoIt = metadata.vcfHeader.getInfoHeaderLines().iterator();
			index = 1;
			while (infoIt.hasNext()) {
				VCFInfoHeaderLine header = infoIt.next();
				String headerNodeName = (fileNodeName + "_INFO_" + index++);
				Node headerNode = tripleGenerator.generateURI(headerNodeName);
				triples.add(new Triple(fileNode, GfvoVocab.has_member.asNode(), headerNode));
				triples.add(new Triple(headerNode, RDF.type.asNode(), GfvoVocab.Comment.asNode()));
				triples.add(
						new Triple(headerNode, RDF.value.asNode(), NodeFactory.createLiteral(header.getDescription())));
				triples.add(new Triple(headerNode, RDFS.label.asNode(), NodeFactory.createLiteral(header.getID())));

				// attributeGenerator(triples, headerNode, headerNodeName,
				// header.getID(), XSDstring, GfvoVocab.Identifier.asNode(), 1);
			}
			Iterator<VCFHeaderLine> metadataIt = metadata.vcfHeader.getOtherHeaderLines().iterator();
			// TODO Can be parsed further
			index = 1;
			while (metadataIt.hasNext()) {
				VCFHeaderLine header = metadataIt.next();
				String headerNodeName = ("_HL_" + index++);
				Node headerNode = tripleGenerator.generateURI(headerNodeName);
				triples.add(new Triple(fileNode, GfvoVocab.has_member.asNode(), headerNode));
				triples.add(new Triple(headerNode, RDF.type.asNode(), GfvoVocab.Comment.asNode()));
				triples.add(new Triple(headerNode, RDF.value.asNode(), NodeFactory.createLiteral(header.getValue())));

			}
		}

		Set<Node> uniqueTypes = new HashSet<Node>(metadata.typeList);
		for (Node x : uniqueTypes) {
			// triples.add(new Triple(Graph, TrackVocab.holds.asNode(), x));
			triples.add(new Triple(fileNode, TrackVocab.holds.asNode(), x));
		}

		return triples;
	}

	public Metadata createVCFEntities(List<Triple> triples, VariantContext entry, Metadata meta) {
		for (VCFFilterHeaderLine filterHeader : meta.vcfHeader.getFilterLines()) {
			String filterName = FILTERBASEURI + ++meta.sumFilterCount;
			Node filter = tripleGenerator.generateURI(filterName);
			meta.readMap.put(filterHeader.getID(), filter);
			triples.add(new Triple(filter, RDF.type.asNode(), GfvoVocab.Variant_Calling.asNode()));
			triples.add(new Triple(filter, RDFS.label.asNode(), NodeFactory.createLiteral(filterHeader.getID())));
			// triples.add(new Triple(filterNode, RDFS.comment.asNode(),
			// NodeFactory.createLiteral(filterHeader.getDescription())));

			// attributeGenerator(triples, filterNode, filterNodeName,
			// filterHeader.getID(), XSDstring, GfvoVocab.Label.asNode(), 1);
			// attributeGenerator(triples, filterNode, filterNodeName,
			// filterHeader.getValue(), XSDstring, GfvoVocab.Comment.asNode(),
			// 2);
		}
		for (String name : entry.getSampleNames()) {
			String sampleName = SAMPLEBASEURI + ++meta.sumSampleCount;
			Node sample = tripleGenerator.generateURI(sampleName);
			meta.sampleMap.put(name, sample);
			triples.add(new Triple(sample, RDF.type.asNode(), GfvoVocab.Biological_Entity.asNode()));
			triples.add(new Triple(sample, RDFS.label.asNode(), NodeFactory.createLiteral(name)));

			// attributeGenerator(triples, sampleNode, sampleNodeName, name,
			// XSDstring, GfvoVocab.Identifier.asNode(), 1);

		}
		for (VCFFormatHeaderLine formatHeader : meta.vcfHeader.getFormatHeaderLines()) {
			meta.formatMap.put(formatHeader.getID(), formatHeader.getType().toString());
		}

		return meta;
	}
	/*
	 * public Metadata createSAMEntities(List<Triple> triples, SAMRecord entry,
	 * Metadata meta){
	 * 
	 * for(SAMSequenceRecord SAMSeq:
	 * meta.samHeader.getSequenceDictionary().getSequences()){ String
	 * referenceName = REFERENCEBASEURI + ++meta.sumSampleCount;//TODO
	 * referencecount Node reference =
	 * tripleGenerator.generateURI(referenceName);
	 * meta.referenceMap.put(SAMSeq.getSequenceName(), reference);
	 * 
	 * triples.add(new Triple(reference, RDF.type.asNode(),
	 * SoVocab.region.asNode()));
	 * 
	 * triples.add(new Triple(reference, RDFS.label.asNode(),
	 * NodeFactory.createLiteral(SAMSeq.getSequenceName())));
	 * if(SAMSeq.getAssembly()!=null){ Node assembly =
	 * tripleGenerator.generateURI(ASSEMBLYBASEURI+ SAMSeq.getAssembly());
	 * triples.add(new Triple(reference, SoVocab.sequence_of.asNode(),
	 * assembly)); triples.add(new Triple(assembly,
	 * RDF.type.asNode(),SoVocab.assembly.asNode())); triples.add(new
	 * Triple(assembly, RDFS.label.asNode(),
	 * NodeFactory.createLiteral(SAMSeq.getAssembly(),XSDstring))); } } return
	 * meta; }
	 */

	public List<Triple> convert(FaldoFeature faldoFeature) {
		List<Triple> result = new LinkedList<Triple>();
		// feature is the subject of all following triples except the FALDO
		// exact position triples
		String featureName = FEATUREBASEURI + faldoFeature.id;
		Node feature = NodeFactory.createURI(featureName);

		Node ref = NodeFactory.createURI(faldoFeature.assembly);

		Node featureBegin = NodeFactory.createURI(LOCATIONBASEURI + faldoFeature.id);
		Node featureEnd = NodeFactory.createURI(LOCATIONBASEURI + faldoFeature.id);
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
