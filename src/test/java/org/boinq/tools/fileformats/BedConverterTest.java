package org.boinq.tools.fileformats;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.RDFNode;
import org.boinq.Application;
import org.boinq.domain.RawSPARQLResultSet;
import org.boinq.domain.jobs.TripleConversion;
import org.boinq.init.TripleStoreInitializer;
import org.boinq.service.FusekiMgmtService;
import org.boinq.service.LocalGraphService;
import org.boinq.service.SPARQLClientService;
import org.boinq.service.TripleUploadService;
import org.boinq.service.TripleUploadService.TripleUploader;
import org.boinq.tools.fileformats.TripleIteratorFactory;
import org.boinq.tools.queries.Prefixes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class BedConverterTest {

	public static final String TESTGRAPH = "testBED";
	
	@Inject
	TripleIteratorFactory tripleIteratorFactory;
	
	@Inject
	LocalGraphService localGraphService;
	
	@Inject
	TripleUploadService tripleUploadService;
	
	@Inject
	SPARQLClientService sparqlClient;
	
	@Inject
	TripleStoreInitializer tripleStoreInitializer;

	@Inject
	FusekiMgmtService fusekiMgmtService;
	
	@Inject
	ApplicationContext context;
	
	@Before
	public void initTripleStore() {
		// FIXME: find a way to delay the tripleStoreInitializer to after start of the web container
		// this is needed for the proxy to be forwarding
		// the ContextRefreshedEvent is too soon
		Boolean up = false;
		Integer tries = 0;
		while (!up && tries < 10) {
			try {
				tries++;
				Thread.sleep(1000);
				URL endpoint = new URL(localGraphService.getSparqlEndpoint());
				URLConnection conn = endpoint.openConnection();
				conn.connect();
				up = true;
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
				break;
			} catch (MalformedURLException e) {
				e.printStackTrace(System.err);
				break;
			} catch (IOException e) {
				// continue
			} 
		}
		tripleStoreInitializer.checkInit();
		localGraphService.deleteLocalGraph(TESTGRAPH);
	}
	
	@Before
	public void init() {
		
	}
	
	@Test
	public void testBedConversion() throws Exception {
		String filePath = getClass().getResource("/inputfiles/ucsc_human_GRCh38_repeatmasker_chr9.bed").getFile();
		TripleConversion.Metadata meta = new TripleConversion.Metadata();
		Iterator<Triple> iterator = tripleIteratorFactory.getIterator(new File(filePath), meta);
		String graphName = localGraphService.createLocalGraph(TESTGRAPH);
		TripleUploader uploader = tripleUploadService.getUploader(localGraphService.getUpdateEndpoint(), graphName, Prefixes.getCommonPrefixes());
		while (iterator.hasNext()) {
			uploader.triple(iterator.next());
		}
		uploader.finish();
		assertTrue(meta.entryCount == 225099L);
		String query1 = "PREFIX fmt: <http://www.boinq.org/iri/ontologies/format#> " +
						"SELECT (COUNT(?feature) as ?numResults) WHERE {?feature a fmt:BED_Entry}";
		RawSPARQLResultSet result1 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query1);
		assertNotNull(result1.getRecords());
		assertTrue(result1.getRecords().size() == 1);
		Map<String, RDFNode> result = result1.getRecords().get(0);
		assertNotNull(result);
		assertTrue(result.containsKey("numResults"));
		assertTrue(result.get("numResults").isLiteral());
		assertTrue(result.get("numResults").asLiteral().getInt() == 225099);
		String query2 = "PREFIX fmt: <http://www.boinq.org/iri/ontologies/format#> "
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX so: <http://purl.obolibrary.org/obo/so-xp.obo#> "
				+ "SELECT ?feature ?label ?score WHERE "
				+ "{?entry a           fmt:BED_Entry; "
				+ "		   fmt:defines ?feature. "
				+ "?feature rdfs:label ?label; "
				+ "			so:has_quality ?quality."
				+ "?quality a <http://purl.obolibrary.org/obo/SO_0001685>;"
				+ "			rdf:value ?score."
				+ "FILTER (?label = '(TAACCC)n')}";
		RawSPARQLResultSet result2 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query2);
		assertNotNull(result2.getRecords());
		assertTrue(result2.getRecords().size() == 1);
		result = result2.getRecords().get(0);
		assertNotNull(result);
		assertTrue(result.get("label").asLiteral().getString().equals("(TAACCC)n"));
		assertTrue(result.get("score").asLiteral().getDouble() == 275.);
		Node feature = result.get("feature").asNode();
		String query3 = "PREFIX faldo: <http://biohackathon.org/resource/faldo#> "
				+ "SELECT ?startpos ?ref ?endpos WHERE { "
				+ "<" + feature.getURI() + "> faldo:location/faldo:begin/faldo:position ?startpos;"
				+ "		 				 faldo:location/faldo:end/faldo:position ?endpos;"
				+ "						 faldo:location/faldo:reference ?ref}";
		RawSPARQLResultSet result3 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query3);
		assertNotNull(result3.getRecords());
		assertTrue(result3.getRecords().size() == 1);
		result = result3.getRecords().get(0);
		assertTrue(result.get("startpos").asLiteral().getInt() == 10002);
		assertTrue(result.get("endpos").asLiteral().getInt() == 10433);
		assertTrue(result.get("ref").asNode().getURI().equals("http://www.boinq.org/resource/chr9"));
		String query4 = "PREFIX fmt: <http://www.boinq.org/iri/ontologies/format#> "
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX so: <http://purl.obolibrary.org/obo/so-xp.obo#> "
				+ "PREFIX faldo: <http://biohackathon.org/resource/faldo#> "
				+ "SELECT ?feature ?label ?score ?startpos ?endpos WHERE "
				+ "{?entry a           fmt:BED_Entry; "
				+ "		   fmt:defines ?feature. "
				+ "?feature rdfs:label ?label; "
				+ "			so:has_quality ?quality;"
				+ "			faldo:location/faldo:begin/faldo:position ?startpos;"
				+ "		 	faldo:location/faldo:end/faldo:position ?endpos;"
				+ "			faldo:location/faldo:reference ?ref."
				+ "?quality a <http://purl.obolibrary.org/obo/SO_0001685>;"
				+ "			rdf:value ?score."
				+ "FILTER (?label = '(ATTTTTA)n')}";
		RawSPARQLResultSet result4 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query4);
		assertNotNull(result4.getRecords());
		assertTrue(result4.getRecords().size() == 7);
	}

}
