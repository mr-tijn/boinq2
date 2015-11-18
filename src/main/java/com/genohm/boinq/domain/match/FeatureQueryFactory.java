package com.genohm.boinq.domain.match;

import java.util.HashMap;
import java.util.Map;

import com.genohm.boinq.web.rest.dto.CriteriaDTO;
import com.genohm.boinq.web.rest.dto.FeatureJoinDTO;
import com.genohm.boinq.web.rest.dto.FeatureQueryDTO;
import com.genohm.boinq.web.rest.dto.FeatureSelectDTO;

public class FeatureQueryFactory {
	
	public static FeatureSelectCriterion createCriterion(CriteriaDTO dto) throws Exception {
		switch (dto.type) {
		case CriteriaDTO.LOCATION_CRITERIA: 
			LocationCriterion result = new LocationCriterion();
			result.setStart(dto.start);
			result.setEnd(dto.end);
			result.setContig(dto.contig);
			result.setStrand(dto.strand);
			return result;
		default: throw new Exception("Unhandled type "+dto.type);
		}
	}

	public static FeatureSelect createSelect(FeatureSelectDTO dto) throws Exception {
		FeatureSelect featureSelect = new FeatureSelect();
		for (CriteriaDTO criterion: dto.criteria) {
			featureSelect.addCriteria(createCriterion(criterion));
		}
		return featureSelect;
	}

	public static FeatureJoin createJoin(FeatureJoinDTO dto) throws Exception {
		switch (dto.type) {
		case FeatureJoinDTO.JOIN_TYPE_OVERLAP: 
			return new LocationOverlap();
		default: throw new Exception("Unhandled type "+dto.type);
		}
	}
	
	public static FeatureQuery createFeatureQuery(FeatureQueryDTO fq) throws Exception {
		FeatureQuery featureQuery = new FeatureQuery();
		Map<FeatureSelectDTO, FeatureSelect> deserialized = new HashMap<>();
		for (FeatureSelectDTO selectDTO: fq.selects) {
			FeatureSelect select = createSelect(selectDTO);
			deserialized.put(selectDTO, select);
			featureQuery.addSelect(select);
		}
		for (FeatureJoinDTO joinDTO: fq.joins) {
			FeatureJoin join = createJoin(joinDTO);
			join.setSource(deserialized.get(joinDTO.sourceSelect));
			join.setTarget(deserialized.get(joinDTO.targetSelect));
			featureQuery.addJoin(createJoin(joinDTO));
		}
		return featureQuery;
	}
}
