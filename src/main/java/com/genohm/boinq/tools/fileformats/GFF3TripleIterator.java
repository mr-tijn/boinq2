package com.genohm.boinq.tools.fileformats;

import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDdouble;
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
import org.apache.jena.vocabulary.SKOS;

import com.genohm.boinq.domain.jobs.TripleConversion.Metadata;
import com.genohm.boinq.generated.vocabularies.FormatVocab;
import com.genohm.boinq.generated.vocabularies.GfvoVocab;
import com.genohm.boinq.generated.vocabularies.SioVocab;
import com.genohm.boinq.generated.vocabularies.SoVocab;
import com.genohm.boinq.service.TripleGeneratorService;

import de.charite.compbio.jannovar.impl.parse.gff.Feature;
import de.charite.compbio.jannovar.impl.parse.gff.FeatureFormatException;
import de.charite.compbio.jannovar.impl.parse.gff.FeatureType;
import de.charite.compbio.jannovar.impl.parse.gff.GFFParser;
import de.charite.compbio.jannovar.impl.parse.gff.GFFVersion;
import htsjdk.samtools.Cigar;
import htsjdk.samtools.CigarElement;
import htsjdk.samtools.TextCigarCodec;
import htsjdk.tribble.readers.AsciiLineReader;
import htsjdk.tribble.readers.AsciiLineReaderIterator;

public class GFF3TripleIterator extends TripleBuilder implements Iterator<Triple> {

	private List<Triple> currentTriples = new LinkedList<Triple>();
	private Map<Node, Node> referenceMap;
	private Metadata meta;
	private GFFParser gffParser;
	private AsciiLineReaderIterator lineIterator;
	private GFFVersion version;
	private File file;
	private Map<String, Node> featureTypeNodes;
	private Map<String, Node> gff3AttributeTypes;
	private Map<String, XSDDatatype> gff3AttributeValueTypes;

	public GFF3TripleIterator(TripleGeneratorService tripleGenerator, File file, Map<Node, Node> referenceMap, Metadata meta)
			throws FileNotFoundException, IOException {
		super(tripleGenerator);
		this.version = new GFFVersion(3);
		this.referenceMap = referenceMap;
		lineIterator = new AsciiLineReaderIterator(AsciiLineReader.from(new FileInputStream(file)));
		this.gffParser = new GFFParser(file.getPath(), version, false);
		this.meta = meta;
		this.file = file;
		initData();
	}

	private void initData() {
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
	
		gff3AttributeTypes = new HashMap<>();
		gff3AttributeTypes.put("Name", RDFS.label.asNode());
		gff3AttributeTypes.put("Alias", SKOS.altLabel.asNode());
		gff3AttributeTypes.put("Note", RDFS.comment.asNode());
		
		gff3AttributeValueTypes = new HashMap<>();
		gff3AttributeValueTypes.put("Name", XSDstring);
		gff3AttributeValueTypes.put("Alias", XSDstring);
		gff3AttributeValueTypes.put("Note", XSDstring);
	}
	
	@Override
	public boolean hasNext() {
		if (currentTriples.isEmpty()) {
			return lineIterator.hasNext();
		} else {
			return true;
		}
	}

	@Override
	public Triple next() {
		if (currentTriples.isEmpty()) {
			meta.entryCount++;
			String nextLine = lineIterator.next();
			while (nextLine.startsWith("##")) {
				this.meta.gffHeader.add(nextLine.substring(2));
				nextLine = lineIterator.next();
			}
			Feature entry = null;
			try {
				entry = gffParser.parseFeature(nextLine);
			} catch (FeatureFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			List<Triple> triples = convert(entry,  meta);
			currentTriples.addAll(triples);
		}
		return currentTriples.remove(0);

	}

	
	public List<Triple> convert(Feature entry, Metadata meta) {
		int attributeCount = 1;

		List<Triple> triples = new LinkedList<Triple>();
		
		// GFF_ENTRY
		String gffEntryURI = (ENTRYBASEURI + meta.entryCount);
		Node gffEntry = tripleGenerator.generateURI(gffEntryURI);
		triples.add(new Triple(gffEntry, RDF.type.asNode(), FormatVocab.GFF_Entry.asNode()));

		// FEATURE
		attributeCount = 1;
		String id = ++meta.featureCount + "";
		if (entry.getAttributes().containsKey("ID")) {
			id = entry.getAttributes().get("ID");
		}
		String featureURI = FEATUREBASEURI + id;
		Node feature = tripleGenerator.generateURI(featureURI);
		triples.add(new Triple(gffEntry, FormatVocab.defines.asNode(), feature));

		if (entry.getScore() != 0.0) {
			generateAttribute(triples, feature, featureURI, String.valueOf(entry.getScore()), XSDdouble, SoVocab.score.asNode(), attributeCount++);
		}
		if (entry.getSource() != null) {
			triples.add(new Triple(feature, GfvoVocab.has_source.asNode(), NodeFactory.createLiteral(entry.getSource())));
//			generateAttribute(triples, gffEntry, gffEntryURI, entry.getSource(), XSDstring,
//					GfvoVocab.has_source.asNode(), attributeCount++);
		}

		if (entry.getPhase() != -1) {
			generateAttribute(triples, feature, featureURI, String.valueOf(entry.getPhase() + 1), XSDint,
					SoVocab.reading_frame.asNode(), attributeCount++);
		}
		if (entry.getType() != null) {
			triples.add(new Triple(feature, RDF.type.asNode(), getType(entry.getType())));
			meta.typeList.add(featureTypeNodes.get(entry.getType().toString()));
		}
		
		
		attributeCount = processAttributes(triples, gffEntry, gffEntryURI, entry.getAttributes(), attributeCount,
				feature, featureURI, meta);
		
		// LOCATION
		addFaldoTriples(feature, Long.valueOf(entry.getStart()), Long.valueOf(entry.getEnd()), entry.getSequenceID(),
				entry.getStrand(), triples, meta);

		return triples;
	}

	
	public Node getType(FeatureType featureType) {
		String typeID = featureType.toString().toUpperCase();
		if (featureTypeNodes.containsKey(typeID)) {
			return featureTypeNodes.get(typeID);
		} else {
			return SoVocab.sequence_feature.asNode();
		}
	}
	
	
	private int processAttributes(List<Triple> triples, Node entry, String entryName, Map<String, String> keyValues,
			int attributeCount, Node feature, String featureName, Metadata meta) {
		for (String key : keyValues.keySet()) {
			// TODO: should it not be the feature having the quality ?
			if (gff3AttributeTypes.get(key) != null) {
				String attributeFeatureName = entryName + "/attribute_" + attributeCount++;
				Node attributeFeature = tripleGenerator.generateURI(attributeFeatureName);
				triples.add(new Triple(entry, SoVocab.has_quality.asNode(), attributeFeature));
				triples.add(new Triple(attributeFeature, RDF.type.asNode(), gff3AttributeTypes.get(key)));
				triples.add(new Triple(attributeFeature, RDF.value.asNode(), NodeFactory.createLiteral(keyValues.get(key), gff3AttributeValueTypes.get(key))));
			} else if (key.equalsIgnoreCase("Target")) {
				// can a feature contain multiple alignment targets ? don't think so
				generateGap(triples, feature, keyValues.get("Gap"), keyValues.get("Target"), meta);
			} else if (key.equalsIgnoreCase("Parent")) {
				String[] parents = keyValues.get(key).split(",");
				for (String parentId: parents) {
					String parentURI = FEATUREBASEURI + parentId;
					triples.add(new Triple(feature, SoVocab.part_of.asNode(), NodeFactory.createURI(parentURI)));
					triples.add(new Triple(NodeFactory.createURI(parentURI), SoVocab.has_part.asNode(), feature));
				}
			}  else if (key.equalsIgnoreCase("ID")) {
				triples.add(new Triple(feature, DCTerms.identifier.asNode(), NodeFactory.createLiteral(keyValues.get(key), XSDstring)));
				//TODO: remove featureIDMap
				//meta.featureIDmap.put(keyValues.get(key), feature);
			} else if (key.equalsIgnoreCase("Derives_from")) {
				String[] parents = keyValues.get(key).split(",");
				for (String parentId: parents) {
					String parentURI = FEATUREBASEURI + parentId;
					triples.add(new Triple(feature, SoVocab.derives_from.asNode(), NodeFactory.createURI(parentURI)));
				}
			} else if (key.equalsIgnoreCase("Is_circular")) {
				triples.add(new Triple(entry, RDF.type.asNode(), (keyValues.get(key).equalsIgnoreCase("true")) ? SoVocab.circular.asNode() : SoVocab.linear.asNode()));
			} else if (key.equalsIgnoreCase("Dbxref")) {
				String[] xrefs = keyValues.get(key).split(",");
				for (String xref: xrefs) {
					String[] values = xref.split(":");
					if (values.length != 2) {
						//LOG ERROR
					}
					Node identifier = NodeFactory.createBlankNode();
					triples.add(new Triple(identifier, RDF.value.asNode(), NodeFactory.createLiteral(values[1])));
					triples.add(new Triple(identifier, SioVocab.has_source.asNode(), NodeFactory.createLiteral(values[0])));
					triples.add(new Triple(feature, RDFS.seeAlso.asNode(), identifier));
					triples.add(new Triple(feature, DCTerms.identifier.asNode(), identifier));
				}
			} else {
				// unknown attribute
				String attributeFeatureName = entryName + "/attribute_" + attributeCount++;
				Node attributeFeature = tripleGenerator.generateURI(attributeFeatureName);
				triples.add(new Triple(entry, SoVocab.has_quality.asNode(), attributeFeature));
				Node attributeType = NodeFactory.createBlankNode();
				triples.add(new Triple(attributeFeature, RDF.type.asNode(), attributeType));
				triples.add(new Triple(attributeType, RDFS.label.asNode(), NodeFactory.createLiteral(key, XSDstring)));
				triples.add(new Triple(attributeFeature, RDF.value.asNode(), NodeFactory.createLiteral(keyValues.get(key), XSDstring)));
			}

		}
		return attributeCount;
	}
	
	private void generateGap(List<Triple> triples, Node feature, String gapString, String targetString, Metadata meta) {
		String[] target = targetString.split(" ");
		try {
			String targetId = target[0]; // reference in FALDO speak
			Long start = Long.parseLong(target[1]);
			Long end = Long.parseLong(target[2]);
			Boolean strand = null;
			if (target.length == 4) {
				strand = target[3].equals("+");
			}
			Node targetFeature = NodeFactory.createURI(feature.getURI() + "_target");
			triples.add(new Triple(targetFeature, RDF.type.asNode(), SoVocab.sequence_feature.asNode()));
			addFaldoTriples(targetFeature, start, end, targetId, strand, triples, meta);
			Node alignment = NodeFactory.createURI(feature.getURI() + "_alignment");
			triples.add(new Triple(alignment, RDF.type.asNode(), GfvoVocab.Sequence_Alignment.asNode()));
			triples.add(new Triple(alignment, RDF.type.asNode(), SoVocab.match_set.asNode()));
			// very weird, see GFVO docs
			triples.add(new Triple(alignment, GfvoVocab.has_source.asNode(), feature));
			triples.add(new Triple(alignment, GfvoVocab.has_input.asNode(), targetFeature));
			int matchIdx = 0;
			Cigar cigar = TextCigarCodec.decode(gapString);
			Node type = null;
			Node prev = null;
			for (CigarElement el: cigar.getCigarElements()) {
				Node alignmentElement = NodeFactory.createURI(alignment.getURI() + "_el" + matchIdx++);
				Node span = NodeFactory.createURI(alignment.getURI() + "_span");
				triples.add(new Triple(alignmentElement, GfvoVocab.has_attribute.asNode(), span));
				triples.add(new Triple(span, RDF.type.asNode(), GfvoVocab.Span.asNode()));
				triples.add(new Triple(span, RDF.value.asNode(), NodeFactory.createLiteral(String.valueOf(el.getLength()), XSDint)));
				switch(el.getOperator()) {
				case M:
					type = SoVocab.match.asNode();
					break;
				case D:
					type = GfvoVocab.Target_Sequence_Gap.asNode();
					break;
				case I:
					type = GfvoVocab.Reference_Sequence_Gap.asNode();
					break;
				default:
						
				}
				triples.add(new Triple(alignmentElement, RDF.type.asNode(), type));
				triples.add(new Triple(alignment, GfvoVocab.has_ordered_part.asNode(), alignmentElement));
				if (prev != null) {
					triples.add(new Triple(alignmentElement, GfvoVocab.is_after.asNode(), prev));
				}
				prev = alignmentElement;
			}
		} catch (Exception e) {
			return;
		}
	}


	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
