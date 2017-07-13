package com.genohm.boinq.web.rest.dto;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.genohm.boinq.domain.query.QueryBridge;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class QueryBridgeDTO {

	public static QueryBridgeDTO create(QueryBridge bridge) {
		return create(bridge.getId(), bridge.getFromGraph().getIdx(), bridge.getToGraph().getIdx(), bridge.getFromNode().getIdx(), bridge.getToNode().getIdx(), bridge.getStringToEntityTemplate(), bridge.getLiteralToLiteralMatchType(), bridge.getMatchStrand());
	}
	
	@JsonCreator public static QueryBridgeDTO create(
			@JsonProperty("id")										Long id,
			@JsonProperty("fromGraphIdx")							Integer fromGraphIdx,
			@JsonProperty("toGraphIdx")								Integer toGraphIdx,
			@JsonProperty("fromNodeIdx")							Integer fromNodeIdx,
			@JsonProperty("toNodeIdx")								Integer toNodeIdx,
			@JsonProperty("stringToEntityTemplate")	@Nullable		String stringToEntityTemplate,
			@JsonProperty("literalToLiteralMatchType") @Nullable	Integer literalToLiteralMatchType,
			@JsonProperty("matchStrand") @Nullable					Boolean matchStrand) {
		return new AutoValue_QueryBridgeDTO(id, fromGraphIdx, toGraphIdx, fromNodeIdx, toNodeIdx, stringToEntityTemplate, literalToLiteralMatchType, matchStrand);
	}

	@JsonProperty("id") @Nullable							public abstract Long id();
	@JsonProperty("fromGraphIdx")							public abstract Integer fromGraphIdx();
	@JsonProperty("toGraphIdx")								public abstract Integer toGraphIdx();
	@JsonProperty("fromNodeIdx")							public abstract Integer fromNodeIdx();
	@JsonProperty("toNodeIdx")								public abstract Integer toNodeIdx();
	@JsonProperty("stringToEntityTemplate")	@Nullable		public abstract String stringToEntityTemplate();
	@JsonProperty("literalToLiteralMatchType") @Nullable	public abstract Integer literalToLiteralMatchType();
	@JsonProperty("matchStrand") @Nullable					public abstract Boolean matchStrand();
}
