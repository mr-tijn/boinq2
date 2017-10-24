package org.boinq.domain.query;

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
@Table(name = "T_QUERYBRIDGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QueryBridge implements Serializable {
	
	public static final int BRIDGE_TYPE_LITERAL_TO_LITERAL = 0;
	public static final int BRIDGE_TYPE_LITERAL_TO_ENTITY = 1;
	public static final int BRIDGE_TYPE_ENTITY_TO_LITERAL = 2;
	public static final int BRIDGE_TYPE_ENTITY_TO_ENTITY = 3;
	
	public static final int BRIDGE_TYPE_LOCATION = 4;
	
	public static final int BRIDGE_MATCH_LESSOREQUAL = 0;
	public static final int BRIDGE_MATCH_LESS = 1;
	public static final int BRIDGE_MATCH_MOREOREQUAL = 2;
	public static final int BRIDGE_MATCH_MORE = 3;
	public static final int BRIDGE_MATCH_EQUAL = 4;
	
	public static final int BRIDGE_MATCH_CONTAINS = 5;
	public static final int BRIDGE_MATCH_ISCONTAINEDIN = 6;
	public static final int BRIDGE_MATCH_STREQUAL = 7;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "from_graph_id")
	private QueryGraph fromGraph;
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "to_graph_id")
	private QueryGraph toGraph;
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "from_node_id")
	private QueryNode fromNode;
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "to_node_id")
	private QueryNode toNode;
	
	@Column(name="string_to_entity_template")
	private String stringToEntityTemplate;
	@Column(name="literal_to_literal_match_type")
	private int literalToLiteralMatchType;
	
	@Column(name="match_strand")
	private Boolean matchStrand = false;
	
	public QueryBridge() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
	public QueryGraph getFromGraph() {
		return fromGraph;
	}
	public void setFromGraph(QueryGraph fromGraph) {
		this.fromGraph = fromGraph;
	}
	public QueryGraph getToGraph() {
		return toGraph;
	}
	public void setToGraph(QueryGraph toGraph) {
		this.toGraph = toGraph;
	}
	public QueryNode getFromNode() {
		return fromNode;
	}
	public void setFromNode(QueryNode fromNode) {
		this.fromNode = fromNode;
	}
	public QueryNode getToNode() {
		return toNode;
	}
	public void setToNode(QueryNode toNode) {
		this.toNode = toNode;
	}
	public String getStringToEntityTemplate() {
		return stringToEntityTemplate;
	}
	public void setStringToEntityTemplate(String stringToEntityTemplate) {
		this.stringToEntityTemplate = stringToEntityTemplate;
	}
	public int getLiteralToLiteralMatchType() {
		return literalToLiteralMatchType;
	}
	public void setLiteralToLiteralMatchType(int literalToLiteralMatchType) {
		this.literalToLiteralMatchType = literalToLiteralMatchType;
	}
	public Boolean getMatchStrand() {
		return matchStrand;
	}
	public void setMatchStrand(Boolean matchStrand) {
		this.matchStrand = matchStrand;
	}
	
	@Transient
	public int getType() {
		// assume editor makes sure only supported combinations are arriving here
		// need compatibility matrix in editor
		if (QueryNode.NODETYPE_FALDOLOCATION == fromNode.getNodeType() && 
				QueryNode.NODETYPE_FALDOLOCATION == toNode.getNodeType()) {
			return BRIDGE_TYPE_LOCATION;
		} else if ((QueryNode.NODETYPE_GENERICENTITY == fromNode.getNodeType() || QueryNode.NODETYPE_TYPEDENTITY == fromNode.getNodeType()) && 
				(QueryNode.NODETYPE_GENERICENTITY == toNode.getNodeType() || QueryNode.NODETYPE_TYPEDENTITY == toNode.getNodeType())) {
			return BRIDGE_TYPE_ENTITY_TO_ENTITY;
		}
		else if (QueryNode.NODETYPE_GENERICLITERAL == fromNode.getNodeType() && 
				QueryNode.NODETYPE_GENERICLITERAL == toNode.getNodeType()) {
			return BRIDGE_TYPE_LITERAL_TO_LITERAL;
		}
		else if(QueryNode.NODETYPE_GENERICLITERAL == fromNode.getNodeType()) {
			return BRIDGE_TYPE_LITERAL_TO_ENTITY; 
		}
		else {
			return BRIDGE_TYPE_ENTITY_TO_LITERAL;
		}
	}
	
	@Transient
	public Boolean adjoins(QueryBridge target) {
		return (fromNode == target.fromNode 
				|| fromNode == target.toNode 
				|| toNode == target.fromNode 
				|| toNode == target.toNode);
	}
	
	@Transient
	public Boolean adjoins(QueryEdge target) {
		return (fromNode == target.getFrom()
				|| fromNode == target.getTo()
				|| toNode == target.getFrom()
				|| toNode == target.getTo());
	}
	
	@Transient
	public Boolean adjoins(Collection<QueryEdge> targets) {
		return targets.stream().anyMatch(target -> this.adjoins(target));
	}

}
