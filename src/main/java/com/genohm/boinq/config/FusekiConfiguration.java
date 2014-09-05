package com.genohm.boinq.config;

import java.util.LinkedList;
import java.util.List;

import org.apache.jena.fuseki.server.DatasetRef;
import org.apache.jena.fuseki.server.FusekiConfig;
import org.apache.jena.fuseki.server.SPARQLServer;
import org.apache.jena.fuseki.server.ServerConfig;
import org.apache.jena.fuseki.server.ServiceRef;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.mem.GraphMem;
import com.hp.hpl.jena.sparql.core.DatasetGraphSimpleMem;
import com.hp.hpl.jena.vocabulary.RDF;

@Configuration
public class FusekiConfiguration implements EnvironmentAware {

    private static final String ENV_SPRING_FUSEKI = "spring.fuseki.";
	private static final String PROP_PORT = "port";
	private static final String PROP_PORT_DEFAULT = "3456";
	private static final String PROP_CONFIGFILE = "configfile";
	private static final String PROP_CONFIGFILE_DEFAULT = "localdata.ttl";
    
    
	private Environment environment;
    private RelaxedPropertyResolver propertyResolver;


    @Override
    public void setEnvironment(Environment environment) {
    	this.environment = environment;
        this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_SPRING_FUSEKI);
    }
	
	@Bean
	public SPARQLServer sparqlServer() {
		List<DatasetRef> datasets = new LinkedList<DatasetRef>();
//		TODO: load config from ttl file	
//		ServerConfig config = FusekiConfig.configure(environment.getProperty(PROP_CONFIGFILE, PROP_CONFIGFILE_DEFAULT));
		
		
		ServerConfig config = new ServerConfig();
		
		int port = Integer.parseInt(environment.getProperty(PROP_PORT, PROP_PORT_DEFAULT));
		config.loopback = true;
		config.port = port;
		config.pagesPort = port;
		config.pages = null;
		config.mgtPort = -1;
		config.jettyConfigFile = null;
		config.verboseLogging = true;
		
		DatasetGraphSimpleMem dsGraph = new DatasetGraphSimpleMem();
		GraphMem graphData = new GraphMem();
		graphData.add(new Triple(NodeFactory.createURI("http://martijn"),RDF.type.asNode(),NodeFactory.createURI("http://king")));
		dsGraph.addGraph(NodeFactory.createURI("http://graph"), graphData);
		ServiceRef queryService = new ServiceRef("query");
		queryService.endpoints = new LinkedList<String>();
		queryService.endpoints.add("query");
		queryService.endpoints.add("sparql");
		DatasetRef dsRef = new DatasetRef();
		dsRef.name = "exampledata";
		dsRef.dataset = dsGraph;
		dsRef.query = queryService;
		datasets.add(dsRef);
		config.datasets = datasets;

		
		return new SPARQLServer(config);
	}
	
}
