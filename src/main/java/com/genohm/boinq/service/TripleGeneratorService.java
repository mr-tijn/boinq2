package com.genohm.boinq.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.httpclient.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;

@Service
public class TripleGeneratorService {
	
	private Logger log = LoggerFactory.getLogger(TripleGeneratorService.class);
	private String termBase;
	
	@Inject
	Environment env;
	
	@PostConstruct
	public void initialize() {
		RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(env);
		termBase = resolver.getProperty("spring.triplegen.baseIRI");
	}
	
	public Node generateURI(String postfix) {
		try {
			URI uri = new URI(termBase, postfix);
			return NodeFactory.createURI(uri.toString());
		} catch (Exception e) {
			log.error("Could not generate URI",e);
		}
		return null;
	}
}
