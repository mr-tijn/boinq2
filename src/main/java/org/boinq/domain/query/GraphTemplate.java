package org.boinq.domain.query;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "T_GRAPHTEMPLATE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GraphTemplate implements Serializable {
	public static final int GRAPH_TYPE_LOCAL = 0;
	public static final int GRAPH_TYPE_REMOTE = 1;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="type", nullable=false)
	private int type;
	
	@Column(name="name", length=255, nullable=true)
	private String name;
	
	@Column(name="endpoint_url", length=255, nullable=true)	
	private String endpointUrl;
	
	@Column(name="graph_iri", length=255, nullable=true)
	private String graphIri;
	
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @JoinColumn(name="graphtemplate_id")
	private Set<EdgeTemplate> edgeTemplates;

    public GraphTemplate() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEndpointUrl() {
		return endpointUrl;
	}
	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}
	public String getGraphIri() {
		return graphIri;
	}
	public void setGraphIri(String graphIri) {
		this.graphIri = graphIri;
	}
	public Set<EdgeTemplate> getEdgeTemplates() {
		return edgeTemplates;
	}
	public void setEdgeTemplates(Set<EdgeTemplate> edgeTemplates) {
		this.edgeTemplates = edgeTemplates;
	}
	
	
}
