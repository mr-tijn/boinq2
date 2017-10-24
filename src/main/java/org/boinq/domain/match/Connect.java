package org.boinq.domain.match;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.boinq.domain.GenomicRegion;
import org.boinq.tools.generators.QueryGenerator;
import org.boinq.web.rest.dto.FeatureJoinDTO;

@Entity
@DiscriminatorValue(value=FeatureJoinDTO.JOIN_TYPE_CONNECT)
public class Connect extends FeatureJoin {

	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="source_connector_id")
	private FeatureConnector sourceConnector;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="target_connector_id")
	private FeatureConnector targetConnector;
	
	@Override
	public Boolean check(QueryGenerator qg, GenomicRegion region) {
		return qg.check(this, region);
	}

	@Override
	public void accept(QueryGenerator qg, GenomicRegion region) {
		qg.visit(this, region);
	}

	public FeatureConnector getSourceConnector() {
		return sourceConnector;
	}

	public void setSourceConnector(FeatureConnector sourceConnector) {
		this.sourceConnector = sourceConnector;
	}

	public FeatureConnector getTargetConnector() {
		return targetConnector;
	}

	public void setTargetConnector(FeatureConnector targetConnector) {
		this.targetConnector = targetConnector;
	}
	
	public FeatureJoinDTO createDTO() {
		FeatureJoinDTO featureJoinDTO = new FeatureJoinDTO();
		featureJoinDTO.type = FeatureJoinDTO.JOIN_TYPE_CONNECT;
		featureJoinDTO.sourceSelectIdx = this.getSource().getIdx();
		featureJoinDTO.targetSelectIdx = this.getTarget().getIdx();
		featureJoinDTO.sourceConnector = this.getSourceConnector().createDTO();
		featureJoinDTO.targetConnector = this.getTargetConnector().createDTO();
		return featureJoinDTO;
	}

}
