package com.genohm.boinq.domain.match;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.genohm.boinq.web.rest.dto.CriteriaDTO;

@Entity
@Table(name="T_FEATURESELECTCRITERION")
@DiscriminatorColumn(name="type")
public abstract class FeatureSelectCriterion implements QueryGeneratorAcceptor {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    
    @ManyToOne(cascade=CascadeType.DETACH, fetch=FetchType.EAGER)
    @JoinColumn(name="feature_select_id")
	private FeatureSelect parent;

    public FeatureSelectCriterion() {}
    
	public CriteriaDTO createDTO() {return null;}

	public FeatureSelect getParent() {
		return parent;
	}
	
	public void setParent(FeatureSelect parent) {
		this.parent = parent;
	}
}
