package com.genohm.boinq.tools.fileformats;

import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDint;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.faldo.FaldoFeature;
import com.genohm.boinq.generated.vocabularies.FaldoVocab;
import com.genohm.boinq.service.TripleGeneratorService;

@Service
public class FaldoBuilder extends TripleBuilder {

	@Inject
	TripleGeneratorService tripleGenerator;

	private FaldoBuilder() {}
	
	public FaldoBuilder(TripleGeneratorService tripleGenerator) {
		super(tripleGenerator);
	}

	// still needed ?
	public List<Triple> convert(FaldoFeature faldoFeature) {
		List<Triple> result = new LinkedList<Triple>();
		// feature is the subject of all following triples except the FALDO
		// exact position triples
		String featureName = FEATUREBASEURI + faldoFeature.id;
		Node feature = NodeFactory.createURI(featureName);

		Node ref = NodeFactory.createURI(faldoFeature.assembly);

		Node featureBegin = NodeFactory.createURI(LOCATIONBASEURI + faldoFeature.id);
		Node featureEnd = NodeFactory.createURI(LOCATIONBASEURI + faldoFeature.id);
		result.add(new Triple(feature, RDFS.label.asNode(), NodeFactory.createLiteral(faldoFeature.id)));
		result.add(new Triple(feature, FaldoVocab.begin.asNode(), featureBegin));
		result.add(new Triple(feature, FaldoVocab.end.asNode(), featureEnd));
		result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));
		result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ExactPosition.asNode()));
		result.add(new Triple(featureBegin, FaldoVocab.position.asNode(),
				NodeFactory.createLiteral(faldoFeature.start.toString(), XSDint)));
		result.add(new Triple(featureBegin, FaldoVocab.reference.asNode(), ref));
		result.add(new Triple(featureEnd, FaldoVocab.position.asNode(),
				NodeFactory.createLiteral(faldoFeature.end.toString(), XSDint)));
		result.add(new Triple(featureEnd, FaldoVocab.reference.asNode(), ref));

		if (faldoFeature.strand) {
			result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ForwardStrandPosition.asNode()));
			result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ForwardStrandPosition.asNode()));
		} else {
			result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ReverseStrandPosition.asNode()));
			result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocab.ReverseStrandPosition.asNode()));

		}
		return result;

	}


}
