package org.boinq.service;

import static org.boinq.domain.query.TemplateFactory.entityNode;
import static org.boinq.domain.query.TemplateFactory.literalNode;
import static org.boinq.domain.query.TemplateFactory.typeEdgeTemplate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.XSD;
import org.boinq.domain.query.EdgeTemplate;
import org.boinq.domain.query.GraphTemplate;
import org.boinq.domain.query.NodeFilter;
import org.boinq.domain.query.NodeTemplate;
import org.boinq.domain.query.QueryBridge;
import org.boinq.domain.query.QueryDefinition;
import org.boinq.domain.query.QueryEdge;
import org.boinq.domain.query.QueryGraph;
import org.boinq.domain.query.QueryNode;

import org.boinq.generated.vocabularies.FaldoVocab;
import org.boinq.generated.vocabularies.FormatVocab;
import org.boinq.generated.vocabularies.GfvoVocab;
import org.boinq.generated.vocabularies.SioVocab;
import org.boinq.generated.vocabularies.SoVocab;

public class QueryDefinitionTestData {
	
	GraphTemplate ensembl = new GraphTemplate();
	GraphTemplate vcf = new GraphTemplate();
	GraphTemplate bed = new GraphTemplate();
	GraphTemplate disGeNet = new GraphTemplate();
	public QueryDefinition qd;
	
	private EdgeTemplate featureLocation;
	private EdgeTemplate featureHasType;
	
	private String ncit(String id) {
		return "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#"+ id;
	}
	
	private void buildDisGeNet() {
		Set<EdgeTemplate> edges = new HashSet<>();
		NodeTemplate gda = entityNode("GeneDiseaseAssociation", false);
		edges.add(typeEdgeTemplate(gda, SioVocab.gene_disease_association.toString()));
		NodeTemplate gene = entityNode("gene", false);
		edges.add(typeEdgeTemplate(gene, ncit("C16612")));
		NodeTemplate dis = entityNode("Disease, Disorder or Finding", false);
		edges.add(new EdgeTemplate(gda, dis, SioVocab.refers_to.toString()));
		edges.add(new EdgeTemplate(gda, gene, SioVocab.refers_to.toString()));
		NodeTemplate disId = entityNode("Disease identifier", true);
		edges.add(new EdgeTemplate(dis, disId, SKOS.exactMatch.toString()));
		NodeTemplate disClass = entityNode("MeSH disease class", true);
		disClass.setValuesEndpoint("http://localhost/static/sparql");
		disClass.setValuesGraph("mesh");
		disClass.setValuesRootTerm("disease class");
		NodeTemplate pathway = entityNode("pathway", false);
		edges.add(new EdgeTemplate(gene, pathway, SioVocab.is_participant_in.toString()));
		edges.add(typeEdgeTemplate(pathway, ncit("C20633")));
		NodeTemplate pathwayId = entityNode("pathway ID", true);
		edges.add(new EdgeTemplate(pathway, pathwayId, SKOS.exactMatch.toString()));
		disGeNet.setEdgeTemplates(edges);
	}
	
	private void buildNextProt() {
		
	}
	
	
	private void buildVcf() {
		Set<EdgeTemplate> edges = new HashSet<>();
		NodeTemplate feature = entityNode("Feature", false);
		NodeTemplate featureType = entityNode("FeatureType", true);
		featureType.setValuesTermList(Arrays.asList(SoVocab.indel.toString(), SoVocab.insertion.toString(), SoVocab.MNP.toString()));
		featureHasType = new EdgeTemplate(feature, featureType, RDF.type.toString());
		edges.add(featureHasType);
		NodeTemplate id = entityNode("ID", false);
		edges.add(typeEdgeTemplate(id, GfvoVocab.Identifier.toString()));
		NodeTemplate idValue = literalNode("value", XSD.xstring.toString());
		edges.add(new EdgeTemplate(id, idValue, RDF.value.toString()));
		edges.add(new EdgeTemplate(feature, id, GfvoVocab.has_identifier.toString()));
		NodeTemplate evidence = entityNode("Evidence", false);
		NodeTemplate sample = entityNode("Sample", false);
		NodeTemplate sampleLabel = literalNode("label", XSD.xstring.toString());
		//check
		edges.add(typeEdgeTemplate(sample, GfvoVocab.Biological_Entity.toString()));
		edges.add(new EdgeTemplate(sample, sampleLabel, RDFS.label.toString()));
		edges.add(new EdgeTemplate(evidence, sample, GfvoVocab.has_source.toString()));
		NodeTemplate genotype = entityNode("Genotype", false);
		edges.add(new EdgeTemplate(evidence, genotype, GfvoVocab.has_attribute.toString()));
		NodeTemplate identifier = literalNode("identifier", XSD.xstring.toString());
		edges.add(new EdgeTemplate(feature, identifier, DCTerms.identifier.toString()));
		NodeTemplate attribute = entityNode("attribute", false);
		edges.add(new EdgeTemplate(feature, attribute, GfvoVocab.has_attribute.toString()));
		//must find possible types
		List<String> attributeTypes = new LinkedList<>();
		attributeTypes.add(XSD.xstring.toString());
		NodeTemplate attributeType = entityNode("attribute", true);
		attributeType.setValuesTermList(attributeTypes);
		edges.add(new EdgeTemplate(attribute, attributeType, RDF.type.toString()));
		NodeTemplate attributeValue = new NodeTemplate();
		attributeValue.setNodeType(QueryNode.NODETYPE_ATTRIBUTE);
		edges.add(new EdgeTemplate(attribute, attributeValue, RDF.value.toString()));
		NodeTemplate location = new NodeTemplate();
		location.setFilterable(true);
		location.setName("Location");
		location.setNodeType(QueryNode.NODETYPE_FALDOLOCATION);
		featureLocation = new EdgeTemplate(feature, location, FaldoVocab.location.toString());
		edges.add(featureLocation);
		NodeTemplate entry = entityNode("Entry", false);
		NodeTemplate entryLabel = literalNode("Label", XSD.xstring.toString());
		edges.add(typeEdgeTemplate(entry, FormatVocab.VCF_Entry.toString()));
		edges.add(new EdgeTemplate(entry, entryLabel, RDFS.label.toString()));
		edges.add(new EdgeTemplate(entry, feature, FormatVocab.defines.toString()));
		vcf.setEdgeTemplates(edges);
		vcf.setEndpointUrl("http://localhost");
		vcf.setGraphIri("vcf");
		vcf.setType(GraphTemplate.GRAPH_TYPE_LOCAL);
	}
	
	
	private EdgeTemplate identifier(NodeTemplate node) {
		NodeTemplate identifier = literalNode("identifier", XSD.xstring.toString());
		return new EdgeTemplate(node, identifier, DC.identifier.toString());
	}
	
	private EdgeTemplate description(NodeTemplate node) {
		NodeTemplate description = literalNode("description", XSD.xstring.toString());
		return new EdgeTemplate(node, description, DC.description.toString());
	}
	
	private EdgeTemplate faldo(NodeTemplate node) {
		NodeTemplate faldo = entityNode("location", true);
		faldo.setNodeType(QueryNode.NODETYPE_FALDOLOCATION);
		faldo.setColor("yellow");
		return new EdgeTemplate(node, faldo, FaldoVocab.location.toString());
	}
	
	private EdgeTemplate exonType;
	private EdgeTemplate exonLocation;
	private EdgeTemplate refersTo;
	private EdgeTemplate hasRank;
	
	private void buildEnsemblTemplate() {
		Set<EdgeTemplate> edges = new HashSet<>();
		NodeTemplate transcript = entityNode("Transcript", false);
		edges.add(typeEdgeTemplate(transcript, SoVocab.transcript.toString()));
		edges.add(identifier(transcript));
		edges.add(description(transcript));
		edges.add(faldo(transcript));
		NodeTemplate exon = entityNode("Exon", false);
		exonType = typeEdgeTemplate(exon, SoVocab.exon.toString()); 
		edges.add(exonType);
		edges.add(new EdgeTemplate(transcript, exon, SoVocab.has_part.toString()));
		edges.add(identifier(exon));
		edges.add(description(exon));
		exonLocation = faldo(exon);
		edges.add(exonLocation);
		NodeTemplate orderedPart = entityNode("Ordered Part", false);
		edges.add(typeEdgeTemplate(orderedPart, SioVocab.ordered_list_item.toString()));
		refersTo = new EdgeTemplate(orderedPart, exon, SioVocab.refers_to.toString()); 
		edges.add(refersTo);
		edges.add(new EdgeTemplate(transcript, orderedPart, SioVocab.has_ordered_part.toString()));
		NodeTemplate rank = literalNode("rank", XSD.xint.toString());
		hasRank = new EdgeTemplate(orderedPart, rank, SioVocab.has_value.toString());
		edges.add(hasRank);
		NodeTemplate translation = entityNode("Translation", false);
		edges.add(new EdgeTemplate(transcript, translation, SoVocab.translates_to.toString()));
		edges.add(identifier(translation));
		edges.add(description(translation));
		edges.add(faldo(translation));
		NodeTemplate proteinFeature = entityNode("Protein feature", false);
		edges.add(new EdgeTemplate(translation, proteinFeature, RDFS.seeAlso.toString()));
		NodeTemplate gene = entityNode("Gene", false);
		edges.add(typeEdgeTemplate(gene, SoVocab.gene.toString()));
		edges.add(new EdgeTemplate(transcript, gene, SoVocab.transcribed_from.toString()));
		edges.add(identifier(gene));
		edges.add(description(gene));
		edges.add(faldo(gene));
		NodeTemplate synonym = literalNode("Synonym", XSD.xstring.toString());
		edges.add(new EdgeTemplate(gene, synonym, SKOS.altLabel.toString()));
		ensembl.setEdgeTemplates(edges);
		ensembl.setEndpointUrl("http://localhost");
		ensembl.setGraphIri("http://ensembl");
		ensembl.setType(GraphTemplate.GRAPH_TYPE_REMOTE);
	}
	
	public QueryDefinitionTestData() {
		buildEnsemblTemplate();
		buildVcf();
		qd = new QueryDefinition();
		// retrieve first exons overlapping with indels and retrieve both with location
		Set<QueryBridge> bridges = new HashSet<>();
		Set<QueryGraph> graphs = new HashSet<>();
		QueryGraph myEnsembl = new QueryGraph();
		myEnsembl.setTemplate(ensembl);
		myEnsembl.setIdx(0);
		Set<QueryEdge> myEnsemblEdges = new HashSet<>();
		
		QueryNode myExon = new QueryNode();
		myExon.setIdx(0);
		myExon.setTemplate(exonType.getFrom());
		QueryNode myType = new QueryNode();
		myType.setIdx(1);
		myType.setTemplate(exonType.getTo());
		QueryEdge myExonType = new QueryEdge();
		myExonType.setTemplate(this.exonType);
		myExonType.setRetrieve(true);
		myExonType.setFrom(myExon);
		myExonType.setTo(myType);
		myEnsemblEdges.add(myExonType);
		
		
		QueryNode myExonLocation = new QueryNode();
		myExonLocation.setIdx(2);
		myExonLocation.setTemplate(exonLocation.getTo());
		QueryEdge myExonHasLocation = new QueryEdge();
		myExonHasLocation.setTemplate(exonLocation);
		myExonHasLocation.setRetrieve(true);
		myExonHasLocation.setFrom(myExon);
		myExonHasLocation.setTo(myExonLocation);
		myEnsemblEdges.add(myExonHasLocation);
		
		QueryNode myOrderedPart = new QueryNode();
		myOrderedPart.setIdx(3);
		myOrderedPart.setTemplate(hasRank.getFrom());
		QueryEdge myRefersTo = new QueryEdge();
		myRefersTo.setTemplate(refersTo);
		myRefersTo.setFrom(myOrderedPart);
		myRefersTo.setTo(myExon);
		myEnsemblEdges.add(myRefersTo);
		
		QueryNode myRank = new QueryNode();
		myRank.setIdx(4);
		myRank.setTemplate(hasRank.getTo());
		myRank.setNodeFilters(new HashSet<>());
		QueryEdge myHasRank = new QueryEdge();
		myHasRank.setTemplate(hasRank);
		myHasRank.setFrom(myOrderedPart);
		myHasRank.setTo(myRank);
		NodeFilter rankIsOne = new NodeFilter();
		rankIsOne.setType(NodeFilter.FILTER_TYPE_GENERIC_EQUALS);
		rankIsOne.setIntegerValue(1L);
		myRank.getNodeFilters().add(rankIsOne);
		myEnsemblEdges.add(myHasRank);
		
		myEnsembl.setQueryEdges(myEnsemblEdges);
		graphs.add(myEnsembl);
		
		QueryGraph myVcf = new QueryGraph();
		myVcf.setTemplate(vcf);
		myVcf.setIdx(2);
		Set<QueryEdge> myVcfEdges = new HashSet<>();
		
		QueryNode myFeature = new QueryNode();
		myFeature.setIdx(0);
		myFeature.setTemplate(featureLocation.getFrom());
		QueryNode myFeatureType = new QueryNode();
		myFeatureType.setIdx(1);
		myFeatureType.setTemplate(featureHasType.getTo());
		NodeFilter filter = new NodeFilter();
		filter.setType(NodeFilter.FILTER_TYPE_GENERIC_VALUES);
		filter.setTermValues(Arrays.asList(SoVocab.insertion.toString()));
		myFeatureType.getNodeFilters().add(filter);
		QueryEdge myFeatureHasType = new QueryEdge();
		myFeatureHasType.setFrom(myFeature);
		myFeatureHasType.setTo(myFeatureType);
		myFeatureHasType.setTemplate(featureHasType);
		myFeatureHasType.setRetrieve(true);
		myVcfEdges.add(myFeatureHasType);
		
		QueryNode myLocation = new QueryNode();
		myLocation.setIdx(2);
		myLocation.setTemplate(featureLocation.getTo());
		QueryEdge myFeatureLocation = new QueryEdge();
		myFeatureLocation.setFrom(myFeature);
		myFeatureLocation.setTo(myLocation);
		myFeatureLocation.setTemplate(featureLocation);
		myFeatureLocation.setRetrieve(true);
		myVcfEdges.add(myFeatureLocation);
		
		myVcf.setQueryEdges(myVcfEdges);
		graphs.add(myVcf);
		
		QueryBridge locationOverlap = new QueryBridge();
		locationOverlap.setFromGraph(myEnsembl);
		locationOverlap.setToGraph(myVcf);
		locationOverlap.setFromNode(myExonLocation);
		locationOverlap.setToNode(myLocation);
		
		bridges.add(locationOverlap);
		qd.setQueryBridges(bridges);
		qd.setQueryGraphs(graphs);
		qd.setResultAsTable(false);
		qd.setTargetGraph("test");
		
	}

	

}