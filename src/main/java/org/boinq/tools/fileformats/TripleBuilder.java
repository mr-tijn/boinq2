package org.boinq.tools.fileformats;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.vocabulary.RDF;
import org.boinq.domain.jobs.TripleConversion.Metadata;
import org.boinq.service.TripleGeneratorService;
import org.springframework.stereotype.Service;
import org.boinq.generated.vocabularies.FaldoVocab;
import org.boinq.generated.vocabularies.GfvoVocab;
import org.boinq.generated.vocabularies.SoVocab;

@Service
public class TripleBuilder {
	public static final String ENTRYBASEURI = "/resource/entry#";
	public static final String LOCATIONBASEURI = "/resource/";
	public static final String FEATUREBASEURI = "/resource/feature#";
	public static final String READBASEURI = "/resource/read#";
	public static final String REFERENCEBASEURI = "/resource/reference#";
	public static final String ASSEMBLYBASEURI = "/resource/Assembly:";

	@Inject
	protected TripleGeneratorService tripleGenerator;

	protected TripleBuilder() {}
	
	public TripleBuilder(TripleGeneratorService tripleGenerator) {
		this.tripleGenerator = tripleGenerator;
	}

	private String contigName(String contig, Metadata meta) {
		return LOCATIONBASEURI + meta.organismMapping + contig;
	}
	
	protected Node ensemblURI(String contig) {
		URI base = null;
		try {
			base = new URI("http://rdf.ebi.ac.uk");
		} catch (Exception e) {
			// should not go wrong
		}
		URI uri = base.resolve(contig);
		return NodeFactory.createURI(uri.toString());
	}
	
	protected String locationName(String contig, Long biologicalStartPosBase1 , Long biologicalEndPosBase1, Boolean forwardStrand, Metadata meta) {
		String locationName = contigName(contig, meta) + ":" + biologicalStartPosBase1 + "-" + biologicalEndPosBase1;
		if (forwardStrand != null) {
			String add = (forwardStrand) ? ":1" : ":-1";
			locationName += add;
		}
		return locationName;
	}
	
	protected void addFaldoTriples(Node feature, Long biologicalStartPosBase1, Long biologicalEndPosBase1, String contig,
			Boolean forwardStrand, List<Triple> triples, Metadata meta) {
		if (meta.fileType.equalsIgnoreCase("BED") || meta.fileType.equalsIgnoreCase("BAM")) {
			biologicalStartPosBase1++;
		}
		// TODO Introduce Assembly
		contig = contig.substring(meta.prefixLength);
		String contigName = contigName(contig, meta);
		Node reference = ensemblURI(contigName);
		String featureLocationName = locationName(contig, biologicalStartPosBase1, biologicalEndPosBase1, forwardStrand, meta);
		String featureBeginName = contigName + ":" + biologicalStartPosBase1;
		String featureEndName = contigName + ":" + biologicalEndPosBase1;
		if (forwardStrand != null) {
			String add = (forwardStrand) ? ":1" : ":-1";
			featureBeginName += add;
			featureEndName += add;
		} 
		Node featureLocation = tripleGenerator.generateURI(featureLocationName);
		Node featureBegin = tripleGenerator.generateURI(featureBeginName);
		Node featureEnd = tripleGenerator.generateURI(featureEndName);
		triples.add(new Triple(feature, FaldoVocab.location.asNode(), featureLocation));
		triples.add(new Triple(featureLocation, FaldoVocab.reference.asNode(), reference));
		triples.add(new Triple(featureLocation, RDF.type.asNode(), FaldoVocab.Region.asNode()));
		triples.add(new Triple(featureLocation, FaldoVocab.begin.asNode(), featureBegin));
		triples.add(new Triple(featureLocation, FaldoVocab.end.asNode(), featureEnd));
		triples.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));
		triples.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));
	
		triples.add(new Triple(featureBegin, FaldoVocab.position.asNode(),
				NodeFactory.createLiteral(biologicalStartPosBase1.toString(), XSDDatatype.XSDint)));
		triples.add(new Triple(featureEnd, FaldoVocab.position.asNode(),
				NodeFactory.createLiteral(biologicalEndPosBase1.toString(), XSDDatatype.XSDint)));
		if (forwardStrand != null) {
			triples.add(new Triple(featureBegin, RDF.type.asNode(), (forwardStrand
					? FaldoVocab.ForwardStrandPosition.asNode() : FaldoVocab.ReverseStrandPosition.asNode())));
			triples.add(new Triple(featureEnd, RDF.type.asNode(), (forwardStrand
					? FaldoVocab.ForwardStrandPosition.asNode() : FaldoVocab.ReverseStrandPosition.asNode())));
		} 
	
	}
	
	
	protected String generateAttribute(List<Triple> triples, Node feature, String featureURI, String value,
			XSDDatatype type, Node attributeType, int attributeIdx) {
		String featureAttributeName = featureURI + "/attribute_" + attributeIdx;
		Node featureAttribute = tripleGenerator.generateURI(featureAttributeName);
		triples.add(new Triple(feature, GfvoVocab.has_attribute.asNode(), featureAttribute));
		triples.add(new Triple(feature, SoVocab.has_quality.asNode(), featureAttribute));
		triples.add(new Triple(featureAttribute, RDF.type.asNode(), attributeType));
		triples.add(new Triple(featureAttribute, RDF.value.asNode(), NodeFactory.createLiteral(value, type)));
		return featureAttributeName;
	}

}
