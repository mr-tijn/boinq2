package com.genohm.boinq.tools.vocabularies;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;

public class DatasourceVocabulary {
	public static String baseURI = "http://www.boinq.org/terms/datasource#";
	public static String propertyHolderBaseURI = "http://www.boinq.org/PropertyHolder#";;
	public static Node provides = NodeFactory.createURI(baseURI+"provides");
	public static Node datasource = NodeFactory.createURI(baseURI+"datasource");
	public static Node faldoDatasource = NodeFactory.createURI(baseURI+"localFaldoDatasource");
	public static Node externalTerm = NodeFactory.createURI(baseURI+"externalTerm");
	public static Node hasGraph = NodeFactory.createURI(baseURI+"hasGraph");
	public static Node hasEndpoint = NodeFactory.createURI(baseURI+"hasEndpoint");
	public static Node targetGraph = NodeFactory.createURI(baseURI+"targetGraph");
	public static Node targetEndpoint = NodeFactory.createURI(baseURI+"targetEndpoint");

}
