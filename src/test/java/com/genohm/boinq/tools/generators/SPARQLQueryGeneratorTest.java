package com.genohm.boinq.tools.generators;

import org.apache.jena.query.Query;
import org.junit.Before;
import org.junit.Test;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.match.FeatureQuery;
import com.genohm.boinq.domain.match.FeatureSelect;
import com.genohm.boinq.generated.vocabularies.TrackVocab;

public class SPARQLQueryGeneratorTest {

	private FeatureQuery featureQueryNoCriteria = new FeatureQuery();
	private GenomicRegion testRegion = new GenomicRegion();
	private Track track = new Track();
	private Datasource datasource = new Datasource();
	
	@Before
	public void initialize() {
		FeatureSelect noCriteria = new FeatureSelect();
		noCriteria.setRetrieveFeatureData(true);
		noCriteria.setTrack(track);
		featureQueryNoCriteria.addSelect(noCriteria);
		testRegion.assemblyURI = TrackVocab.GRCh37chr01.getURI();
		testRegion.start = 0L;
		testRegion.end = 10000L;
		testRegion.strand = true;
		track.setName("testTrack");
		track.setGraphName("http://www.boinq.org/iri/testGraph");
		track.setDatasource(datasource);
		datasource.setType(Datasource.TYPE_REMOTE_FALDO);
		datasource.setEndpointUrl("http://remote/sparql");
	}
	
	@Test
	public void noCriteriaSingleSelect() {
		SPARQLQueryGenerator generator = new SPARQLQueryGenerator();
		Query noCriteriaQuery = generator.computeQuery(featureQueryNoCriteria, testRegion);
		System.out.println(noCriteriaQuery.toString());
	}

}
