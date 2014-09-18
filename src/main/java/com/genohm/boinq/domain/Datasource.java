package com.genohm.boinq.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.genohm.boinq.domain.util.CustomLocalDateSerializer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;

/**
 * A Datasource.
 */
@Entity
@Table(name = "T_DATASOURCE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Datasource implements Serializable {

	public static final int DATASOURCE_TYPE_LOCAL_FALDO = 0;
	public static final int DATASOURCE_TYPE_REMOTE_FALDO = 1;

	public static final int DATASOURCE_STATUS_EMPTY = 0;
	public static final int DATASOURCE_STATUS_RAW_DATA = 1;
	public static final int DATASOURCE_STATUS_LOADING = 2;
	public static final int DATASOURCE_STATUS_COMPLETE = 3;
	public static final int DATASOURCE_STATUS_ERROR = 4;
	
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Size(min = 1, max = 200)
    @Column(name = "endpoint_url", nullable = true)
    private String endpointUrl;

    @Size(min = 1, max = 200)
    @Column(name = "graph_name", nullable = true)
    private String graphName;
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    
    @Column(name = "is_public")
    private Boolean isPublic;
    
    
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private int type;
    
    @Column(name="status")
    private int status;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getGraphName() {
		return graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Datasource datasource = (Datasource) o;

        if (id != datasource.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Datasource{" +
                "id=" + id +
                ", endpointUrl='" + endpointUrl + '\'' +
                ", graphName='" + graphName + "'" +
                ", public='" + isPublic +"'" +
                ", type=" + type + ", status=" + status +
                '}';
    }
}
