package com.genohm.boinq.service;

import org.apache.jena.riot.system.PrefixMap;
import org.apache.jena.riot.system.PrefixMapStd;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.match.Match;
import com.genohm.boinq.domain.match.MatchFactory;
import com.genohm.boinq.tools.generators.ARQGenerator;
import com.genohm.boinq.tools.vocabularies.CommonVocabulary;
import com.genohm.boinq.web.rest.dto.MatchDTO;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;
import com.hp.hpl.jena.sparql.core.TriplePath;
import com.hp.hpl.jena.sparql.expr.E_Bound;
import com.hp.hpl.jena.sparql.expr.E_LogicalNot;
import com.hp.hpl.jena.sparql.expr.E_Regex;
import com.hp.hpl.jena.sparql.expr.ExprVar;
import com.hp.hpl.jena.sparql.modify.request.QuadDataAcc;
import com.hp.hpl.jena.sparql.modify.request.UpdateDataInsert;
import com.hp.hpl.jena.sparql.path.P_Link;
import com.hp.hpl.jena.sparql.path.P_ZeroOrMoreN;
import com.hp.hpl.jena.sparql.syntax.ElementFilter;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementOptional;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;
import com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock;
import com.hp.hpl.jena.sparql.util.NodeFactoryExtra;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.vocabulary.RDFS;

@Service
public class QueryBuilderService {

	
	public static PrefixMapping commonPrefixes = new PrefixMappingImpl();
	{
		commonPrefixes.setNsPrefix("rdf",CommonVocabulary.rdfBaseURI);
		commonPrefixes.setNsPrefix("rdfs", CommonVocabulary.rdfsBaseURI);
		commonPrefixes.setNsPrefix("obo", CommonVocabulary.oboBaseURI);
		commonPrefixes.setNsPrefix("xmlschema", CommonVocabulary.xmlSchemaURI);
		commonPrefixes.setNsPrefix("owl", CommonVocabulary.owlBaseURI);
	}

	public static PrefixMap commonPrefixMap = new PrefixMapStd();
	{
		commonPrefixMap.add("rdf", CommonVocabulary.rdfBaseURI);
		commonPrefixMap.add("rdfs", CommonVocabulary.rdfsBaseURI);
		commonPrefixMap.add("obo", CommonVocabulary.oboBaseURI);
		commonPrefixMap.add("xmlschema", CommonVocabulary.xmlSchemaURI);
		commonPrefixMap.add("owl", CommonVocabulary.owlBaseURI);
	}
	
	public String getRootNodes() {
		Query query = new Query();
		query.setQuerySelectType();
		query.setPrefixMapping(commonPrefixes);
		Node uri = NodeFactory.createVariable("uri");
		Node label = NodeFactory.createVariable("label");
		Node superr = NodeFactory.createVariable("super");
		query.addResultVar(uri);
		query.addResultVar(label);
		ElementTriplesBlock triples = new ElementTriplesBlock();
		triples.addTriple(new Triple(uri, RDFS.label.asNode(), label));
		ElementTriplesBlock optionalTriplesBlock = new ElementTriplesBlock();
		optionalTriplesBlock.addTriple(new Triple(uri, RDFS.subClassOf.asNode(), superr));
		ElementOptional optionalTriples = new ElementOptional(optionalTriplesBlock);
		ElementFilter noSuper = new ElementFilter(new E_LogicalNot(new E_Bound(new ExprVar(superr))));
		ElementGroup group = new ElementGroup();
		group.addElement(triples);
		group.addElement(optionalTriples);
		group.addElement(noSuper);
		query.setQueryPattern(group);
		return query.toString(Syntax.syntaxSPARQL_11);
	}
	
	public String getChildNodes(String parentUri) {
		Query query = new Query();
		query.setQuerySelectType();
		query.setPrefixMapping(commonPrefixes);
		Node uri = NodeFactory.createVariable("uri");
		Node label = NodeFactory.createVariable("label");
		query.addResultVar(uri);
		query.addResultVar(label);
		ElementTriplesBlock triples = new ElementTriplesBlock();
		triples.addTriple(new Triple(uri, RDFS.label.asNode(), label));
		triples.addTriple(new Triple(uri, RDFS.subClassOf.asNode(), NodeFactory.createURI(parentUri)));
		ElementGroup group = new ElementGroup();
		group.addElement(triples);
		query.setQueryPattern(group);
		return query.toString(Syntax.syntaxSPARQL_11);
	}
	
	public String getFilteredTree(String match) {
		Query query = new Query();
		query.setQuerySelectType();
		query.setPrefixMapping(commonPrefixes);
		Node matchingTerm = NodeFactory.createVariable("matchingterm");
		Node parent = NodeFactory.createVariable("parent");
		Node label = NodeFactory.createVariable("label");
		Node term = NodeFactory.createVariable("term");
		ElementTriplesBlock optionalTriples = new ElementTriplesBlock();
		optionalTriples.addTriple(new Triple(term, RDFS.subClassOf.asNode(), parent));
		ElementOptional optional = new ElementOptional(optionalTriples);
		ElementPathBlock pathTriples = new ElementPathBlock();
		TriplePath allParents = new TriplePath(matchingTerm, new P_ZeroOrMoreN(new P_Link(RDFS.subClassOf.asNode())), term);
		pathTriples.addTriplePath(allParents);
		pathTriples.addTriple(new Triple(matchingTerm, RDFS.label.asNode(), label));
		ElementFilter filter= new ElementFilter(new E_Regex(new ExprVar(label),match,"i"));
		ElementGroup group = new ElementGroup();
		group.addElement(pathTriples);
		group.addElement(optional);
		group.addElement(filter);
		query.addResultVar(parent);
		query.addResultVar(term);
		query.setDistinct(true);
		query.setQueryPattern(group);
		//TODO: matchingterm not interesting; add termlabel to term and retrieve
		return query.toString(Syntax.syntaxSPARQL_11);
	}
	
	public String insertStatement(String graphUri, String subject, String predicate, String object) {
		QuadDataAcc newData = new QuadDataAcc();
		newData.setGraph(NodeFactoryExtra.parseNode(graphUri));
		newData.addTriple(new Triple(NodeFactoryExtra.parseNode(subject,commonPrefixMap),NodeFactoryExtra.parseNode(predicate,commonPrefixMap),NodeFactoryExtra.parseNode(object,commonPrefixMap)));
		UpdateDataInsert insertStatement = new UpdateDataInsert(newData);
		return insertStatement.toString(commonPrefixes);
	}

	public String getQueryFromMatch(MatchDTO matchDTO) throws Exception {
		ARQGenerator generator = new ARQGenerator();
		Match rootMatch = MatchFactory.fromDTO(matchDTO);
		return generator.generateQuery(rootMatch);
	}
	
}
