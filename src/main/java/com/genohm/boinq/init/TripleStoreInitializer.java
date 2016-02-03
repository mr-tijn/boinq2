package com.genohm.boinq.init;


import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.genohm.boinq.domain.SPARQLResultSet;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.service.SPARQLClientService;
import com.genohm.boinq.service.TripleUploadService;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.queries.Prefixes;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;


@Component
public class TripleStoreInitializer implements EnvironmentAware, ApplicationListener<ContextRefreshedEvent> {
    
	// if necessary clean out with
	// delete { ?s ?p ?o } using <http://www.boinq.org/iri/graph/meta/> where { ?s ?p ?o } 
	
	private final Logger log = LoggerFactory.getLogger(TripleStoreInitializer.class);
	
	public static final String ENV_SPRING_TRIPLESTORE = "spring.triplestore.";
    public static final String PROPERTY_ENDPOINT_SPARQL = "endpoint.sparql";
    public static final String PROPERTY_ENDPOINT_UPDATE = "endpoint.update";
    public static final String PROPERTY_METAGRAPH = "metagraph";
	public static final String PROPERTY_LOCALDATASOURCE = "localdatasource";

    private RelaxedPropertyResolver propertyResolver;
	
    @Inject
    SPARQLClientService sparqlClient;
    @Inject
    TripleUploadService tripleUploadService;

	private String localDatasource;

	private String updateEndpoint;

	private String metaGraph;

	private String queryEndpoint;
	
	private Boolean dev;
    
	public void checkInit() {
		Triple triple = new Triple(NodeFactory.createURI(localDatasource), RDF.type.asNode(), TrackVocab.Datasource.asNode());
		if (!alreadyPresent(triple)) {
			init();
		}
	}
	
	public void checkExtraTriples() {
		Triple triple = new Triple(NodeFactory.createURI(localDatasource), TrackVocab.provides.asNode(), NodeFactory.createURI("http://www.boinq.org/data/graph/1bc44707-ad1b-4781-8413-0834a2ed6844"));
		if (!alreadyPresent(triple)) {
			addFromFile("ontologies/track.owl", Lang.RDFXML);
			addFromFile("ontologies/meta.ttl", Lang.TTL);
		}
	}
    
	@Override
	public void setEnvironment(Environment environment) {
		propertyResolver = new RelaxedPropertyResolver(environment, ENV_SPRING_TRIPLESTORE);
		localDatasource = propertyResolver.getProperty(PROPERTY_LOCALDATASOURCE);
		updateEndpoint = propertyResolver.getProperty(PROPERTY_ENDPOINT_UPDATE);
		metaGraph = propertyResolver.getProperty(PROPERTY_METAGRAPH);
		queryEndpoint = propertyResolver.getProperty(PROPERTY_ENDPOINT_SPARQL);
		dev = false;
		for (String profile: environment.getActiveProfiles()) {
			if ("dev".equals(profile)) {
				dev = true;
				break;
			}
		}
	}
	
	private Boolean alreadyPresent(Triple t) {
		Query query = new Query();
		query.setQueryAskType();
		ElementTriplesBlock triples = new ElementTriplesBlock();
		triples.addTriple(t);
		query.setQueryPattern(triples);
		try {
			SPARQLResultSet resultSet = sparqlClient.query(queryEndpoint, metaGraph, query);
			return (resultSet != null && resultSet.getAskResult());
		} catch (Exception e) {
			log.error("Could not check for initialization of triplestore");
		}
		return false;
	}
	
	private void init() {
		Node datasource =  NodeFactory.createURI(localDatasource);
		TripleUploader uploader = tripleUploadService.getUploader(updateEndpoint, metaGraph, Prefixes.getCommonPrefixes());
		uploader.triple(new Triple(datasource, RDF.type.asNode(), TrackVocab.Datasource.asNode()));
		uploader.triple(new Triple(datasource, RDF.type.asNode(), TrackVocab.SPARQLDatasource.asNode()));
		uploader.triple(new Triple(datasource, TrackVocab.references.asNode(), TrackVocab.GRCh37.asNode()));
		uploader.triple(new Triple(datasource, TrackVocab.endpointUrl.asNode(), NodeFactory.createLiteral(queryEndpoint)));
		uploader.finish();
	}
	
	private void addFromFile(String path, Lang lang) {
		InputStream file = this.getClass().getClassLoader().getResourceAsStream(path);
		TripleUploader uploader = tripleUploadService.getUploader(updateEndpoint, metaGraph);
		RDFDataMgr.parse(uploader, file, lang);
		try {
			file.close();
		} catch (IOException io) {
			log.error("Could not close metadata file", io);
		}

	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		checkInit();
		if (dev) checkExtraTriples();
	}

}
