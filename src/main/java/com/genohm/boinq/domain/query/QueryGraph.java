package com.genohm.boinq.domain.query;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "T_QUERYGRAPH")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QueryGraph implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="graphtemplate_id")
	private GraphTemplate template;
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="querygraph_id")
	private Set<QueryEdge> queryEdges;
	
	
	public QueryGraph() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
	public GraphTemplate getTemplate() {
		return template;
	}
	public void setTemplate(GraphTemplate template) {
		this.template = template;
	}
	public Set<QueryEdge> getQueryEdges() {
		return queryEdges;
	}
	public void setQueryEdges(Set<QueryEdge> queryEdges) {
		this.queryEdges = queryEdges;
	}
	

	
}
