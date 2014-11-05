package com.genohm.boinq.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RawDataFile.
 */
@Entity
@Table(name = "T_RAWDATAFILE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RawDataFile implements Serializable {

	private static final long serialVersionUID = -7426424004291201354L;
	
	public static final int STATUS_LOADING = 0;
	public static final int STATUS_COMPLETE = 1;
	public static final int STATUS_ERROR = 2;

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Size(min = 1, max = 500)
    @Column(name = "file_path")
    private String filePath;

    @Column(name = "status")
    private int status;

    @ManyToOne
    private Datasource datasource;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

    public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}

	public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RawDataFile rawdatafile = (RawDataFile) o;

        if (id != rawdatafile.id) {
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
        return "RawDataFile{" +
                "id=" + id +
                "status=" + status +
                "datasource='" + (datasource==null?"":datasource.getName()) + "'" +
                ", filePath='" + filePath + '\'' +
                '}';
    }

    public RawDataFile() {}
    
	public RawDataFile(String filePath) {
		super();
		this.filePath = filePath;
	}
}
