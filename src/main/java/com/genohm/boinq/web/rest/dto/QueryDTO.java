package com.genohm.boinq.web.rest.dto;

public class QueryDTO {
	
	private String query;
	private String syntax;
	
	public QueryDTO() {
	}
	
	public QueryDTO(String query, String syntax) {
		super();
		this.query = query;
		this.syntax = syntax;
	}

	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getSyntax() {
		return syntax;
	}
	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}
	
	

}
