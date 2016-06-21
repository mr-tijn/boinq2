package com.genohm.boinq.domain.match;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.http.protocol.HTTP;

import com.genohm.boinq.web.rest.dto.FeatureConnectorDTO;

@Entity
@Table(name="T_FEATURECONNECTOR")
public class FeatureConnector {
	
	public static int CONNECTOR_TYPE_ENTITY = 0;
	public static int CONNECTOR_TYPE_PATH = 1;
	
	@Column(name="type")
	private int type;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name="name")
	private String name;
	
	@Column(name="path_expression")
	private String pathExpression;
	
	
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
	public String getPathExpression() {
		return pathExpression;
	}
	public void setPathExpression(String pathExpression) {
		this.pathExpression = pathExpression;
	}
	
	public FeatureConnectorDTO createDTO() {
		FeatureConnectorDTO result = new FeatureConnectorDTO();
		result.name = this.name;
		result.pathExpression = this.pathExpression;
		return result;
	}
	
}
