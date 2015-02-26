package com.genohm.boinq.tools.vocabularies;

import java.util.HashMap;
import java.util.Map;

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

	public static Node Reference = NodeFactory.createURI(baseURI+"Reference");
	public static Node ReferenceAssembly = NodeFactory.createURI(baseURI+"ReferenceAssembly");
	public static Node ReferenceMapEntry = NodeFactory.createURI(baseURI+"ReferenceMapEntry");
	public static Node Track = NodeFactory.createURI(baseURI+"Track");
	public static Node FaldoTrack = NodeFactory.createURI(baseURI+"FaldoTrack");

	// Object properties
	
	public static Node endpoint = NodeFactory.createURI(baseURI+"endpoint");
	public static Node entry = NodeFactory.createURI(baseURI+"entry");
	public static Node holds = NodeFactory.createURI(baseURI+"holds");
	public static Node provides = NodeFactory.createURI(baseURI+"provides");
	public static Node references = NodeFactory.createURI(baseURI+"references");
	public static Node originalReference = NodeFactory.createURI(baseURI+"originalReference");
	public static Node targetReference = NodeFactory.createURI(baseURI+"targetReference");
	public static Node partOf = NodeFactory.createURI(baseURI+"partOf");
	public static Node supports = NodeFactory.createURI(baseURI+"supports");
	public static Node targets = NodeFactory.createURI(baseURI+"targets");

	// Data properties
	
	public static Node endpointUrl = NodeFactory.createURI(baseURI+"endpointUrl");
	public static Node graphUri = NodeFactory.createURI(baseURI+"graphUri");
	public static Node motherTerm = NodeFactory.createURI(baseURI+"motherTerm");

	// Individuals
	
	public static Node GRCh37 = NodeFactory.createURI(baseURI+"GRCh37");
	public static Node GRCh37chr01 = NodeFactory.createURI(baseURI+"GRCh37chr01");
	public static Node GRCh37chr02 = NodeFactory.createURI(baseURI+"GRCh37chr02");
	public static Node GRCh37chr03 = NodeFactory.createURI(baseURI+"GRCh37chr03");
	public static Node GRCh37chr04 = NodeFactory.createURI(baseURI+"GRCh37chr04");
	public static Node GRCh37chr05 = NodeFactory.createURI(baseURI+"GRCh37chr05");
	public static Node GRCh37chr06 = NodeFactory.createURI(baseURI+"GRCh37chr06");
	public static Node GRCh37chr07 = NodeFactory.createURI(baseURI+"GRCh37chr07");
	public static Node GRCh37chr08 = NodeFactory.createURI(baseURI+"GRCh37chr08");
	public static Node GRCh37chr09 = NodeFactory.createURI(baseURI+"GRCh37chr09");
	public static Node GRCh37chr10 = NodeFactory.createURI(baseURI+"GRCh37chr10");
	public static Node GRCh37chr11 = NodeFactory.createURI(baseURI+"GRCh37chr11");
	public static Node GRCh37chr12 = NodeFactory.createURI(baseURI+"GRCh37chr12");
	public static Node GRCh37chr13 = NodeFactory.createURI(baseURI+"GRCh37chr13");
	public static Node GRCh37chr14 = NodeFactory.createURI(baseURI+"GRCh37chr14");
	public static Node GRCh37chr15 = NodeFactory.createURI(baseURI+"GRCh37chr15");
	public static Node GRCh37chr16 = NodeFactory.createURI(baseURI+"GRCh37chr16");
	public static Node GRCh37chr17 = NodeFactory.createURI(baseURI+"GRCh37chr17");
	public static Node GRCh37chr18 = NodeFactory.createURI(baseURI+"GRCh37chr18");
	public static Node GRCh37chr19 = NodeFactory.createURI(baseURI+"GRCh37chr19");
	public static Node GRCh37chr20 = NodeFactory.createURI(baseURI+"GRCh37chr20");
	public static Node GRCh37chr21 = NodeFactory.createURI(baseURI+"GRCh37chr21");
	public static Node GRCh37chr22 = NodeFactory.createURI(baseURI+"GRCh37chr22");
	public static Node GRCh37chrX = NodeFactory.createURI(baseURI+"GRCh37chrX");
	public static Node GRCh37chrY = NodeFactory.createURI(baseURI+"GRCh37chrY");
	
	
	// Convenience
	public static Map<String, Node> GRCh37RefMap;
	
	static {
		GRCh37RefMap = new HashMap<String, Node>();
		GRCh37RefMap.put("1", GRCh37chr01);
		GRCh37RefMap.put("2", GRCh37chr02);
		GRCh37RefMap.put("3", GRCh37chr03);
		GRCh37RefMap.put("4", GRCh37chr04);
		GRCh37RefMap.put("5", GRCh37chr05);
		GRCh37RefMap.put("6", GRCh37chr06);
		GRCh37RefMap.put("7", GRCh37chr07);
		GRCh37RefMap.put("8", GRCh37chr08);
		GRCh37RefMap.put("9", GRCh37chr09);
		GRCh37RefMap.put("10", GRCh37chr10);
		GRCh37RefMap.put("11", GRCh37chr11);
		GRCh37RefMap.put("12", GRCh37chr12);
		GRCh37RefMap.put("13", GRCh37chr13);
		GRCh37RefMap.put("14", GRCh37chr14);
		GRCh37RefMap.put("15", GRCh37chr15);
		GRCh37RefMap.put("16", GRCh37chr16);
		GRCh37RefMap.put("17", GRCh37chr17);
		GRCh37RefMap.put("18", GRCh37chr18);
		GRCh37RefMap.put("19", GRCh37chr19);
		GRCh37RefMap.put("20", GRCh37chr20);
		GRCh37RefMap.put("21", GRCh37chr21);
		GRCh37RefMap.put("22", GRCh37chr22);
		GRCh37RefMap.put("X", GRCh37chrX);
		GRCh37RefMap.put("Y", GRCh37chrY);
	}
	
	
}
