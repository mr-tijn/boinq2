package com.genohm.boinq.domain.match;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;
import com.genohm.boinq.web.rest.dto.FeatureJoinDTO;

@Entity
@Table(name="T_FEATUREJOIN")
@DiscriminatorColumn(name="type")
public abstract class FeatureJoin implements QueryGeneratorAcceptor {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    public Long getId() { return id; }

    @ManyToOne
    @JoinColumn(name="source_select_id")
	private FeatureSelect sourceSelect;
    
    @ManyToOne
    @JoinColumn(name="target_select_id")
	private FeatureSelect targetSelect;
    
	public void setSource(FeatureSelect sourceSelect) {
		this.sourceSelect = sourceSelect;
	}
	public FeatureSelect getSource() {
		return sourceSelect;
	}
	public void setTarget(FeatureSelect targetSelect) {
		this.targetSelect = targetSelect;
	}
	public FeatureSelect getTarget() {
		return targetSelect;
	}
	public abstract void accept(QueryGenerator qg, GenomicRegion region);
	
	public FeatureJoinDTO createDTO() {
		FeatureJoinDTO result = new FeatureJoinDTO();
		result.sourceSelectIdx = this.sourceSelect.getIdx();
		result.targetSelectIdx = this.targetSelect.getIdx();
		return result;
	}
}
