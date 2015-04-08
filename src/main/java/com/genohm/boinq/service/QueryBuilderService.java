package com.genohm.boinq.service;

import org.apache.jena.riot.system.PrefixMap;
import org.apache.jena.riot.system.PrefixMapStd;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.match.Match;
import com.genohm.boinq.domain.match.MatchFactory;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.tools.generators.ARQGenerator;
import com.genohm.boinq.tools.vocabularies.CommonVocabulary;
import com.genohm.boinq.tools.vocabularies.FaldoVocabulary;
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
import com.hp.hpl.jena.sparql.expr.E_Equals;
import com.hp.hpl.jena.sparql.expr.E_GreaterThanOrEqual;
import com.hp.hpl.jena.sparql.expr.E_LessThanOrEqual;
import com.hp.hpl.jena.sparql.expr.E_LogicalNot;
import com.hp.hpl.jena.sparql.expr.E_Regex;
import com.hp.hpl.jena.sparql.expr.E_Str;
import com.hp.hpl.jena.sparql.expr.ExprVar;
import com.hp.hpl.jena.sparql.modify.request.QuadDataAcc;
import com.hp.hpl.jena.sparql.modify.request.UpdateDataInsert;
import com.hp.hpl.jena.sparql.path.P_Link;
import com.hp.hpl.jena.sparql.path.P_ZeroOrMoreN;
import com.hp.hpl.jena.sparql.syntax.ElementFilter;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementNamedGraph;
import com.hp.hpl.jena.sparql.syntax.ElementOptional;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;
import com.hp.hpl.jena.sparql.syntax.ElementService;
import com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock;
import com.hp.hpl.jena.sparql.util.ExprUtils;
import com.hp.hpl.jena.sparql.util.NodeFactoryExtra;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

@Service
public class QueryBuilderService {

	
	public static final String ORIGINAL_REFERENCE_LABEL = "originalReferenceLabel";
	public static final String TARGET_REFERENCE = "targetReference";
	public static final String ORIGINAL_REFERENCE = "originalReference";
	public static final String FEATURE_STRAND = "featureStrand";
	public static final String FEATURE_REFERENCE = "featureReference";
	public static final String FEATURE_END_POS = "featureEndPos";
	public static final String FEATURE_BEGIN_POS = "featureBeginPos";
	public static final String FEATURE_ID = "featureId";
	public static PrefixMapping commonPrefixes = new PrefixMappingImpl();
	public static PrefixMapping faldoPrefixes = new PrefixMappingImpl();
	{
		commonPrefixes.setNsPrefix("rdf",CommonVocabulary.rdfBaseURI);
		commonPrefixes.setNsPrefix("rdfs", CommonVocabulary.rdfsBaseURI);
		commonPrefixes.setNsPrefix("obo", CommonVocabulary.oboBaseURI);
		commonPrefixes.setNsPrefix("xsd", CommonVocabulary.xmlSchemaURI);
		commonPrefixes.setNsPrefix("owl", CommonVocabulary.owlBaseURI);
		commonPrefixes.setNsPrefix("skos", CommonVocabulary.skosBaseURI);
		
		faldoPrefixes.setNsPrefixes(commonPrefixes);
		faldoPrefixes.setNsPrefix("faldo", FaldoVocabulary.baseURI);
	}

	public static PrefixMap commonPrefixMap = new PrefixMapStd();
	public static PrefixMap faldoPrefixMap = new PrefixMapStd();
	{
		commonPrefixMap.add("rdf", CommonVocabulary.rdfBaseURI);
		commonPrefixMap.add("rdfs", CommonVocabulary.rdfsBaseURI);
		commonPrefixMap.add("obo", CommonVocabulary.oboBaseURI);
		commonPrefixMap.add("xsd", CommonVocabulary.xmlSchemaURI);
		commonPrefixMap.add("owl", CommonVocabulary.owlBaseURI);
		commonPrefixMap.putAll(commonPrefixMap);
		commonPrefixMap.add("faldo", FaldoVocabulary.baseURI);
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
		Node matchingLabel = NodeFactory.createVariable("matchinglabel");
		Node parenturi = NodeFactory.createVariable("parenturi");
		Node uri = NodeFactory.createVariable("uri");
		Node label = NodeFactory.createVariable("label");
		ElementTriplesBlock optionalTriples = new ElementTriplesBlock();
		optionalTriples.addTriple(new Triple(uri, RDFS.subClassOf.asNode(), parenturi));
		ElementOptional optional = new ElementOptional(optionalTriples);
		ElementPathBlock pathTriples = new ElementPathBlock();
		pathTriples.addTriplePath(new TriplePath(matchingTerm, new P_ZeroOrMoreN(new P_Link(RDFS.subClassOf.asNode())), uri));
		pathTriples.addTriple(new Triple(matchingTerm, RDFS.label.asNode(), matchingLabel));
		pathTriples.addTriple(new Triple(uri, RDFS.label.asNode(), label));
		ElementFilter filter= new ElementFilter(new E_Regex(new ExprVar(matchingLabel),match,"i"));
		ElementGroup group = new ElementGroup();
		group.addElement(pathTriples);
		group.addElement(optional);
		group.addElement(filter);
		query.addResultVar(parenturi);
		query.addResultVar(uri);
		query.addResultVar(label);
		query.setDistinct(true);
		query.setQueryPattern(group);
		query.setLimit(1000);
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
	
	public String getFaldoFeatures(String localReference, Long begin, Long end, Boolean strand) {
		
		// finds by assembly name, begin, end, strand
		
		Query mainQuery = new Query();
		mainQuery.setQuerySelectType();

		Node featureId = NodeFactory.createVariable(FEATURE_ID);
		Node featureBeginPos = NodeFactory.createVariable(FEATURE_BEGIN_POS);
		Node featureEndPos = NodeFactory.createVariable(FEATURE_END_POS);
		Node featureReference = NodeFactory.createVariable(FEATURE_REFERENCE);
		Node featurePositionType = NodeFactory.createVariable("featurePositionType");
		
		Node feature = NodeFactory.createVariable("feature");
		
		mainQuery.addResultVar(featureId);
		mainQuery.addResultVar(featureBeginPos);
		mainQuery.addResultVar(featureEndPos);
		if (localReference != null) {
			mainQuery.addResultVar(featureReference, ExprUtils.nodeToExpr(NodeFactory.createURI(localReference)));
		} else {
			mainQuery.addResultVar(featureReference);
		}
		mainQuery.addResultVar(FEATURE_STRAND, new E_Equals(new ExprVar(featurePositionType), ExprUtils.nodeToExpr(FaldoVocabulary.ForwardStrandPosition)));
		
		ElementGroup mainSelect = new ElementGroup();

		ElementTriplesBlock triples = new ElementTriplesBlock();
		triples.addTriple(new Triple(feature, RDFS.label.asNode(), featureId));
		Node featureBegin = NodeFactory.createVariable("featureBegin");
		Node featureEnd = NodeFactory.createVariable("featureEnd");
		triples.addTriple(new Triple(feature, FaldoVocabulary.begin, featureBegin));
		triples.addTriple(new Triple(feature, FaldoVocabulary.end, featureEnd));
		triples.addTriple(new Triple(featureBegin, FaldoVocabulary.position, featureBeginPos));
		if (localReference != null) {
//			mainSelect.addElementFilter(new ElementFilter(new E_Equals(new ExprVar(featureReference), ExprUtils.nodeToExpr(NodeFactory.createURI(localReference))))); 
			triples.addTriple(new Triple(featureBegin, FaldoVocabulary.reference, NodeFactory.createURI(localReference)));
		} else {
			triples.addTriple(new Triple(featureBegin, FaldoVocabulary.reference, featureReference));
		}
		triples.addTriple(new Triple(featureBegin, RDF.type.asNode(), featurePositionType));
		if (strand != null) {
			if (strand) {
				triples.addTriple(new Triple(featureBegin, RDF.type.asNode(),FaldoVocabulary.ForwardStrandPosition));
			} else {
				triples.addTriple(new Triple(featureBegin, RDF.type.asNode(),FaldoVocabulary.ReverseStrandPosition));
			}
		}
		triples.addTriple(new Triple(featureEnd, FaldoVocabulary.position, featureEndPos));
		
		mainSelect.addElement(triples);

		if (end != null) {
			mainSelect.addElementFilter(new ElementFilter(new E_LessThanOrEqual(new ExprVar(featureBeginPos), ExprUtils.parse(end.toString()))));
		}
		if (begin != null) {
			mainSelect.addElementFilter(new ElementFilter(new E_GreaterThanOrEqual(new ExprVar(featureEndPos), ExprUtils.parse(begin.toString()))));
		}
		
		
		
		mainQuery.setQueryPattern(mainSelect);
		mainQuery.addOrderBy(featureBeginPos, 1); //asc
		
		
		return mainQuery.toString(Syntax.syntaxSPARQL_11);
	}
	
	
	public String findLocalReference(Track track, Node globalReference) {
		
		Query mainQuery = new Query();
		mainQuery.setQuerySelectType();
		
		ElementGroup mainSelect = new ElementGroup();
		
		// get sequence type ?
		// get reference map
		
		Node trackGraph = NodeFactory.createURI(track.getGraphName());
		Node referenceMapEntry = NodeFactory.createVariable("referenceMapEntry");
		Node originalReference = NodeFactory.createVariable(ORIGINAL_REFERENCE);
		
		mainQuery.addResultVar(originalReference);
		
		ElementTriplesBlock triples = new ElementTriplesBlock();
		triples.addTriple(new Triple(trackGraph, RDF.type.asNode(), TrackVocab.Track.asNode()));
		triples.addTriple(new Triple(trackGraph, TrackVocab.entry.asNode(), referenceMapEntry));
		triples.addTriple(new Triple(referenceMapEntry, TrackVocab.originalReference.asNode(), originalReference));
		triples.addTriple(new Triple(referenceMapEntry, TrackVocab.targetReference.asNode(), globalReference));
		
		mainSelect.addElement(triples);
		mainQuery.setQueryPattern(mainSelect);
		
		return mainQuery.toString(Syntax.syntaxSPARQL_11);
		
	}

}
