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
	private BEDCodec codec = new BEDCodec();
	private Map<Node, Node> referenceMap;
	private TripleConverter converter;
	private Metadata meta;
	
	public BedTripleIterator(TripleConverter converter, File file, Map<Node, Node> referenceMap, Metadata meta) throws FileNotFoundException, IOException{
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
			meta.sumEntryCount++;
			String nextLine = lineIterator.next();
			BEDFeature entry = codec.decode(nextLine);
			while (entry == null){
				this.meta.bedHeader.add(nextLine);
				nextLine = lineIterator.next();
				entry = codec.decode(nextLine);
			}
			currentTriples.addAll(converter.convert(entry, meta));
		}
		return currentTriples.remove(0);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	
}
