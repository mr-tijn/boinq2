package com.genohm.boinq.tools.fileformats;

import htsjdk.tribble.bed.BEDCodec;
import htsjdk.tribble.bed.BEDFeature;
import htsjdk.tribble.readers.AsciiLineReader;
import htsjdk.tribble.readers.AsciiLineReaderIterator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

public class HTSJDKBedTripleIterator implements Iterator<Triple> {

	private List<Triple> currentTriples = new LinkedList<Triple>();
	private AsciiLineReaderIterator lineIterator;
	private int idCounter = 0;
	private BEDCodec codec = new BEDCodec();
	private Map<String, Node> referenceMap;
	private TripleConverter converter;
	
	public HTSJDKBedTripleIterator(TripleConverter converter, File file, Map<String, Node> referenceMap) throws FileNotFoundException, IOException{
		this.converter = converter;
		this.referenceMap = referenceMap;
		lineIterator = new AsciiLineReaderIterator(new AsciiLineReader(new FileInputStream(file)));
		codec.readActualHeader(lineIterator);
	}
	
	
		//TODO: zoek block en feature type
		// als  blockType of featureType meegegeven == null, gebruik biological_region of meer algemene term
	//}
	
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
			BEDFeature feature = codec.decode(lineIterator.next());
			Node globalReference = null;
			if (referenceMap != null) {
				globalReference = referenceMap.get(feature.getContig());
			}
			String id = feature.getName();
			if (id == null) {
				id = "GENERATED_ID_" + ++idCounter ;
			}
			currentTriples.addAll(converter.convert(feature, id, globalReference));
		}
		return currentTriples.remove(0);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	
}
