package com.genohm.boinq.tools.fileformats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;

import edu.unc.genomics.GFFEntry;
import edu.unc.genomics.io.GFFFileReader;

public class GFF3TripleIterator implements Iterator<Triple> {
	public static String featureBaseURI = "http://www.genohm.com/gff3/feature#";
	public static String positionBaseURI = "http://www.genohm.com/gff3/position#";
	public static String attributeBaseURI = "http://www.genohm.com/gff3/attribute#";
	private Iterator<GFFEntry> gffIterator;
	private List<Triple> currentTriples = new LinkedList<Triple>();
	private int idCounter = 0;
	private Map<String, Node> referenceMap;
	private TripleConverter converter;
	
	public GFF3TripleIterator(TripleConverter converter, File file, Map<String, Node> referenceMap) throws FileNotFoundException, IOException {
		this.converter = converter;
		this.referenceMap = referenceMap;
		GFFFileReader reader = new GFFFileReader(file.toPath());
		Iterator<GFFEntry> gffIterator = reader.iterator();
		this.gffIterator = gffIterator;
	}

	@Override
	public boolean hasNext() {
		if (currentTriples.isEmpty()) {
			return gffIterator.hasNext();
		} else {
			return true;
		}
	}
	@Override
	public Triple next() {

		if (currentTriples.isEmpty()) {
			GFFEntry entry = gffIterator.next();
			String id = entry.getId();
			if (id == null) {
				id = "GFFASSEMBLER_GENERATED_ID_" + ++idCounter;
			}
			Node reference = referenceMap.get(entry.getChr());
			if (reference == null) {
				reference = NodeFactory.createLiteral(entry.getChr());
			}
			List<Triple> triples = converter.convert(entry, reference, id);
			currentTriples.addAll(triples);
		
		}
		return currentTriples.remove(0);
	}
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
