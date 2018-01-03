package com.genohm.boinq.tools.fileformats;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.jobs.TripleConversion.Metadata;
import com.genohm.boinq.service.TripleGeneratorService;

@Service
public class TripleIteratorFactory {

	private static final String GFF3_EXTENSIONS[] = {"GTF", "GFF3"};
	private static final String BED_EXTENSIONS[] = {"BED"};
	private static final String VCF_EXTENSIONS[] = {"VCF"};
	private static final String SAM_EXTENSIONS[] = {"SAM", "BAM"};

	@Inject
	TripleGeneratorService tripleGenerator;
	
	public Iterator<Triple> getIterator(File inputFile, Map<Node, Node> referenceMap, Metadata meta) throws Exception {
		String extension = FilenameUtils.getExtension(inputFile.getName());
		meta.fileType = extension;
		for (String ext: GFF3_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new GFF3TripleIterator(tripleGenerator, inputFile, referenceMap, meta);
		}
		for (String ext: BED_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new BedTripleIterator(tripleGenerator, inputFile, referenceMap, meta);
		}
		for (String ext: VCF_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new VCFTripleIterator(tripleGenerator, inputFile, referenceMap, meta);
		}
		for (String ext: SAM_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new SAMTripleIterator(tripleGenerator, inputFile, referenceMap, meta);
		}
		throw new Exception("No triple iterator available for extension " + extension);
	}
}
