package com.genohm.boinq.tools.fileformats;

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
import com.genohm.boinq.domain.jobs.TripleConversion.Metadata;

import htsjdk.tribble.readers.AsciiLineReader;
import htsjdk.tribble.readers.AsciiLineReaderIterator;

import de.charite.compbio.jannovar.impl.parse.gff.Feature;
import de.charite.compbio.jannovar.impl.parse.gff.FeatureFormatException;
import de.charite.compbio.jannovar.impl.parse.gff.GFFParser;
import de.charite.compbio.jannovar.impl.parse.gff.GFFVersion;

public class GFF3TripleIterator implements Iterator<Triple> {

	private List<Triple> currentTriples = new LinkedList<Triple>();
	private Map<String, Node> referenceMap;
	private TripleConverter converter;
	private Metadata meta;
	private GFFParser Gffparse;
	private AsciiLineReaderIterator lineIterator;
	private GFFVersion version;

	public GFF3TripleIterator(TripleConverter converter, File file, Map<String, Node> referenceMap, Metadata meta)
			throws FileNotFoundException, IOException {
		this.version = new GFFVersion(3);
		this.converter = converter;
		this.referenceMap = referenceMap;
		lineIterator = new AsciiLineReaderIterator(new AsciiLineReader(new FileInputStream(file)));
		this.Gffparse = new GFFParser(file.getPath(), version, false);
		this.meta = meta;
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
			meta.sumFeatureCount++;
			String nextLine = lineIterator.next();
			while (nextLine.startsWith("##")) {
				this.meta.gffHeader.add(nextLine.substring(2));
				nextLine = lineIterator.next();
			}
			Feature entry = null;
			try {
				entry = Gffparse.parseFeature(nextLine);
			} catch (FeatureFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Node reference = null;
			if (referenceMap != null) {
				reference = referenceMap.get(entry.getSequenceID());
			}
			if (reference == null) {
				reference = NodeFactory.createLiteral(entry.getSequenceID());
			}
			List<Triple> triples = converter.convert(entry, reference,  meta);
			currentTriples.addAll(triples);
		}
		return currentTriples.remove(0);

	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
