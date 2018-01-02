package org.boinq.tools.fileformats;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.RDFNode;
import org.boinq.Application;
import org.boinq.domain.RawSPARQLResultSet;
import org.boinq.domain.jobs.TripleConversion;
import org.boinq.generated.vocabularies.FaldoVocab;
import org.boinq.generated.vocabularies.SoVocab;
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
public class GFFConverterTest {
	
	public static final String TESTGRAPH = "testGFF";
	
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
	
	@Test
	public void testGffConversion() throws Exception {
		// known problem: htsjdk parser does not understand unknown strands; and only supports limited feature types
		String filePath = getClass().getResource("/inputfiles/test.gff3").getFile();
		TripleConversion.Metadata meta = new TripleConversion.Metadata();
		Iterator<Triple> iterator = tripleIteratorFactory.getIterator(new File(filePath), null, meta);
		String graphName = localGraphService.createLocalGraph(TESTGRAPH);
		TripleUploader uploader = tripleUploadService.getUploader(localGraphService.getUpdateEndpoint(), graphName, Prefixes.getCommonPrefixes());
		while (iterator.hasNext()) {
			uploader.triple(iterator.next());
		}
		uploader.finish();
		assertTrue(meta.entryCount == 49L);
		List<String> typeURIs = meta.typeList.stream().map(node -> node.getURI()).collect(Collectors.toList());
		assertTrue(typeURIs.containsAll(Arrays.asList(SoVocab.operon.getURI(), SoVocab.mRNA.getURI(),
				SoVocab.exon.getURI(),SoVocab.match.getURI(),SoVocab.gene.getURI(), SoVocab.five_prime_UTR.getURI(), 
				SoVocab.three_prime_UTR.getURI(), SoVocab.CDS.getURI(), SoVocab.EST_match.getURI())));
		// microarray_oligo ?
		String query1 = "PREFIX fmt: <http://www.boinq.org/iri/ontologies/format#> " +
						"SELECT (COUNT(?feature) as ?numResults) WHERE {?feature a fmt:GFF_Entry}";
		RawSPARQLResultSet result1 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query1);
		assertNotNull(result1.getRecords());
		assertTrue(result1.getRecords().size() == 1);
		Map<String, RDFNode> result = result1.getRecords().get(0);
		assertNotNull(result);
		assertTrue(result.containsKey("numResults"));
		assertTrue(result.get("numResults").isLiteral());
		assertTrue(result.get("numResults").asLiteral().getInt() == meta.entryCount);
		// 5 entries describe 5 mrna's
		String query2 = "PREFIX fmt: <http://www.boinq.org/iri/ontologies/format#> "
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX so: <http://purl.obolibrary.org/obo/so-xp.obo#> "
				+ "SELECT ?feature ?label WHERE "
				+ "{?entry a           fmt:GFF_Entry; "
				+ "		   fmt:defines ?feature. "
				+ "?feature rdfs:label ?label; "
				+ "			a <" + SoVocab.mRNA.getURI() + ">."
				+ "}";
		RawSPARQLResultSet result2 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query2);
		assertNotNull(result2.getRecords());
		assertTrue(result2.getRecords().size() == 5);
		List<String> names = result2.getRecords().stream().map(r -> r.get("label").asLiteral().getString()).collect(Collectors.toList());
		assertTrue(names.containsAll(Arrays.asList("sonichedgehog", "subsonicsquirrel", "EDEN.1", "EDEN.2", "EDEN.3")));
		// match001 has 5 locations
		String query3 = "PREFIX faldo: <http://biohackathon.org/resource/faldo#> "
				+ "SELECT ?location WHERE {"
				+ "			<http://www.boinq.org/resource/feature#match001> faldo:location ?location.}";
		RawSPARQLResultSet result3 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query3);
		assertNotNull(result3.getRecords());
		assertTrue(result3.getRecords().size() == 5);
		// mrna002 has 2 exons
		String query4 = "PREFIX so: <http://purl.obolibrary.org/obo/so-xp.obo#> "
				+ "SELECT ?feature WHERE {"
				+ "			?feature so:part_of <http://www.boinq.org/resource/feature#mrna0002>."
				+ "			?feature a <"+ SoVocab.exon.getURI() + "> .}";
		RawSPARQLResultSet result4 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query4);
		assertNotNull(result4.getRecords());
		assertTrue(result4.getRecords().size() == 2);
		// gene EDEN has 3 mRNA's
		String query5 = "PREFIX so: <http://purl.obolibrary.org/obo/so-xp.obo#> "
				+ "SELECT ?subfeature WHERE {"
				+ "			?feature rdfs:label \"EDEN\";"
				+ "					 a <"+ SoVocab.gene.getURI() + "> ;"
				+ "					 so:has_part ?subfeature."
				+ "			?subfeature a <"+ SoVocab.mRNA.getURI() + "> .}";
		RawSPARQLResultSet result5 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query5);
		assertNotNull(result5.getRecords());
		assertTrue(result5.getRecords().size() == 3);
		// match1 is source of two alignment; both alignment and target have a location
		String query6 = "PREFIX faldo: <http://biohackathon.org/resource/faldo#> " 
				+ "PREFIX gfvo:<http://www.biointerchange.org/gfvo#> "
				+ "SELECT ?alignment ?target WHERE {"
				+ "?alignment gfvo:hasSource <http://www.boinq.org/resource/feature#Match1>. "
				+ "?alignment faldo:location ?source_location. "
				+ "?alignment gfvo:hasInput ?target. "
				+ "?target faldo:location ?target_location. }";
		RawSPARQLResultSet result6 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query6);
		assertNotNull(result6.getRecords());
		assertTrue(result6.getRecords().size() == 2);
		
				
	}

}