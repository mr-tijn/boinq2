package com.genohm.boinq.domain.match;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Service;

import com.genohm.boinq.repository.TrackRepository;
import com.genohm.boinq.web.rest.dto.CriteriaDTO;
import com.genohm.boinq.web.rest.dto.FeatureJoinDTO;
import com.genohm.boinq.web.rest.dto.FeatureQueryDTO;
import com.genohm.boinq.web.rest.dto.FeatureSelectDTO;

@Service
public class FeatureQueryFactory {
	
	@Inject
	TrackRepository trackRepository;
	
	public FeatureSelectCriterion createCriterion(CriteriaDTO dto) throws Exception {
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

	public FeatureSelect createSelect(FeatureSelectDTO dto) throws Exception {
		FeatureSelect featureSelect = new FeatureSelect();
		for (CriteriaDTO criterion: dto.criteria) {
			featureSelect.addCriteria(createCriterion(criterion));
		}
		featureSelect.setTrack(trackRepository.findOne(Integer.toUnsignedLong(dto.trackId)));
		featureSelect.setViewX(dto.viewX);
		featureSelect.setViewY(dto.viewY);
		featureSelect.setRetrieveFeatureData(dto.retrieve);
		return featureSelect;
	}

	public FeatureJoin createJoin(FeatureJoinDTO dto) throws Exception {
		switch (dto.type) {
		case FeatureJoinDTO.JOIN_TYPE_OVERLAP: 
			LocationOverlap result = new LocationOverlap();
			return result;
		default: throw new Exception("Unhandled type "+dto.type);
		}
	}
	
	public FeatureQuery createFeatureQuery(FeatureQueryDTO fq) throws Exception {
		FeatureQuery featureQuery = new FeatureQuery();
		Map<Integer, FeatureSelect> deserialized = new HashMap<>();
		for (FeatureSelectDTO selectDTO: fq.selects) {
			FeatureSelect select = createSelect(selectDTO);
			deserialized.put(selectDTO.idx, select);
			featureQuery.addSelect(select);
		}
		for (FeatureJoinDTO joinDTO: fq.joins) {
			FeatureJoin join = createJoin(joinDTO);
			join.setSource(deserialized.get(joinDTO.sourceSelect.idx));
			join.setTarget(deserialized.get(joinDTO.targetSelect.idx));
			featureQuery.addJoin(join);
		}
		return featureQuery;
	}
}
