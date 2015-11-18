package com.genohm.boinq.tools.generators;

import java.util.HashMap;
import java.util.Map;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.Syntax;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.sparql.core.Prologue;
import org.apache.jena.sparql.expr.E_Equals;
import org.apache.jena.sparql.expr.E_GreaterThanOrEqual;
import org.apache.jena.sparql.expr.E_LessThanOrEqual;
import org.apache.jena.sparql.expr.E_LogicalAnd;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.expr.nodevalue.NodeValueInteger;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.sparql.syntax.ElementOptional;
import org.apache.jena.sparql.syntax.ElementService;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.sparql.util.ExprUtils;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.match.FeatureJoin;
import com.genohm.boinq.domain.match.FeatureQuery;
import com.genohm.boinq.domain.match.FeatureSelect;
import com.genohm.boinq.domain.match.FeatureSelectCriterion;
import com.genohm.boinq.domain.match.LocationCriterion;
import com.genohm.boinq.domain.match.LocationOverlap;
import com.genohm.boinq.generated.vocabularies.FaldoVocab;
import com.genohm.boinq.generated.vocabularies.GfvoVocab;
import com.genohm.boinq.tools.vocabularies.FaldoVocabulary;

public class SPARQLQueryGenerator implements QueryGenerator {
	//some service to generate unique id's
	
	private Long idCounter = 0L;
	private Query mainQuery;
	private ElementGroup mainGroup;
	private PrefixMapping prefixMap;

	private String nextId(String prefix) {
		return (prefix == null ? "" : prefix) + idCounter++;
	}

	private Map<String, FeatureSelect> featureSelectMap = new HashMap<>();
	private Map<String, FeatureJoin> featureJoinMap = new HashMap<>();
	
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
		Prologue prologue = new Prologue(prefixMap);
		mainQuery = new Query(prologue);
		mainQuery.setQuerySelectType();
		mainGroup = new ElementGroup();
	}
	
	public void computeQuery(FeatureQuery queryDefinition, GenomicRegion region) {

		init();
		
		for (FeatureSelect select: queryDefinition.getSelects()) {
			select.accept(this, region);
		}
		for (FeatureJoin join: queryDefinition.getJoins()) {
			join.accept(this, region);
		}
		
		mainQuery.setQueryPattern(mainGroup);
	}
		
	@Override
	public void visit(LocationCriterion lc, GenomicRegion r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LocationOverlap lo, GenomicRegion r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FeatureSelect fs, GenomicRegion region) {
		String featureVarName = nextId("feature_"); 
		featureSelectMap.put(featureVarName, fs);
		
		Track track = fs.getTrack();
		Element featureSelectElement = null;
		
		if (track.getDatasource().getType() == Datasource.TYPE_LOCAL_FALDO) {
			Node graph = NodeFactory.createURI(track.getGraphName());
			Element faldoSelect = getFaldoSelect(featureVarName,fs.retrieveFeatureData(),region);
			for (FeatureSelectCriterion crit: fs.getCriteria()) {
				crit.accept(this, region);
			}
			featureSelectElement = new ElementNamedGraph(graph, faldoSelect);
			
		} else if (track.getDatasource().getType() == Datasource.TYPE_REMOTE_FALDO) {
			String serviceURI = track.getDatasource().getEndpointUrl();
			Node graph = NodeFactory.createURI(track.getGraphName());
			featureSelectElement = new ElementService(serviceURI, new ElementNamedGraph(graph, getFaldoSelect(featureVarName,fs.retrieveFeatureData(),region)));
		}
		
		mainGroup.addElement(featureSelectElement);
	}
	
	private Element getFaldoSelect(String featureVarName, Boolean retrieve, GenomicRegion region) {
		Node feature = NodeFactory.createVariable(featureVarName);
		
		Node featureId = NodeFactory.createVariable(featureVarName + "_id");
		Node featureBeginPos = NodeFactory.createVariable(featureVarName + "_BeginPos");
		Node featureEndPos = NodeFactory.createVariable(featureVarName + "_EndPos");
		Node featureReference = NodeFactory.createVariable(featureVarName + "_Reference");
		Node featureReferenceName = NodeFactory.createVariable(featureVarName + "_ReferenceName");
		Node featurePositionType = NodeFactory.createVariable(featureVarName + "_PositionType");
		
		if (retrieve) {
			mainQuery.addResultVar(featureId);
			mainQuery.addResultVar(featureBeginPos);
			mainQuery.addResultVar(featureEndPos);
			mainQuery.addResultVar(featureReferenceName);
			mainQuery.addResultVar(featureVarName + "_Strand", new E_Equals(new ExprVar(featurePositionType), ExprUtils.nodeToExpr(FaldoVocabulary.ForwardStrandPosition)));
		}
		
		ElementGroup faldoSelect = new ElementGroup();
		
		ElementTriplesBlock faldoTriples = new ElementTriplesBlock();
		
		faldoTriples.addTriple(new Triple(feature, RDFS.label.asNode(), featureId));
		Node featureBegin = NodeFactory.createVariable(featureVarName + "_Begin");
		faldoTriples.addTriple(new Triple(feature, FaldoVocabulary.begin, featureBegin));
		faldoTriples.addTriple(new Triple(featureBegin, FaldoVocabulary.position, featureBeginPos));
		Node featureEnd = NodeFactory.createVariable(featureVarName + "_End");
		faldoTriples.addTriple(new Triple(feature, FaldoVocabulary.end, featureEnd));
		faldoTriples.addTriple(new Triple(featureEnd, FaldoVocabulary.position, featureEndPos));
		faldoTriples.addTriple(new Triple(featureBegin, FaldoVocabulary.reference, featureReference));
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
			
		return faldoTriples;
	}

	@Override
	public void visit(FeatureQuery fq, GenomicRegion r) {
		computeQuery(fq, r);
	}
	
}
