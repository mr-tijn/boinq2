package com.genohm.boinq.domain.match;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import com.genohm.boinq.web.rest.dto.FeatureQueryDTO;

@Entity
@Table(name="T_FEATUREQUERY")
public class FeatureQuery implements QueryGeneratorAcceptor {
	
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	@JoinColumn(name="feature_query_id")
	private Set<FeatureJoin> joins = new HashSet<>();

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	@JoinColumn(name="feature_query_id")
	private Set<FeatureSelect> selects = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name="owner_id")
	private User owner;

	@Column(name="name")
	private String name;

	public Long getId() {
		return id;
	}
	
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public FeatureQueryDTO createDTO() {
		FeatureQueryDTO result = new FeatureQueryDTO();
		result.id = this.id;
		result.name = this.name;
		result.ownerId = this.owner.getLogin();
		result.joins = new HashSet<>();
		for (FeatureJoin join: this.joins) {
			result.joins.add(join.createDTO());
		}
		result.selects = new HashSet<>();
		for (FeatureSelect select: this.selects) {
			result.selects.add(select.createDTO());
		}
		return result;
	}
}
