package org.boinq.domain.query;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.expr.nodevalue.NodeValueDouble;
import org.apache.jena.sparql.expr.nodevalue.NodeValueInteger;
import org.apache.jena.sparql.expr.nodevalue.NodeValueString;
import org.apache.jena.vocabulary.XSD;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "T_NODEFILTER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NodeFilter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5482918562348397479L;
	public static final int FILTER_TYPE_GENERIC_EQUALS = 0;
	public static final int FILTER_TYPE_GENERIC_BETWEEN = 1;
	public static final int FILTER_TYPE_GENERIC_STARTSWITH = 2;
	public static final int FILTER_TYPE_GENERIC_CONTAINS = 3;
	
	public static final int FILTER_TYPE_GENERIC_VALUES = 20;
	
	public static final int FILTER_TYPE_FALDOLOCATION = 21;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name="type", nullable=false)
	private int type;
	
	// literal
	
	@Column(name="case_insensitive", nullable=false)
	private Boolean caseInsensitive = false;
	@Column(name="negate", nullable=false)
	private Boolean not = false;
	@Column(name="exact_match")
	private Boolean exactMatch = false;
	
	@Column(name="min_integer")
	private Long minInteger;
	@Column(name="max_integer")
	private Long maxInteger;
	@Column(name="integer_value")
	private Long integerValue;
	@Column(name="min_double")
	private Double minDouble;
	@Column(name="max_double")
	private Double maxDouble;
	@Column(name="double_value")
	private Double doubleValue;
	@Column(name="include_min")
	private Boolean includeMin;
	@Column(name="include_max")
	private Boolean includeMax;
	
	@Column(name="string_value")
	private String stringValue;

	// entity: values
	@Lob
	@Column(name="term_values")
	private String termValues;
	
	@Column(name="contig")
    private String contig;
	@Column(name="strand")
    private Boolean strand;
	
	public NodeFilter() {}
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
	public Boolean getCaseInsensitive() {
		return caseInsensitive;
	}
	public void setCaseInsensitive(Boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}
	public Long getMinInteger() {
		return minInteger;
	}
	public Boolean getNot() {
		return not;
	}
	public void setNot(Boolean not) {
		this.not = not;
	}
	public Boolean getExactMatch() {
		return exactMatch;
	}
	public void setExactMatch(Boolean exactMatch) {
		this.exactMatch = exactMatch;
	}
	public void setMinInteger(Long minInteger) {
		this.minInteger = minInteger;
	}
	public Long getMaxInteger() {
		return maxInteger;
	}
	public void setMaxInteger(Long maxInteger) {
		this.maxInteger = maxInteger;
	}
	public Long getIntegerValue() {
		return integerValue;
	}
	public void setIntegerValue(Long integerValue) {
		this.integerValue = integerValue;
	}
	public Double getMinDouble() {
		return minDouble;
	}
	public void setMinDouble(Double minDouble) {
		this.minDouble = minDouble;
	}
	public Double getMaxDouble() {
		return maxDouble;
	}
	public void setMaxDouble(Double maxDouble) {
		this.maxDouble = maxDouble;
	}
	public Double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}
	public Boolean getIncludeMin() {
		return includeMin;
	}
	public void setIncludeMin(Boolean includeMin) {
		this.includeMin = includeMin;
	}
	public Boolean getIncludeMax() {
		return includeMax;
	}
	public void setIncludeMax(Boolean includeMax) {
		this.includeMax = includeMax;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	public List<String> getTermValues() {
		return (termValues != null ? Arrays.asList(termValues.split(" ")): new LinkedList<>());
	}
	public void setTermValues(List<String> termValues) {
		this.termValues = termValues.stream().collect(Collectors.joining(" "));
	}
	public String getContig() {
		return contig;
	}
	public void setContig(String contig) {
		this.contig = contig;
	}
	public Boolean getStrand() {
		return strand;
	}
	public void setStrand(Boolean strand) {
		this.strand = strand;
	}

	private static final List<String> decimalTypes = Arrays.asList(XSD.decimal, XSD.xdouble, XSD.xfloat).stream().map((Resource node) -> node.asNode().toString()).collect(Collectors.toList());
	private static final List<String> integerTypes = Arrays.asList(XSD.integer, XSD.negativeInteger, XSD.nonNegativeInteger, XSD.positiveInteger, XSD.nonPositiveInteger, XSD.unsignedInt, XSD.xint, XSD.xlong, XSD.unsignedLong, XSD.xshort, XSD.unsignedShort).stream().map((Resource node) -> node.asNode().toString()).collect(Collectors.toList());
	private static final List<String> stringTypes = Arrays.asList(XSD.normalizedString, XSD.xstring).stream().map((Resource node) -> node.asNode().toString()).collect(Collectors.toList());
	
	@Transient
	public static Boolean isInteger(String nodeType) {
		return integerTypes.stream().anyMatch(t -> t.equals(nodeType));
	}
	
	@Transient
	public static Boolean isDecimal(String nodeType) {
		return decimalTypes.stream().anyMatch(t -> t.equals(nodeType));
	}
	
	@Transient
	public static Boolean isString(String nodeType) {
		return stringTypes.stream().anyMatch(t -> t.equals(nodeType));
	}
	
	@Transient
	public NodeValue getValue(String nodeType) {
		NodeValue value = null;
		if (isInteger(nodeType)) {
			value = new NodeValueInteger(this.getIntegerValue());
		}
		else if (isDecimal(nodeType)) {
			value = new NodeValueDouble(this.getDoubleValue());
		}
		else if (isString(nodeType)) {
			value = new NodeValueString(this.getStringValue());
		}
		return value;
	}
	
	@Transient
	public NodeValue getMinValue(String nodeType) {
		NodeValue value = null;
		if (isInteger(nodeType)) {
			value = new NodeValueInteger(this.getMinInteger());
		}
		else if (isDecimal(nodeType)) {
			value = new NodeValueDouble(this.getMinDouble());
		}
		return value;
	}

	@Transient
	public NodeValue getMaxValue(String nodeType) {
		NodeValue value = null;
		if (isInteger(nodeType)) {
			value = new NodeValueInteger(this.getMaxInteger());
		}
		else if (isDecimal(nodeType)) {
			value = new NodeValueDouble(this.getMaxDouble());
		}
		return value;
	}

	
}
