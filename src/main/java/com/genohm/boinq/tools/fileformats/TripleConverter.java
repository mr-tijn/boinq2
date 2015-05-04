package com.genohm.boinq.tools.fileformats;


import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.faldo.FaldoFeature;
import com.genohm.boinq.service.TripleGeneratorService;
import com.genohm.boinq.tools.vocabularies.FaldoVocabulary;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import edu.unc.genomics.BedEntry;
import edu.unc.genomics.GFFEntry;
import edu.unc.genomics.ValuedInterval;

@Service
public class TripleConverter {
	// genohm.com / bed-gff-bedgraph??? / feature ------> datasource 

	// all variable names that are not included in the valuedInterval variables keep their original variable names
	// the variable names that are included in the valuedInterval are given a more generic name

	// vInt static finals
	public static final String FEATUREBASEURI ="/feature#";
	public static final String FEATUREBEGINURI = "feature_begin";
	public static final String FEATUREENDURI = "feature_end";
	public static final String CHRBASEURI = "http://www.genohm.com/chr";
	public static final String VALUEURI = "http://www.genohm.com/value";

	
	// todo: manage these terms through ontology
	// gff static finals
	public static final String SOURCEURI = "http://www.genohm.com/datasource/source";
	public static final String ATTRIBUTEURI ="http://www.genohm.com/datasource/attribute";
	public static final String FRAMEURI = "http://www.genohm.com/datasource/frame";
	public static final String TYPEURI = "http://www.genohm.com/datasource/type";


	// bed static finals
	public static final String NAMEURI = "http://www.genohm.com/datasource/name";
	public static final String THICKSTARTURI = "http://www.genohm.com/datasource/thickstart";
	public static final String THICKENDURI = "http://www.genohm.com/datasource/thickend";
	public static final String ITEMRBGURI = "http://www.genohm.com/datasource/itemrbg";
	public static final String BLOCKBASEURI = "http://www.genohm.com/datasource/block#";
	public static final String BLOCKURI = "http://www.genohm.com/datasource/block";
	public static final String BLOCKCOUNTURI = "http://www.genohm.com/datasource/blockcount";
	public static final String BLOCKSIZEURI = "http://www.genohm.com/datasource/blocksize";
	public static final String BLOCKSTARTURI = "http://www.genohm.com/datasource/blockstart";
	private static int blockCounter = 0;
	private static String chrValue;

	@Inject
	TripleGeneratorService tripleGenerator;

	public List<Triple> convert(ValuedInterval vInt, String id) {

		List<Triple> result = new LinkedList<Triple>();
		// feature is the subject of all following triples except the FALDO exact position triples
		String featureName = FEATUREBASEURI + id;
		Node feature = tripleGenerator.generateURI(featureName);

		// TODO: check chr input for "chr#", or just "#" (if chr.startwith("chr")){ 	}
		Node chrURI = NodeFactory.createURI(CHRBASEURI);

		if(vInt.getChr().startsWith("chr")){
			chrValue = vInt.getChr().substring(3);
		}else{
			chrValue = vInt.getChr();
		}
		Node chr = NodeFactory.createLiteral(String.valueOf(chrValue), new XSDDatatype("string"));
		result.add(new Triple(feature, chrURI, chr));

		Node featureBegin = tripleGenerator.generateURI(FEATUREBEGINURI + id);
		Node featureEnd = tripleGenerator.generateURI(FEATUREENDURI + id);
		result.add(new Triple(feature, FaldoVocabulary.begin, featureBegin));
		result.add(new Triple(feature, FaldoVocabulary.end, featureEnd));
		result.add(new Triple(featureBegin, FaldoVocabulary.position,NodeFactory.createLiteral(String.valueOf(Math.min(vInt.getStart(),vInt.getStop())),new XSDDatatype("int"))));
		result.add(new Triple(featureEnd, FaldoVocabulary.position,NodeFactory.createLiteral(String.valueOf(Math.max(vInt.getStart(),vInt.getStop())),new XSDDatatype("int"))));

		if (vInt.getStart() <= vInt.getStop()) {
			result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocabulary.ForwardStrandPosition));			
			result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocabulary.ForwardStrandPosition));			
		} else {
			result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocabulary.ReverseStrandPosition));
			result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocabulary.ReverseStrandPosition));

		}

		if (vInt.getValue()!=null){
			Node valueURI = NodeFactory.createURI(VALUEURI);
			Node value = NodeFactory.createLiteral(String.valueOf(vInt.getValue()));
			result.add(new Triple(feature, valueURI, value));
		}

		return result;
	}

	// BedGraph kan volledig geconverteerd worden via vInt

	//		public static List<Triple> convert(BedGraphEntry entry){
	//			
	//			List<Triple> result = convert((ValuedInterval) entry);
	//			Node feature = NodeFactory.createURI(FEATUREBASEURI + entry.getId());
	//			
	//			return result;
	//			
	//		}

	public List<Triple> convert(GFFEntry entry, String id) {
		List<Triple> result = convert((ValuedInterval) entry, id);
		Node feature = tripleGenerator.generateURI(FEATUREBASEURI + id);

		if(entry.getSource() != null && !entry.getSource().isEmpty() && !entry.getSource().equalsIgnoreCase(".")){
			Node sourceURI = NodeFactory.createURI(SOURCEURI);
			Node source = NodeFactory.createLiteral(String.valueOf(entry.getSource()), new XSDDatatype("string"));
			result.add(new Triple(feature, sourceURI, source));
		}
		if(entry.getFrame() != null && !entry.getFrame().isEmpty() && !entry.getFrame().equalsIgnoreCase(".")){
			Node frameURI = NodeFactory.createURI(FRAMEURI);
			Node frame = NodeFactory.createLiteral(String.valueOf(entry.getFrame()), new XSDDatatype("int"));
			result.add(new Triple(feature, frameURI,frame));
		}
		if(entry.getFeature()!=null && !entry.getFeature().isEmpty() && !entry.getFeature().equalsIgnoreCase(".")){
			Node typeURI = NodeFactory.createURI(TYPEURI);
			Node type = NodeFactory.createLiteral(String.valueOf(entry.getFeature()),new XSDDatatype("string"));
			result.add(new Triple(feature, typeURI, type));
		}


		// notes 
//		if (entry.getNotes() != null) {
//			List<String> noteValues;
//			String[] notes = entry.getNotes();
//			for (String noteKVpair : notes) {
//				noteValues = new ArrayList<String>();
//				String[] noteKeySplitValue = noteKVpair.split("=");
//				String noteKeys = (noteKeySplitValue[0]);
//				String[] noteValue = noteKeySplitValue[1].split(",");
//				Node attributeURIkey = NodeFactory.createURI(ATTRIBUTEURI+noteKeys);
//				for (String oneValue : noteValue) {
//					Node attributeValue = NodeFactory.createLiteral(oneValue);
//					noteValues.add(oneValue);
//					result.add(new Triple(feature, attributeURIkey, attributeValue));
//				}
//			}
//		}


		return result;
	}

	public List<Triple> convert(BedEntry entry, String id) {
		List<Triple> result = convert((ValuedInterval) entry, id);
		Node feature = tripleGenerator.generateURI(FEATUREBASEURI + entry.getId());
		String scoreString = String.valueOf(entry.getValue());
		if(scoreString != null && !scoreString.isEmpty()){
			Node featureScoreURI = NodeFactory.createURI(feature + "Value");
			result.add(new Triple(feature, featureScoreURI, NodeFactory.createLiteral(String.valueOf(entry.getValue()),new XSDDatatype("double"))));
		}

		if(entry.getId()!=null && !entry.getId().isEmpty()){
			Node nameURI = NodeFactory.createURI(NAMEURI);
			Node name = NodeFactory.createLiteral(String.valueOf(entry.getId()), new XSDDatatype("string"));
			result.add(new Triple(feature, nameURI, name));
		}
		if(String.valueOf(entry.getThickStart())!= null && !String.valueOf(entry.getThickStart()).isEmpty()){
			Node thickStartURI = NodeFactory.createURI(THICKSTARTURI);
			Node thickStart = NodeFactory.createLiteral(String.valueOf((entry.getThickStart())), new XSDDatatype("int"));
			result.add(new Triple(feature, thickStartURI, thickStart));
		}
		if(String.valueOf(entry.getThickEnd())!=null && !String.valueOf(entry.getThickEnd()).isEmpty()){
			Node thickEndURI = NodeFactory.createURI(THICKENDURI);
			Node thickEnd = NodeFactory.createLiteral(String.valueOf(entry.getThickEnd()), new XSDDatatype("int"));
			result.add(new Triple(feature, thickEndURI, thickEnd));
		}

		// BlokCount, BlockSizes, BlockStarts
		Node featureBlock = NodeFactory.createLiteral(BLOCKBASEURI + id);
		Node blockURI = NodeFactory.createLiteral(BLOCKURI);
		result.add(new Triple(feature, blockURI, featureBlock));

		if (String.valueOf(entry.getBlockCount()) != null
				&& !String.valueOf(entry.getBlockCount()).isEmpty()) {
			Node blockCountURI = NodeFactory.createURI(BLOCKCOUNTURI);
			Node blockCount = NodeFactory.createLiteral(String.valueOf(entry
					.getBlockCount()), new XSDDatatype("int"));
			result.add(new Triple(featureBlock, blockCountURI, blockCount));
		}
		if (entry.getBlockSizes() != null
				&& !(entry.getBlockSizes().length == 0)
				&& entry.getBlockStarts() != null
				&& !(entry.getBlockStarts().length == 0)) {
			Node blockStartURI = NodeFactory.createURI(BLOCKSTARTURI);
			Node blockSizeURI = NodeFactory.createURI(BLOCKSIZEURI);
			for (blockCounter = 1; blockCounter < entry.getBlockCount(); blockCounter++) {
				int[] blockSizes = entry.getBlockSizes();
				Node blockSize = NodeFactory.createLiteral(
						String.valueOf(blockSizes[blockCounter]),
						new XSDDatatype("int"));
				result.add(new Triple(featureBlock, blockSizeURI, blockSize));

				int[] blockStarts = entry.getBlockStarts();
				Node blockStart = NodeFactory.createLiteral(
						String.valueOf(blockStarts[blockCounter]),
						new XSDDatatype("int"));
				result.add(new Triple(featureBlock, blockStartURI, blockStart));
			}

		}

		return result;
	}	
	
	public List<Triple> convert(FaldoFeature faldoFeature) {
		List<Triple> result = new LinkedList<Triple>();
		// feature is the subject of all following triples except the FALDO exact position triples
		String featureName = FEATUREBASEURI + faldoFeature.id;
		Node feature = NodeFactory.createURI(featureName);

		Node ref = NodeFactory.createURI(faldoFeature.assembly);

		Node featureBegin = NodeFactory.createURI(FEATUREBEGINURI + faldoFeature.id);
		Node featureEnd = NodeFactory.createURI(FEATUREENDURI + faldoFeature.id);
		result.add(new Triple(feature, RDFS.label.asNode(), NodeFactory.createLiteral(faldoFeature.id)));
		result.add(new Triple(feature, FaldoVocabulary.begin, featureBegin));
		result.add(new Triple(feature, FaldoVocabulary.end, featureEnd));
		result.add(new Triple(featureBegin, FaldoVocabulary.position,NodeFactory.createLiteral(faldoFeature.start.toString(),new XSDDatatype("int"))));
		result.add(new Triple(featureBegin, FaldoVocabulary.reference, ref));
		result.add(new Triple(featureEnd, FaldoVocabulary.position,NodeFactory.createLiteral(faldoFeature.end.toString(),new XSDDatatype("int"))));
		result.add(new Triple(featureEnd, FaldoVocabulary.reference, ref));


		if (faldoFeature.strand) {
			result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocabulary.ForwardStrandPosition));			
			result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocabulary.ForwardStrandPosition));			
		} else {
			result.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocabulary.ReverseStrandPosition));
			result.add(new Triple(featureEnd, RDF.type.asNode(), FaldoVocabulary.ReverseStrandPosition));

		}
		return result;

	}
}

