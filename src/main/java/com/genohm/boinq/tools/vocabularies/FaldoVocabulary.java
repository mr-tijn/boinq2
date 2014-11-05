package com.genohm.boinq.tools.vocabularies;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;

public class FaldoVocabulary {
	public static String baseURI = "http://biohackathon.org/resource/faldo#";
	public static Node position = NodeFactory.createURI(baseURI+"position");
	public static Node begin = NodeFactory.createURI(baseURI+"begin");
	public static Node end = NodeFactory.createURI(baseURI+"end");
	public static Node reference = NodeFactory.createURI(baseURI+"reference");
	public static Node Position = NodeFactory.createURI(baseURI+"Position");
	public static Node StrandPosition = NodeFactory.createURI(baseURI+"StrandedPosition");
	public static Node ForwardStrandPosition = NodeFactory.createURI(baseURI+"ForwardStrandPosition");
	public static Node ReverseStrandPosition = NodeFactory.createURI(baseURI+"ReverseStrandPosition");
}
