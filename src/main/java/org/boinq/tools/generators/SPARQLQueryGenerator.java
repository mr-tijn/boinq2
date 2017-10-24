package org.boinq.tools.generators;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.sparql.core.Prologue;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.binding.BindingFactory;
import org.apache.jena.sparql.engine.binding.BindingMap;
import org.apache.jena.sparql.expr.E_Equals;
import org.apache.jena.sparql.expr.E_GreaterThanOrEqual;
import org.apache.jena.sparql.expr.E_IRI;
import org.apache.jena.sparql.expr.E_LessThanOrEqual;
import org.apache.jena.sparql.expr.E_LogicalAnd;
import org.apache.jena.sparql.expr.E_OneOf;
import org.apache.jena.sparql.expr.E_Regex;
import org.apache.jena.sparql.expr.E_SameTerm;
import org.apache.jena.sparql.expr.E_Str;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.expr.nodevalue.NodeValueDouble;
import org.apache.jena.sparql.expr.nodevalue.NodeValueInteger;
import org.apache.jena.sparql.expr.nodevalue.NodeValueNode;
import org.apache.jena.sparql.expr.nodevalue.NodeValueString;
import org.apache.jena.sparql.modify.request.QuadAcc;
import org.apache.jena.sparql.modify.request.UpdateModify;
import org.apache.jena.sparql.path.Path;
import org.apache.jena.sparql.path.PathParser;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementBind;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementService;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.sparql.util.ExprUtils;
import org.apache.jena.update.Update;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.boinq.domain.Datasource;
import org.boinq.domain.GenomicRegion;
import org.boinq.domain.Track;
import org.boinq.domain.match.Connect;
import org.boinq.domain.match.FeatureConnector;
import org.boinq.domain.match.FeatureJoin;
import org.boinq.domain.match.FeatureQuery;
import org.boinq.domain.match.FeatureSelect;
import org.boinq.domain.match.FeatureTypeCriterion;
import org.boinq.domain.match.LocationCriterion;
import org.boinq.domain.match.LocationOverlap;
import org.boinq.domain.match.MatchDecimalCriterion;
import org.boinq.domain.match.MatchIntegerCriterion;
import org.boinq.domain.match.MatchStringCriterion;
import org.boinq.domain.match.MatchTermCriterion;
import org.boinq.domain.match.QueryGeneratorAcceptor;
import org.boinq.service.MetaInfoService;
import org.boinq.service.QueryBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.boinq.generated.vocabularies.BoinqVocab;
import org.boinq.generated.vocabularies.FaldoVocab;
import org.boinq.generated.vocabularies.GfvoVocab;
import org.boinq.generated.vocabularies.TrackVocab;

@Component
public class SPARQLQueryGenerator implements QueryGenerator {
	
	private static Logger log = LoggerFactory.getLogger(QueryGenerator.class);
	
	@Inject
	QueryBuilderService queryBuilderService;
	@Inject
	MetaInfoService metaInfoService;
	
	//TODO: some service to generate unique id's
	
//	private Long idCounter = 0L;
	private Query mainQuery;
	private ElementGroup mainGroup;
	private PrefixMapping prefixMap;
	
	private UpdateModify updateQuery;
	private QuadAcc insertQuads;
	
	private Map<String, Long> idCounters;
	
	private String nextId(String prefix) {
		if (prefix == null) {
			prefix = "";
		}
		if (idCounters.containsKey(prefix)) {
			idCounters.put(prefix, idCounters.get(prefix) + 1);
		} else {
			idCounters.put(prefix, 0L);
		}
		return prefix + idCounters.get(prefix);
	}

	private Map<String, FeatureSelect> featureSelectMap = new HashMap<>();
	private Map<FeatureSelect, String> featureNameMap = new HashMap<>();
	private Map<String, ElementGroup> selectTriples = new HashMap<>();

	private Map<FeatureSelect, Map<Node,Node>> referenceMapMap;
	private List<Node> globalReferences = new LinkedList<>();
	private List<Var> valuesVariables = new LinkedList<>();
	private List<Binding> valuesBindings = new LinkedList<>();
	private Var globalReferenceId;
	
	public SPARQLQueryGenerator() {
	}
	
	private Node beginVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_Begin");}
	private Node endVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_End");}
	private Node beginPosVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_BeginPos");}
	private Node endPosVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_EndPos");}
	private Node referenceVar(String featureVarName) {return NodeFactory.createVariable(featureVarName+"_Reference");}
	private Node locationVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_Location");}
	private Node strandVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_Strand");}

	public void setReferences(List<Node> references) {
		this.globalReferences = references;
	}
	
	private void init() {
//		idCounter = 0L;
		idCounters = new HashMap<String,Long>();
		prefixMap = new PrefixMappingImpl();
		prefixMap.setNsPrefix("rdf", RDF.getURI());
		prefixMap.setNsPrefix("rdfs", RDFS.getURI());
		prefixMap.setNsPrefix("xsd", XSD.getURI());
		prefixMap.setNsPrefix("owl", OWL.getURI());
		prefixMap.setNsPrefix("dcterms", DCTerms.getURI());
		prefixMap.setNsPrefix("faldo", FaldoVocab.getURI());
		prefixMap.setNsPrefix("gfvo", GfvoVocab.getURI());
		prefixMap.setNsPrefix("so", "http://purl.obolibrary.org/obo/so-xp.obo#");
		prefixMap.setNsPrefix("boinq", BoinqVocab.getURI());
		prefixMap.setNsPrefix("track", TrackVocab.getURI());
		mainGroup = new ElementGroup();
		referenceMapMap = new HashMap<>();
		globalReferenceId = Var.alloc("globalReferenceId");
		valuesVariables.add(globalReferenceId);
//		for (Integer i=1; i < 23; i++) {
//			globalReferences.add(NodeFactory.createURI("http://www.boinq.org/resource/homo_sapiens/GRCh38/" + i));
//		}
//		globalReferences.add(NodeFactory.createURI("http://www.boinq.org/resource/homo_sapiens/GRCh38/X"));
//		globalReferences.add(NodeFactory.createURI("http://www.boinq.org/resource/homo_sapiens/GRCh38/Y"));
	}
	
	
	private void prepareSelect() {
		Prologue prologue = new Prologue(prefixMap);
		mainQuery = new Query(prologue);
		mainQuery.setQuerySelectType();
	}
	
	private Update prepareUpdate(String targetGraph) {
		updateQuery = new UpdateModify();
		updateQuery.setElement(mainGroup);
		insertQuads = updateQuery.getInsertAcc();
		insertQuads.setGraph(NodeFactory.createURI(targetGraph));
		for (String featureName: featureSelectMap.keySet()) {
			if (featureSelectMap.get(featureName).retrieveFeatureData()) {
				for (Triple triple: updateTriplePattern(featureName)) {
					insertQuads.addTriple(triple);
				}
			}
		}
		return updateQuery;
	}
	
	private Set<Triple> updateTriplePattern(String featureVarName) {
		HashSet<Triple> result = new HashSet<>();
		result.add(new Triple(NodeFactory.createVariable(featureVarName), RDF.type.asNode(), FaldoVocab.Region.asNode()));
		result.add(new Triple(NodeFactory.createVariable(featureVarName), FaldoVocab.location.asNode(), locationVar(featureVarName)));
		result.add(new Triple(locationVar(featureVarName), FaldoVocab.begin.asNode(), beginVar(featureVarName)));
		result.add(new Triple(locationVar(featureVarName), FaldoVocab.end.asNode(), endVar(featureVarName)));
		result.add(new Triple(locationVar(featureVarName), FaldoVocab.reference.asNode(), globalReferenceId));
		result.add(new Triple(locationVar(featureVarName), RDF.type.asNode(), strandVar(featureVarName)));
		result.add(new Triple(beginVar(featureVarName), FaldoVocab.position.asNode(), beginPosVar(featureVarName)));
		result.add(new Triple(beginVar(featureVarName), FaldoVocab.reference.asNode(), globalReferenceId));
		result.add(new Triple(beginVar(featureVarName), RDF.type.asNode(), strandVar(featureVarName)));
		result.add(new Triple(endVar(featureVarName), FaldoVocab.position.asNode(), endPosVar(featureVarName)));
		result.add(new Triple(endVar(featureVarName), FaldoVocab.reference.asNode(), globalReferenceId));
		result.add(new Triple(endVar(featureVarName), RDF.type.asNode(), strandVar(featureVarName)));
		return result;
	}
	
	public Update computeUpdate(FeatureQuery queryDefinition, GenomicRegion region) {
		computeQuery(queryDefinition, region);
		return prepareUpdate(queryDefinition.getTargetGraph());
		
	}
	
	public Query computeQuery(FeatureQuery queryDefinition, GenomicRegion region) {

		init();
		prepareSelect();
		// do we already know the query will not retrieve anything ?
		Boolean retrieve = queryDefinition.getSelects().stream().anyMatch(select -> select.check(this, region) && select.retrieveFeatureData());
		if (!retrieve) {
			return null;
		}
		if (queryDefinition.check(this, region)) {
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
		}
		mainQuery.setQueryPattern(mainGroup);
		
		// add values block
		addReferenceBindings();
		mainQuery.setValuesDataBlock(valuesVariables, valuesBindings);
				
		return mainQuery;
	}
	
	
	@Override
	public void visit(LocationCriterion lc, GenomicRegion region) {
		
	}

	private void addReferenceBindings() {
		// use refMapMap to process all in the end
		
		for (FeatureSelect select: referenceMapMap.keySet()) {
			if (select.getTrack().getDatasource().getType() == Datasource.TYPE_REMOTE_FALDO || 
					select.getTrack().getDatasource().getType() == Datasource.TYPE_LOCAL_FALDO) {
				String refVarName = featureNameMap.get(select) + "_Reference";
				valuesVariables.add(Var.alloc(refVarName));
			}
		}
		for (Node globalRef: globalReferences) {
			BindingMap binding = BindingFactory.create();
			binding.add(Var.alloc("globalReferenceId"), globalRef);
			for (FeatureSelect select: referenceMapMap.keySet()) {
				String refVarName = featureNameMap.get(select) + "_Reference";
				Var refVar = Var.alloc(refVarName);
				if (select.getTrack().getDatasource().getType() == Datasource.TYPE_REMOTE_FALDO) {
					Map<Node,Node> refMap = referenceMapMap.get(select);
					Node target = refMap.get(globalRef);
					if (target != null) {
						binding.add(refVar, target);
					}
				}
				if (select.getTrack().getDatasource().getType() == Datasource.TYPE_LOCAL_FALDO) {
					binding.add(refVar, globalRef);
				}
			}
			valuesBindings.add(binding);
		}
		
	}
	
	@Override
	public void visit(LocationOverlap lo, GenomicRegion r) {
		String from = featureNameMap.get(lo.getSource()) ;
		String to = featureNameMap.get(lo.getTarget()) ;
//		Expr sameRef = new E_Equals(new ExprVar(referenceVar(from)), new ExprVar(referenceVar(to)));
		// for each select in both queries, add the reference variable to the global binding list, and a column of bindings for every one
		// example: variables
		// ?refId ?refEnsembl ?refBoinq
		// values
		// (1 http://rdf.ebi.ac.uk/chr1 boinq:GRCh38chr01)
		// (2 http://rdf.ebi.ac.uk/chr2 boinq:GRCh38chr02)
		// IDMatch will need to explicitly query the ID mapping RDF
		// http://www.genome.jp/linkdb/linkdb_rdf.html
		Expr overlap = new E_LogicalAnd(new E_LessThanOrEqual(new ExprVar(beginPosVar(from)),new ExprVar(endPosVar(to))), new E_GreaterThanOrEqual(new ExprVar(endPosVar(from)), new ExprVar(beginPosVar(to))));
		mainGroup.addElementFilter(new ElementFilter(overlap));
		if (lo.getSameStrand()) {
			Expr sameStrand = new E_Equals(new ExprVar(strandVar(from)), new ExprVar(strandVar(to)));
			mainGroup.addElementFilter(new ElementFilter(sameStrand));
		}
		
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
		for (QueryGeneratorAcceptor crit: fs.getCriteria()) {
			crit.accept(this, region);
		}		
		if (track.getDatasource().getType() == Datasource.TYPE_REMOTE_FALDO || track.getDatasource().getType() == Datasource.TYPE_REMOTE_SPARQL) {
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
			mainQuery.addResultVar(strandVar(featureVarName), new E_Equals(new ExprVar(featurePositionType), ExprUtils.nodeToExpr(FaldoVocab.ForwardStrandPosition.asNode())));
		}
		
		ElementGroup faldoSelect = new ElementGroup();
		
		ElementTriplesBlock faldoTriples = new ElementTriplesBlock();
		
		faldoTriples.addTriple(new Triple(feature, RDFS.label.asNode(), featureId));
		Node featureLocation = feature;
		if (locationIndirection) {
			featureLocation = locationVar(featureVarName);
			faldoTriples.addTriple(new Triple(feature, FaldoVocab.location.asNode(), featureLocation));
		}
		Node featureBegin = beginVar(featureVarName);
		faldoTriples.addTriple(new Triple(featureLocation, FaldoVocab.begin.asNode(), featureBegin));
		faldoTriples.addTriple(new Triple(featureBegin, FaldoVocab.position.asNode(), featureBeginPos));
		Node featureEnd = endVar(featureVarName);
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
		return true;
	}

	@Override
	public Boolean check(FeatureSelect fs, GenomicRegion region) {
		return fs.getCriteria().stream().allMatch(crit -> crit.check(this, region));
	}

	@Override
	public Boolean check(FeatureQuery fq, GenomicRegion r) {
		if (fq.getTargetGraph() == null) return false;
		// build reference map
		referenceMapMap = new HashMap<>();
		for (FeatureSelect select: fq.getSelects()) {
			if (select.getTrack().getDatasource().getType() == Datasource.TYPE_REMOTE_FALDO) {
				Map<Node,Node> localRefToBoinqRef = select.getTrack().getReferenceMap();
				if (localRefToBoinqRef != null) {
					referenceMapMap.put(select, localRefToBoinqRef);
				}
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
		// TODO: should only check for overlap clusters within a connected set
		// as we only support overlap joins for now this is automatically fulfilled
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
	public void visit(MatchTermCriterion mc, GenomicRegion r) {
		String parentFeatureName = featureNameMap.get(mc.getParent());
		Node feature = NodeFactory.createVariable(parentFeatureName);
		Node field = NodeFactory.createVariable(nextId("field"));
		ElementGroup parentGroup = selectTriples.get(parentFeatureName);
		ElementPathBlock link = new ElementPathBlock();
		TriplePath featureToField = new TriplePath(feature, parsePath(mc.getPathExpression()), field);
		link.addTriplePath(featureToField);
		parentGroup.addElement(link);
		parentGroup.addElement(new ElementFilter(new E_SameTerm(new ExprVar(field), new NodeValueNode(NodeFactory.createURI(mc.getTermUri())))));
	}
	
	@Override
	public void visit(MatchIntegerCriterion mc, GenomicRegion r) {
		String parentFeatureName = featureNameMap.get(mc.getParent());
		Node feature = NodeFactory.createVariable(parentFeatureName);
		Node field = NodeFactory.createVariable(nextId("field"));
		ElementGroup parentGroup = selectTriples.get(parentFeatureName);
		ElementPathBlock link = new ElementPathBlock();
		TriplePath featureToField = new TriplePath(feature, parsePath(mc.getPathExpression()), field);
		link.addTriplePath(featureToField);
		parentGroup.addElement(link);
		if (mc.getExactMatch()) {
			parentGroup.addElement(new ElementFilter(new E_Equals(new ExprVar(field), new NodeValueInteger(mc.getMatchLong()))));
		} else {
			if (null != mc.getMinLong()) {
				parentGroup.addElement(new ElementFilter(new E_GreaterThanOrEqual(new ExprVar(field), new NodeValueInteger(mc.getMinLong()))));
			}
			if (null != mc.getMaxLong()) {
				parentGroup.addElement(new ElementFilter(new E_LessThanOrEqual(new ExprVar(field), new NodeValueInteger(mc.getMaxLong()))));
			}
		}
	}
	
	
	@Override
	public void visit(MatchDecimalCriterion mc, GenomicRegion r) {
		String parentFeatureName = featureNameMap.get(mc.getParent());
		Node feature = NodeFactory.createVariable(parentFeatureName);
		Node field = NodeFactory.createVariable(nextId("field"));
		ElementGroup parentGroup = selectTriples.get(parentFeatureName);
		ElementPathBlock link = new ElementPathBlock();
		TriplePath featureToField = new TriplePath(feature, parsePath(mc.getPathExpression()), field);
		link.addTriplePath(featureToField);
		parentGroup.addElement(link);
		if (mc.getExactMatch()) {
			parentGroup.addElement(new ElementFilter(new E_Equals(new ExprVar(field), new NodeValueDouble(mc.getMatchDouble()))));
		} else {
			if (null != mc.getMinDouble()) {
				parentGroup.addElement(new ElementFilter(new E_GreaterThanOrEqual(new ExprVar(field), new NodeValueDouble(mc.getMinDouble()))));
			}
			if (null != mc.getMaxDouble()) {
				parentGroup.addElement(new ElementFilter(new E_LessThanOrEqual(new ExprVar(field), new NodeValueDouble(mc.getMaxDouble()))));
			}
		}
	}
	
	
	@Override
	public void visit(MatchStringCriterion mc, GenomicRegion r) {
		String parentFeatureName = featureNameMap.get(mc.getParent());
		Node feature = NodeFactory.createVariable(parentFeatureName);
		Node field = NodeFactory.createVariable(nextId("field"));
		ElementGroup parentGroup = selectTriples.get(parentFeatureName);
		ElementPathBlock link = new ElementPathBlock();
		TriplePath featureToField = new TriplePath(feature, parsePath(mc.getPathExpression()), field);
		link.addTriplePath(featureToField);
		parentGroup.addElement(link);
		if (mc.getExactMatch()) {
			parentGroup.addElement(new ElementFilter(new E_Equals(new E_Str(new ExprVar(field)), new NodeValueString(mc.getMatchString()))));
		} else {
			parentGroup.addElement(new ElementFilter(new E_Regex(new ExprVar(field), mc.getMatchString(), "i")));
		}
	}

	@Override
	public Boolean check(MatchIntegerCriterion mc, GenomicRegion r) {
		if (mc.getExactMatch() && null == mc.getMatchLong()) return false;
		if (!mc.getExactMatch() && (mc.getMinLong() == null && mc.getMaxLong() == null)) return false;
		return checkPathExpression(mc.getPathExpression());
	}

	@Override
	public Boolean check(MatchDecimalCriterion mc, GenomicRegion r) {
		if (mc.getExactMatch() && null == mc.getMatchDouble()) return false;
		if (!mc.getExactMatch() && (mc.getMinDouble() == null && mc.getMaxDouble() == null)) return false;
		return checkPathExpression(mc.getPathExpression());
	}
	
	@Override
	public Boolean check(MatchStringCriterion mc, GenomicRegion r) {
		if (mc.getMatchString() == null) return false;
		return checkPathExpression(mc.getPathExpression());
	}

	@Override
	public Boolean check(MatchTermCriterion mc, GenomicRegion r) {
		if (!(NodeFactory.createURI(mc.getTermUri())).isURI()) return false;
		return checkPathExpression(mc.getPathExpression());
	}

	// not used yet
	
	private String urldecode(String input) {
		String output = null;
		try {
			output = URLDecoder.decode(input, StandardCharsets.UTF_8.toString());
		} catch (Exception e) {
			log.error("Cannot urldecode "+ input, e);
		}
		return output;
	}
	
	private Boolean checkPathExpression(String pathExpression) {
		try {
			String decoded = URLDecoder.decode(pathExpression, StandardCharsets.UTF_8.toString());
			if ((PathParser.parse(decoded, prefixMap)) == null) return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@Override
	public Boolean check(Connect idMatch, GenomicRegion region)  {
		return (checkPathExpression(idMatch.getSourceConnector().getPathExpression()) 
				&& checkPathExpression(idMatch.getTargetConnector().getPathExpression()));
	}

	@Override
	public void visit(Connect idMatch, GenomicRegion region) {

		String idString = nextId("idMatch");
		
		String from = featureNameMap.get(idMatch.getSource()) ;
		String to = featureNameMap.get(idMatch.getTarget()) ;
		
		int sourceType = idMatch.getSourceConnector().getType();
		if (FeatureConnector.CONNECTOR_TYPE_PATH == sourceType) {
			addConnectTriples(selectTriples.get(from),from,idMatch.getSourceConnector().getPathExpression(),idString);
		} else if (FeatureConnector.CONNECTOR_TYPE_ENTITY == sourceType) {
			addConnectBind(selectTriples.get(from),from,idString);
		} else {
//			throw new Exception("Cannot handle connector type "+sourceType);
		}
		int targetType = idMatch.getTargetConnector().getType();
		if (FeatureConnector.CONNECTOR_TYPE_PATH == targetType) {
			addConnectTriples(selectTriples.get(to),to,idMatch.getTargetConnector().getPathExpression(),idString);
		} else if (FeatureConnector.CONNECTOR_TYPE_ENTITY == targetType) {
			addConnectBind(selectTriples.get(to),to,idString);
		} else {
			// TODO CHECK if we know the type
		}
		
	}
	
	public void addConnectBind(ElementGroup featureTriples, String orig, String target) {
		featureTriples.addElement(new ElementBind(Var.alloc(target), new ExprVar(orig)));
	}
	
	public void addConnectTriples(ElementGroup featureTriples, String from, String path, String common) {
		ElementPathBlock connect = new ElementPathBlock();
		try {
			connect.addTriplePath(new TriplePath(NodeFactory.createVariable(from), parsePath(path), NodeFactory.createVariable(common)));
		} catch (Exception e) {
			log.error("Could not urldecode path expression ", e);
		}
		featureTriples.addElement(connect);
	}

	private Path parsePath(String path) {
		return PathParser.parse(urldecode(path), prefixMap);
	}

}
