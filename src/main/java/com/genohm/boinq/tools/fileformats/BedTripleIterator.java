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

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;

import com.genohm.boinq.domain.jobs.TripleConversion.Metadata;

public class BedTripleIterator implements Iterator<Triple> {

	private List<Triple> currentTriples = new LinkedList<Triple>();
	private AsciiLineReaderIterator lineIterator;
	private int idCounter = 0;
	private BEDCodec codec = new BEDCodec();
	private Map<String, Node> referenceMap;
	private TripleConverter converter;
	private Metadata meta;
	
	public BedTripleIterator(TripleConverter converter, File file, Map<String, Node> referenceMap, Metadata meta) throws FileNotFoundException, IOException{
		this.converter = converter;
		this.referenceMap = referenceMap;
		lineIterator = new AsciiLineReaderIterator(new AsciiLineReader(new FileInputStream(file)));
		this.meta = meta;
		
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
			String nextLine = lineIterator.next();
			BEDFeature feature = codec.decode(nextLine);
			while (feature == null){
				this.meta.bedHeader.add(nextLine);
				nextLine = lineIterator.next();
				feature = codec.decode(nextLine);
			}
			Node globalReference = null;
			if (referenceMap != null) {
				globalReference = referenceMap.get(feature.getContig());
			}
			String id = feature.getName();
			if (id == null) {
				id = "GENERATED_ID_" + ++idCounter ;
			}
			currentTriples.addAll(converter.convert(feature, id, globalReference, meta));
		}
		return currentTriples.remove(0);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	
}
