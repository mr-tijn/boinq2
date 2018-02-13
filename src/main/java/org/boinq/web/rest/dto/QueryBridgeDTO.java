package org.boinq.web.rest.dto;

import javax.annotation.Nullable;

import org.boinq.domain.query.QueryBridge;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.boinq.web.rest.dto.AutoValue_QueryBridgeDTO;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class QueryBridgeDTO {

	public static QueryBridgeDTO create(QueryBridge bridge) {
		return create(bridge.getId(), bridge.getFromGraph().getIdx(), bridge.getToGraph().getIdx(), bridge.getFromNode().getIdx(), bridge.getToNode().getIdx(), bridge.getFromX(), bridge.getFromY(), bridge.getToX(), bridge.getToY(), bridge.getStringToEntityTemplate(), bridge.getLiteralToLiteralMatchType(), bridge.getMatchStrand());
	}
	
	@JsonCreator public static QueryBridgeDTO create(
			@JsonProperty("id")										Long id,
			@JsonProperty("fromGraphIdx")							Integer fromGraphIdx,
			@JsonProperty("toGraphIdx")								Integer toGraphIdx,
			@JsonProperty("fromNodeIdx")							Integer fromNodeIdx,
			@JsonProperty("toNodeIdx")								Integer toNodeIdx,
			@JsonProperty("fromX") @Nullable						Integer fromX,
			@JsonProperty("fromY") @Nullable						Integer fromY,
			@JsonProperty("toX") @Nullable							Integer toX,
			@JsonProperty("toY") @Nullable							Integer toY,
			@JsonProperty("stringToEntityTemplate")	@Nullable		String stringToEntityTemplate,
			@JsonProperty("literalToLiteralMatchType") @Nullable	Integer literalToLiteralMatchType,
			@JsonProperty("matchStrand") @Nullable					Boolean matchStrand) {
		return new AutoValue_QueryBridgeDTO(id, fromGraphIdx, toGraphIdx, fromNodeIdx, toNodeIdx, fromX, fromY, toX ,toY, stringToEntityTemplate, literalToLiteralMatchType, matchStrand);
	}

	@JsonProperty("id") @Nullable							public abstract Long id();
	@JsonProperty("fromGraphIdx")							public abstract Integer fromGraphIdx();
	@JsonProperty("toGraphIdx")								public abstract Integer toGraphIdx();
	@JsonProperty("fromNodeIdx")							public abstract Integer fromNodeIdx();
	@JsonProperty("toNodeIdx")								public abstract Integer toNodeIdx();
	@JsonProperty("fromX") @Nullable						public abstract Integer fromX();
	@JsonProperty("fromY") @Nullable						public abstract Integer fromY();
	@JsonProperty("toX") @Nullable							public abstract Integer toX();
	@JsonProperty("toY") @Nullable							public abstract Integer toY();
	@JsonProperty("stringToEntityTemplate")	@Nullable		public abstract String stringToEntityTemplate();
	@JsonProperty("literalToLiteralMatchType") @Nullable	public abstract Integer literalToLiteralMatchType();
	@JsonProperty("matchStrand") @Nullable					public abstract Boolean matchStrand();
}
