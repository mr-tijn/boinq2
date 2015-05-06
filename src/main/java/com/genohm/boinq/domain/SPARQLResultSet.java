package com.genohm.boinq.domain;

import java.util.List;
import java.util.Map;


public class SPARQLResultSet {
	protected List<Map<String,String>> records;
	protected List<String> variableNames;
	public List<Map<String, String>> getRecords() {
		return records;
	}
	public void setRecords(List<Map<String, String>> records) {
		this.records = records;
	}
	public List<String> getVariableNames() {
		return variableNames;
	}
	public void setVariableNames(List<String> variableNames) {
		this.variableNames = variableNames;
	}
	
}