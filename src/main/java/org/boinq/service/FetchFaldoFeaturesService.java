package org.boinq.service;
//package org.boinq.service;
//
//import java.util.List;
//
//import javax.inject.Inject;
//
//import org.springframework.stereotype.Service;
//
//import org.boinq.domain.Datasource;
//import org.boinq.domain.SPARQLResultSet;
//import org.boinq.domain.Track;
//
//
//@Service
//public class FetchFaldoFeaturesService {
//
//	@Inject
//	QueryBuilderService queryBuilderService;
//	@Inject
//	SPARQLClientService sparqlClientService;
//	
//	public List<Feature> fetchFeatures(Track track, String ref, Long begin, Long end, Boolean strand) {
//		switch (track.getDatasource().getType()) {
//		case Datasource.TYPE_LOCAL_FALDO:
//			String query = queryBuilderService.getFaldoFeatures(ref, begin, end, strand);
//			SPARQLResultSet rs = sparqlClientService.querySelect(track.getDatasource().getEndpointUrl(), track.getGraphName(), query);
//		}
//	}
//	
//	
//}
