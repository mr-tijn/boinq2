package com.genohm.boinq.service;

import org.apache.jena.riot.system.PrefixMap;
import org.apache.jena.riot.system.PrefixMapStd;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.Track;
import com.genohm.boinq.generated.vocabularies.FaldoVocab;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.tools.vocabularies.CommonVocabulary;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.Syntax;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.expr.E_Bound;
import org.apache.jena.sparql.expr.E_Equals;
import org.apache.jena.sparql.expr.E_GreaterThanOrEqual;
import org.apache.jena.sparql.expr.E_IRI;
import org.apache.jena.sparql.expr.E_LessThanOrEqual;
import org.apache.jena.sparql.expr.E_LogicalAnd;
import org.apache.jena.sparql.expr.E_LogicalNot;
import org.apache.jena.sparql.expr.E_OneOf;
import org.apache.jena.sparql.expr.E_Regex;
import org.apache.jena.sparql.expr.E_Str;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.expr.nodevalue.NodeValueNode;
import org.apache.jena.sparql.modify.request.QuadDataAcc;
import org.apache.jena.sparql.modify.request.UpdateDataInsert;
import org.apache.jena.sparql.path.P_Link;
import org.apache.jena.sparql.path.P_ZeroOrMore1;
import org.apache.jena.sparql.path.PathFactory;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementOptional;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.sparql.syntax.ElementUnion;
import org.apache.jena.sparql.util.ExprUtils;
import org.apache.jena.sparql.util.NodeFactoryExtra;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import static org.apache.jena.sparql.path.PathFactory.*;
@Service
public class QueryBuilderService {

	
	public static final String OPERATOR_NAME = "operatorName";
	public static final String ORIGINAL_REFERENCE_LABEL = "originalReferenceLabel";
	public static final String TARGET_REFERENCE = "targetReference";
	public static final String ORIGINAL_REFERENCE = "originalReference";
	public static final String FEATURE_STRAND = "featureStrand";
	public static final String FEATURE_REFERENCE = "featureReference";
	public static final String FEATURE_END_POS = "featureEndPos";
	public static final String FEATURE_BEGIN_POS = "featureBeginPos";
	public static final String FEATURE_ID = "featureId";
	public static final String VARIABLE_FEATURE_TYPE = "featureType";
	public static final String VARIABLE_FEATURE_TYPE_LABEL = "featureTypeLabel";
	public static final String OPERATOR = "operator";
	public static final String ENDPOINT_URI = "endpointUri";
	public static final String ROOT_TERM = "rootTerm";
	public static final String GRAPH_URI = "graphUri";
	public static final String REFERENCE_ASSEMBLY = "referenceAssembly";
	public static final String LOCALIZED_SEARCH = "localizedSearch";
	public static final String OPERATOR_TYPE = "operatorType";
	
	
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
		faldoPrefixes.setNsPrefix("faldo", FaldoVocab.NS);
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
		commonPrefixMap.add("faldo", FaldoVocab.NS);
	}
	
	private Node uri = NodeFactory.createVariable("uri");
	private Node label = NodeFactory.createVariable("label");
	private Node preflabelvar = NodeFactory.createVariable("preflabel");
	private Node superClass = NodeFactory.createVariable("super");
	private Node thingChild = NodeFactory.createVariable("thingChild");


	private Element labelPattern(Node uri, Node label) {
		ElementGroup matchLabelIfNotPrefLabel = new ElementGroup();
		
		ElementTriplesBlock labelPattern_label = new ElementTriplesBlock();
		labelPattern_label.addTriple(new Triple(uri, RDFS.label.asNode(), label));
		ElementTriplesBlock labelPattern_prefLabel = new ElementTriplesBlock();
		labelPattern_prefLabel.addTriple(new Triple(uri, SKOS.prefLabel.asNode(), preflabelvar));
		ElementOptional optionalPrefLabel = new ElementOptional(labelPattern_prefLabel);
		ElementFilter prefLabelNotBound = new ElementFilter(new E_LogicalNot(new E_Bound(new ExprVar(preflabelvar))));
		matchLabelIfNotPrefLabel.addElement(labelPattern_label);
		matchLabelIfNotPrefLabel.addElement(optionalPrefLabel);
		matchLabelIfNotPrefLabel.addElement(prefLabelNotBound);
		
		ElementTriplesBlock matchPrefLabel = new ElementTriplesBlock();
		matchPrefLabel.addTriple(new Triple(uri, SKOS.prefLabel.asNode(), label));

		ElementUnion labelPattern = new ElementUnion();
		labelPattern.addElement(matchPrefLabel);
		labelPattern.addElement(matchLabelIfNotPrefLabel);
		
		return labelPattern;
	}

	public String getRootNodes() {
		Query query = new Query();
		query.setQuerySelectType();
		query.setPrefixMapping(commonPrefixes);
		query.addResultVar(uri);
		query.addResultVar(label);
		
		ElementTriplesBlock triples = new ElementTriplesBlock();
		triples.addTriple(new Triple(uri, RDF.type.asNode() , OWL.Class.asNode()));
		
		ElementTriplesBlock optionalSuperClassTriples = new ElementTriplesBlock();
		optionalSuperClassTriples.addTriple(new Triple(uri, RDFS.subClassOf.asNode(), superClass));
		ElementOptional optionalSuperClass = new ElementOptional(optionalSuperClassTriples);
		
		ElementTriplesBlock optionalChildOfThingTriples = new ElementTriplesBlock();
		optionalChildOfThingTriples.addTriple(new Triple(thingChild, RDF.type.asNode(), OWL.Class.asNode()));
		optionalChildOfThingTriples.addTriple(new Triple(thingChild, RDFS.subClassOf.asNode(), OWL.Thing.asNode()));
		ElementOptional optionalChildOfThing = new ElementOptional(optionalChildOfThingTriples);
		
		ElementFilter notBound = new ElementFilter(new E_LogicalAnd(new E_LogicalNot(new E_Bound(new ExprVar(superClass))),new E_LogicalNot(new E_Bound(new ExprVar(thingChild)))));
		
		ElementGroup orphans = new ElementGroup();
		orphans.addElement(triples);
		orphans.addElement(labelPattern(uri,label));
		orphans.addElement(optionalSuperClass);
		orphans.addElement(optionalChildOfThing);
		orphans.addElement(notBound);
		
		ElementGroup childrenOfThing = new ElementGroup();
		
		ElementTriplesBlock childrenOfThingTriples = new ElementTriplesBlock();
		childrenOfThingTriples.addTriple(new Triple(uri, RDF.type.asNode() , OWL.Class.asNode()));
		childrenOfThingTriples.addTriple(new Triple(uri, RDFS.subClassOf.asNode(), OWL.Thing.asNode()));
		
		childrenOfThing.addElement(childrenOfThingTriples);
		childrenOfThing.addElement(labelPattern(uri,label));
		
		ElementUnion union = new ElementUnion();
		union.addElement(orphans);
		union.addElement(childrenOfThing);
		
		query.setQueryPattern(union);
		query.addOrderBy(label, 0);
		
		return query.toString(Syntax.syntaxSPARQL_11);
	}
	
	public String getChildNodes(String parentUri) {
		Query query = new Query();
		query.setQuerySelectType();
		query.setPrefixMapping(commonPrefixes);
		query.addResultVar(uri);
		query.addResultVar(label);
		ElementTriplesBlock childOf = new ElementTriplesBlock();
		childOf.addTriple(new Triple(uri, RDFS.subClassOf.asNode(), NodeFactory.createURI(parentUri)));
		ElementGroup pattern = new ElementGroup();
		pattern.addElement(labelPattern(uri,label));
		pattern.addElement(childOf);
		query.setQueryPattern(pattern);
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
		pathTriples.addTriplePath(new TriplePath(matchingTerm, new P_ZeroOrMore1(new P_Link(RDFS.subClassOf.asNode())), uri));
		pathTriples.addTriple(new Triple(matchingTerm, RDFS.label.asNode(), matchingLabel));
		pathTriples.addTriple(new Triple(uri, RDFS.label.asNode(), label));
		ElementFilter filter= new ElementFilter(new E_Regex(new ExprVar(matchingLabel),match,"i"));
		ElementGroup group = new ElementGroup();
		group.addElement(pathTriples);
//		group.addElement(labelPattern(uri,label));
//		group.addElement(labelPattern(matchingTerm, matchingLabel));
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
		mainQuery.addResultVar(FEATURE_STRAND, new E_Equals(new ExprVar(featurePositionType), ExprUtils.nodeToExpr(FaldoVocab.ForwardStrandPosition.asNode())));
		
		ElementGroup mainSelect = new ElementGroup();

		ElementTriplesBlock triples = new ElementTriplesBlock();
		triples.addTriple(new Triple(feature, RDFS.label.asNode(), featureId));
		Node featureBegin = NodeFactory.createVariable("featureBegin");
		Node featureEnd = NodeFactory.createVariable("featureEnd");
		triples.addTriple(new Triple(feature, FaldoVocab.begin.asNode(), featureBegin));
		triples.addTriple(new Triple(feature, FaldoVocab.end.asNode(), featureEnd));
		triples.addTriple(new Triple(featureBegin, FaldoVocab.position.asNode(), featureBeginPos));
		if (localReference != null) {
//			mainSelect.addElementFilter(new ElementFilter(new E_Equals(new ExprVar(featureReference), ExprUtils.nodeToExpr(NodeFactory.createURI(localReference))))); 
			triples.addTriple(new Triple(featureBegin, FaldoVocab.reference.asNode(), NodeFactory.createURI(localReference)));
		} else {
			triples.addTriple(new Triple(featureBegin, FaldoVocab.reference.asNode(), featureReference));
		}
		triples.addTriple(new Triple(featureBegin, RDF.type.asNode(), featurePositionType));
		if (strand != null) {
			if (strand) {
				triples.addTriple(new Triple(featureBegin, RDF.type.asNode(),FaldoVocab.ForwardStrandPosition.asNode()));
			} else {
				triples.addTriple(new Triple(featureBegin, RDF.type.asNode(),FaldoVocab.ReverseStrandPosition.asNode()));
			}
		}
		triples.addTriple(new Triple(featureEnd, FaldoVocab.position.asNode(), featureEndPos));
		
		mainSelect.addElement(triples);

		if (end != null) {
			mainSelect.addElementFilter(new ElementFilter(new E_LessThanOrEqual(new ExprVar(featureBeginPos), ExprUtils.parse(end.toString()))));
		}
		if (begin != null) {
			mainSelect.addElementFilter(new ElementFilter(new E_GreaterThanOrEqual(new ExprVar(featureEndPos), ExprUtils.parse(begin.toString()))));
		}
		ExprList targetExpressions = new ExprList();
		targetExpressions.add(new E_IRI(new NodeValueNode(FaldoVocab.ForwardStrandPosition.asNode())));
		targetExpressions.add(new E_IRI(new NodeValueNode(FaldoVocab.ReverseStrandPosition.asNode())));
		E_OneOf orientations = new E_OneOf(new E_IRI(new ExprVar(featurePositionType)), targetExpressions);

		mainSelect.addElementFilter(new ElementFilter(orientations));
		
		mainQuery.setQueryPattern(mainSelect);
		mainQuery.addOrderBy(featureBeginPos, 1); //asc
		
		
		return mainQuery.toString(Syntax.syntaxSPARQL_11);
	}
	
	
	public String findReferenceMap(Track track) {
		
		Query mainQuery = new Query();
		mainQuery.setQuerySelectType();
		
		ElementGroup mainSelect = new ElementGroup();
		
		Node trackGraph = NodeFactory.createURI(track.getGraphName());
		Node referenceMapEntry = NodeFactory.createVariable("referenceMapEntry");
		Node originalReference = NodeFactory.createVariable(ORIGINAL_REFERENCE);
		Node targetReference = NodeFactory.createVariable(TARGET_REFERENCE);
		
		mainQuery.addResultVar(targetReference);
		mainQuery.addResultVar(originalReference);
		
		ElementTriplesBlock triples = new ElementTriplesBlock();
		triples.addTriple(new Triple(trackGraph, RDF.type.asNode(), TrackVocab.Track.asNode()));
		triples.addTriple(new Triple(trackGraph, TrackVocab.entry.asNode(), referenceMapEntry));
		triples.addTriple(new Triple(referenceMapEntry, TrackVocab.originalReference.asNode(), originalReference));
		triples.addTriple(new Triple(referenceMapEntry, TrackVocab.targetReference.asNode(), targetReference));
		
		mainSelect.addElement(triples);
		mainQuery.setQueryPattern(mainSelect);
		
		return mainQuery.toString(Syntax.syntaxSPARQL_11);
	
	}
	
	
	public String findGlobalReference(Track track, String originalReferenceStr) {

		Query mainQuery = new Query();
		mainQuery.setQuerySelectType();
		
		ElementGroup mainSelect = new ElementGroup();
		
		// get sequence type ?
		// get reference map
		
		Node trackGraph = NodeFactory.createURI(track.getGraphName());
		Node referenceMapEntry = NodeFactory.createVariable("referenceMapEntry");
		Node originalReference = NodeFactory.createVariable(ORIGINAL_REFERENCE);
		Node targetReference = NodeFactory.createVariable(TARGET_REFERENCE);
		
		mainQuery.addResultVar(targetReference);
		
		ElementTriplesBlock triples = new ElementTriplesBlock();
		triples.addTriple(new Triple(trackGraph, RDF.type.asNode(), TrackVocab.Track.asNode()));
		triples.addTriple(new Triple(trackGraph, TrackVocab.entry.asNode(), referenceMapEntry));
		triples.addTriple(new Triple(referenceMapEntry, TrackVocab.originalReference.asNode(), originalReference));
		triples.addTriple(new Triple(referenceMapEntry, TrackVocab.targetReference.asNode(), targetReference));
		
		ElementFilter filterTargetRef = new ElementFilter(new E_Equals(new E_Str(new ExprVar(originalReference)), ExprUtils.nodeToExpr(NodeFactory.createLiteral(originalReferenceStr))));
		
		mainSelect.addElement(triples);
		mainSelect.addElement(filterTargetRef);
		mainQuery.setQueryPattern(mainSelect);
		
		return mainQuery.toString(Syntax.syntaxSPARQL_11);

	}
	
	
	public String findFeatureTypes(String trackGraphName) {
		Query mainQuery = new Query();
		mainQuery.setQuerySelectType();
		
		Node featureType = NodeFactory.createVariable(VARIABLE_FEATURE_TYPE);
		Node label = NodeFactory.createVariable(VARIABLE_FEATURE_TYPE_LABEL);
		
		ElementGroup main = new ElementGroup();
		ElementTriplesBlock triples = new ElementTriplesBlock();
		triples.addTriple(new Triple(NodeFactory.createURI(trackGraphName),TrackVocab.holds.asNode(), featureType));
		main.addElement(triples);
		main.addElement(labelPattern(featureType, label));
		
		mainQuery.addResultVar(featureType);
		mainQuery.addResultVar(label);
		mainQuery.setQueryPattern(main);
		
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

	public String getOperators(Track track) {
		
		Query mainQuery = new Query();
		mainQuery.setQuerySelectType();
		
		ElementGroup mainSelect = new ElementGroup();
		
		Node trackGraph = NodeFactory.createURI(track.getGraphName());
		Node supportedOperator = NodeFactory.createVariable(OPERATOR);
		Node endpointVar = NodeFactory.createVariable(ENDPOINT_URI);
		Node graphVar = NodeFactory.createVariable(GRAPH_URI);
		Node rootVar = NodeFactory.createVariable(ROOT_TERM);
		Node refSeqVar = NodeFactory.createVariable(REFERENCE_ASSEMBLY);
		Node operatorTypeVar = NodeFactory.createVariable(OPERATOR_TYPE);
		Node operatorNameVar = NodeFactory.createVariable(OPERATOR_NAME);
		
		mainQuery.addResultVar(supportedOperator);
		mainQuery.addResultVar(operatorNameVar);
		
		ElementPathBlock mainElement = new ElementPathBlock();
		mainElement.addTriple(new Triple(trackGraph, RDF.type.asNode(), TrackVocab.Track.asNode()));
		mainElement.addTriple(new Triple(trackGraph, TrackVocab.supports.asNode(), supportedOperator));
		mainElement.addTriple(new Triple(supportedOperator, RDF.type.asNode(), operatorTypeVar));
		mainElement.addTriplePath(new TriplePath(operatorTypeVar, pathZeroOrMore1(pathLink(RDFS.subClassOf.asNode())),TrackVocab.Operator.asNode()));
		mainElement.addTriple(new Triple(operatorTypeVar, SKOS.prefLabel.asNode(), operatorNameVar));
		mainSelect.addElement(mainElement);

		//TODO: handle all specific operators here !
		//TERM MATCH
		ElementTriplesBlock termMatchTriples = new ElementTriplesBlock();
		termMatchTriples.addTriple(new Triple(supportedOperator, RDF.type.asNode(), TrackVocab.MatchTerm.asNode()));
		termMatchTriples.addTriple(new Triple(supportedOperator, TrackVocab.endpointUrl.asNode(), endpointVar));
		termMatchTriples.addTriple(new Triple(supportedOperator, TrackVocab.graphUri.asNode(), graphVar));
		mainSelect.addElement(new ElementOptional(termMatchTriples));
		ElementTriplesBlock rootTermMatch = new ElementTriplesBlock();
		rootTermMatch.addTriple(new Triple(supportedOperator, RDF.type.asNode(), TrackVocab.MatchTerm.asNode()));
		rootTermMatch.addTriple(new Triple(supportedOperator, TrackVocab.motherTerm.asNode(), rootVar));
		mainSelect.addElement(new ElementOptional(rootTermMatch));
		mainQuery.addResultVar(endpointVar);
		mainQuery.addResultVar(graphVar);
		mainQuery.addResultVar(rootVar);
		// triple pattern to link feature to term ?
		// or catch in builder ?
		
		
		// LOCATIONFILTER
		ElementTriplesBlock localizedSearchTriples = new ElementTriplesBlock();
		localizedSearchTriples.addTriple(new Triple(supportedOperator, RDF.type.asNode(), TrackVocab.LocationFilter.asNode()));
		localizedSearchTriples.addTriple(new Triple(supportedOperator, TrackVocab.references.asNode(), refSeqVar));
		ElementOptional localizedSearch = new ElementOptional(localizedSearchTriples);
		mainQuery.addResultVar(refSeqVar);
		mainSelect.addElement(localizedSearch);
		mainQuery.setQueryPattern(mainSelect);
		
		return mainQuery.toString(Syntax.syntaxSPARQL_11);
	}

}
