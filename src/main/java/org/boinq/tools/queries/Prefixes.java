package org.boinq.tools.queries;

import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
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
			commonPrefixes.setNsPrefix("xsd", CommonVocabulary.xmlSchemaURI);
			commonPrefixes.setNsPrefix("owl", CommonVocabulary.owlBaseURI);
		}
		return commonPrefixes;
	}

	
}
