package org.boinq.service;

import static org.boinq.domain.query.TemplateFactory.attributeNode;
import static org.boinq.domain.query.TemplateFactory.entityNode;
import static org.boinq.domain.query.TemplateFactory.link;
import static org.boinq.domain.query.TemplateFactory.literalNode;
import static org.boinq.domain.query.TemplateFactory.locationNode;
import static org.boinq.domain.query.TemplateFactory.typedEntityNode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jena.graph.Node;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.XSD;
import org.boinq.domain.Track;
import org.boinq.domain.jobs.TripleConversion.Metadata;
import org.boinq.domain.query.EdgeTemplate;
import org.boinq.domain.query.GraphTemplate;
import org.boinq.domain.query.NodeTemplate;
import org.boinq.domain.query.TemplateFactory.Increment;
import org.boinq.domain.query.TemplateFactory.Position;
import org.boinq.generated.vocabularies.FaldoVocab;
import org.boinq.generated.vocabularies.FormatVocab;
import org.boinq.generated.vocabularies.GfvoVocab;
import org.boinq.generated.vocabularies.SoVocab;
import org.boinq.tools.fileformats.VCFTripleIterator;
import org.springframework.stereotype.Service;


@Service
public class GraphTemplateBuilderService {	
	
	public GraphTemplate fromFile(Metadata meta, Track track) {
		if (track.getType().equals(Track.TYPE_FEATURE)) {
			switch (track.getFileType().toUpperCase()) {
			case "BED":
				return fromBed(meta.mainType.getURI(), meta.subType.getURI(), track.getAssembly());
			case "GFF":
				return fromGff(meta.typeList.stream().map(Node::getURI).collect(Collectors.toList()), track.getAssembly());
			case "VCF":
				return fromVCF(meta, track.getAssembly());
			}
		}
		return null;
	}
	
	public GraphTemplate fromGff(List<String> validTypes, String assembly) {
		GraphTemplate gffTemplate = new GraphTemplate();
		Set<EdgeTemplate> edgeTemplates = new HashSet<>();
		
		Increment idx = new Increment(0);
		Position pen = new Position(150, 150);
		
		NodeTemplate gffEntry = typedEntityNode("Entry", FormatVocab.GFF_Entry.getURI(), false, pen, idx);
		gffEntry.setDescription("The entry describing the feature");
		
		pen.right(150);
		NodeTemplate gffFeature = entityNode("Feature", "mainFeature", false, pen, idx);
		gffFeature.setDescription("Main (level 1) feature");
		edgeTemplates.add(link(gffEntry, gffFeature, FormatVocab.defines.getURI()));
		
		pen.down(50);
		Position main = pen.copy();
		NodeTemplate mainType = entityNode("Type", "featureType", true, pen, idx);
		mainType.setValueSource(NodeTemplate.SOURCE_LIST);
		mainType.setValuesTermList(validTypes);
		mainType.setDescription("Type of the main feature");
		edgeTemplates.add(link(gffFeature, mainType, RDF.type.getURI(), "type"));

		pen.to(main);
		pen.down(50);
		pen.right(50);
		NodeTemplate mainLocation = locationNode(assembly, pen, idx);
		mainLocation.setX(pen.x);
		mainLocation.setY(pen.y);
		mainLocation.setIdx(idx.next());
		edgeTemplates.add(link(gffFeature,mainLocation,FaldoVocab.location.getURI(), "location"));
		
		pen.to(main);
		pen.down(100);
		pen.left(50);
		NodeTemplate parentFeature = entityNode("Feature", "containingFeature", false, pen, idx);
		parentFeature.setDescription("Containing feature");
		edgeTemplates.add(link(gffFeature, parentFeature, SoVocab.part_of.getURI(), "part of"));
		
		pen.right(100);
		NodeTemplate originFeature = entityNode("Feature", "originFeature", false, pen, idx);
		originFeature.setDescription("Origin feature");
		edgeTemplates.add(link(gffFeature, originFeature, SoVocab.derives_from.getURI(), "derives from"));
		
		
		pen.to(main);
		pen.down(50);
		pen.left(50);
		NodeTemplate id = literalNode("id", XSD.xstring.getURI(), pen, idx);
		edgeTemplates.add(link(gffFeature, id, DCTerms.identifier.getURI()));
		
		pen.up(100);
		NodeTemplate circularLinear = entityNode("Circularity", "circularity",true, pen, idx);
		circularLinear.setDescription("This is either a linear or circular feature");
		List<String> values = new LinkedList<>();
		values.add(SoVocab.linear.getURI());
		values.add(SoVocab.circular.getURI());
		circularLinear.setValueSource(NodeTemplate.SOURCE_LIST);
		circularLinear.setValuesTermList(values);
		edgeTemplates.add(link(gffFeature, circularLinear, RDF.type.getURI(), "type"));
		

		pen.up(50);;
		NodeTemplate xref = entityNode("xref", "xref", false, pen, idx);
		edgeTemplates.add(link(gffFeature, xref, RDFS.seeAlso.getURI()));
		
		pen.left(50);
		pen.down(25);
		NodeTemplate xref_source = literalNode("xrefSource", XSD.xstring.asNode(), pen, idx);
		edgeTemplates.add(link(xref, xref_source, GfvoVocab.has_source.getURI()));
		
		pen.up(50);
		NodeTemplate xref_value = literalNode("xrefValue", XSD.xstring.asNode(), pen, idx);
		edgeTemplates.add(link(xref, xref_value, RDF.value.getURI()));
		
		// attributes
		List<NodeTemplate> attributes = new LinkedList<>();
		NodeTemplate label = literalNode("label", XSD.xstring.asNode(), pen, idx);
		attributes.add(label);
		edgeTemplates.add(link(gffFeature, label, RDFS.label.getURI()));
		NodeTemplate alias = literalNode("alias", XSD.xstring.asNode(), pen, idx);
		attributes.add(alias);
		edgeTemplates.add(link(gffFeature, alias, SKOS.altLabel.getURI()));
		NodeTemplate note = literalNode("note", XSD.xstring.asNode(), pen, idx);
		attributes.add(note);
		edgeTemplates.add(link(gffFeature, note, RDFS.comment.getURI()));
		pen.to(main);
		double angle = 30;
		pen.angle(50, angle);
		for (NodeTemplate attribute: attributes) {
			attribute.setX(pen.x);
			attribute.setY(pen.y);
			pen.to(main);
			angle += 15;
			pen.angle(50, angle);
		}
		
		pen.to(main);
		pen.right(100);
		NodeTemplate customAttribute = entityNode("Custom Attribute", "customAttribute", false, pen, idx);
		edgeTemplates.add(link(gffFeature, customAttribute, GfvoVocab.has_attribute.getURI()));
		pen.down(50);
		NodeTemplate customAttributeType = entityNode("type", "attributeType", false, pen, idx);
		edgeTemplates.add(link(customAttribute, customAttributeType, RDF.type.getURI(), "type"));
		pen.down(50);
		NodeTemplate customAttributeLabel = literalNode("Type Label", XSD.xstring.getURI(), pen, idx);
		edgeTemplates.add(link(customAttributeType, customAttributeLabel, RDFS.label.getURI(), "label"));
		pen.up(100);
		pen.right(50);
		NodeTemplate customAttributeStringValue = literalNode("String value", XSD.xstring.getURI(), pen, idx);
		edgeTemplates.add(link(customAttribute, customAttributeStringValue, RDF.value.getURI(), "value"));

		
		
		// alignments
		pen.move(150, 300);
		NodeTemplate sourceFeature = entityNode("Source Feature","sourceFeature", false, pen, idx);
		sourceFeature.setDescription("The source of the alignment");
		
		pen.right(100);
		NodeTemplate targetFeature = entityNode("Target Feature", "targetFeature", false, pen, idx);
		targetFeature.setDescription("The target of the alignment");
		
		pen.left(50);
		NodeTemplate alignment = typedEntityNode("alignment", GfvoVocab.Sequence_Alignment.getURI(), false, pen, idx);
		edgeTemplates.add(link(alignment, sourceFeature, GfvoVocab.has_source.getURI()));
		edgeTemplates.add(link(alignment, targetFeature, GfvoVocab.has_input.getURI()));
		
		pen.up(50);
		NodeTemplate alignmentlocation = locationNode(assembly, pen, idx);
		alignmentlocation.setIdx(idx.next());
		alignmentlocation.setX(pen.x);
		alignmentlocation.setY(pen.y);
		edgeTemplates.add(link(alignment, alignmentlocation, FaldoVocab.location.getURI()));
		
		pen.down(100);
		NodeTemplate element = entityNode("element", "element", false, pen, idx);
		edgeTemplates.add(link(alignment, element, GfvoVocab.has_ordered_part.getURI()));
		
		pen.down(50);
		NodeTemplate elementType = entityNode("type", "elementType", false, pen, idx);
		List<String> typeValues = new LinkedList<>();
		typeValues.add(GfvoVocab.Target_Sequence_Gap.getURI());
		typeValues.add(GfvoVocab.Reference_Sequence_Gap.getURI());
		typeValues.add(GfvoVocab.Match.getURI());
		elementType.setValueSource(NodeTemplate.SOURCE_LIST);
		edgeTemplates.add(link(element, elementType, RDF.type.getURI()));
		
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
		
		NodeTemplate bedEntry = typedEntityNode("Entry", FormatVocab.BED_Entry.getURI(), false, pen, idx);
		bedEntry.setDescription("The entry describing the feature and its subfeatures");
		Position entry = pen.copy();
		
		// bed feature attributes
		pen.down(50);
		pen.left(50);
		edges.add(addAttribute(bedEntry, FormatVocab.RGBred.getURI(), XSD.integer.getURI(), "RGB red", idx, pen.x, pen.y));
		pen.right(50);
		edges.add(addAttribute(bedEntry, FormatVocab.RGBgreen.getURI(), XSD.integer.getURI(), "RGB green", idx, pen.x, pen.y));
		pen.right(50);
		edges.add(addAttribute(bedEntry, FormatVocab.RGBblue.getURI(), XSD.integer.getURI(), "RGB blue", idx, pen.x, pen.y));

		
		pen.to(entry);
		pen.right(150);
		Position main = pen.copy();
		NodeTemplate mainFeature = entityNode("Main feature", "mainFeature", false, pen, idx);
		mainFeature.setDescription("The main feature");
		edges.add(link(bedEntry, mainFeature, FormatVocab.defines.getURI()));

		pen.right(150);
		NodeTemplate subFeature = entityNode("Subfeature", "subFeature", false, pen, idx);
		subFeature.setDescription("Subfeature. For filtering, link to main feature on other graph node.");
		edges.add(link(mainFeature, subFeature, SoVocab.has_part.getURI()));
		
		pen.to(main);
		pen.down(100);
		NodeTemplate mainId = literalNode("Identifier", XSD.xstring.asNode(), pen, idx);
		mainId.setVariablePrefix("mainId");
		edges.add(link(mainFeature, mainId, DCTerms.identifier.getURI()));
		
		pen.right(100);
		NodeTemplate mainLabel = literalNode("Label", XSD.xstring.asNode(), pen, idx);
		mainLabel.setVariablePrefix("mainLabel");
		mainLabel.setFilterable(true);
		edges.add(link(mainFeature, mainLabel, RDFS.label.getURI()));
		
		pen.left(200);
		NodeTemplate mainDescription = literalNode("Description", XSD.xstring.asNode(), pen, idx);
		mainDescription.setVariablePrefix("mainDescription");
		mainDescription.setFilterable(true);
		edges.add(link(mainFeature, mainDescription, DCTerms.description.getURI()));
		
		pen.right(100);
		pen.up(150);
		NodeTemplate mainLocation = locationNode(assembly, pen, idx);
		mainLocation.setIdx(idx.next());
		mainLocation.setName("Location");
		mainLocation.setVariablePrefix("mainLocation");
		mainLocation.setX(pen.x);
		mainLocation.setY(pen.y);
		edges.add(link(mainFeature, mainLocation, FaldoVocab.location.getURI()));

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
		NodeTemplate featureType = entityNode("Feature Type", "featureType", true, pen, idx);
		featureType.setValueSource(NodeTemplate.SOURCE_LIST);
		featureType.setValuesTermList(possibleTypes);
		edges.add(new EdgeTemplate(mainFeature, featureType, RDF.type.getURI()));
		
		bedTemplate.setEdgeTemplates(edges);
		return bedTemplate;
	}
		
	public GraphTemplate fromVCF(Metadata meta, String assembly) {
		GraphTemplate vcfTemplate = new GraphTemplate();
		Set<EdgeTemplate> edges = new HashSet<>();
		
		Increment idx = new Increment(0);
		Position pen = new Position(150,300);
		
		NodeTemplate vcfEntry = typedEntityNode("Entry", FormatVocab.VCF_Entry.getURI(), false, pen, idx);
		vcfEntry.setDescription("The entry describing the feature and its subfeatures");

		pen.right(150);
		Position main = pen.copy();
		NodeTemplate mainFeature = entityNode("Main feature", "mainFeature", false, pen, idx);
		mainFeature.setDescription("The main feature");
		edges.add(link(vcfEntry, mainFeature, FormatVocab.defines.getURI()));
		
		pen.left(50);
		pen.down(50);
		NodeTemplate mainId = literalNode("Identifier", XSD.xstring.asNode(), pen, idx);
		mainId.setVariablePrefix("mainId");
		mainId.setFilterable(true);
		edges.add(link(mainFeature, mainId, DCTerms.identifier.getURI()));

		pen.up(100);
		NodeTemplate filter = typedEntityNode("Filter", GfvoVocab.Variant_Calling.getURI(), false, pen, idx);
		//todo: annotate filter from header info
		filter.setFilterable(false);
		edges.add(link(mainFeature, filter, GfvoVocab.is_refuted_by.getURI()));
		
		pen.up(50);
		NodeTemplate filterLabel = literalNode("Label", XSD.xstring.asNode(), pen, idx);
		filterLabel.setVariablePrefix("filterLabel");
		edges.add(link(filter, filterLabel, RDFS.label.getURI()));
		pen.down(50);
		pen.left(50);
		NodeTemplate filterDescription = literalNode("Description", XSD.xstring.asNode(), pen, idx);
		filterLabel.setVariablePrefix("filterDescription");
		edges.add(link(filter, filterDescription, RDFS.comment.getURI()));
		
		pen.to(main);
		pen.down(100);
		pen.left(50);
		NodeTemplate reference = attributeNode("RefSequence", GfvoVocab.Reference_Sequence.getURI(), XSD.xstring.getURI(), pen, idx);
		edges.add(link(mainFeature, reference, GfvoVocab.has_attribute.getURI()));
		
		pen.right(50);
		NodeTemplate variantType = entityNode("Type","variantType",true,pen,idx);
		List<String> variantTypes = new LinkedList<>();
		variantTypes.add(SoVocab.indel.getURI());
		variantTypes.add(SoVocab.insertion.getURI());
		variantTypes.add(SoVocab.deletion.getURI());
		variantTypes.add(SoVocab.MNP.getURI());
		variantTypes.add(SoVocab.nucleotide_match.getURI());
		variantTypes.add(SoVocab.SNP.getURI());
		variantType.setValueSource(NodeTemplate.SOURCE_LIST);
		variantType.setValuesTermList(variantTypes);
		edges.add(link(mainFeature, variantType, RDF.type.getURI()));

		pen.right(50);
		Position var = pen.copy();
		NodeTemplate variant = attributeNode("Variant", GfvoVocab.Sequence_Variant.getURI(), XSD.xstring.getURI(), pen, idx);
		edges.add(link(mainFeature, variant, GfvoVocab.has_attribute.getURI()));
		
		pen.down(50);
		NodeTemplate variantScore = attributeNode("Phred Score", GfvoVocab.Phred_Score.getURI(), XSD.xdouble.getURI(), pen, idx);
		edges.add(link(variant, variantScore, GfvoVocab.has_attribute.getURI()));
		
		pen.to(main);
		pen.down(50);
		pen.right(50);
		NodeTemplate location = locationNode(assembly, pen, idx);
		edges.add(link(mainFeature, location, FaldoVocab.location.toString()));

		int step = 105/(1+meta.attributeTypes.size());
		int angle = 30;
		int off = -1;
		for (String attr : meta.attributeTypes.keySet()) {
			pen.to(var);
			pen.angle(100 + 25*off, angle);
			NodeTemplate attribute = attributeNode(attr,  meta.attributeTypes.get(attr).getURI(), meta.attributeValueTypes.get(attr).getURI(), pen, idx);
			edges.add(link(variant, attribute, GfvoVocab.has_attribute.getURI()));
			angle += step;
			off *= -1;
		}
		pen.to(var);
		pen.angle(105 + 25*off, angle);
		NodeTemplate freeAttribute = entityNode("Attribute", "otherAttribute", false, pen, idx);
		edges.add(link(variant, freeAttribute, GfvoVocab.has_attribute.getURI()));
		Position ref = pen;
		pen.angle(50, angle + 20);
		NodeTemplate attributeType = entityNode("Type", "attributeType", false, pen, idx);
		edges.add(link(freeAttribute, attributeType, RDF.type.getURI()));
		pen.angle(50, angle + 20);
		NodeTemplate attributeTypeLabel = literalNode("Label", XSD.xstring.asNode(), pen, idx);
		attributeTypeLabel.setVariablePrefix("attributeTypeLabel");
		edges.add(link(attributeType, attributeTypeLabel, RDFS.label.getURI()));
		pen.to(ref);
		pen.angle(50, angle - 20);
		NodeTemplate attributeValue = literalNode("Value", XSD.xstring.asNode(), pen, idx); 
		attributeValue.setVariablePrefix("otherAttributeValue");
		edges.add(link(freeAttribute, attributeValue, RDF.value.getURI()));
				
		pen.to(main);
		pen.right(150);
		Position ev = pen.copy();
		NodeTemplate evidence = entityNode("Evidence", "evidence", false, pen, idx);
		edges.add(link(mainFeature, evidence, GfvoVocab.has_evidence.getURI()));
		
		pen.to(ev);
		pen.up(50);
		NodeTemplate sample = entityNode("Sample", "sample", false, pen, idx);
		edges.add(link(evidence, sample, GfvoVocab.has_source.getURI()));
		pen.down(100);
		NodeTemplate zygosity = entityNode("Zygosity", "zygosity", true, pen, idx);
		List<String> zygosities = new LinkedList<>();
		zygosities.add(GfvoVocab.Homozygous.getURI());
		zygosities.add(GfvoVocab.Heterozygous.getURI());
		zygosity.setValuesTermList(zygosities);
		zygosity.setValueSource(NodeTemplate.SOURCE_LIST);
		edges.add(link(evidence, zygosity, GfvoVocab.has_quality.getURI()));
		
		pen.to(ev);
		pen.right(150);
		Position gt = pen.copy();
		NodeTemplate genotype = typedEntityNode("Genotype", GfvoVocab.Genotype.getURI(), false, pen, idx);
		edges.add(link(evidence, genotype, GfvoVocab.has_attribute.getURI()));
		
		pen.to(gt);
		pen.down(100);
		pen.left(50);
		NodeTemplate gtReference = attributeNode("Reference", GfvoVocab.Reference_Sequence.getURI(), XSD.xstring.getURI(), pen, idx);
		gtReference.setVariablePrefix("genotypeReference");
		edges.add(link(genotype, gtReference, GfvoVocab.has_attribute.getURI()));
		
		pen.right(100);
		NodeTemplate gtVariant = attributeNode("Variant", GfvoVocab.Sequence_Variant.getURI(), XSD.xstring.getURI(), pen, idx);
		gtVariant.setVariablePrefix("genotypeVariant");
		edges.add(link(genotype, gtVariant, GfvoVocab.has_attribute.getURI()));
		
		pen.up(50);
		NodeTemplate gtFilter = typedEntityNode("Filter", GfvoVocab.Variant_Calling.getURI(), false, pen, idx);
		gtFilter.setVariablePrefix("genotypeFilter");
		edges.add(link(genotype, gtFilter, GfvoVocab.is_refuted_by.getURI()));
		
		pen.right(50);
		NodeTemplate gtFilterLabel = literalNode("Label", XSD.xstring.getURI(), pen, idx);
		gtFilterLabel.setVariablePrefix("genotypeFilterLabel");
		edges.add(link(gtFilter, gtFilterLabel, RDFS.label.getURI()));
		
		pen.to(gt);
		pen.left(50);
		pen.up(50);
		NodeTemplate gq = attributeNode("Phred score", GfvoVocab.Conditional_Genotype_Quality.getURI(), XSD.xstring.getURI(), pen, idx);
		gq.setVariablePrefix("genotypeQuality");
		edges.add(link(genotype, gq, GfvoVocab.has_attribute.getURI()));
		
		pen.right(50);
		NodeTemplate dp = attributeNode("depth", GfvoVocab.Coverage.getURI(), XSD.xstring.getURI(), pen, idx);
		dp.setVariablePrefix("genotypeCoverage");
		edges.add(link(genotype, dp, GfvoVocab.has_attribute.getURI()));
		
		
		vcfTemplate.setEdgeTemplates(edges);
		return vcfTemplate;
	}
	
	
	private EdgeTemplate addAttribute(NodeTemplate source, String attributeType, String valueType, String attributeName, Increment idx, Integer x, Integer y) {
		NodeTemplate attributeNode = attributeNode(attributeName, attributeType, valueType);
		attributeNode.setX(x);
		attributeNode.setY(y);
		attributeNode.setIdx(idx.next());
		return new EdgeTemplate(source, attributeNode, SoVocab.has_quality.getURI());
	}
	
}
