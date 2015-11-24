package com.genohm.boinq.tools.fileformats;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;

@Service
public class TripleIteratorFactory {

	private static final String GFF3_EXTENSIONS[] = {"GFF", "GFF3"};
	private static final String BED_EXTENSIONS[] = {"BED"};
	private static final String VCF_EXTENSIONS[] = {"VCF"};

	@Inject
	TripleConverter tripleConverter;
	
	public Iterator<Triple> getIterator(File inputFile, Map<String, Node> referenceMap, List<String> typeList) throws Exception {
		String extension = FilenameUtils.getExtension(inputFile.getName());
		for (String ext: GFF3_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new GFF3TripleIterator(tripleConverter, inputFile, referenceMap, typeList);
		}
		for (String ext: BED_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new HTSJDKBedTripleIterator(tripleConverter, inputFile, referenceMap, typeList);
		}
		for (String ext: VCF_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new VCFTripleIterator(tripleConverter, inputFile, referenceMap, typeList);
		}
		throw new Exception("No triple iterator available for extension " + extension);
	}
}
