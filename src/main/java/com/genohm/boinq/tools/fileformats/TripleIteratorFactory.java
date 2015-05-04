package com.genohm.boinq.tools.fileformats;

import java.io.File;
import java.util.Iterator;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.graph.Triple;

@Service
public class TripleIteratorFactory {

	private static final String GFF3_EXTENSIONS[] = {"GFF", "GFF3"};
	private static final String BED_EXTENSIONS[] = {"BED" };

	@Inject
	TripleConverter tripleConverter;
	
	public Iterator<Triple> getIterator(File inputFile) throws Exception {
		String extension = FilenameUtils.getExtension(inputFile.getName());
		for (String ext: GFF3_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new GFF3TripleIterator(tripleConverter, inputFile);
		}
		for (String ext: BED_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new BedTripleIterator(tripleConverter, inputFile);
		}
		throw new Exception("No triple iterator available for extension " + extension);
	}
}
