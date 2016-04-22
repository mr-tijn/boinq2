package com.genohm.boinq.tools.fileformats;


import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMRecordIterator;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;

import com.genohm.boinq.domain.jobs.TripleConversion.Metadata;

public class SAMTripleIterator implements Iterator<Triple> {
	
	private TripleConverter converter;
	private SAMRecordIterator recordIterator;
	private List<Triple> currentTriples = new LinkedList<Triple>();
	private Map<String, Node> referenceMap;
	private Metadata meta;
	private SamReader samReader;
	

	public SAMTripleIterator(TripleConverter converter, File file, Map<String, Node> referenceMap, Metadata meta) {
		
		this.converter = converter;
		this.referenceMap = referenceMap;
		SamReaderFactory factory =SamReaderFactory.makeDefault();
		this.samReader = factory.open(new File(file.getPath()));
		this.recordIterator = samReader.iterator();
        meta.samHeader = samReader.getFileHeader();
		this.meta = meta; 
	}
	
	@Override
	public boolean hasNext() {
		if (currentTriples == null) return false;
		if (currentTriples.isEmpty()){
			return recordIterator.hasNext();
		} else{
			return true;
		}
	}

	@Override
	public Triple next() {
		if (currentTriples.isEmpty()){
			meta.sumEntryCount++;
			SAMRecord entry = recordIterator.next();
			currentTriples.addAll(converter.convert(entry, meta));
		}
		return currentTriples.remove(0);
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
