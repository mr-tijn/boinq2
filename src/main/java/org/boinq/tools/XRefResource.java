package org.boinq.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import org.boinq.generated.vocabularies.SoVocab;

@Service
public class XRefResource {

	public final static String PROPERTY_ABBREVIATION = "abbreviation";
	public final static String PROPERTY_DATABASE = "database";
	public final static String PROPERTY_DESCRIPTION = "description";
	public final static String PROPERTY_GENERIC_URL = "generic_url";
	public final static String PROPERTY_LOCAL_ID_SYNTAX = "local_id_syntax";
	public final static String PROPERTY_URL_SYNTAX = "url_syntax";
	public final static String PROPERTY_EXAMPLE_ID = "example_id";
	public final static String PROPERTY_URL_EXAMPLE = "url_example";
	public final static String PROPERTY_ENTITY_TYPE = "entity_type";
	public final static String PROPERTY_SYNONYM = "synonym";
	
	class Xref {
		public String abbreviation;
		public String database;
		public String description;
		public String genericUrl;
		public List<String> synonyms = new LinkedList<>(); 
		public List<IdSyntax> syntaxes = new LinkedList<>();
	}
	
	class IdSyntax {
		public String idSyntax;
		public String urlSyntax;
		public String exampleId;
		public String exampleUrl;
		public String entityType;
	}

	private String goXrefFilePath = "ontologies/GO.xrf_abbs.txt";
	
	private Map<String, Xref> XrefIndex = new HashMap<>(); 
	
	public Xref find(String abbrev) {
		return XrefIndex.get(abbrev);
	}
	
	private String getType(String type) {
		if (type.startsWith("SO:")) {
			type = "http://purl.obolibrary.org/obo/SO_" + type.substring(3);
		} else if (type.equals("PR:000000001")) {
			type = SoVocab.polypeptide.getURI();
		} else if (type.equals("BET:0000000")) {
			type = SoVocab.sequence_feature.getURI();
		}
		return type;
	}
	
	@PostConstruct
	public void readData() {
		
		try(BufferedReader br = new BufferedReader(new FileReader(goXrefFilePath))) {
			Xref entry = null;
			IdSyntax syntax = null;
		    for(String line; (line = br.readLine()) != null; ) {
		    	line.trim();
		    	if (line.startsWith("!")) {
		    		continue;
		    	}
		    	if (line.length() == 0) {
		    		if (entry != null) {
		    			if (syntax != null) {
		    				entry.syntaxes.add(syntax);
		    				syntax = null;
		    			}
		    			XrefIndex.put(entry.abbreviation, entry);
		    		}
		    		entry = new Xref();
		    		continue;
		    	}
		    	if (line.indexOf("!") > -1) {
		    		line = line.substring(0, line.indexOf("!"));
		    	}
		    	int pos = line.indexOf(":");
		        String property = line.substring(0, pos).trim();
		        String value = line.substring(pos + 1).trim();
		        switch (property) {
			        case PROPERTY_ABBREVIATION:
			        	entry.abbreviation = value;
			        	break;
			        case PROPERTY_DATABASE:
			        	entry.database = value;
			        	break;
			        case PROPERTY_DESCRIPTION:
			        	entry.description = value;
			        	break;
			        case PROPERTY_SYNONYM:
			        	entry.synonyms.add(value);
			        	break;
			        case PROPERTY_GENERIC_URL:
			        	entry.genericUrl = value;
			        	break;
			        case PROPERTY_LOCAL_ID_SYNTAX:
			        	if (syntax != null) {
			        		entry.syntaxes.add(syntax);
			        	}
			        	syntax = new IdSyntax();
			        	syntax.idSyntax = value;
			        	break;
			        case PROPERTY_URL_SYNTAX:
			        	if (syntax == null) {
			        		// sometimes for single format no localId defined
			        		syntax = new IdSyntax();
			        	}
			        	syntax.urlSyntax = value;
			        	break;
			        case PROPERTY_EXAMPLE_ID:
			        	if (syntax == null) {
			        		// sometimes for single format no localId defined
			        		syntax = new IdSyntax();
			        	}
			        	syntax.exampleId = value;
			        	break;
			        case PROPERTY_URL_EXAMPLE:
			        	if (syntax == null) {
			        		// sometimes for single format no localId defined
			        		syntax = new IdSyntax();
			        	}
			        	syntax.exampleUrl = value;
			        	break;
			        case PROPERTY_ENTITY_TYPE:
			        	if (syntax == null) {
			        		// sometimes for single format no localId defined
			        		syntax = new IdSyntax();
			        	}
			        	syntax.entityType = getType(value);
			        	break;
		        	}
		        if (entry != null) {
			        if (syntax != null) {
			        	entry.syntaxes.add(syntax);
			        	
			        }
	    			XrefIndex.put(entry.abbreviation, entry);
		        }
		    }
		    
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
