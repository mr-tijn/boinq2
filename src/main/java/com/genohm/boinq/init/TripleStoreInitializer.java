package com.genohm.boinq.init;


import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.genohm.boinq.domain.SPARQLResultSet;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.service.FusekiMgmtService;
import com.genohm.boinq.service.SPARQLClientService;
import com.genohm.boinq.service.TripleUploadService;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.queries.Prefixes;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock;
import com.hp.hpl.jena.vocabulary.RDF;

@Configuration
@AutoConfigureAfter(FusekiMgmtService.class)
public class TripleStoreInitializer implements EnvironmentAware {

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
    @Inject
    FusekiMgmtService fusekiMgmtService; // only way to make it start before this class ?

	private String localDatasource;

	private String updateEndpoint;

	private String metaGraph;

	private String queryEndpoint;
    
    @PostConstruct
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
		query.setQuerySelectType();
		ElementTriplesBlock triples = new ElementTriplesBlock();
		triples.addTriple(t);
		query.setQueryPattern(triples);
		try {
			SPARQLResultSet resultSet = sparqlClient.query(queryEndpoint, metaGraph, query);
			if (resultSet != null && resultSet.getRecords() != null && resultSet.getRecords().size() > 0) {
				return true;
			}
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
			uploader.put(triple);
		}
	}
	
}
