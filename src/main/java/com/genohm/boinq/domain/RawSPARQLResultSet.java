package com.genohm.boinq.domain;

import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.RDFNode;

public class RawSPARQLResultSet {
		protected List<Map<String,RDFNode>> records;
		protected List<String> variableNames;
		public List<Map<String, RDFNode>> getRecords() {
			return records;
		}
		public void setRecords(List<Map<String, RDFNode>> records) {
			this.records = records;
		}
		public List<String> getVariableNames() {
			return variableNames;
		}
		public void setVariableNames(List<String> variableNames) {
			this.variableNames = variableNames;
		}
		

}
