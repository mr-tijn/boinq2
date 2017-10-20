package com.genohm.boinq.tools.fileformats;

import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDdouble;
import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDint;
import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDstring;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import com.genohm.boinq.domain.jobs.TripleConversion.Metadata;
import com.genohm.boinq.generated.vocabularies.FormatVocab;
import com.genohm.boinq.generated.vocabularies.SoVocab;
import com.genohm.boinq.service.TripleGeneratorService;

import htsjdk.tribble.annotation.Strand;
import htsjdk.tribble.bed.BEDCodec;
import htsjdk.tribble.bed.BEDFeature;
import htsjdk.tribble.bed.FullBEDFeature.Exon;
import htsjdk.tribble.readers.AsciiLineReader;
import htsjdk.tribble.readers.AsciiLineReaderIterator;

public class BedTripleIterator extends TripleBuilder implements Iterator<Triple> {

	private List<Triple> currentTriples = new LinkedList<Triple>();
	private AsciiLineReaderIterator lineIterator;
	private BEDCodec codec = new BEDCodec();
	private Map<Node, Node> referenceMap;
	private Metadata meta;
	private File file;
	
	public BedTripleIterator(TripleGeneratorService tripleGenerator, File file, Map<Node, Node> referenceMap, Metadata meta) throws FileNotFoundException, IOException{
		super(tripleGenerator);
		this.referenceMap = referenceMap;
		lineIterator = new AsciiLineReaderIterator(AsciiLineReader.from(new FileInputStream(file)));
		this.meta = meta;
		this.file = file;
		
		}
	
	@Override
	public boolean hasNext() {
		if (currentTriples.isEmpty()){
			return lineIterator.hasNext();
		}else{
			return true;
		}
	}

	@Override
	public Triple next() {
		if (currentTriples.isEmpty()){
			meta.entryCount++;
			String nextLine = lineIterator.next();
			BEDFeature entry = codec.decode(nextLine);
			while (entry == null){
				this.meta.bedHeader.add(nextLine);
				nextLine = lineIterator.next();
				entry = codec.decode(nextLine);
			}
			currentTriples.addAll(convert(entry, meta));
		}
		return currentTriples.remove(0);
	}

	
	public List<Triple> convert(BEDFeature entry, Metadata meta) {
		List<Triple> triples = new LinkedList<Triple>();
		int attributeCount = 1;
		// BEDentry
		String bedEntryURI = ENTRYBASEURI + Long.toString(meta.entryCount);
		Node bedEntry = tripleGenerator.generateURI(bedEntryURI);
		triples.add(new Triple(bedEntry, RDF.type.asNode(), FormatVocab.BED_Entry.asNode()));

		if (entry.getColor() != null) {
			String attributeName = generateAttribute(triples, bedEntry, bedEntryURI,
					String.valueOf(entry.getColor()), XSDstring, FormatVocab.RGBcolor.asNode(), attributeCount++);
			Node attributeNameNode = tripleGenerator.generateURI(attributeName);
			generateAttribute(triples, attributeNameNode, attributeName, String.valueOf(entry.getColor().getBlue()),
					XSDint, FormatVocab.RGBblue.asNode(), attributeCount++);
			generateAttribute(triples, attributeNameNode, attributeName, String.valueOf(entry.getColor().getGreen()),
					XSDint, FormatVocab.RGBgreen.asNode(), attributeCount++);
			generateAttribute(triples, attributeNameNode, attributeName, String.valueOf(entry.getColor().getRed()),
					XSDint, FormatVocab.RGBred.asNode(), attributeCount++);
		}
		// FEATURE
		attributeCount = 1;
		String featureURI = FEATUREBASEURI + ++meta.featureCount;
		Node feature = tripleGenerator.generateURI(featureURI);
		triples.add(new Triple(bedEntry, FormatVocab.defines.asNode(), feature));
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
		if (score != null) {
			generateAttribute(triples, feature, featureURI, score.toString(), XSDdouble, SoVocab.score.asNode(),
					attributeCount++);
		}

		addFaldoTriples(feature, Long.valueOf(entry.getStart()), Long.valueOf(entry.getEnd()), entry.getContig(),
				entry.getStrand() == Strand.POSITIVE, triples, meta);

		int idx = 1;
		for (Exon exon : entry.getExons()) {
			String subFeatureURI = FEATUREBASEURI + ++meta.featureCount;
			Node subFeature = tripleGenerator.generateURI(subFeatureURI);
			triples.add(new Triple(bedEntry, FormatVocab.defines.asNode(), subFeature));
			if (meta.subType != null) {
				triples.add(new Triple(subFeature, RDF.type.asNode(), meta.subType));
			}
			triples.add(new Triple(subFeature, SoVocab.part_of.asNode(), feature));
			triples.add(new Triple(feature, SoVocab.has_part.asNode(), subFeature));
			triples.add(new Triple(subFeature, RDFS.label.asNode(),
					NodeFactory.createLiteral(subFeatureURI + "_BLOCK#" + idx++, XSDstring)));
			addFaldoTriples(subFeature, Long.valueOf(exon.getCdStart()), Long.valueOf(exon.getCdEnd()),
					entry.getContig(), entry.getStrand() == Strand.POSITIVE, triples, meta);
			// BUG TICKBOXES HAVE TO SPAN WHOLE GENE AREA, OTHERWHISE REGIONS OF
			// EXONS ARE WRONG
		}
		return triples;
	}

	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	
}
