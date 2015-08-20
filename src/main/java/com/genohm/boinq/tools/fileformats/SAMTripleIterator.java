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

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;


public class SAMTripleIterator implements Iterator<Triple> {
	
	private TripleConverter converter;
	private SAMRecordIterator recordIterator;
	private List<Triple> currentTriples = new LinkedList<Triple>();
	
	private int idCounter = 0;
	private Map<String, Node> referenceMap;

	public SAMTripleIterator(TripleConverter converter, File file, Map<String, Node> referenceMap) {
		this.converter = converter;
		this.referenceMap = referenceMap;
		SamReader reader = SamReaderFactory.makeDefault().open(file);
		this.recordIterator = reader.iterator();
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
			SAMRecord record = recordIterator.next();
			String id = "SAMASSEMBLER_GENERATED_ID_" + ++idCounter;
			Node reference = referenceMap.get(record.getContig());
			if (reference == null){
				reference = NodeFactory.createLiteral(record.getContig());
			}
			currentTriples.addAll(converter.convert(record, reference, id));
		}
		return currentTriples.remove(0);
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
