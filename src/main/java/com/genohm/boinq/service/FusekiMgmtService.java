package com.genohm.boinq.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.jena.fuseki.server.SPARQLServer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Service;

import com.genohm.boinq.config.ProxyConfiguration;

@Service
@AutoConfigureAfter(ProxyConfiguration.class)
public class FusekiMgmtService {

	
	@Inject
	private SPARQLServer sparqlServer;
	
	@PostConstruct
	public void start() {
		startServer();
	}
	
	
	public Boolean isUp() {
		return sparqlServer.getServer().isRunning();
	}
	
	public String getStatus() {
		return sparqlServer.getServer().getState();
	}
	
	public void startServer() {
		if (sparqlServer!= null) {
			sparqlServer.start();
		}
	}
	
	public void stopServer() {
		if (sparqlServer != null) {
			sparqlServer.stop();
		}
	}
	
	public void restartServer() {
		if (sparqlServer != null) {
			sparqlServer.stop();
			sparqlServer.start();
		}
	}
	
}
