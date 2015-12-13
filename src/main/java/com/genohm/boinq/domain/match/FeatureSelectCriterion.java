package com.genohm.boinq.domain.match;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;

@Entity
@Table(name="T_FEATURESELECTCRITERION")
@DiscriminatorColumn(name="type")
public abstract class FeatureSelectCriterion implements QueryGeneratorAcceptor {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    public FeatureSelectCriterion() {}
    
	public void accept(QueryGenerator qg, GenomicRegion region) {}
}
