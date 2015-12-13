package com.genohm.boinq.domain.match;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.domain.User;
import com.genohm.boinq.tools.generators.QueryGenerator;

@Entity
@Table(name="T_FEATUREQUERY")
public class FeatureQuery implements QueryGeneratorAcceptor {
	
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="feature_query_id")
	private Set<FeatureJoin> joins = new HashSet<>();

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="feature_query_id")
	Set<FeatureSelect> selects = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name="owner_id")
	User owner;

	public Set<FeatureJoin> getJoins() {
		return joins;
	}

	public void addJoin(FeatureJoin join) {
		joins.add(join);
	}
	
	public void addSelect(FeatureSelect select) {
		selects.add(select);
	}

	public Set<FeatureSelect> getSelects() {
		return selects;
	}
	
	public void accept(QueryGenerator qg, GenomicRegion region) {
		qg.visit(this, region);
	}
}
