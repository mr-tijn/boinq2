package org.boinq.service;

import java.net.URI;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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
			URI base = new URI(termBase);
			URI uri = base.resolve(postfix);
			return NodeFactory.createURI(uri.toString());
		} catch (Exception e) {
			log.error("Could not generate URI",e);
		}
		return null;
	}
}
