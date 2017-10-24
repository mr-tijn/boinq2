package org.boinq.service;

import static org.boinq.domain.query.TemplateFactory.attributeNode;
import static org.boinq.domain.query.TemplateFactory.entityNode;
import static org.boinq.domain.query.TemplateFactory.literalNode;
import static org.boinq.domain.query.TemplateFactory.locationNode;
import static org.boinq.domain.query.TemplateFactory.typedEntityNode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.XSD;
import org.boinq.domain.query.EdgeTemplate;
import org.boinq.domain.query.GraphTemplate;
import org.boinq.domain.query.NodeTemplate;
import org.boinq.domain.query.QueryNode;
import org.boinq.repository.GraphTemplateRepository;
import org.springframework.stereotype.Service;

import org.boinq.generated.vocabularies.FaldoVocab;
import org.boinq.generated.vocabularies.FormatVocab;
import org.boinq.generated.vocabularies.GfvoVocab;
import org.boinq.generated.vocabularies.SoVocab;


@Service
public class GraphTemplateBuilderService {
	
	private static final long BED_TEMPLATE_ID = 1L;
	
	@Inject
	private GraphTemplateRepository graphTemplateRepo;
	
	private class Increment {
		// simple incrementer to be able to share state of counters
		// not really needed anymore
		private Integer count;
		public Increment(Integer init) {
			this.count = init; 
		}
		public Integer next() {
			return count++;
		}
	}
	
	private class Position {
		// simple helper for positioning nodes
		public int x;
		public int y;
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
		private int cartesianX(double r, double theta) {
			return (int) Math.round(r*Math.cos(2*Math.PI*theta/360.));
		}
		
		private int cartesianY(double r, double theta) {
			return (int) Math.round(r*Math.sin(2*Math.PI*theta/360.));
		}
		public void up(int offset) { y -= offset; }
		public void down(int offset) { y += offset; }
		public void left(int offset) { x -= offset; }
		public void right(int offset) { x += offset; }
		public void angle(int offset, double theta) {
			x += cartesianX(offset, theta);
			y += cartesianY(offset, theta);
		}
		public void move(int x, int y) { this.x = x; this.y = y; }
		public Position copy() { return new Position(this.x, this.y); }
	}
	
	public GraphTemplate fromGff(List<String> validTypes, String assembly) {
		GraphTemplate gffTemplate = new GraphTemplate();
		Set<EdgeTemplate> edgeTemplates = new HashSet<>();
		
		Increment idx = new Increment(0);
		Position pen = new Position(100, 100);
		
		NodeTemplate gffEntry = new NodeTemplate();
		gffEntry.setIdx(idx.next());
		gffEntry.setNodeType(QueryNode.NODETYPE_TYPEDENTITY);
		gffEntry.setFixedType(FormatVocab.GFF_Entry.getURI());
		gffEntry.setDescription("The entry describing the feature");
		gffEntry.setName("Entry");
		gffEntry.setVariablePrefix("entry");
		gffEntry.setFilterable(false);
		gffEntry.setColor("blue");
		gffEntry.setX(pen.x);
		gffEntry.setY(pen.y);
		
		pen.right(50);
		NodeTemplate gffFeature = new NodeTemplate();
		gffFeature.setIdx(idx.next());
		gffFeature.setNodeType(QueryNode.NODETYPE_GENERICENTITY);
		gffFeature.setDescription("Main (level 1) feature");
		gffFeature.setName("Feature");
		gffFeature.setVariablePrefix("mainFeature");
		gffFeature.setFilterable(false);
		gffFeature.setColor("blue");
		gffFeature.setX(pen.x);
		gffFeature.setY(pen.y);
		edgeTemplates.add(new EdgeTemplate(gffEntry, gffFeature, FormatVocab.defines.getURI(), "defines"));
		
		pen.up(50);
		NodeTemplate mainType = new NodeTemplate();
		mainType.setIdx(idx.next());
		mainType.setNodeType(QueryNode.NODETYPE_GENERICENTITY);
		mainType.setValueSource(NodeTemplate.SOURCE_LIST);
		mainType.setValuesTermList(validTypes);
		mainType.setDescription("Feature type");
		mainType.setName("Type");
		mainType.setDescription("Type of the main feature");
		mainType.setFilterable(true);
		mainType.setColor("blue");
		mainType.setX(pen.x);
		mainType.setY(pen.y);
		edgeTemplates.add(new EdgeTemplate(gffFeature, mainType, RDF.type.getURI(), "type"));

		pen.down(50);
		Position ref = pen.copy();
		pen.angle(50, 75);
		NodeTemplate mainLocation = locationNode(assembly);
		mainLocation.setX(pen.x);
		mainLocation.setY(pen.y);
		edgeTemplates.add(new EdgeTemplate(gffFeature,mainLocation,FaldoVocab.location.getURI(), "location"));
		
		pen = ref;
		pen.angle(50, 60);
		NodeTemplate parentFeature = new NodeTemplate();
		parentFeature.setIdx(idx.next());
		parentFeature.setNodeType(QueryNode.NODETYPE_GENERICENTITY);
		parentFeature.setDescription("Containing feature");
		parentFeature.setName("Feature");
		parentFeature.setVariablePrefix("containingFeature");
		parentFeature.setFilterable(false);
		parentFeature.setColor("blue");
		parentFeature.setX(pen.x);
		parentFeature.setY(pen.y);
		edgeTemplates.add(new EdgeTemplate(gffFeature, parentFeature, SoVocab.part_of.getURI(), "part of"));
		
		pen = ref;
		pen.angle(50, 45);
		NodeTemplate originFeature = new NodeTemplate();
		originFeature.setIdx(idx.next());
		originFeature.setNodeType(QueryNode.NODETYPE_GENERICENTITY);
		originFeature.setDescription("Origin feature");
		originFeature.setName("Feature");
		originFeature.setVariablePrefix("originFeature");
		originFeature.setFilterable(false);
		originFeature.setColor("blue");
		originFeature.setX(pen.x);
		originFeature.setY(pen.y);
		edgeTemplates.add(new EdgeTemplate(gffFeature, originFeature, SoVocab.derives_from.getURI(), "derives from"));
		
		
		pen = ref;
		pen.angle(50, 30);
		NodeTemplate circularLinear = entityNode("Circularity", true);
		circularLinear.setIdx(idx.next());
		circularLinear.setName("Circular or Linear");
		circularLinear.setDescription("This is either a linear or circular feature");
		circularLinear.setVariablePrefix("circularLinear");
		circularLinear.setX(pen.x);
		circularLinear.setY(pen.y);
		List<String> values = new LinkedList<>();
		values.add(SoVocab.linear.getURI());
		values.add(SoVocab.circular.getURI());
		circularLinear.setValueSource(NodeTemplate.SOURCE_LIST);
		circularLinear.setValuesTermList(values);
		edgeTemplates.add(new EdgeTemplate(gffFeature, circularLinear, RDF.type.getURI(), "type"));
		
		pen = ref;
		pen.angle(50, 15);
		NodeTemplate id = literalNode("id", XSD.xstring.getURI());
		id.setIdx(idx.next());
		id.setX(pen.x);
		id.setY(pen.y);
		edgeTemplates.add(new EdgeTemplate(gffFeature, id, DCTerms.identifier.getURI()));

		pen = ref;
		pen.angle(50, -15);
		NodeTemplate xref = entityNode("xref", false);
		xref.setIdx(idx.next());
		xref.setX(pen.x);
		xref.setY(pen.y);
		
		pen.right(50);
		NodeTemplate xref_source = literalNode("xrefSource", XSD.xstring.asNode());
		xref_source.setIdx(idx.next());
		xref_source.setX(pen.x);
		xref_source.setY(pen.y);
		
		pen.left(50);
		pen.angle(50, -30);
		NodeTemplate xref_value = literalNode("xrefValue", XSD.xstring.asNode());
		xref_value.setIdx(idx.next());
		xref_value.setX(pen.x);
		xref_value.setY(pen.y);
		
		edgeTemplates.add(new EdgeTemplate(gffFeature, xref, RDFS.seeAlso.getURI()));
		edgeTemplates.add(new EdgeTemplate(xref, xref_source, GfvoVocab.has_source.getURI()));
		edgeTemplates.add(new EdgeTemplate(xref, xref_value, RDF.value.getURI()));
		
		
		// attributes
		List<NodeTemplate> attributes = new LinkedList<>();
		attributes.add(attributeNode("name", RDFS.label.getURI(), XSD.xstring.getURI()));
		attributes.add(attributeNode("alias", SKOS.altLabel.getURI(), XSD.xstring.getURI()));
		attributes.add(attributeNode("note", RDFS.comment.getURI(), XSD.xstring.getURI()));
		pen = ref;
		double angle = -30;
		pen.angle(50, angle);
		for (NodeTemplate attribute: attributes) {
			attribute.setX(pen.x);
			attribute.setY(pen.y);
			pen = ref;
			angle -= 15;
			pen.angle(50, angle);
		}
		
		pen = ref;
		pen.down(50);
		NodeTemplate customAttribute = entityNode("Custom Attribute", "customAttribute", false);
		customAttribute.setX(pen.x);
		customAttribute.setY(pen.y);
		pen.down(50);
		NodeTemplate customAttributeType = entityNode("type", false);
		customAttributeType.setX(pen.x);
		customAttributeType.setY(pen.y);
		pen.up(50);
		ref = pen.copy();
		pen.angle(50, -60);
		NodeTemplate customAttributeLabel = literalNode("Type Label", XSD.xstring.getURI());
		customAttributeLabel.setX(pen.x);
		customAttributeLabel.setY(pen.y);
		pen = ref;
		pen.angle(50, -120);
		NodeTemplate customAttributeStringValue = literalNode("String value", XSD.xstring.getURI());
		customAttributeStringValue.setX(pen.x);
		customAttributeStringValue.setY(pen.y);
		edgeTemplates.add(new EdgeTemplate(gffFeature, customAttribute, GfvoVocab.has_attribute.getURI()));
		edgeTemplates.add(new EdgeTemplate(customAttribute, customAttributeType, RDF.type.getURI(), "type"));
		edgeTemplates.add(new EdgeTemplate(customAttribute, customAttributeStringValue, RDF.value.getURI(), "value"));
		edgeTemplates.add(new EdgeTemplate(customAttributeType, customAttributeLabel, RDFS.label.getURI(), "label"));

		
		// alignments
		pen.move(150, 300);
		NodeTemplate sourceFeature = entityNode("sourceFeature", false);
		sourceFeature.setIdx(idx.next());
		sourceFeature.setName("Source Feature");
		sourceFeature.setDescription("The source of the alignment");
		sourceFeature.setVariablePrefix("sourceFeature");
		sourceFeature.setX(pen.x);
		sourceFeature.setY(pen.y);
		
		pen.right(100);
		NodeTemplate targetFeature = entityNode("targetFeature", false);
		targetFeature.setIdx(idx.next());
		targetFeature.setName("Target Feature");
		targetFeature.setDescription("The target of the alignment");
		targetFeature.setVariablePrefix("targetFeature");
		targetFeature.setX(pen.x);
		targetFeature.setY(pen.y);
		
		pen.left(50);
		NodeTemplate alignment = typedEntityNode("alignment", GfvoVocab.Sequence_Alignment.getURI(), false);
		alignment.setIdx(idx.next());
		alignment.setX(pen.x);
		alignment.setY(pen.y);
		edgeTemplates.add(new EdgeTemplate(alignment, sourceFeature, GfvoVocab.has_source.getURI()));
		edgeTemplates.add(new EdgeTemplate(alignment, targetFeature, GfvoVocab.has_input.getURI()));
		
		pen.down(50);
		NodeTemplate element = entityNode("element", false);
		element.setIdx(idx.next());
		element.setX(pen.x);
		element.setY(pen.y);
		edgeTemplates.add(new EdgeTemplate(alignment, element, GfvoVocab.has_ordered_part.getURI()));
		
		pen.down(50);
		NodeTemplate elementType = entityNode("elementType", false);
		elementType.setIdx(idx.next());
		elementType.setX(pen.x);
		elementType.setY(pen.y);
		List<String> typeValues = new LinkedList<>();
		typeValues.add(GfvoVocab.Target_Sequence_Gap.getURI());
		typeValues.add(GfvoVocab.Reference_Sequence_Gap.getURI());
		typeValues.add(GfvoVocab.Match.getURI());
		elementType.setValueSource(NodeTemplate.SOURCE_LIST);
		edgeTemplates.add(new EdgeTemplate(element, elementType, RDF.type.getURI()));
		
		pen.up(50);
		pen.right(50);
		NodeTemplate span = attributeNode("span", XSD.integer.getURI(), GfvoVocab.Span.getURI());
		span.setIdx(idx.next());
		span.setX(pen.x);
		span.setY(pen.y);
		edgeTemplates.add(new EdgeTemplate(element, span, GfvoVocab.has_attribute.getURI()));
		
		
		gffTemplate.setEdgeTemplates(edgeTemplates);
		return gffTemplate;
	}
	
	public GraphTemplate fromBed(String mainType, String subType, String assembly) {
		GraphTemplate bedTemplate = new GraphTemplate();
		Set<EdgeTemplate> edges = new HashSet<>();
		
		Increment idx = new Increment(0);
		Position pen = new Position(150,150);
		
		NodeTemplate bedEntry = typedEntityNode("Entry", FormatVocab.BED_Entry.getURI(), false);
		bedEntry.setIdx(idx.next());
		bedEntry.setColor("blue");
		bedEntry.setDescription("The entry describing the feature and its subfeatures");
		bedEntry.setX(pen.x);
		bedEntry.setY(pen.y);
		Position entry = pen.copy();

		
		// bed feature attributes
		pen.down(50);
		pen.left(50);
		edges.add(addAttribute(bedEntry, FormatVocab.RGBred.getURI(), XSD.integer.getURI(), "RGB red", idx, pen.x, pen.y));
		pen.right(50);
		edges.add(addAttribute(bedEntry, FormatVocab.RGBgreen.getURI(), XSD.integer.getURI(), "RGB green", idx, pen.x, pen.y));
		pen.right(50);
		edges.add(addAttribute(bedEntry, FormatVocab.RGBblue.getURI(), XSD.integer.getURI(), "RGB blue", idx, pen.x, pen.y));

		
		pen = entry;
		pen.right(150);
		Position main = pen.copy();
		NodeTemplate mainFeature = entityNode("Main feature", "mainFeature", false);
		mainFeature.setIdx(idx.next());
		mainFeature.setColor("blue");
		mainFeature.setDescription("The main feature");
		mainFeature.setX(pen.x);
		mainFeature.setY(pen.y);
		edges.add(new EdgeTemplate(bedEntry, mainFeature, FormatVocab.defines.getURI()));

		pen.right(150);
		NodeTemplate subFeature = entityNode("Subfeature", "subFeature", false);
		subFeature.setIdx(idx.next());
		subFeature.setColor("blue");
		subFeature.setDescription("Subfeature");
		subFeature.setX(pen.x);
		subFeature.setY(pen.y);
		edges.add(new EdgeTemplate(mainFeature, subFeature, SoVocab.has_part.getURI()));
		
		pen = main;
		pen.down(100);
		NodeTemplate mainId = literalNode("Identifier", XSD.xstring.asNode());
		mainId.setIdx(idx.next());
		mainId.setVariablePrefix("mainId");
		mainId.setFilterable(true);
		mainId.setX(pen.x);
		mainId.setY(pen.y);
		edges.add(new EdgeTemplate(mainFeature, mainId, DCTerms.identifier.getURI()));
		
		pen.right(100);
		NodeTemplate mainLabel = literalNode("Label", XSD.xstring.asNode());
		mainLabel.setIdx(idx.next());
		mainLabel.setVariablePrefix("mainLabel");
		mainLabel.setFilterable(true);
		mainLabel.setX(pen.x);
		mainLabel.setY(pen.y);
		edges.add(new EdgeTemplate(mainFeature, mainLabel, RDFS.label.getURI()));
		
		pen.left(200);
		NodeTemplate mainDescription = literalNode("Description", XSD.xstring.asNode());
		mainDescription.setIdx(idx.next());
		mainDescription.setVariablePrefix("mainDescription");
		mainDescription.setFilterable(true);
		mainDescription.setX(pen.x);
		mainDescription.setY(pen.y);
		edges.add(new EdgeTemplate(mainFeature, mainDescription, DCTerms.description.getURI()));
		
		pen.right(100);
		pen.up(150);
		NodeTemplate mainLocation = locationNode(assembly);
		mainLocation.setIdx(idx.next());
		mainLocation.setName("Location");
		mainLocation.setVariablePrefix("mainLocation");
		mainLocation.setX(pen.x);
		mainLocation.setY(pen.y);
		edges.add(new EdgeTemplate(mainFeature, mainLocation, FaldoVocab.location.getURI()));

		pen.left(100);
		edges.add(addAttribute(mainFeature, SoVocab.score.getURI(), XSD.xdouble.getURI(), "Score", idx, pen.x, pen.y));

		pen.right(200);
		List<String> possibleTypes = new LinkedList<>();
		if (mainType != null && mainType.length() > 0) {
			possibleTypes.add(mainType);
		}
		if (subType != null && subType.length() > 0) {
			possibleTypes.add(subType);
		}
		NodeTemplate featureType = entityNode("Feature Type", "featureType", true);
		featureType.setIdx(idx.next());
		featureType.setX(pen.x);
		featureType.setY(pen.y);
		featureType.setValueSource(NodeTemplate.SOURCE_LIST);
		featureType.setValuesTermList(possibleTypes);
		edges.add(new EdgeTemplate(mainFeature, featureType, RDF.type.getURI()));
		
		bedTemplate.setEdgeTemplates(edges);
		return bedTemplate;
	}
		

	private EdgeTemplate addAttribute(NodeTemplate source, String attributeType, String valueType, String attributeName, Increment idx, Integer x, Integer y) {
		NodeTemplate attributeNode = attributeNode(attributeName, attributeType, valueType);
		attributeNode.setX(x);
		attributeNode.setY(y);
		attributeNode.setIdx(idx.next());
		return new EdgeTemplate(source, attributeNode, SoVocab.has_quality.getURI());
	}
	
}
