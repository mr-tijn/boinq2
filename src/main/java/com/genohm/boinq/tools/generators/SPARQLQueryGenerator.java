package com.genohm.boinq.tools.generators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Prologue;
import org.apache.jena.sparql.expr.E_Equals;
import org.apache.jena.sparql.expr.E_GreaterThanOrEqual;
import org.apache.jena.sparql.expr.E_IRI;
import org.apache.jena.sparql.expr.E_LessThanOrEqual;
import org.apache.jena.sparql.expr.E_LogicalAnd;
import org.apache.jena.sparql.expr.E_OneOf;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.expr.nodevalue.NodeValueInteger;
import org.apache.jena.sparql.expr.nodevalue.NodeValueNode;
import org.apache.jena.sparql.modify.request.QuadAcc;
import org.apache.jena.sparql.modify.request.UpdateModify;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.sparql.syntax.ElementService;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.sparql.util.ExprUtils;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.match.FeatureJoin;
import com.genohm.boinq.domain.match.FeatureQuery;
import com.genohm.boinq.domain.match.FeatureSelect;
import com.genohm.boinq.domain.match.FeatureSelectCriterion;
import com.genohm.boinq.domain.match.FeatureTypeCriterion;
import com.genohm.boinq.domain.match.LocationCriterion;
import com.genohm.boinq.domain.match.LocationOverlap;
import com.genohm.boinq.domain.match.MatchGOTermCriterion;
import com.genohm.boinq.generated.vocabularies.FaldoVocab;
import com.genohm.boinq.generated.vocabularies.GfvoVocab;
import com.genohm.boinq.service.MetaInfoService;
import com.genohm.boinq.service.QueryBuilderService;

@Component
public class SPARQLQueryGenerator implements QueryGenerator {
	
	private static Logger log = LoggerFactory.getLogger(QueryGenerator.class);
	
	@Inject
	QueryBuilderService queryBuilderService;
	@Inject
	MetaInfoService metaInfoService;
	
	//TODO: some service to generate unique id's
	
	private Long idCounter = 0L;
	private Query mainQuery;
	private ElementGroup mainGroup;
	private PrefixMapping prefixMap;
	private BasicPattern constructPattern;

	private UpdateModify updateQuery;
	private QuadAcc insertQuads;
	
	private String nextId(String prefix) {
		return (prefix == null ? "" : prefix) + idCounter++;
	}

	private Map<String, FeatureSelect> featureSelectMap = new HashMap<>();
	private Map<FeatureSelect, String> featureNameMap = new HashMap<>();
	private Map<String, ElementGroup> selectTriples = new HashMap<>();

	private Map<FeatureSelect, Map<Node,Node>> referenceMapMap;
	
	public SPARQLQueryGenerator() {
	}

	private void init() {
		idCounter = 0L;
		prefixMap = new PrefixMappingImpl();
		prefixMap.setNsPrefix("rdf", RDF.getURI());
		prefixMap.setNsPrefix("rdfs", RDFS.getURI());
		prefixMap.setNsPrefix("xsd", XSD.getURI());
		prefixMap.setNsPrefix("owl", OWL.getURI());
		prefixMap.setNsPrefix("faldo", FaldoVocab.getURI());
		prefixMap.setNsPrefix("gfvo", GfvoVocab.getURI());
		prefixMap.setNsPrefix("so", "http://purl.obolibrary.org/obo/so-xp.obo#");
		mainGroup = new ElementGroup();
	}
	
	
	private void prepareSelect() {
		Prologue prologue = new Prologue(prefixMap);
		mainQuery = new Query(prologue);
		mainQuery.setQuerySelectType();
	}
	
	private void prepareUpdate() {
		UpdateModify updateQuery = new UpdateModify();
		updateQuery.setElement(mainGroup);
		insertQuads = updateQuery.getInsertAcc();
		
	}
	
	public Query computeQuery(FeatureQuery queryDefinition, GenomicRegion region) {

		init();
		prepareSelect();
		prepareUpdate();
		// do we already know the query will not retrieve anything ?
		Boolean retrieve = queryDefinition.getSelects().stream().anyMatch(select -> select.check(this, region) && select.retrieveFeatureData());
		if (!retrieve) {
			return null;
		}
		
		for (FeatureSelect select: queryDefinition.getSelects()) {
			if (select.check(this, region)) {
				select.accept(this, region);
			}
		}
		for (FeatureJoin join: queryDefinition.getJoins()) {
			if (join.check(this, region)) {
				join.accept(this, region);
			}
		}
		
		mainQuery.setQueryPattern(mainGroup);
		
		return mainQuery;
	}
	
	
	@Override
	public void visit(LocationCriterion lc, GenomicRegion region) {
		
	}

	@Override
	public void visit(LocationOverlap lo, GenomicRegion r) {
		String from = featureNameMap.get(lo.getSource());
		String to = featureNameMap.get(lo.getTarget());
		
		Expr sameRef = new E_Equals(new ExprVar(referenceVar(from)), new ExprVar(referenceVar(to)));
		// TODO: replace sameRef by understanding same reference means they map on the same global reference:
//		String fromReference = getLocalReference(r.assemblyURI);
//		String toReference = getLocalReference(r.assemblyURI);
//		Expr fromRef = new E_Equals(new ExprVar(referenceVar(from)), fromReference);
//		Expr toRef = new E_Equals(new ExprVar());
		
		// IDMatch will need to explicitly query the ID mapping RDF
		// http://www.genome.jp/linkdb/linkdb_rdf.html
		Expr overlap = new E_LogicalAnd(new E_LessThanOrEqual(new ExprVar(beginPosVar(from)),new ExprVar(endPosVar(to))), new E_GreaterThanOrEqual(new ExprVar(endPosVar(from)), new ExprVar(beginPosVar(to))));
		mainGroup.addElementFilter(new ElementFilter(new E_LogicalAnd(sameRef, overlap)));
		
		//TODO: handle same strand
	}

	@Override
	public void visit(FeatureSelect fs, GenomicRegion region) {
		
		Map<Node,Node> featureReferenceMap = new HashMap<>();
		
		String featureVarName = nextId("feature_"); 
		featureSelectMap.put(featureVarName, fs);
		featureNameMap.put(fs, featureVarName);
		
		Track track = fs.getTrack();
		Element featureSelectElement = null;
		
		Node graph = NodeFactory.createURI(track.getGraphName());
		ElementGroup faldoSelect = getFaldoSelect(featureVarName,fs.retrieveFeatureData(),fs.getLocationIndirection(),region);
		selectTriples.put(featureVarName, faldoSelect);
		featureSelectElement = new ElementNamedGraph(graph, faldoSelect);
		for (FeatureSelectCriterion crit: fs.getCriteria()) {
			crit.accept(this, featureSelectElement, region);
		}		
		if (track.getDatasource().getType() == Datasource.TYPE_REMOTE_FALDO) {
			String serviceURI = track.getDatasource().getEndpointUrl();
			//TODO: checkifnotnull
			featureSelectElement = new ElementService(serviceURI, featureSelectElement);
		}
		if (fs.retrieveFeatureData()) {
			mainQuery.addResultVar(NodeFactory.createVariable(featureVarName));
//			constructPattern.add(new Triple());
		}
		
		mainGroup.addElement(featureSelectElement);
	}
	
	private Node beginPosVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_BeginPos");}
	private Node endPosVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_EndPos");}
	private Node referenceVar(String featureVarName) {return NodeFactory.createVariable(featureVarName+"_Reference");}
	private Node locationVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_Location");}
	
	private ElementGroup getFaldoSelect(String featureVarName, Boolean retrieve, Boolean locationIndirection, GenomicRegion region) {
		Node feature = NodeFactory.createVariable(featureVarName);
		
		Node featureId = NodeFactory.createVariable(featureVarName + "_id");
		Node featureBeginPos = beginPosVar(featureVarName);
		Node featureEndPos = endPosVar(featureVarName);
		Node featureReference = referenceVar(featureVarName);
		Node featureReferenceName = NodeFactory.createVariable(featureVarName + "_ReferenceName");
		Node featurePositionType = NodeFactory.createVariable(featureVarName + "_PositionType");
		
		if (retrieve) {
			mainQuery.addResultVar(featureId);
			mainQuery.addResultVar(featureBeginPos);
			mainQuery.addResultVar(featureEndPos);
			mainQuery.addResultVar(featureReferenceName);
			mainQuery.addResultVar(featureVarName + "_Strand", new E_Equals(new ExprVar(featurePositionType), ExprUtils.nodeToExpr(FaldoVocab.ForwardStrandPosition.asNode())));
		}
		
		ElementGroup faldoSelect = new ElementGroup();
		
		ElementTriplesBlock faldoTriples = new ElementTriplesBlock();
		
		faldoTriples.addTriple(new Triple(feature, RDFS.label.asNode(), featureId));
		Node featureLocation = feature;
		if (locationIndirection) {
			featureLocation = locationVar(featureVarName);
			faldoTriples.addTriple(new Triple(feature, FaldoVocab.location.asNode(), featureLocation));
		}
		Node featureBegin = NodeFactory.createVariable(featureVarName + "_Begin");
		faldoTriples.addTriple(new Triple(featureLocation, FaldoVocab.begin.asNode(), featureBegin));
		faldoTriples.addTriple(new Triple(featureBegin, FaldoVocab.position.asNode(), featureBeginPos));
		Node featureEnd = NodeFactory.createVariable(featureVarName + "_End");
		faldoTriples.addTriple(new Triple(feature, FaldoVocab.end.asNode(), featureEnd));
		faldoTriples.addTriple(new Triple(featureEnd, FaldoVocab.position.asNode(), featureEndPos));
		faldoTriples.addTriple(new Triple(featureReference, RDFS.label.asNode(), featureReferenceName));
		faldoTriples.addTriple(new Triple(featureBegin, RDF.type.asNode(), featurePositionType));
		
		faldoSelect.addElement(faldoTriples);
				
		if (region != null) {
			if (region.start != null) {
				faldoSelect.addElement(new ElementFilter(new E_GreaterThanOrEqual(new ExprVar(featureEndPos), new NodeValueInteger(region.start))));
			}
			
			if (region.end != null) {
				faldoSelect.addElement(new ElementFilter(new E_LessThanOrEqual(new ExprVar(featureBeginPos), new NodeValueInteger(region.end))));
			}
			
			if (region.strand != null) {
				if (region.strand) {
					faldoTriples.addTriple(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ForwardStrandPosition.asNode()));
				} else {
					faldoTriples.addTriple(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ReverseStrandPosition.asNode()));
				}
			}

			if (region.assemblyURI != null) {
				faldoTriples.addTriple(new Triple(featureBegin, FaldoVocab.reference.asNode(), NodeFactory.createURI(region.assemblyURI)));
			}
		}
		
		ExprList targetExpressions = new ExprList();
		targetExpressions.add(new E_IRI(new NodeValueNode(FaldoVocab.ForwardStrandPosition.asNode())));
		targetExpressions.add(new E_IRI(new NodeValueNode(FaldoVocab.ReverseStrandPosition.asNode())));
		E_OneOf orientations = new E_OneOf(new E_IRI(new ExprVar(featurePositionType)), targetExpressions);

		faldoSelect.addElementFilter(new ElementFilter(orientations));
		
		return faldoSelect;
	}

	@Override
	public void visit(FeatureQuery fq, GenomicRegion r) {
		computeQuery(fq, r);
	}

	@Override
	public void visit(FeatureTypeCriterion fc, GenomicRegion r) {
		String parentFeatureName = featureNameMap.get(fc.getParent());
		Node feature = NodeFactory.createVariable(parentFeatureName);
		ElementGroup parentTriples = selectTriples.get(parentFeatureName);
		parentTriples.addTriplePattern(new Triple(feature, RDF.type.asNode(), NodeFactory.createURI(fc.getFeatureTypeUri())));
	}

	@Override
	public Boolean check(LocationCriterion lc, GenomicRegion region) {
		GenomicRegion criterionregion = new GenomicRegion();
		criterionregion.start = lc.getStart();
		criterionregion.end = lc.getEnd();
		criterionregion.assemblyURI = lc.getContig();
		criterionregion.strand = lc.getStrand();
		
		GenomicRegion intersect = criterionregion.intersect(region);
		return (intersect != null);
	}

	@Override
	public Boolean check(FeatureTypeCriterion fc, GenomicRegion r) {
		// we currently assume the type asked for is actually present (querybuilder makes sure of this)
		return true;
	}

	@Override
	public Boolean check(LocationOverlap lo, GenomicRegion r) {
		// we could check if the target of the overlap has a location filter and see if it overlaps the region
		return true;
	}

	@Override
	public Boolean check(FeatureSelect fs, GenomicRegion region) {
		return fs.getCriteria().stream().allMatch(crit -> crit.check(this, region));
	}

	@Override
	public Boolean check(FeatureQuery fq, GenomicRegion r) {
		// reference map
		referenceMapMap = new HashMap<>();
		for (FeatureSelect select: fq.getSelects()) {
			Map<Node,Node> localRefToBoinqRef = select.getTrack().getReferenceMap();
			if (localRefToBoinqRef != null) {
				referenceMapMap.put(select, localRefToBoinqRef);
			}
		}
		// get operators
		
		// we currently only handle queries with a single overlapcluster
		// split the analysis if more is needed
		Set<Set<FeatureSelect>> overlapClusters = cluster(fq);
		if (overlapClusters.size() > 1) {
			return false;
		}
		return true;
	}
	
	private Set<Set<FeatureSelect>> cluster(FeatureQuery fq) {
		Set<Set<FeatureSelect>> clusters = new HashSet<>();
		for (FeatureSelect select: fq.getSelects()) {
			Boolean alreadyIn = clusters.stream().anyMatch(cluster -> cluster.contains(select));
			if (!alreadyIn) {
				Set<FeatureSelect> newCluster = new HashSet<>();
				newCluster.addAll(fq.getOverlappingSelects(select));
			}
		}
		return clusters;
	}

	@Override
	public void visit(MatchGOTermCriterion tc, GenomicRegion r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean check(MatchGOTermCriterion tc, GenomicRegion r) {
		// TODO Auto-generated method stub
		return null;
	}

}
