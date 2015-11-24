package com.genohm.boinq.init;


import java.util.LinkedList;
import java.util.List;

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
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.vocabulary.RDF;


@Component
public class TripleStoreInitializer implements EnvironmentAware, ApplicationListener<ContextRefreshedEvent> {
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
    
	public void checkInit() {
		Triple triple = new Triple(NodeFactory.createURI(localDatasource), RDF.type.asNode(), TrackVocab.Datasource.asNode());
		if (!alreadyPresent(triple)) {
			init();
		}
		
	}
    
	@Override
	public void setEnvironment(Environment environment) {
		propertyResolver = new RelaxedPropertyResolver(environment, ENV_SPRING_TRIPLESTORE);
		localDatasource = propertyResolver.getProperty(PROPERTY_LOCALDATASOURCE);
		updateEndpoint = propertyResolver.getProperty(PROPERTY_ENDPOINT_UPDATE);
		metaGraph = propertyResolver.getProperty(PROPERTY_METAGRAPH);
		queryEndpoint = propertyResolver.getProperty(PROPERTY_ENDPOINT_SPARQL);
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
		List<Triple> triples = new LinkedList<Triple>();
		triples.add(new Triple(datasource, RDF.type.asNode(), TrackVocab.Datasource.asNode()));
		triples.add(new Triple(datasource, RDF.type.asNode(), TrackVocab.SPARQLDatasource.asNode()));
		triples.add(new Triple(datasource, TrackVocab.references.asNode(), TrackVocab.GRCh37.asNode()));
		triples.add(new Triple(datasource, TrackVocab.endpointUrl.asNode(), NodeFactory.createLiteral(queryEndpoint)));
		for (Triple triple: triples) {
			uploader.triple(triple);
		}
		uploader.finish();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		checkInit();
	}

}
