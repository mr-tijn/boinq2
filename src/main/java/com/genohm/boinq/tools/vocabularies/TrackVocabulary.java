package com.genohm.boinq.tools.vocabularies;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;

public class TrackVocabulary {
	
	//todo work with track.owl
	
	public static String baseURI = "http://www.boinq.org/ontologies/track#";
	
	// Classes
	
	public static Node Datasource = NodeFactory.createURI(baseURI+"Datasource");
	public static Node SPARQLDatasource = NodeFactory.createURI(baseURI+"SPARQLDatasource");
	public static Node FeatureDatasource = NodeFactory.createURI(baseURI+"FeatureDatasource");
	public static Node FaldoDatasource = NodeFactory.createURI(baseURI+"FaldoDatasource");
	public static Node TermDatasource = NodeFactory.createURI(baseURI+"TermDatasource");
	
	public static Node FeatureType = NodeFactory.createURI(baseURI+"FeatureType");
	public static Node Field = NodeFactory.createURI(baseURI+"Field");
	public static Node Operator = NodeFactory.createURI(baseURI+"Operator");
	
	public static Node Match = NodeFactory.createURI(baseURI+"Match");
	public static Node MatchField = NodeFactory.createURI(baseURI+"MatchField");
	public static Node MatchNumber = NodeFactory.createURI(baseURI+"MatchNumber");
	public static Node MatchString = NodeFactory.createURI(baseURI+"MatchString");
	public static Node MatchTerm = NodeFactory.createURI(baseURI+"MatchTerm");

	public static Node ReferenceAssembly = NodeFactory.createURI(baseURI+"ReferenceAssembly");
	public static Node Track = NodeFactory.createURI(baseURI+"Track");
	public static Node FaldoTrack = NodeFactory.createURI(baseURI+"FaldoTrack");

	// Object properties
	
	public static Node endpoint = NodeFactory.createURI(baseURI+"endpoint");
	public static Node holds = NodeFactory.createURI(baseURI+"holds");
	public static Node provides = NodeFactory.createURI(baseURI+"provides");
	public static Node references = NodeFactory.createURI(baseURI+"references");
	public static Node supports = NodeFactory.createURI(baseURI+"supports");
	public static Node targets = NodeFactory.createURI(baseURI+"targets");

	// Data properties
	
	public static Node endpointUrl = NodeFactory.createURI(baseURI+"endpointUrl");
	public static Node graphUri = NodeFactory.createURI(baseURI+"graphUri");
	public static Node motherTerm = NodeFactory.createURI(baseURI+"motherTerm");

	// Individuals
	
	public static Node GRCh37 = NodeFactory.createURI(baseURI+"GRCh37");
	
}
