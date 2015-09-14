package com.genohm.boinq.tools.generators;

import java.util.LinkedList;
import java.util.List;

import com.genohm.boinq.domain.match.Match;
import com.genohm.boinq.domain.match.MatchAll;
import com.genohm.boinq.domain.match.MatchLocation;
import com.genohm.boinq.tools.vocabularies.CommonVocabulary;
import com.genohm.boinq.tools.vocabularies.FaldoVocabulary;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.Syntax;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.sparql.expr.E_Equals;
import org.apache.jena.sparql.expr.E_GreaterThanOrEqual;
import org.apache.jena.sparql.expr.E_LessThanOrEqual;
import org.apache.jena.sparql.expr.E_Str;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.sparql.util.ExprUtils;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

public class ARQGenerator implements SPARQLGenerator {

	protected int featureCounter = 1;
	protected int entityCounter = 1;
	protected ElementTriplesBlock triples = new ElementTriplesBlock();
	protected List<ElementFilter> filters = new LinkedList<ElementFilter>();
	protected List<Element> subElements = new LinkedList<Element>();
	
	protected String quote(String input) {
		if (input.startsWith("'")) return input;
		return String.format("'%s'", input);
	}

	protected String getFeatureIdentifier() {
		return "feature" + featureCounter++;
	}

	protected String getEntityIdentifier() {
		return "entity" + entityCounter++;
	}
	
	public String generateQuery(Match mainMatch) {
		Query mainQuery = new Query();
		mainQuery.setQuerySelectType();
		PrefixMapping prefixMap = new PrefixMappingImpl();
		prefixMap.setNsPrefix("rdf",CommonVocabulary.rdfBaseURI);
		prefixMap.setNsPrefix("rdfs", CommonVocabulary.rdfsBaseURI);
//		prefixMap.setNsPrefix("ensembl", DatasourceVocabulary.ensemblURI);
//		prefixMap.setNsPrefix("ds", DatasourceVocabulary.baseURI);
//		prefixMap.setNsPrefix("prop", DatasourceVocabulary.propertyHolderBaseURI);
		prefixMap.setNsPrefix("obo", CommonVocabulary.oboBaseURI);
		prefixMap.setNsPrefix("xsd", CommonVocabulary.xmlSchemaURI);
		prefixMap.setNsPrefix("owl", CommonVocabulary.owlBaseURI);
		prefixMap.setNsPrefix("faldo", FaldoVocabulary.baseURI);
		mainQuery.setPrefixMapping(prefixMap);
		Node featureId = NodeFactory.createVariable("featureId");
		Node featureBeginPos = NodeFactory.createVariable("featureBeginPos");
		Node featureEndPos = NodeFactory.createVariable("featureEndPos");
		Node featureReference = NodeFactory.createVariable("featureReference");
		Node featureReferenceName = NodeFactory.createVariable("featureReferenceName");
		Node featurePositionType = NodeFactory.createVariable("featurePositionType");
		
		Node feature = NodeFactory.createVariable("feature");
		
		mainQuery.addResultVar(featureId);
		mainQuery.addResultVar(featureBeginPos);
		mainQuery.addResultVar(featureEndPos);
		mainQuery.addResultVar(featureReferenceName);
		mainQuery.addResultVar("featureStrand", new E_Equals(new ExprVar(featurePositionType), ExprUtils.nodeToExpr(FaldoVocabulary.ForwardStrandPosition)));
		
		ElementGroup mainSelect = new ElementGroup();

		triples.addTriple(new Triple(feature, RDFS.label.asNode(), featureId));
		Node featureBegin = NodeFactory.createVariable("featureBegin");
		triples.addTriple(new Triple(feature, FaldoVocabulary.begin, featureBegin));
		triples.addTriple(new Triple(featureBegin, FaldoVocabulary.position, featureBeginPos));
		Node featureEnd = NodeFactory.createVariable("featureEnd");
		triples.addTriple(new Triple(feature, FaldoVocabulary.end, featureEnd));
		triples.addTriple(new Triple(featureEnd, FaldoVocabulary.position, featureEndPos));
		triples.addTriple(new Triple(featureBegin, FaldoVocabulary.reference, featureReference));
		triples.addTriple(new Triple(featureReference, RDFS.label.asNode(), featureReferenceName));
		triples.addTriple(new Triple(featureBegin, RDF.type.asNode(), featurePositionType));
	
		
		mainMatch.acceptGenerator(this, "feature");
		mainSelect.addElement(triples);
		for (Element subElement: subElements) {
			mainSelect.addElement(subElement);
		}
		for (ElementFilter filter: filters) {
			mainSelect.addElement(filter);
		}
		
		mainQuery.setQueryPattern(mainSelect);
		mainQuery.addOrderBy(featureBeginPos, 1); //asc
		
		
		return mainQuery.toString(Syntax.syntaxSPARQL_11);
	}


	public void visitMatch(MatchAll match, String subjectIdentifier) {
		for (Match node : match.getNodes()) {
			node.acceptGenerator(this, subjectIdentifier);
		}
	}

	public void visitMatch(MatchLocation match, String subjectIdentifier) {
		Node subject = NodeFactory.createVariable(subjectIdentifier);
		Node begin = NodeFactory.createVariable(subjectIdentifier + "Begin");
		Node end = NodeFactory.createVariable(subjectIdentifier + "End");
		Node beginPos = NodeFactory.createVariable(subjectIdentifier + "BeginPos");
		Node endPos = NodeFactory.createVariable(subjectIdentifier + "EndPos");
		Node featureReference = NodeFactory.createVariable(subjectIdentifier + "Reference");
		Node featureReferenceName = NodeFactory.createVariable(subjectIdentifier + "ReferenceName");
		Boolean addTriples = !"feature".equals(subjectIdentifier);
		if (addTriples) {
			triples.addTriple(new Triple(subject, FaldoVocabulary.begin, begin));
			triples.addTriple(new Triple(subject, FaldoVocabulary.end, end));
		}
		if (match.getContig() != null) {
			if (addTriples) {
				triples.addTriple(new Triple(begin, FaldoVocabulary.reference,	featureReference));
				triples.addTriple(new Triple(featureReference, RDFS.label.asNode(), featureReferenceName));
			}
			filters.add(new ElementFilter(new E_Equals(new E_Str(new ExprVar(featureReferenceName)), ExprUtils.parse(quote(match.getContig())))));
		}
		if (match.getEnd() != null) {
			if (addTriples) triples.addTriple(new Triple(begin, FaldoVocabulary.position, beginPos));
			filters.add(new ElementFilter(new E_LessThanOrEqual(new ExprVar(beginPos), ExprUtils.parse(match.getEnd().toString()))));
		}
		if (match.getStart() != null) {
			if (addTriples) triples.addTriple(new Triple(end, FaldoVocabulary.position, endPos));
			filters.add(new ElementFilter(new E_GreaterThanOrEqual(new ExprVar(endPos), ExprUtils.parse(match.getStart().toString()))));
		}
		if (match.getStrand() != null) {
			if (match.getStrand()) {
				triples.addTriple(new Triple(begin, RDF.type.asNode(),FaldoVocabulary.ForwardStrandPosition));
			} else {
				triples.addTriple(new Triple(begin, RDF.type.asNode(),FaldoVocabulary.ReverseStrandPosition));
			}
		}
	}
	
}
