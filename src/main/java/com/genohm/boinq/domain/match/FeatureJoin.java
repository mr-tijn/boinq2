package com.genohm.boinq.domain.match;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;

@Entity
@Table(name="T_FEATUREJOIN")
@DiscriminatorColumn(name="type")
public abstract class FeatureJoin implements QueryGeneratorAcceptor {
	
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
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
	public void setTarget(FeatureSelect targetSelect) {
		this.targetSelect = targetSelect;
	}
	public void accept(QueryGenerator qg, GenomicRegion region) {}
}
