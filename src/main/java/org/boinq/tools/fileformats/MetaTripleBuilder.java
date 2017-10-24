package org.boinq.tools.fileformats;

import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDint;
import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDstring;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.boinq.domain.Track;
import org.boinq.domain.jobs.TripleConversion.Metadata;
import org.boinq.service.TripleGeneratorService;
import org.boinq.tools.Counter;
import org.springframework.stereotype.Service;

import org.boinq.generated.vocabularies.GfvoVocab;
import org.boinq.generated.vocabularies.SoVocab;
import org.boinq.generated.vocabularies.TrackVocab;

import htsjdk.samtools.SAMProgramRecord;
import htsjdk.variant.vcf.VCFFilterHeaderLine;
import htsjdk.variant.vcf.VCFFormatHeaderLine;
import htsjdk.variant.vcf.VCFHeaderLine;
import htsjdk.variant.vcf.VCFInfoHeaderLine;

@Service
public class MetaTripleBuilder extends TripleBuilder {

	@Inject
	TripleGeneratorService tripleGenerator;
	
	private MetaTripleBuilder() {}
	
	public MetaTripleBuilder(TripleGeneratorService tripleGenerator) {
		super(tripleGenerator);
	}
	
	public List<Triple> createMetadata(String file, Metadata metadata, String graphName, Counter attributeCounter) {
		List<Triple> triples = new LinkedList<>();
		Node graph = NodeFactory.createURI(graphName);
	
		String fileNodeName = file.replace("\\", "/").replace(" ", "_");
		Node fileNode = tripleGenerator.generateURI(fileNodeName);
		triples.add(new Triple(graph, TrackVocab.holds.asNode(), fileNode));
		triples.add(new Triple(fileNode, RDF.type.asNode(), TrackVocab.File.asNode()));
		generateAttribute(triples, fileNode, fileNodeName, Long.toString(metadata.entryCount), XSDint,
				TrackVocab.EntryCount.asNode(), attributeCounter.next());
		generateAttribute(triples, fileNode, fileNodeName, Long.toString(metadata.featureCount), XSDint,
				TrackVocab.FeatureCount.asNode(), attributeCounter.next());
		generateAttribute(triples, fileNode, fileNodeName, Long.toString(metadata.tripleCount), XSDint,
				TrackVocab.TripleCount.asNode(), attributeCounter.next());
		generateAttribute(triples, fileNode, fileNodeName,
				Long.toString(metadata.filterCount), XSDint, TrackVocab.FilterCount.asNode(),
				attributeCounter.next());
		generateAttribute(triples, fileNode, fileNodeName,
				Long.toString(metadata.sampleCount), XSDint, TrackVocab.SampleCount.asNode(),
				attributeCounter.next());
		generateAttribute(triples, fileNode, fileNodeName, Long.toString(metadata.readCount),
				XSDint, TrackVocab.ReadCount.asNode(), attributeCounter.next());
		generateAttribute(triples, fileNode, fileNodeName, metadata.fileName, XSDstring,
				TrackVocab.FileName.asNode(), attributeCounter.next());
		switch(metadata.fileType.toUpperCase()) {
		case "GFF3":
		case "GTF":
				Iterator<String> gffIt = metadata.gffHeader.iterator();
				headerIterator(triples, gffIt, fileNode, TrackVocab.HeaderGFF.asNode());
			break;
		case "BED":
				Iterator<String> bedIt = metadata.bedHeader.iterator();
				headerIterator(triples, bedIt, fileNode, TrackVocab.HeaderBED.asNode());
			break;
		case "BAM":
			metadata.typeList.add(SoVocab.read.asNode());
			metadata.typeList.add(SoVocab.nucleotide_match.asNode());
				// TODO Can be parsed further, ontology terms needed
				Iterator<SAMProgramRecord> samit = metadata.samHeader.getProgramRecords().iterator();
				// headerIterator(triples, samit, fileNode);
			break;
		case "VCF":
				int index = 1;
				Iterator<VCFFilterHeaderLine> filterIt = metadata.vcfHeader.getFilterLines().iterator();
				while (filterIt.hasNext()) {
					VCFFilterHeaderLine header = filterIt.next();
					String headerNodeName = (fileNodeName + "_FILTER_" + index++);
					Node headerNode = tripleGenerator.generateURI(headerNodeName);
					triples.add(new Triple(fileNode, GfvoVocab.has_member.asNode(), headerNode));
					triples.add(new Triple(headerNode, RDF.type.asNode(), GfvoVocab.Variant_Calling.asNode()));
					// attributeNGenerator(triples, headerNode, headerNodeName,
					// header.getID(), XSDstring, GfvoVocab.Label.asNode(), 1);
					// attributeNGenerator(triples, headerNode, headerNodeName,
					// header.getValue(), XSDstring, GfvoVocab.Comment.asNode(), 2);

					// triples.add(new Triple(headerNode, RDFS.label.asNode(),
					// NodeFactory.createLiteral(header.getDescription())));
					triples.add(
							new Triple(headerNode, RDFS.comment.asNode(), NodeFactory.createLiteral(header.toString())));

				}
				Iterator<VCFFormatHeaderLine> formatIt = metadata.vcfHeader.getFormatHeaderLines().iterator();
				index = 1;
				while (formatIt.hasNext()) {
					VCFFormatHeaderLine header = formatIt.next();
					String headerNodeName = (fileNodeName + "_FORMAT_" + index++);
					Node headerNode = tripleGenerator.generateURI(headerNodeName);
					triples.add(new Triple(fileNode, GfvoVocab.has_member.asNode(), headerNode));
					triples.add(new Triple(headerNode, RDF.type.asNode(), GfvoVocab.Comment.asNode()));
					triples.add(
							new Triple(headerNode, RDF.value.asNode(), NodeFactory.createLiteral(header.getDescription())));
					triples.add(new Triple(headerNode, RDFS.label.asNode(), NodeFactory.createLiteral(header.getID())));

					// attributeNGenerator(triples, headerNode, headerNodeName,
					// header.getID(), XSDstring, GfvoVocab.Identifier.asNode(), 1);
				}
				Iterator<VCFInfoHeaderLine> infoIt = metadata.vcfHeader.getInfoHeaderLines().iterator();
				index = 1;
				while (infoIt.hasNext()) {
					VCFInfoHeaderLine header = infoIt.next();
					String headerNodeName = (fileNodeName + "_INFO_" + index++);
					Node headerNode = tripleGenerator.generateURI(headerNodeName);
					triples.add(new Triple(fileNode, GfvoVocab.has_member.asNode(), headerNode));
					triples.add(new Triple(headerNode, RDF.type.asNode(), GfvoVocab.Comment.asNode()));
					triples.add(
							new Triple(headerNode, RDF.value.asNode(), NodeFactory.createLiteral(header.getDescription())));
					triples.add(new Triple(headerNode, RDFS.label.asNode(), NodeFactory.createLiteral(header.getID())));

					// attributeNGenerator(triples, headerNode, headerNodeName,
					// header.getID(), XSDstring, GfvoVocab.Identifier.asNode(), 1);
				}
				Iterator<VCFHeaderLine> metadataIt = metadata.vcfHeader.getOtherHeaderLines().iterator();
				// TODO Can be parsed further
				index = 1;
				while (metadataIt.hasNext()) {
					VCFHeaderLine header = metadataIt.next();
					String headerNodeName = ("_HL_" + index++);
					Node headerNode = tripleGenerator.generateURI(headerNodeName);
					triples.add(new Triple(fileNode, GfvoVocab.has_member.asNode(), headerNode));
					triples.add(new Triple(headerNode, RDF.type.asNode(), GfvoVocab.Comment.asNode()));
					triples.add(new Triple(headerNode, RDF.value.asNode(), NodeFactory.createLiteral(header.getValue())));

				}

		}
		return triples;
	}

	
	public List<Triple> createGeneralMetadata(Track track, List<Node> types, String graphName, Counter attributeCounter) {
		List<Triple> triples = new LinkedList<>();
		Node graph = NodeFactory.createURI(graphName);
		generateAttribute(triples, graph, graphName, track.getFileType(), XSDstring,
				TrackVocab.FileExtension.asNode(), attributeCounter.next());
		generateAttribute(triples, graph, graphName, (new Date()).toString(), XSDstring,
				TrackVocab.ConversionDate.asNode(), attributeCounter.next());
		
		Set<Node> uniqueTypes = new HashSet<Node>(types);
		for (Node x : uniqueTypes) {
			// triples.add(new Triple(Graph, TrackVocab.holds.asNode(), x));
			triples.add(new Triple(graph, TrackVocab.holds.asNode(), x));
		}

		return triples;
	}

	public List<Triple> headerIterator(List<Triple> triples, Iterator<String> it, Node fileNode, Node attributeNode) {
		int index = 1;
		while (it.hasNext()) {
			String header = it.next();
			String headerNodeName = ("HL_" + index++);
			Node headerNode = tripleGenerator.generateURI(headerNodeName);
			triples.add(new Triple(fileNode, TrackVocab.hasAttribute.asNode(), headerNode));
			triples.add(new Triple(headerNode, RDF.type.asNode(), attributeNode));
			triples.add(new Triple(headerNode, RDF.value.asNode(), NodeFactory.createLiteral(header)));
		}
		return triples;
	}

}
