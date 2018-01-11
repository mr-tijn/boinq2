package org.boinq.tools.fileformats;

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
import org.boinq.domain.jobs.TripleConversion.Metadata;
import org.boinq.service.TripleGeneratorService;
import org.boinq.tools.Counter;
import org.boinq.generated.vocabularies.FaldoVocab;
import org.boinq.generated.vocabularies.FormatVocab;
import org.boinq.generated.vocabularies.GfvoVocab;
import org.boinq.generated.vocabularies.SioVocab;
import org.boinq.generated.vocabularies.SoVocab;

import de.charite.compbio.jannovar.impl.parse.gff.Feature;
import de.charite.compbio.jannovar.impl.parse.gff.FeatureFormatException;
import de.charite.compbio.jannovar.impl.parse.gff.GFFParser;
import de.charite.compbio.jannovar.impl.parse.gff.GFFVersion;
import htsjdk.samtools.Cigar;
import htsjdk.samtools.CigarElement;
import htsjdk.samtools.TextCigarCodec;
import htsjdk.tribble.readers.AsciiLineReader;
import htsjdk.tribble.readers.AsciiLineReaderIterator;

public class GFF3TripleIterator extends TripleBuilder implements Iterator<Triple> {
	
	public static final String ALIGNMENTBASEURI = "/resource/alignment#";
	public static final String ALIGNMENTTARGETBASEURI = "/resource/target#";
	
	private List<Triple> currentTriples = new LinkedList<Triple>();
	private Metadata meta;
	private GFFParser gffParser;
	private AsciiLineReaderIterator lineIterator;
	private GFFVersion version;
	private Map<String, Node> gff3AttributeTypes;
	private Map<String, XSDDatatype> gff3AttributeValueTypes;
	private Counter alignmentIdx = new Counter(0);

	public GFF3TripleIterator(TripleGeneratorService tripleGenerator, File file, Metadata meta)
			throws FileNotFoundException, IOException {
		super(tripleGenerator);
		this.version = new GFFVersion(3);
		lineIterator = new AsciiLineReaderIterator(AsciiLineReader.from(new FileInputStream(file)));
		this.gffParser = new GFFParser(file.getPath(), version, false);
		this.meta = meta;
		initData();
	}

	private void initData() {
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
			while (nextLine.length() == 0 || nextLine.startsWith("#")) {
				nextLine = lineIterator.next();
			}
			Feature entry = null;
			String rawType = null;
			try {
				entry = gffParser.parseFeature(nextLine);
			} catch (FeatureFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// current htsjdk implementation only recognizes limited set of types
			// so we need to pass the raw type string
			rawType = nextLine.split("\t")[2];
			List<Triple> triples = convert(entry,  meta, rawType);
			currentTriples.addAll(triples);
	
		}
		return currentTriples.remove(0);

	}

	
	public List<Triple> convert(Feature entry, Metadata meta, String rawType) {
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
		if (rawType != null) {
			Node featureType = getType(rawType);
			triples.add(new Triple(feature, RDF.type.asNode(), featureType));
			meta.typeList.add(featureType);
		}
		
		
		attributeCount = processAttributes(triples, gffEntry, gffEntryURI, entry.getAttributes(), attributeCount,
				feature, featureURI, meta);
		
		// LOCATION
		addFaldoTriples(feature, Long.valueOf(entry.getStart()), Long.valueOf(entry.getEnd()), entry.getSequenceID(),
				entry.getStrand(), triples, meta);
		
		// in case of alignments, we need to point the src to a single location
		if (entry.getAttributes().containsKey("Target")) {
			triples.add(new Triple(tripleGenerator.generateURI(ALIGNMENTBASEURI + alignmentIdx.current()),FaldoVocab.location.asNode(),tripleGenerator.generateURI(locationName(entry.getSequenceID(), Long.valueOf(entry.getStart()), Long.valueOf(entry.getEnd()), entry.getStrand(), meta))));
		}

		return triples;
	}

	
	public Node getType(String featureType) {
		String typeID = featureType.toUpperCase();
		if (SoVocab.OntClassesByLabel.containsKey(typeID)) {
			return SoVocab.OntClassesByLabel.get(typeID).asNode();
		} else {
			return SoVocab.sequence_feature.asNode();
		}
	}
	
	
	private int processAttributes(List<Triple> triples, Node entry, String entryName, Map<String, String> keyValues,
			int attributeCount, Node feature, String featureName, Metadata meta) {
		for (String key : keyValues.keySet()) {
			if (gff3AttributeTypes.get(key) != null) {
//				String attributeFeatureName = entryName + "/attribute_" + attributeCount++;
//				Node attributeFeature = tripleGenerator.generateURI(attributeFeatureName);
//				triples.add(new Triple(entry, SoVocab.has_quality.asNode(), attributeFeature));
//				triples.add(new Triple(attributeFeature, RDF.type.asNode(), gff3AttributeTypes.get(key)));
//				triples.add(new Triple(attributeFeature, RDF.value.asNode(), NodeFactory.createLiteral(keyValues.get(key), gff3AttributeValueTypes.get(key))));
				triples.add(new Triple(feature, gff3AttributeTypes.get(key), NodeFactory.createLiteral(keyValues.get(key), gff3AttributeValueTypes.get(key))));
			} else if (key.equalsIgnoreCase("Target")) {
				// can a feature contain multiple alignment targets ? don't think so
				generateGap(triples, feature, keyValues.get("Gap"), keyValues.get("Target"), meta);
			} else if (key.equalsIgnoreCase("Parent")) {
				String[] parents = keyValues.get(key).split(",");
				for (String parentId: parents) {
					String parentURI = FEATUREBASEURI + parentId;
					triples.add(new Triple(feature, SoVocab.part_of.asNode(), tripleGenerator.generateURI(parentURI)));
					triples.add(new Triple(tripleGenerator.generateURI(parentURI), SoVocab.has_part.asNode(), feature));
				}
			} else if (key.equalsIgnoreCase("Name")) {
				triples.add(new Triple(feature, RDFS.label.asNode(), NodeFactory.createLiteral(keyValues.get(key), XSDstring)));
			} else if (key.equalsIgnoreCase("ID")) {
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
			Node targetFeature = tripleGenerator.generateURI(ALIGNMENTTARGETBASEURI + alignmentIdx.next());
			triples.add(new Triple(targetFeature, RDF.type.asNode(), SoVocab.sequence_feature.asNode()));
			addFaldoTriples(targetFeature, start, end, targetId, strand, triples, meta);
			Node alignment = tripleGenerator.generateURI(ALIGNMENTBASEURI + alignmentIdx.current());
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
