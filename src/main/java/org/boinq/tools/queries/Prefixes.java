package org.boinq.tools.queries;

import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.XSD;
import org.boinq.generated.vocabularies.FaldoVocab;
import org.boinq.generated.vocabularies.GfvoVocab;
import org.boinq.tools.vocabularies.CommonVocabulary;


public class Prefixes {

	private static PrefixMapping commonPrefixes = null;
	
	public static PrefixMapping getCommonPrefixes()  {
		if (commonPrefixes == null) {
			commonPrefixes = new PrefixMappingImpl();
			commonPrefixes.setNsPrefix("rdf",CommonVocabulary.rdfBaseURI);
			commonPrefixes.setNsPrefix("rdfs", CommonVocabulary.rdfsBaseURI);
//			commonPrefixes.setNsPrefix("track", TrackVocabulary.baseURI);
			commonPrefixes.setNsPrefix("obo", CommonVocabulary.oboBaseURI);
			commonPrefixes.setNsPrefix("owl", CommonVocabulary.owlBaseURI);
			commonPrefixes.setNsPrefix("dc", DC.NS);
			commonPrefixes.setNsPrefix("dcterms", DCTerms.NS);
			commonPrefixes.setNsPrefix("skos", SKOS.NAMESPACE.getURI());
			commonPrefixes.setNsPrefix("faldo", FaldoVocab.NS);
			commonPrefixes.setNsPrefix("foaf", FOAF.NS);
			commonPrefixes.setNsPrefix("xsd", XSD.NS);
			commonPrefixes.setNsPrefix("gfvo", GfvoVocab.NS);
			commonPrefixes.setNsPrefix("sio", "http://semanticscience.org/resource/");
			commonPrefixes.setNsPrefix("ensembl", "http://rdf.ebi.ac.uk/resource/");
			commonPrefixes.setNsPrefix("ncit", "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
			commonPrefixes.setNsPrefix("mesh", "http://id.nlm.nih.gov/mesh/");
			commonPrefixes.setNsPrefix("meshv", "http://id.nlm.nih.gov/mesh/vocab#");
		}
		return commonPrefixes;
	}

	
}
