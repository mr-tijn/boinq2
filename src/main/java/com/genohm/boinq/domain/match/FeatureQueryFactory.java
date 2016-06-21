package com.genohm.boinq.domain.match;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.User;
import com.genohm.boinq.repository.FeatureQueryRepository;
import com.genohm.boinq.repository.TrackRepository;
import com.genohm.boinq.repository.UserRepository;
import com.genohm.boinq.web.rest.dto.CriteriaDTO;
import com.genohm.boinq.web.rest.dto.FeatureConnectorDTO;
import com.genohm.boinq.web.rest.dto.FeatureJoinDTO;
import com.genohm.boinq.web.rest.dto.FeatureQueryDTO;
import com.genohm.boinq.web.rest.dto.FeatureSelectDTO;

@Service
public class FeatureQueryFactory {
	
	@Inject
	TrackRepository trackRepository;
	@Inject
	UserRepository userRepository;
	@Inject
	FeatureQueryRepository featureQueryRepository;
	
	public FeatureSelectCriterion createCriterion(CriteriaDTO dto) throws Exception {
		switch (dto.type) {
		case CriteriaDTO.LOCATION_CRITERIA: 
			LocationCriterion locationCrit = new LocationCriterion();
			locationCrit.setStart(dto.start);
			locationCrit.setEnd(dto.end);
			locationCrit.setContig(dto.contig);
			locationCrit.setStrand(dto.strand);
			return locationCrit;
		case CriteriaDTO.FEATURETYPE_CRITERIA:
			FeatureTypeCriterion featureTypeCrit = new FeatureTypeCriterion();
			featureTypeCrit.setFeatureTypeLabel(dto.featureTypeLabel);
			featureTypeCrit.setFeatureTypeUri(dto.featureTypeUri);
			return featureTypeCrit;
		case CriteriaDTO.MATCHTERM_CRITERIA:
			MatchTermCriterion matchTermCrit = new MatchTermCriterion();
			matchTermCrit.setTermUri(dto.termUri);
			matchTermCrit.setTermLabel(dto.termLabel);
			matchTermCrit.setPathExpression(dto.pathExpression);
			// todo: other fields ?
			return matchTermCrit;
		case CriteriaDTO.MATCHDECIMAL_CRITERIA:
			MatchDecimalCriterion matchDecimalCriterion = new MatchDecimalCriterion();
			matchDecimalCriterion.setExactMatch(dto.exactMatch);
			matchDecimalCriterion.setMatchDouble(dto.minDouble);
			matchDecimalCriterion.setMinDouble(dto.minDouble);
			matchDecimalCriterion.setMaxDouble(dto.maxDouble);
			return matchDecimalCriterion;
		case CriteriaDTO.MATCHINTEGER_CRITERIA:
			MatchIntegerCriterion matchIntegerCriterion = new MatchIntegerCriterion();
			matchIntegerCriterion.setExactMatch(dto.exactMatch);
			matchIntegerCriterion.setMatchLong(dto.minLong);
			matchIntegerCriterion.setMinLong(dto.minLong);
			matchIntegerCriterion.setMaxLong(dto.maxLong);
			return matchIntegerCriterion;
		case CriteriaDTO.MATCHSTRING_CRITERIA:
			MatchStringCriterion matchStringCriterion = new MatchStringCriterion();
			matchStringCriterion.setExactMatch(dto.exactMatch);
			matchStringCriterion.setMatchString(dto.matchString);
			return matchStringCriterion;
			// TODO: other criteria
		default: throw new Exception("Unhandled type "+dto.type);
		}
	}

	public FeatureSelect createSelect(FeatureSelectDTO dto) throws Exception {
		FeatureSelect featureSelect = new FeatureSelect();
		for (CriteriaDTO criterion: dto.criteria) {
			featureSelect.addCriteria(createCriterion(criterion));
		}
		featureSelect.setTrack(trackRepository.findOneWithMeta(dto.trackId).get());
		featureSelect.setViewX(dto.viewX);
		featureSelect.setViewY(dto.viewY);
		featureSelect.setIdx(dto.idx);
		featureSelect.setRetrieveFeatureData(dto.retrieve);
		return featureSelect;
	}

	public FeatureJoin createJoin(FeatureJoinDTO dto) throws Exception {
		switch (dto.type) {
		case FeatureJoinDTO.JOIN_TYPE_OVERLAP: 
			LocationOverlap overlap = new LocationOverlap();
			overlap.setSameStrand(dto.sameStrand);
			return overlap;
		case FeatureJoinDTO.JOIN_TYPE_CONNECT:
			Connect connect = new Connect();
			connect.setSourceConnector(createFeatureConnector(dto.sourceConnector));
			connect.setTargetConnector(createFeatureConnector(dto.targetConnector));
			return connect;
		default: throw new Exception("Unhandled type "+dto.type);
		}
	}
	
	public FeatureConnector createFeatureConnector(FeatureConnectorDTO dto) throws Exception {
		FeatureConnector connector = new FeatureConnector();
		connector.setType(dto.type);
		connector.setName(dto.name);
		connector.setPathExpression(dto.pathExpression);
		return connector;
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
			join.setSource(deserialized.get(joinDTO.sourceSelectIdx));
			join.setTarget(deserialized.get(joinDTO.targetSelectIdx));
			featureQuery.addJoin(join);
		}
		User owner = userRepository.findOneByLogin(fq.ownerId).get();
		featureQuery.setOwner(owner);
		featureQuery.setName(fq.name);
		featureQuery.setReferenceAssemblyUri(fq.referenceAssemblyUri);
		featureQuery.setTargetGraph(fq.targetGraph);
		return featureQuery;
	}
}
