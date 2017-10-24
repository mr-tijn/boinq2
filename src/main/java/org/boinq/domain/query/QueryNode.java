package org.boinq.domain.query;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
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
	public static final int NODETYPE_TYPEDENTITY = 4;

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

	@Column(name="idx")
	private Integer idx;
	
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
		return (entityValues != null? Arrays.asList(entityValues.split("|")): new LinkedList<String>());
	}
	public void setEntityValues(List<String> entityValues) {
		this.entityValues = entityValues.stream().collect(Collectors.joining("|"));
	}
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entityValues == null) ? 0 : entityValues.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((nodeFilters == null) ? 0 : nodeFilters.hashCode());
		result = prime * result + ((template == null) ? 0 : template.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryNode other = (QueryNode) obj;
		if (entityValues == null) {
			if (other.entityValues != null)
				return false;
		} else if (!entityValues.equals(other.entityValues))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (nodeFilters == null) {
			if (other.nodeFilters != null)
				return false;
		} else if (!nodeFilters.equals(other.nodeFilters))
			return false;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (template.getId() != other.template.getId())
			return false;
		return true;
	}
	
}
