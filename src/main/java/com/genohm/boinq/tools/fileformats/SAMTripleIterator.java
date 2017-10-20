package com.genohm.boinq.tools.fileformats;


import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDint;
import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDstring;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import com.genohm.boinq.domain.jobs.TripleConversion.Metadata;
import com.genohm.boinq.generated.vocabularies.FormatVocab;
import com.genohm.boinq.generated.vocabularies.SoVocab;
import com.genohm.boinq.service.TripleGeneratorService;

import htsjdk.samtools.AlignmentBlock;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMRecord.SAMTagAndValue;
import htsjdk.samtools.SAMRecordIterator;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;

public class SAMTripleIterator extends TripleBuilder implements Iterator<Triple> {
	
	private SAMRecordIterator recordIterator;
	private List<Triple> currentTriples = new LinkedList<Triple>();
	private Metadata meta;
	private SamReader samReader;
	

	public SAMTripleIterator(TripleGeneratorService tripleGenerator, File file, Map<Node, Node> referenceMap, Metadata meta) {
		super(tripleGenerator);
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
			meta.entryCount++;
			SAMRecord entry = recordIterator.next();
			currentTriples.addAll(convert(entry, meta));
		}
		return currentTriples.remove(0);
	}

	
	public List<Triple> convert(SAMRecord entry, Metadata meta) {
		int attributeCount = 1;
		List<Triple> triples = new LinkedList<Triple>();
		/*
		 * if (meta.tripleCount == 0){ createSAMEntities(triples, entry, meta);
		 * }
		 */
		// SAMEntry
		String SAMentryName = ENTRYBASEURI + Long.toString(meta.entryCount);
		Node SAMentry = tripleGenerator.generateURI(SAMentryName);
		triples.add(new Triple(SAMentry, RDF.type.asNode(), FormatVocab.SAM_Entry.asNode()));

		// FLAG: each bit explains different description
		generateAttribute(triples, SAMentry, SAMentryName, String.valueOf(entry.getFlags()), XSDint,
				FormatVocab.FLAG.asNode(), attributeCount++);
		// RNAME: Reference sequence name of the alignment
		// generateAttribute(triples, SAMentry, SAMentryName,
		// entry.getReferenceName(), XSDstring, FormatVocab.RNAME.asNode(),
		// attributeCount++);
		// MAPQ: mapping quality -10logBase
		// generateAttribute(triples, SAMentry, SAMentryName,
		// String.valueOf(entry.getMappingQuality()), XSDint,
		// FormatVocab.MAPQ.asNode(), attributeCount++);
		// CIGAR: CIGAR String
		generateAttribute(triples, SAMentry, SAMentryName, entry.getCigarString(), XSDstring,
				FormatVocab.CIGAR.asNode(), attributeCount++);
		// RNEXT: Reference sequence name of the NEXT read in the template
		generateAttribute(triples, SAMentry, SAMentryName, entry.getMateReferenceName(), XSDstring,
				FormatVocab.RNEXT.asNode(), attributeCount++);
		// PNEXT: Position of the primary alignment of the NEXT read. not
		// necessary -> positions are iterated at end of function
		// TLEN: signed observed Template LENgth.
		generateAttribute(triples, SAMentry, SAMentryName, String.valueOf(entry.getInferredInsertSize()), XSDint,
				FormatVocab.TLEN.asNode(), attributeCount++);
		// SEQ: segment Sequence
		// generateAttribute(triples, SAMentry, SAMentryName,
		// entry.getReadString(), XSDstring, FormatVocab.SEQ.asNode(),
		// attributeCount++);
		// QUAL: base quality plus 33
		generateAttribute(triples, SAMentry, SAMentryName, String.valueOf(entry.getBaseQualityString()), XSDstring,
				FormatVocab.QUAL.asNode(), attributeCount++);
		// ATTRIBUTES
		for (SAMTagAndValue attribute : entry.getAttributes()) {
			// TODO not implemented enough in htsjdk
			// How to get class from object?
			// String key = attribute.tag;
			// String value = (String)attribute.value;
		}

		// FEATURE
		attributeCount = 1;
		String featureName = FEATUREBASEURI + ++meta.featureCount;
		Node feature = tripleGenerator.generateURI(featureName);
		triples.add(new Triple(SAMentry, FormatVocab.defines.asNode(), feature));
		String RName = entry.getReadName();
		if (meta.readMap.get(RName) != null && entry.getMateReferenceName() != null) {
			triples.add(new Triple(meta.readMap.get(RName), SoVocab.evidence_for_feature.asNode(), feature));
		} else {
			String readNodeName = READBASEURI + ++meta.readCount;
			Node readNode = tripleGenerator.generateURI(readNodeName);
			meta.readMap.put(RName, readNode);
			triples.add(new Triple(readNode, RDF.type.asNode(), SoVocab.read.asNode()));
			triples.add(new Triple(readNode, RDFS.label.asNode(), NodeFactory.createLiteral(RName)));
			triples.add(new Triple(meta.readMap.get(RName), SoVocab.evidence_for_feature.asNode(), feature));

		}
		// triples.add(new Triple(feature, RDF.type.asNode(),
		// SoVocab.read.asNode()));
		triples.add(
				new Triple(feature, RDFS.label.asNode(), NodeFactory.createLiteral(entry.getReadName(), XSDstring)));

		if (entry.getMappingQuality() != 255) {
			generateAttribute(triples, feature, featureName, String.valueOf(entry.getMappingQuality() - 33), XSDint,
					FormatVocab.MAPQ.asNode(), attributeCount++);
		}
		if (entry.getReadString() != null) {
			String featureAttributeName = featureName + "/attribute_" + attributeCount++;
			Node featureAttribute = tripleGenerator.generateURI(featureAttributeName);
			triples.add(new Triple(feature, SoVocab.has_quality.asNode(), featureAttribute));
			triples.add(new Triple(featureAttribute, RDF.value.asNode(),
					NodeFactory.createLiteral(entry.getReadString(), XSDstring)));
			triples.add(new Triple(featureAttribute, RDF.type.asNode(), SoVocab.sequence_motif.asNode()));

			if (entry.getBaseQualityString() != "*") {
				triples.add(new Triple(featureAttribute, RDFS.comment.asNode(),
						NodeFactory.createLiteral(entry.getBaseQualityString(), XSDstring)));
			}
		}

		// LOCATION

		addFaldoTriples(feature, new Long(entry.getStart()), new Long(entry.getEnd()), entry.getContig(),
				!entry.getReadNegativeStrandFlag(), triples, meta);

		int childno = 1;
		for (AlignmentBlock block : entry.getAlignmentBlocks()) {
			String subFeatureName = featureName + "_Block_" + childno++;
			Node subFeature = tripleGenerator.generateURI(subFeatureName);
			triples.add(new Triple(subFeature, RDF.type.asNode(), SoVocab.nucleotide_match.asNode()));
			triples.add(new Triple(feature, SoVocab.evidence_for_feature.asNode(), subFeature));
			generateAttribute(triples, subFeature, subFeatureName,
					entry.getReadString().substring(block.getReadStart() - 1,
							block.getReadStart() - 1 + block.getLength()),
					XSDstring, SoVocab.sequence_motif.asNode(), 1);
					/*
					 * if(meta.referenceMap.get(entry.getReferenceName())!=null)
					 * { triples.add(new Triple(subFeature,
					 * SoVocab.part_of.asNode(),
					 * meta.referenceMap.get(entry.getReferenceName())));
					 * triples.add(new
					 * Triple(meta.referenceMap.get(entry.getReferenceName()),
					 * SoVocab.has_part.asNode(), subFeature)); }
					 */

			// POSITIONS
			addFaldoTriples(subFeature, new Long(block.getReferenceStart()),
					new Long(block.getReferenceStart() + block.getLength() - 1), entry.getContig(),
					!entry.getReadNegativeStrandFlag(), triples, meta);

		}
		return triples;
	}

	
	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
