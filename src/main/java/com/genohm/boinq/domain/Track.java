package com.genohm.boinq.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Track.
 */
@Entity
@Table(name = "T_TRACK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Track implements Serializable {

	private static final long serialVersionUID = 6345156276128476305L;
	public static final int STATUS_EMPTY = 0;
	public static final int STATUS_RAW_DATA = 1;
	
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Size(min = 1, max = 200)
    @Column(name = "graph_name", nullable = true)
    private String graphName;

    @Size(min = 1, max = 200)
    @Column(name = "name", nullable = true)
    private String name;

    @Size(min = 1, max = 500)
    @Column(name = "type", nullable = true)
    private String type;
    
    @OneToMany(fetch=FetchType.EAGER, orphanRemoval=true,targetEntity=RawDataFile.class)
    private Set<RawDataFile> rawDataFiles;
    
    @ManyToOne
    private Datasource datasource;
    
    @Column(name="status")
    private int status;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
	public String getGraphName() {
		return graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<RawDataFile> getRawDataFiles() {
		return rawDataFiles;
	}

	public void setRawDataFiles(Set<RawDataFile> rawDataFiles) {
		this.rawDataFiles = rawDataFiles;
	}

	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
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

        Track track = (Track) o;

        if (id != track.id) {
            return false;
        }

        return true;
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
        return "Track{" +
                "id=" + id +
                ", graphName='" + graphName + "'" +
                ", name='" + name + "'" +
                ", type='" + type + "'" +
                ", rawDataFiles = [" + rawDataFilesString + "]" +
                '}';
    }
}
