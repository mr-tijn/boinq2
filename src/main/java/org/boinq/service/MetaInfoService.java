package org.boinq.service;

import java.util.Map;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.boinq.domain.SPARQLResultSet;
import org.boinq.domain.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class MetaInfoService {
	
	//TODO: merge with metadatagraphservice into metadataservice

	private static Logger log = LoggerFactory.getLogger(MetaInfoService.class);

	@Inject
	private QueryBuilderService queryBuilderService;
	@Inject
	private SPARQLClientService sparqlClientService;

	
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

}
