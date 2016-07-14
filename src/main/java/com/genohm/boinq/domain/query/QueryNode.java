package com.genohm.boinq.domain.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "T_QUERYNODE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QueryNode implements Serializable {
	
	public static final int NODETYPE_GENERICENTITY = 0;
	public static final int NODETYPE_GENERICLITERAL = 1;

	public static final int NODETYPE_FALDOLOCATION = 2;
	public static final int NODETYPE_ATTRIBUTE = 3;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="nodetemplate_id")
	private NodeTemplate template;

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="querynode_id")
	private Set<NodeFilter> nodeFilters = new HashSet<>();
	
	@Lob
	@Column(name="entity_values")
	private String entityValues;

	public QueryNode() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
	public Integer getNodeType() {
		return template.getNodeType();
	}

	public NodeTemplate getTemplate() {
		return template;
	}
	public void setTemplate(NodeTemplate template) {
		this.template = template;
	}
	public Set<NodeFilter> getNodeFilters() {
		return nodeFilters;
	}
	public void setNodeFilters(Set<NodeFilter> nodeFilters) {
		this.nodeFilters = nodeFilters;
	}
	public List<String> getEntityValues() {
		return Arrays.asList(entityValues.split("|"));
	}
	public void setEntityValues(List<String> entityValues) {
		this.entityValues = entityValues.stream().collect(Collectors.joining("|"));
	}
	
	
}
