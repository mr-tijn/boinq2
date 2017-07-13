package com.genohm.boinq.service;

import static com.genohm.boinq.domain.query.TemplateFactory.*;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.match.Term;
import com.genohm.boinq.domain.query.EdgeTemplate;
import com.genohm.boinq.domain.query.GraphTemplate;
import com.genohm.boinq.domain.query.NodeTemplate;
import com.genohm.boinq.generated.vocabularies.FaldoVocab;
import com.genohm.boinq.generated.vocabularies.FormatVocab;
import com.genohm.boinq.generated.vocabularies.SoVocab;
import com.genohm.boinq.repository.GraphTemplateRepository;


@Service
public class GraphTemplateBuilderService {
	
	private static final long BED_TEMPLATE_ID = 1L;
	
	@Inject
	private GraphTemplateRepository graphTemplateRepo;
	
	private class Increment {
		private Integer count;
		public Increment(Integer init) {
			this.count = init; 
		}
		public Integer next() {
			return count++;
		}
	}
	
	public GraphTemplate fromBed(String mainType, String subType, String assembly) {
		GraphTemplate bedTemplate = new GraphTemplate();
		Set<EdgeTemplate> edges = new HashSet<>();
		
		Increment idx = new Increment(0);
		
		NodeTemplate bedEntry = new NodeTemplate();
		bedEntry.setIdx(idx.next());
		bedEntry.setColor("blue");
		bedEntry.setDescription("The entry describing the feature and its subfeatures");
		bedEntry.setFilterable(false);
		bedEntry.setFixedType(FormatVocab.BED_Entry.toString());
		bedEntry.setX(50);
		bedEntry.setY(50);
		
		NodeTemplate mainFeature = new NodeTemplate();
		mainFeature.setIdx(idx.next());
		mainFeature.setColor("blue");
		mainFeature.setDescription("The main feature");
		mainFeature.setFilterable(false);
		mainFeature.setFixedType(mainType);
		mainFeature.setX(166);
		mainFeature.setY(302);
		edges.add(new EdgeTemplate(bedEntry, mainFeature, FormatVocab.defines.toString()));
		
		NodeTemplate subFeature = new NodeTemplate();
		subFeature.setIdx(idx.next());
		subFeature.setColor("blue");
		subFeature.setDescription("Subfeature");
		subFeature.setFilterable(false);
		subFeature.setFixedType(subType);
		subFeature.setX(791);
		subFeature.setY(183);
		edges.add(new EdgeTemplate(mainFeature, subFeature, SoVocab.has_part.toString()));
		
		NodeTemplate mainId = literalNode("Identifier", XSD.xstring.asNode());
		mainId.setIdx(idx.next());
		mainId.setFilterable(true);
		mainId.setX(261);
		mainId.setY(296);
		edges.add(new EdgeTemplate(mainFeature, mainId, DCTerms.identifier.toString()));
		
		NodeTemplate mainLabel = literalNode("Label", XSD.xstring.asNode());
		mainLabel.setIdx(idx.next());
		mainLabel.setFilterable(true);
		mainLabel.setX(164);
		mainLabel.setY(285);
		edges.add(new EdgeTemplate(mainFeature, mainLabel, RDFS.label.toString()));
		
		NodeTemplate mainDescription = literalNode("Description", XSD.xstring.asNode());
		mainDescription.setIdx(idx.next());
		mainDescription.setFilterable(true);
		mainDescription.setX(58);
		mainDescription.setY(281);
		edges.add(new EdgeTemplate(mainFeature, mainDescription, DCTerms.description.toString()));
		
		NodeTemplate subId = literalNode("Identifier", XSD.xstring.asNode());
		subId.setIdx(idx.next());
		subId.setFilterable(true);
		subId.setX(905);
		subId.setY(311);
		edges.add(new EdgeTemplate(subFeature, subId, DCTerms.identifier.toString()));
		
		NodeTemplate subLabel = literalNode("Label", XSD.xstring.asNode());
		subLabel.setIdx(idx.next());
		subLabel.setFilterable(true);
		subLabel.setX(784);
		subLabel.setY(310);
		edges.add(new EdgeTemplate(subFeature, subLabel, RDFS.label.toString()));

		NodeTemplate subDescription = literalNode("Description", XSD.xstring.asNode());
		subDescription.setIdx(idx.next());
		subDescription.setFilterable(true);
		subDescription.setX(708);
		subDescription.setY(302);
		edges.add(new EdgeTemplate(subFeature, subDescription, DCTerms.description.toString()));
		
		NodeTemplate mainLocation = locationNode(assembly);
		mainLocation.setIdx(idx.next());
		mainLocation.setX(152);
		mainLocation.setY(58);
		edges.add(new EdgeTemplate(mainFeature, mainLocation, FaldoVocab.location.toString()));
		NodeTemplate subLocation = locationNode(assembly);
		subLocation.setIdx(idx.next());
		subLocation.setX(736);
		subLocation.setY(91);
		edges.add(new EdgeTemplate(subFeature, subLocation, FaldoVocab.location.toString()));

		// bed feature attributes
		edges.addAll(bedEntryAttributes(bedEntry, idx));
		edges.addAll(bedFeatureAttributes(mainFeature, idx));
		edges.addAll(bedFeatureAttributes(subFeature, idx));
		
		bedTemplate.setEdgeTemplates(edges);
		return bedTemplate;
	}
		
	private Set<EdgeTemplate> bedFeatureAttributes(NodeTemplate bedFeature, Increment idx) {
		Set<EdgeTemplate> attributes = new HashSet<>();
		attributes.addAll(addAttribute(bedFeature, SoVocab.score.toString(), XSD.xdouble.toString(), "Score", idx, bedFeature.getX()+100, bedFeature.getY()-100));
		return attributes;
	}
	
	private Set<EdgeTemplate> bedEntryAttributes(NodeTemplate bedEntry, Increment idx) {
		Set<EdgeTemplate> attributes = new HashSet<>();
		attributes.addAll(addAttribute(bedEntry, FormatVocab.RGBred.toString(), XSD.integer.toString(), "RGB red", idx, bedEntry.getX()+100, bedEntry.getY()-100));
		attributes.addAll(addAttribute(bedEntry, FormatVocab.RGBgreen.toString(), XSD.integer.toString(), "RGB green", idx, bedEntry.getX()+100, bedEntry.getY()-50));
		attributes.addAll(addAttribute(bedEntry, FormatVocab.RGBblue.toString(), XSD.integer.toString(), "RGB blue", idx, bedEntry.getX()+100, bedEntry.getY()));
		return attributes;
	}

	private Set<EdgeTemplate> addAttribute(NodeTemplate source, String attributeType, String valueType, String attributeName, Increment idx, Integer x, Integer y) {
		Set<EdgeTemplate> attributeEdges = new HashSet<>();
		NodeTemplate attributeNode = typedEntityNode(attributeName, attributeType, false);
		attributeNode.setIdx(idx.next());
		attributeNode.setX(x);
		attributeNode.setY(y);
		NodeTemplate valueNode = literalNode("value", valueType);
		valueNode.setIdx(idx.next());
		valueNode.setFilterable(true);
		valueNode.setX(x+150);
		valueNode.setY(y);
		attributeEdges.add(new EdgeTemplate(source, attributeNode, SoVocab.has_quality.toString()));
		attributeEdges.add(new EdgeTemplate(attributeNode, valueNode, RDF.value.toString()));
		return attributeEdges;
	}
	
}
