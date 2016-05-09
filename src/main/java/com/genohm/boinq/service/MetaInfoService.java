package com.genohm.boinq.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.SPARQLResultSet;
import com.genohm.boinq.domain.Track;


@Service
public class MetaInfoService {
	
	//TODO: merge with metadatagraphservice into metadataservice

	private static Logger log = LoggerFactory.getLogger(MetaInfoService.class);

	@Inject
	private QueryBuilderService queryBuilderService;
	@Inject
	private SPARQLClientService sparqlClientService;

	public void getLocalToBoinqReferenceMap(Track track) {
		try {
			String query = queryBuilderService.findReferenceMap(track);
			SPARQLResultSet result = sparqlClientService.querySelect(track.getDatasource().getMetaEndpointUrl(),
					track.getDatasource().getMetaGraphName(), query);
			Map<Node, Node> results = new HashMap<>();
			for (Map<String, String> record : result.getRecords()) {
				results.put(NodeFactory.createURI(record.get(QueryBuilderService.ORIGINAL_REFERENCE)),
						NodeFactory.createURI(QueryBuilderService.TARGET_REFERENCE));
			}
			track.setReferenceMap(results);
		} catch (Exception e) {
			log.error("Couldn't get reference map for track " + track.getGraphName(), e);
			track.setReferenceMap(null);
		}

	}

	public void getSupportedFilters(Track track) {
		try {
			String query = queryBuilderService.getFilters(track);
			SPARQLResultSet result = sparqlClientService.querySelect(track.getDatasource().getMetaEndpointUrl(), track.getDatasource().getMetaGraphName(), query);
			List<Map<String,String>> filters = new LinkedList<>();
			for (Map<String,String> record: result.getRecords()) {
				filters.add(record);
			}
			track.setSupportedFilters(filters);
		} catch (Exception e) {
			log.error("Could not find supported operators for track " + track.getGraphName(), e);
			track.setSupportedFilters(null);
		}
	}
	
	public void getSupportedConnectors(Track track) {
		try {
			String query = queryBuilderService.getConnectors(track);
			SPARQLResultSet result = sparqlClientService.querySelect(track.getDatasource().getMetaEndpointUrl(), track.getDatasource().getMetaGraphName(), query);
			List<Map<String,String>> connectors = new LinkedList<>();
			for (Map<String,String> record: result.getRecords()) {
				connectors.add(record);
			}
			track.setSupportedConnectors(connectors);
		} catch (Exception e) {
			log.error("Could not find supported connectors for track " + track.getGraphName(), e);
			track.setSupportedConnectors(null);
		}
	}

	public long getFileAttributeCount(Track track, Node attributeType) {
		long count = 0;
		try {
			String query = queryBuilderService.findFileAttributeCount(track.getGraphName(), attributeType);
			SPARQLResultSet result = sparqlClientService.querySelect(track.getDatasource().getMetaEndpointUrl(),
					track.getDatasource().getMetaGraphName(), query);
			for (Map<String, String> record : result.getRecords()) {
				count += Integer.parseInt(record.get(QueryBuilderService.VARIABLE_ATTRIBUTE_COUNT));
			}

		} catch (Exception e) {
			log.error("Could not find supported attributes in track " + track.getGraphName(), e);
			track.setFeatureCount(0);
		}
		return count;
	}

	public void getSupportedFeatureTypes(Track track) {
		try {
			String query = queryBuilderService.findFeatureTypes(track.getGraphName());
			SPARQLResultSet result = sparqlClientService.querySelect(track.getDatasource().getMetaEndpointUrl(),
					track.getDatasource().getMetaGraphName(), query);
			Map<String, String> results = new HashMap<>();
			for (Map<String, String> record : result.getRecords()) {
				results.put(record.get(QueryBuilderService.VARIABLE_FEATURE_TYPE_LABEL),
						record.get(QueryBuilderService.VARIABLE_FEATURE_TYPE));
			}
			track.setSupportedFeatureTypes(results);
		} catch (Exception e) {
			log.error("Could not find supported operators for track " + track.getGraphName(), e);
			track.setSupportedFilters(null);

		}
	}

	public void addMetaInfo(Track track) {
		getLocalToBoinqReferenceMap(track);
		getSupportedFilters(track);
		getSupportedFeatureTypes(track);
	}

}
