package com.genohm.boinq.service;

import static com.genohm.boinq.domain.query.TemplateFactory.entityNode;
import static com.genohm.boinq.domain.query.TemplateFactory.typeEdgeTemplate;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.vocabulary.SKOS;

import com.genohm.boinq.domain.query.EdgeTemplate;
import com.genohm.boinq.domain.query.GraphTemplate;
import com.genohm.boinq.domain.query.NodeTemplate;
import com.genohm.boinq.generated.vocabularies.SioVocab;

public class GraphTemplateRepositoryTestData {
	
	public static final String DIS_GE_NET = "DisGeNet";

	static {
		GraphTemplateRepositoryTestData.disGenet = buildDisGeNet();
	}
	
	public static GraphTemplate disGenet;
	public static NodeTemplate geneNode;
	
	private static String ncit(String id) {
		return "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#"+ id;
	}

	private static GraphTemplate buildDisGeNet() {
		GraphTemplate disGeNet = new GraphTemplate();
		Set<EdgeTemplate> edges = new HashSet<>();
		NodeTemplate gda = entityNode("GeneDiseaseAssociation", false);
		edges.add(typeEdgeTemplate(gda, SioVocab.gene_disease_association.toString()));
		geneNode = entityNode("gene", false);
		edges.add(typeEdgeTemplate(geneNode, ncit("C16612")));
		NodeTemplate dis = entityNode("Disease, Disorder or Finding", false);
		edges.add(new EdgeTemplate(gda, dis, SioVocab.refers_to.toString()));
		edges.add(new EdgeTemplate(gda, geneNode, SioVocab.refers_to.toString()));
		NodeTemplate disId = entityNode("Disease identifier", true);
		edges.add(new EdgeTemplate(dis, disId, SKOS.exactMatch.toString()));
		NodeTemplate disClass = entityNode("MeSH disease class", true);
		disClass.setValuesEndpoint("http://localhost/static/sparql");
		disClass.setValuesGraph("mesh");
		disClass.setValuesRootTerm("disease class");
		NodeTemplate pathway = entityNode("pathway", false);
		edges.add(new EdgeTemplate(geneNode, pathway, SioVocab.is_participant_in.toString()));
		edges.add(typeEdgeTemplate(pathway, ncit("C20633")));
		NodeTemplate pathwayId = entityNode("pathway ID", true);
		edges.add(new EdgeTemplate(pathway, pathwayId, SKOS.exactMatch.toString()));
		disGeNet.setEdgeTemplates(edges);
		disGeNet.setEndpointUrl("http://disgenet");
		disGeNet.setGraphIri("brol");
		disGeNet.setName(DIS_GE_NET);
		disGeNet.setType(GraphTemplate.GRAPH_TYPE_REMOTE);
		return disGeNet;
	}
	
	private GraphTemplateRepositoryTestData() {
	}

}