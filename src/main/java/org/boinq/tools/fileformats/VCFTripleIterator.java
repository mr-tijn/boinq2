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
import org.boinq.service.TripleGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.boinq.generated.vocabularies.BoinqVocab;
import org.boinq.generated.vocabularies.FormatVocab;
import org.boinq.generated.vocabularies.GfvoVocab;
import org.boinq.generated.vocabularies.SoVocab;

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

public class VCFTripleIterator extends TripleBuilder implements Iterator<Triple>  {
	
	public static final String FILTERBASEURI = "/resource/filter#";
	public static final String SAMPLEBASEURI = "/resource/sample#";

	private Map<String, Node> variantAttributeNodes;
	private Map<String, XSDDatatype> variantAttributeTypeNodes;

	private static Logger log = LoggerFactory.getLogger(VCFTripleIterator.class);
	private AsciiLineReaderIterator lineIterator;
	private List<Triple> currentTriples = new LinkedList<Triple>();
	private VCFCodec codec = new VCFCodec();
	private Metadata meta;
	
	public VCFTripleIterator(TripleGeneratorService tripleGenerator, File file, Map<Node, Node> referenceMap, Metadata meta) throws FileNotFoundException, IOException {
		super(tripleGenerator);
		initData();
		lineIterator = new AsciiLineReaderIterator(AsciiLineReader.from(new FileInputStream(file)));
		try {
			initializeVCFMetadata(currentTriples, meta);
		} catch (Exception e) {
			log.error("Malformed Header" , e);
		}
       this.meta = meta;
	}

	
	private void initData() {
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
	}
	
	public List<Triple> convert(VariantContext entry, int start, Metadata meta) {
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

		if (entry.getID() != ".") {
			idGenerator(triples, feature, featureName, entry.getID());
			triples.add(new Triple(feature, DCTerms.identifier.asNode(), NodeFactory.createLiteral(entry.getID(),XSDstring)));
		}
		for (String filter : entry.getFilters()) {
			if (meta.readMap.get(filter) != null) {
				triples.add(new Triple(feature, GfvoVocab.is_refuted_by.asNode(), meta.readMap.get(filter)));
			} else {
				String filterNodeName = FILTERBASEURI + ++meta.filterCount;
				Node filterNode = tripleGenerator.generateURI(filterNodeName);
				meta.readMap.put(filter, filterNode);
				triples.add(new Triple(filterNode, RDF.type.asNode(), GfvoVocab.Variant_Calling.asNode()));
				// attributeNGenerator(triples, filterNode, filterNodeName,
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
		super.addFaldoTriples(feature, Long.valueOf(entry.getStart()), Long.valueOf(entry.getEnd()), entry.getContig(), true,
				triples, meta);

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
				generateAttribute(triples, genotype, genotypeName, String.valueOf(evidence.getGQ()), XSDint,
						GfvoVocab.Conditional_Genotype_Quality.asNode(), index1++);
			}
			if (evidence.getDP() != 0) {
				generateAttribute(triples, genotype, genotypeName, String.valueOf(evidence.getDP()), XSDint,
						GfvoVocab.Coverage.asNode(), index1++);
			}
			for (int ad : evidence.getAD()) {
				// attributeNGenerator(triples, genotype, genotypeName,
				// String.valueOf(ad), XSDint, GfvoVocab.Allelic_Depth.asNode(),
				// index1++);
			}
			for (int pl : evidence.getPL()) {
				// attributeNGenerator(triples, genotype, genotypeName,
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

	public void initializeVCFMetadata(List<Triple> triples, Metadata meta) throws IOException {
		FeatureCodecHeader header = codec.readHeader(lineIterator);
		meta.vcfHeader = (VCFHeader) header.getHeaderValue();
		for (VCFFilterHeaderLine filterHeader : meta.vcfHeader.getFilterLines()) {
			String filterName = FILTERBASEURI + ++meta.filterCount;
			Node filter = tripleGenerator.generateURI(filterName);
			meta.readMap.put(filterHeader.getID(), filter);
			
			triples.add(new Triple(filter, RDF.type.asNode(), GfvoVocab.Variant_Calling.asNode()));
			triples.add(new Triple(filter, RDFS.label.asNode(), NodeFactory.createLiteral(filterHeader.getID())));

			
		}
		for (String name: meta.vcfHeader.getSampleNamesInOrder()) {
			String sampleName = SAMPLEBASEURI + ++meta.sampleCount;
			Node sample = tripleGenerator.generateURI(sampleName);
			meta.sampleMap.put(name, sample);
			
			triples.add(new Triple(sample, RDF.type.asNode(), GfvoVocab.Biological_Entity.asNode()));
			triples.add(new Triple(sample, RDFS.label.asNode(), NodeFactory.createLiteral(name)));

		}
		for (VCFFormatHeaderLine formatHeader : meta.vcfHeader.getFormatHeaderLines()) {
			meta.formatMap.put(formatHeader.getID(), formatHeader.getType().toString());
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

/*
 * 
 * 
 * module BioInterchange::Genomics

require 'date'

class VCFReader < GFF3Reader

  # Register reader:
  BioInterchange::Registry.register_reader(
    'biointerchange.vcf',
    VCFReader,
    [ 'name', 'name_uri', 'date', 'batch_size' ],
    true,
    'Variant Call Format (VCF) version 4.1/4.2 reader',
    [
      [ 'date <date>', 'date when the GVF file was created (optional)' ],
      [ 'name <name>', 'name of the GVF file creator (optional)' ],
      [ 'name_id <id>', 'email address of the GVF file creator (optional)' ]
    ]
  )

  # Creates a new instance of a Genome Variation Format (GVF) reader.
  #
  # +name+:: Optional name of the person who generated the GVF file.
  # +name_uri+:: Optional e-mail address of the person who generated the GVF file.
  # +date+:: Optional date of when the GVF file was produced.
  def initialize(name = nil, name_uri = nil, date = nil, batch_size = nil)
    # Remember: calling super without brackets passes all arguments of initialize!
    super
  end

protected

  def create_feature_set
    BioInterchange::Genomics::VCFFeatureSet.new()
  end

  def add_pragma(feature_set, line)
    line.chomp!
    name, value = line[2..-1].split(/=/, 2)
    value.strip!

    # Interpret pragmas, and if not known, delegate to GFF3Reader (in alphabetical order):
    if name == 'assembly' then
      # attributes = split_attributes(value)
      # structured_attributes = feature_set.pragma(name)
      # structured_attributes = { name => [] } unless structured_attributes
      # structured_attributes[name] << attributes
      # feature_set.set_pragma(name, structured_attributes)
    elsif name == 'center' then
      #
    elsif name == 'contig' then
      self.add_vcf_pragma(feature_set, name, value)
    elsif name == 'fileDate' then
      feature_set.set_pragma(name, { name => Date.parse(value) })
    elsif name == 'fileformat' then
      feature_set.set_pragma(name, { name => value.sub(/^VCFv/, '').to_f })
    elsif name == 'FILTER' then
      self.add_vcf_pragma(feature_set, name, value)
    elsif name == 'FORMAT' then
      self.add_vcf_pragma(feature_set, name, value)
    elsif name == 'geneAnno' then
      #
    elsif name == 'ID' then
      #
    elsif name == 'INFO' then
      feature_set.set_pragma(name, vcf_mapping(value))
    elsif name == 'Number' then
      #
    elsif name == 'PEDIGREE' then
      self.add_vcf_pragma(feature_set, name, value)
    elsif name == 'phasing' then
      #
    elsif name == 'reference' then
      #
    elsif name == 'SAMPLE' then
      #
    elsif name == 'tcgaversion' then
      #
    elsif name == 'Type' then
      #
    elsif name == 'vcfProcessLog' then
      #
    elsif name == 'reference' then
      # 'reference' is not specified in VCF 4.1, but used in examples and real-world
      # VCF files nevertheless.
      # TODO What if reference already set?
      feature_set.set_pragma(name, value)
    else
      # Cannot be passed to super class, because GFF3 has inherently different pragma statements.
      feature_set.set_pragma(name, { name => value })
    end
  end

  # Adds pragma information where the pragma can appear multiple times
  # in the input (application: VCF). Each pragma information is still a hash,
  # which is stored in an array.
  #
  # +feature_set+:: feature set to which the pragma information is added
  # +name+:: name of the pragma under which the information is being stored
  # +value+:: hashmap of the actual pragma information (will be passed through vcf_mapping call)
  def add_vcf_pragma(feature_set, name, value)
    values = feature_set.pragma(name)
    if values then
      values << vcf_mapping(value)
    else
      values = [ vcf_mapping(value) ]
    end
    feature_set.set_pragma(name, values)
  end

  # Adds a comment to the feature set; ignores the header line that preceds VCF features.
  # Comments are added on a line-by-line basis.
  #
  # +feature_set+:: VCF feature set to which the comment line is being added
  # +comment+:: comment line in the VCF file
  def add_comment(feature_set, comment)
    if comment.start_with?("CHROM\tPOS\tID\tREF\tALT") then
      columns = comment.split("\t")
      @samples = columns[9..-1]
      @samples = [] unless @samples
    else
      @comment << comment
    end
  end

  # Adds a VCF feature to a VCF feature set.
  #
  # +feature_set+:: feature set to which the feature should be added to
  # +line+:: line from the VCF that describes the feature
  def add_feature(feature_set, line)
    line.chomp!
    chrom, pos, id, ref, alt, qual, filter, info, format, samples = line.split("\t")

    # Replace an unknown ID by nil, so that feature coordinates are used during serialization:
    id = nil

    #
    # Split composite fields
    #

    # Alternative alleles:
    alt = alt.split(',')

    # Filters:
    filter = filter.split(';')

    # Feature information:
    info = info.split(';')
    info = info.map { |key_value_pair| key, values = key_value_pair.split('=', 2) }
    info = Hash[info]
    info = Hash[info.map { |key, value|
      if value then
        [ key, value.split(',') ]
      else
        [ key, true ]
      end
    }]

    # Format for following sample columns:
    format = format.split(':')

    # Sample columns (need to be further split in the writer -- depends on format):
    samples = samples.split("\t").map { |value|
      # Dot: not data provided for the sample
      if value == '.' then
        {}
      else
        values = value.split(':')
        Hash[format.zip(values)]
      end
    }

    feature_set.add(BioInterchange::Genomics::VCFFeature.new(chrom, pos, id, ref, alt, qual, filter, info, samples))
  end

private

  # Takes a VCF meta-information string and returns a key-value mapping.
  #
  # +value+:: value of a meta-information assignment in VCF (key/value mappings of the form "<ID=value,...>")
  def vcf_mapping(value)
    value = value[1..-2]

    mapping = {}
    identifier = ''
    assignment = ''
    state = :id
    value.each_char { |character|
      if state == :value then
        if character == '"' then
          state = :quoted
          next
        else
          state = :plain
        end
      end

      state = :separator if state == :plain and character == ','

      if state == :id then
        if character == '=' then
          state = :value
          assignment = ''
        else
          identifier << character
        end
      elsif state == :separator then
        if character == ',' then
          state = :id
          mapping[identifier] = assignment
          identifier = ''
        else
          # TODO Format error.
        end
      elsif state == :quoted then
        if character == '"' then
          state = :separator
          mapping[identifier] = assignment
          identifier = ''
        else
          assignment << character
        end
      elsif state == :plain then
        assignment << character
      else
        # TODO Whoops. Report error.
      end
    }

    mapping[identifier] = assignment unless identifier.empty?

    mapping
  end

end

end


 * 
 * 
 */

