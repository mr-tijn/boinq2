package com.genohm.boinq.service;

import org.apache.jena.riot.system.PrefixMap;
import org.apache.jena.riot.system.PrefixMapStd;
import org.springframework.stereotype.Service;

import com.genohm.boinq.tools.vocabularies.CommonVocabulary;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;
import com.hp.hpl.jena.sparql.expr.E_Bound;
import com.hp.hpl.jena.sparql.expr.E_LogicalNot;
import com.hp.hpl.jena.sparql.expr.ExprVar;
import com.hp.hpl.jena.sparql.modify.request.QuadDataAcc;
import com.hp.hpl.jena.sparql.modify.request.UpdateDataInsert;
import com.hp.hpl.jena.sparql.syntax.ElementFilter;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementOptional;
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
	
	public String insertStatement(String graphUri, String subject, String predicate, String object) {
		QuadDataAcc newData = new QuadDataAcc();
		newData.setGraph(NodeFactoryExtra.parseNode(graphUri));
		newData.addTriple(new Triple(NodeFactoryExtra.parseNode(subject,commonPrefixMap),NodeFactoryExtra.parseNode(predicate,commonPrefixMap),NodeFactoryExtra.parseNode(object,commonPrefixMap)));
		UpdateDataInsert insertStatement = new UpdateDataInsert(newData);
		return insertStatement.toString(commonPrefixes);
	}
	
}
