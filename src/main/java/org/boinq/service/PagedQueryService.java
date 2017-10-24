package org.boinq.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.boinq.domain.SPARQLResultSet;
import org.springframework.stereotype.Service;

@Service
public class PagedQueryService {
	private static Long BATCHSIZE = 1000L;

	@Inject
	private SPARQLClientService sparqlClientService;
	@Inject
	private FileManagerService fileManagerService;
	
	public PagedQuery create(String fileName, String sparqlQuery, String endpoint, String graph) throws IOException {
		File targetFile = fileManagerService.createFile(fileName);
		return new PagedQuery(targetFile, sparqlQuery, endpoint, graph);
	}
	
	public class PagedQuery {
		
		private FileWriter csvWriter = null;
		private List<String> varNames = null;
		private Long originalLimit = null;
		private Long originalOffset = null;
		private Long limit = null;
		private Long offset = null;
		private Query parsedQuery = null;
		private String endpoint = null;
		private String graph = null;
		private File file = null;
		private SPARQLResultSet result = null;
		
		private PagedQuery(File file, String sparqlQuery, String endpoint, String graph) {
			this.parsedQuery = QueryFactory.create(sparqlQuery);
			this.originalLimit = parsedQuery.getLimit();
			this.originalOffset = parsedQuery.getOffset();
			this.endpoint = endpoint;
			this.graph = graph;
			this.file = file;
		}
		
		public File getFile() {
			return file;
		}
				
		public void init() {
			if (originalOffset == null) {
				originalOffset = 0L;
			}
			this.offset = originalOffset;
			this.limit = BATCHSIZE;
			if (originalLimit != null) {
				limit = Math.min(originalLimit, BATCHSIZE);
			} 
		}
		
		public Boolean hasNext() {
			return (result == null || (limit > 0  && result.getRecords().size() > 0));
		}
		
		public Integer next() throws Exception {
			parsedQuery.setLimit(limit);
			parsedQuery.setOffset(offset);
			result = sparqlClientService.query(endpoint, graph, parsedQuery);
			appendCsv(result);
			offset += BATCHSIZE;
			if (originalLimit != null) {
				limit = Math.min( originalOffset + originalLimit - offset , BATCHSIZE);
			}
			return result.getRecords().size();
		}
		
		private void appendCsv(SPARQLResultSet result) throws Exception {
			if (csvWriter == null) {
				varNames = result.getVariableNames();
				csvWriter = new FileWriter(file, true);
				csvWriter.write(String.join(",",varNames)+"\n");
			}
			for (Map<String, String> record: result.getRecords()) {
				List<String> results = new LinkedList<>();
				for (String varName: varNames) {
					results.add(record.get(varName));
				}
				csvWriter.append(String.join(",",results)+"\n");
			}
			
		}
		
	}

}
