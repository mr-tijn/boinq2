package org.boinq.tools.fileformats;

import java.io.File;
import java.util.Iterator;
import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.apache.jena.graph.Triple;
import org.boinq.domain.jobs.TripleConversion.Metadata;
import org.boinq.service.TripleGeneratorService;
import org.springframework.stereotype.Service;

@Service
public class TripleIteratorFactory {

	private static final String GFF3_EXTENSIONS[] = {"GTF", "GFF3"};
	private static final String BED_EXTENSIONS[] = {"BED"};
	private static final String VCF_EXTENSIONS[] = {"VCF"};
	private static final String SAM_EXTENSIONS[] = {"SAM", "BAM"};

	@Inject
	TripleGeneratorService tripleGenerator;
	
	public Iterator<Triple> getIterator(File inputFile, Metadata meta) throws Exception {
		String extension = FilenameUtils.getExtension(inputFile.getName());
		meta.fileType = extension;
		for (String ext: GFF3_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new GFF3TripleIterator(tripleGenerator, inputFile, meta);
		}
		for (String ext: BED_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new BedTripleIterator(tripleGenerator, inputFile, meta);
		}
		for (String ext: VCF_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new VCFTripleIterator(tripleGenerator, inputFile, meta);
		}
		for (String ext: SAM_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new SAMTripleIterator(tripleGenerator, inputFile, meta);
		}
		throw new Exception("No triple iterator available for extension " + extension);
	}
}
