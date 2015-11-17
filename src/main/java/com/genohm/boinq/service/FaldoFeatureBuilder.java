package com.genohm.boinq.service;

import java.util.Map;

import com.genohm.boinq.domain.faldo.FaldoFeature;

public class FaldoFeatureBuilder {
	
	public static final String featureId = "featureId";
	public static final String featureBeginPos = "featureBeginPos";
	public static final String featureEndPos = "featureEndPos";
	public static final String featureStrand = "featureStrand";
	public static final String featureReference = "featureReference";
	public static final String featureReferenceName = "featureReferenceName";
	public static final String featurePositionType = "featurePositionType";

	public FaldoFeature fromResultRecord(Map<String,String> resultRecord) throws Exception {
		FaldoFeature result = new FaldoFeature();
		result.assembly = resultRecord.get(featureReference);
		result.id = resultRecord.get(featureId);
		result.start = Long.parseLong(resultRecord.get(featureBeginPos));
		result.end = Long.parseLong(resultRecord.get(featureEndPos));
		result.strand = Boolean.parseBoolean(resultRecord.get(featureStrand));
		return result;
	}
}
