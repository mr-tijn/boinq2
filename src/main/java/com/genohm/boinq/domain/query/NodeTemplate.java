package com.genohm.boinq.domain.query;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "T_NODETEMPLATE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NodeTemplate implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name="type", nullable=false)
	private int nodeType;

	public static final int SOURCE_LIST = 0;
	public static final int SOURCE_ENDPOINT = 1;
	public static final int SOURCE_FIXED = 2;
	
    @Column(name="name", length = 255, nullable = false)
	private String name;
    @Column(name="description", length = 255, nullable = false)
	private String description;
    @Column(name="variable_prefix", length = 255, nullable = false)
	private String variablePrefix;
	
	// entity
	@Column(name="value_source_type", nullable=false)
	private int valueSource;
	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn(name="idx")
	@Column(name="value")
	@CollectionTable(name="T_STRINGCOLLECTIONS", joinColumns=@JoinColumn(name="nodetemplate_id"))
	private List<String> valuesTermList;
	@Column(name="values_endpoint")
	private String valuesEndpoint;
	@Column(name="values_graph")
	private String valuesGraph;
	@Column(name="values_root_term")
	private String valuesRootTerm; 
	@Column(name="fixed_value")
	private String fixedValue;

	// literal
	@Column(name="literal_xsd_type")
	private String literalXsdType;
	
	// filters for literals are hardcoded
	
	@Column(name="filterable")
	private Boolean filterable;
	@Column(name="color")
	private String color;
	@Column(name="x")
	private int x;
	@Column(name="y")
	private int y;
	

	public NodeTemplate() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVariablePrefix() {
		return variablePrefix;
	}
	public void setVariablePrefix(String variablePrefix) {
		this.variablePrefix = variablePrefix;
	}
	public int getNodeType() {
		return nodeType;
	}
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	public int getValueSource() {
		return valueSource;
	}
	public void setValueSource(int valueSource) {
		this.valueSource = valueSource;
	}
	public List<String> getValuesTermList() {
		return valuesTermList;
	}
	public void setValuesTermList(List<String> valuesTermList) {
		this.valuesTermList = valuesTermList;
	}
	public String getValuesEndpoint() {
		return valuesEndpoint;
	}
	public void setValuesEndpoint(String valuesEndpoint) {
		this.valuesEndpoint = valuesEndpoint;
	}
	public String getValuesGraph() {
		return valuesGraph;
	}
	public void setValuesGraph(String valuesGraph) {
		this.valuesGraph = valuesGraph;
	}
	public String getValuesRootTerm() {
		return valuesRootTerm;
	}
	public void setValuesRootTerm(String valuesRootTerm) {
		this.valuesRootTerm = valuesRootTerm;
	}
	public String getFixedValue() {
		return fixedValue;
	}
	public void setFixedValue(String fixedValue) {
		this.fixedValue = fixedValue;
	}
	public String getLiteralXsdType() {
		return literalXsdType;
	}
	public void setLiteralXsdType(String literalXsdType) {
		this.literalXsdType = literalXsdType;
	}
	public Boolean getFilterable() {
		return filterable;
	}
	public void setFilterable(Boolean filterable) {
		this.filterable = filterable;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
}
