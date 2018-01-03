package org.boinq.domain.query;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "T_NODETEMPLATE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NodeTemplate implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5693255674095768036L;

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
	@Column(name="fixed_type")
	private String fixedType;

	// literal
	@Column(name="literal_xsd_type")
	private String literalXsdType;
	@Column(name="assembly")
	private String assembly;
	
	// filters for literals are hardcoded
	
	// generic
	@Column(name="filterable")
	private Boolean filterable;
	@Column(name="color")
	private String color;
	@Column(name="x")
	private int x;
	@Column(name="y")
	private int y;
	
	@Column(name="idx")
	private Integer idx;

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
	public String getFixedType() {
		return fixedType;
	}
	public void setFixedType(String fixedType) {
		this.fixedType = fixedType;
	}
	public String getLiteralXsdType() {
		return literalXsdType;
	}
	public void setLiteralXsdType(String literalXsdType) {
		this.literalXsdType = literalXsdType;
	}
	public String getAssembly() {
		return assembly;
	}
	public void setAssembly(String assembly) {
		this.assembly = assembly;
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
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	
	
	@Override
	public String toString() {
		return "(" + id + "|" + (name != null?name:variablePrefix) + ")";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assembly == null) ? 0 : assembly.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((filterable == null) ? 0 : filterable.hashCode());
		result = prime * result + ((fixedType == null) ? 0 : fixedType.hashCode());
		result = prime * result + ((fixedValue == null) ? 0 : fixedValue.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((literalXsdType == null) ? 0 : literalXsdType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + nodeType;
		result = prime * result + valueSource;
		result = prime * result + ((valuesEndpoint == null) ? 0 : valuesEndpoint.hashCode());
		result = prime * result + ((valuesGraph == null) ? 0 : valuesGraph.hashCode());
		result = prime * result + ((valuesRootTerm == null) ? 0 : valuesRootTerm.hashCode());
		result = prime * result + ((valuesTermList == null) ? 0 : valuesTermList.hashCode());
		result = prime * result + ((variablePrefix == null) ? 0 : variablePrefix.hashCode());
		result = prime * result + x;
		result = prime * result + y;
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
		NodeTemplate other = (NodeTemplate) obj;
		if (assembly == null) {
			if (other.assembly != null)
				return false;
		} else if (!assembly.equals(other.assembly))
			return false;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (filterable == null) {
			if (other.filterable != null)
				return false;
		} else if (!filterable.equals(other.filterable))
			return false;
		if (fixedType == null) {
			if (other.fixedType != null)
				return false;
		} else if (!fixedType.equals(other.fixedType))
			return false;
		if (fixedValue == null) {
			if (other.fixedValue != null)
				return false;
		} else if (!fixedValue.equals(other.fixedValue))
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
		if (literalXsdType == null) {
			if (other.literalXsdType != null)
				return false;
		} else if (!literalXsdType.equals(other.literalXsdType))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nodeType != other.nodeType)
			return false;
		if (valueSource != other.valueSource)
			return false;
		if (valuesEndpoint == null) {
			if (other.valuesEndpoint != null)
				return false;
		} else if (!valuesEndpoint.equals(other.valuesEndpoint))
			return false;
		if (valuesGraph == null) {
			if (other.valuesGraph != null)
				return false;
		} else if (!valuesGraph.equals(other.valuesGraph))
			return false;
		if (valuesRootTerm == null) {
			if (other.valuesRootTerm != null)
				return false;
		} else if (!valuesRootTerm.equals(other.valuesRootTerm))
			return false;
		if (valuesTermList == null) {
			if (other.valuesTermList != null)
				return false;
		} else if (!valuesTermList.equals(other.valuesTermList))
			return false;
		if (variablePrefix == null) {
			if (other.variablePrefix != null)
				return false;
		} else if (!variablePrefix.equals(other.variablePrefix))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	
}
