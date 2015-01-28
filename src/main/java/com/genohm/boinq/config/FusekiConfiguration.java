package com.genohm.boinq.config;

import org.apache.jena.fuseki.server.FusekiConfig;
import org.apache.jena.fuseki.server.SPARQLServer;
import org.apache.jena.fuseki.server.ServerConfig;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FusekiConfiguration implements EnvironmentAware {

    public static final String ENV_SPRING_FUSEKI = "spring.fuseki.";
	public static final String PROP_PORT = "port";
	public static final String PROP_CONFIGFILE = "configfile";
	public static final String PROP_JETTYCONFIGFILE = "jettyconfigfile";
    
	protected Environment environment;
    protected RelaxedPropertyResolver propertyResolver;


    @Override
    public void setEnvironment(Environment environment) {
    	this.environment = environment;
        this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_SPRING_FUSEKI);
    }
	
	@Bean
	public SPARQLServer sparqlServer() {
		ServerConfig serverConfig = FusekiConfig.configure(propertyResolver.getProperty(PROP_CONFIGFILE));
		serverConfig.port = Integer.parseInt(propertyResolver.getProperty(PROP_PORT));
		serverConfig.jettyConfigFile = propertyResolver.getProperty(PROP_JETTYCONFIGFILE);
		return new SPARQLServer(serverConfig);
	}
	
}
