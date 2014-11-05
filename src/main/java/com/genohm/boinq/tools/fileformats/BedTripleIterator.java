package com.genohm.boinq.tools.fileformats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.graph.Triple;

import edu.unc.genomics.BedEntry;
import edu.unc.genomics.io.BedFileReader;

public class BedTripleIterator implements Iterator<Triple> {

	
	public static String featureBaseURI ="http://www.genohm.com/bed/feature#";
	public static String positionBaseURI ="http://www.genohm.com/bed/position#";
	public static String attributeBaseURI ="http://www.genohm.com/bed/attribute#";
	private List<Triple> currentTriples = new LinkedList<Triple>();
	private Iterator<BedEntry> bedIterator;
	private int idCounter = 0;
	
	
	public BedTripleIterator(File file) throws FileNotFoundException, IOException{
		BedFileReader reader = new BedFileReader(file.toPath());
		Iterator<BedEntry> bedIterator = reader.iterator();
		this.bedIterator= bedIterator;
	}
	
	
		//TODO: zoek block en feature type
		// als  blockType of featureType meegegeven == null, gebruik biological_region of meer algemene term
	//}
	
	@Override
	public boolean hasNext() {
		if (currentTriples.isEmpty()){
			return bedIterator.hasNext();
		}else{
			return true;
		}
	}
	@Override
	public Triple next() {
		
		if (currentTriples.isEmpty()){
			BedEntry entry = bedIterator.next();
			String id = entry.getId();
			if (id == null) {
				id = "GFFASSEMBLER_GENERATED_ID_" + ++idCounter ;
			}
			currentTriples.addAll(TripleConverter.convert(entry, id));
		}
		
		
		
		return currentTriples.remove(0);
	}
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	
}
