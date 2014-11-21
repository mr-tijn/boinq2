package com.genohm.boinq.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Datasource.
 */
@Entity
@Table(name = "T_DATASOURCE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Datasource implements Serializable {

	private static final long serialVersionUID = -8065826415415661663L;
	public static final int TYPE_LOCAL_FALDO = 0;
	public static final int TYPE_REMOTE_FALDO = 1;
	public static final int TYPE_LOCAL_SPARQL = 2;
	public static final int TYPE_REMOTE_SPARQL = 3;

	public static final int STATUS_EMPTY = 0;
	public static final int STATUS_RAW_DATA = 1;
	
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Size(min = 1, max = 200)
    @Column(name = "endpoint_url", nullable = true)
    private String endpointUrl;

    @Size(min = 1, max = 200)
    @Column(name = "endpoint_update_url", nullable = true)
    private String endpointUpdateUrl;
   
    @Size(min = 1, max = 200)
    @Column(name = "meta_endpoint_url", nullable = true)
    private String metaEndpointUrl;
    
    @Size(min = 1, max = 200)
    @Column(name = "graph_name", nullable = true)
    private String graphName;
    
    @Size(min = 1, max = 200)
    @Column(name = "meta_graph_name", nullable = true)
    private String metaGraphName;
    
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
    
    @OneToMany(fetch=FetchType.EAGER, orphanRemoval=true)
    private Set<RawDataFile> rawDataFiles;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getEndpointUpdateUrl() {
		return endpointUpdateUrl;
	}

	public void setEndpointUpdateUrl(String endpointUpdateUrl) {
		this.endpointUpdateUrl = endpointUpdateUrl;
	}

	public String getMetaEndpointUrl() {
		return metaEndpointUrl;
	}

	public void setMetaEndpointUrl(String metaEndpointUrl) {
		this.metaEndpointUrl = metaEndpointUrl;
	}

	public String getGraphName() {
		return graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public String getMetaGraphName() {
		return metaGraphName;
	}

	public void setMetaGraphName(String metaGraphName) {
		this.metaGraphName = metaGraphName;
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

	public Set<RawDataFile> getRawDataFiles() {
		return rawDataFiles;
	}

	public void setRawDataFiles(Set<RawDataFile> rawDataFiles) {
		this.rawDataFiles = rawDataFiles;
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
    	String rawDataFilesString = "";
    	for (RawDataFile rawDataFile : rawDataFiles) {
    		rawDataFilesString += rawDataFile + ",";
    	}
    	if (rawDataFilesString.length() > 0) {
    		rawDataFilesString = rawDataFilesString.substring(0,rawDataFilesString.length() - 1);
    	}
        return "Datasource{" +
                "id=" + id +
                ", endpointUrl='" + endpointUrl + '\'' +
                ", endpointUpdateUrl='" + endpointUpdateUrl + '\'' +
                ", metaEndpointUrl='" + metaEndpointUrl + '\'' +
                ", graphName='" + graphName + "'" +
                ", metaGraphName='" + metaGraphName + "'" +
                ", public=" + isPublic +
                ", type=" + type + 
                ", status=" + status +
                ", raw data files= [" +rawDataFilesString + "] " +
                '}';
    }
}
