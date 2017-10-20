package com.genohm.boinq.domain.query;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "T_QUERYEDGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QueryEdge implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
	@JoinColumn(name="edgetemplate_id")
	private EdgeTemplate template;
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="from_querynode_id")
	private QueryNode from;
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="to_querynode_id")
	private QueryNode to;
	
	// retrieve source and target
	@Column(name="retrieve")
	private Boolean retrieve = false;

	public QueryEdge() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
	public EdgeTemplate getTemplate() {
		return template;
	}
	public void setTemplate(EdgeTemplate template) {
		this.template = template;
	}
	public QueryNode getFrom() {
		return from;
	}
	public void setFrom(QueryNode from) {
		this.from = from;
	}
	public QueryNode getTo() {
		return to;
	}
	public void setTo(QueryNode to) {
		this.to = to;
	}
	public Boolean getRetrieve() {
		return retrieve;
	}
	public void setRetrieve(Boolean retrieve) {
		this.retrieve = retrieve;
	}

	@Transient
	public Boolean adjoins(QueryEdge target) {
		return (from.getId().equals(target.from.getId()) 
				|| from.getId().equals(target.to.getId()) 
				|| to.getId().equals(target.from.getId())
				|| to.getId().equals(target.to.getId()));
	}
	
	@Transient
	public Boolean adjoins(QueryBridge target) {
		return (from.getId().equals(target.getFromNode().getId())
				|| from.getId().equals(target.getToNode().getId()) 
				|| to.getId().equals(target.getFromNode().getId()) 
				|| to.getId().equals(target.getToNode().getId()));
	}

	@Transient
	public Boolean adjoins(Collection<QueryBridge> targets) {
		return targets.stream().anyMatch(target -> this.adjoins(target));
	}
	
}
