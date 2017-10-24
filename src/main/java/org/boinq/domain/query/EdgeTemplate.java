package org.boinq.domain.query;

import java.io.Serializable;

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
@Table(name = "T_EDGETEMPLATE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EdgeTemplate implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "from_nodetemplate_id")
	private NodeTemplate from;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "to_nodetemplate_id")
	private NodeTemplate to;

    @Column(name="term", length = 255, nullable = false)
	private String term;
	
    @Column(name="label", length = 255, nullable = true)
    private String label;
    
	public EdgeTemplate(NodeTemplate from, NodeTemplate to, String term, String label) {
		super();
		this.from = from;
		this.to = to;
		this.term = term;
		this.label = label;
	}
	public EdgeTemplate(NodeTemplate from, NodeTemplate to, String term) {
		super();
		this.from = from;
		this.to = to;
		this.term = term;
	}
	public EdgeTemplate() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
	public NodeTemplate getFrom() {
		return from;
	}
	public void setFrom(NodeTemplate from) {
		this.from = from;
	}
	public NodeTemplate getTo() {
		return to;
	}
	public void setTo(NodeTemplate to) {
		this.to = to;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
