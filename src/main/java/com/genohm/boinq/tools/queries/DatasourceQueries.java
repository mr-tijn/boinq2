package com.genohm.boinq.tools.queries;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.SPARQLResultSet;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.service.SPARQLClientService;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.sparql.core.TriplePath;
import com.hp.hpl.jena.sparql.path.P_Alt;
import com.hp.hpl.jena.sparql.path.P_Link;
import com.hp.hpl.jena.sparql.path.P_Seq;
import com.hp.hpl.jena.sparql.path.P_ZeroOrMore1;
import com.hp.hpl.jena.sparql.path.Path;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class DatasourceQueries {
	
	private static final Logger log = LoggerFactory.getLogger(DatasourceQueries.class);
	
	private SPARQLClientService sparqlClient;
	
	
	public Set<String> getTracks(Datasource datasource) {
		final String trackString = "track";
		Query query = new Query();
		query.setQuerySelectType();
		query.setPrefixMapping(Prefixes.getCommonPrefixes());
		Node datasourceGraph = NodeFactory.createURI(datasource.getName());
		Path sub_or_equiv = new P_Alt(new P_Link(RDFS.subClassOf.asNode()), new P_Link(OWL.equivalentClass.asNode()));
		Path substar = new P_ZeroOrMore1(sub_or_equiv);
		Path instance = new P_Seq(new P_Link(RDF.type.asNode()), substar);
		Node supertype = NodeFactory.createVariable("supertype");
		Node track = NodeFactory.createVariable(trackString);
		query.addResultVar(track);
		ElementPathBlock datasourceProvidesTrack = new ElementPathBlock();
		datasourceProvidesTrack.addTriplePath(new TriplePath(datasourceGraph, instance, supertype));
		datasourceProvidesTrack.addTriplePath(new TriplePath(supertype, substar, TrackVocab.FaldoDatasource.asNode()));
		datasourceProvidesTrack.addTriplePath(new TriplePath(track, instance, TrackVocab.FaldoTrack.asNode()));
		datasourceProvidesTrack.addTriple(new Triple(datasourceGraph, TrackVocab.provides.asNode(), track));
		query.setQueryPattern(datasourceProvidesTrack);
		query.addGraphURI(datasource.getMetaGraphName());
		log.debug("Querying for tracks. Query: \n"+query.toString(Syntax.syntaxSPARQL));
		try {
			SPARQLResultSet sparqlResult = sparqlClient.query(datasource.getMetaEndpointUrl(), datasource.getMetaGraphName(), query);
			Set<String> result = new HashSet<String>();
			for (Map<String,String> match: sparqlResult.getRecords()) {
				if (match.get(trackString) != null) {
					String uri = match.get(trackString);
					result.add(uri.substring(uri.lastIndexOf('#')));
				}
			}
			return result;
		} catch (Exception e) {
			log.error("Could not query fields",e);
		}
		return null;
	}
	
	public Set<String> getMatches(Datasource datasource) {
		Query query = new Query();
		query.setQuerySelectType();
		query.setPrefixMapping(Prefixes.getCommonPrefixes());
		Node datasourceGraph = NodeFactory.createURI(datasource.getName());
		Path sub_or_equiv = new P_Alt(new P_Link(RDFS.subClassOf.asNode()), new P_Link(OWL.equivalentClass.asNode()));
		Path substar = new P_ZeroOrMore1(sub_or_equiv);
		Path instance = new P_Seq(new P_Link(RDF.type.asNode()), substar);
		Node supertype = NodeFactory.createVariable("supertype");
		Node operation = NodeFactory.createVariable("operation");
		query.addResultVar(operation);
		ElementPathBlock datasourceSuperTypes = new ElementPathBlock();
		datasourceSuperTypes.addTriplePath(new TriplePath(datasourceGraph, instance, supertype));
		datasourceSuperTypes.addTriplePath(new TriplePath(supertype, substar, TrackVocab.Datasource.asNode()));
		datasourceSuperTypes.addTriple(new Triple(supertype, TrackVocab.supports.asNode(), operation));
		datasourceSuperTypes.addTriple(new TriplePath(operation, instance, TrackVocab.Match.asNode()));
		query.setQueryPattern(datasourceSuperTypes);
		query.addGraphURI(datasource.getMetaGraphName());
		
		log.debug("Querying for fields. Query: \n"+query.toString(Syntax.syntaxSPARQL));
		try {
			SPARQLResultSet sparqlResult = sparqlClient.query(datasource.getMetaEndpointUrl(), datasource.getMetaGraphName(), query);
			Set<String> result = new HashSet<String>();
			for (Map<String,String> match: sparqlResult.getRecords()) {
				if (match.get("operation") != null) {
					String uri = match.get("operation");
					result.add(uri.substring(uri.lastIndexOf('#')));
				}
			}
			return result;
		} catch (Exception e) {
			log.error("Could nog query fields",e);
		}
		return null;
	}
	
	
	
	
	

}

class DatasourceField {
	
	public static final int TYPE_NUMERIC = 1;
	public static final int TYPE_STRING = 2;
	public static final int TYPE_TERM = 3;
	
	private String name;
	private int type;
	private String targetGraph;
	private String targetEndpoint;
	
	public DatasourceField(int type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public DatasourceField(int type, String name, String targetGraph, String targetEndpoint) {
		this(type, name);
		this.targetGraph = targetGraph;
		this.targetEndpoint = targetEndpoint;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTargetGraph() {
		return targetGraph;
	}

	public void setTargetGraph(String targetGraph) {
		this.targetGraph = targetGraph;
	}

	public String getTargetEndpoint() {
		return targetEndpoint;
	}

	public void setTargetEndpoint(String targetEndpoint) {
		this.targetEndpoint = targetEndpoint;
	}
	
	
}

