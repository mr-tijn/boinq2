package org.boinq.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.jena.graph.Node;
import org.boinq.domain.query.GraphTemplate;
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
	public static final int STATUS_PROCESSING = 2;
	public static final int STATUS_DONE = 3;
	public static final int STATUS_ERROR = 4;
	
	public static final String TYPE_GENERIC = "generic";
	public static final String TYPE_FEATURE = "feature";

	
	//meta info
	@Transient
	private List<Map<String,String>> supportedFilters;
	@Transient
	private List<Map<String, String>> supportedConnectors;
	@Transient
	private Map<String,String> supportedFeatureTypes;
	@Transient
	private Map<Node, Node> referenceMap;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 200)
    @Column(name = "graph_name", nullable = true)
    private String graphName;
    
    @Size(min =1, max = 200)
    @Column(name = "file_type", nullable = true)
    private String fileType;

    @Size(min = 1, max = 200)
    @Column(name = "name", nullable = true)
    private String name;
    
	@Lob
	@Column(name="description")
	private String description;

    @Size(min = 1, max = 500)
    @Column(name = "type", nullable = true)
    private String type;
    
    @Size(min = 1, max = 200)
    @Column(name = "species", nullable = true)
    private String species;
    
    @Size(min = 1, max = 200)
    @Column(name = "assembly", nullable = true)
    private String assembly;
    
    @Size(min = 1, max = 200)
    @Column(name = "contig_prefix", nullable = true)
    private String contigPrefix;
    
    @Column(name = "entry_count")
    private long entryCount;
    
    @Column(name = "feature_count")
    private long featureCount;
    
    @Column(name = "triple_count")
    private long tripleCount;
    
    @OneToMany(fetch=FetchType.EAGER, orphanRemoval=true,targetEntity=RawDataFile.class)
    private Set<RawDataFile> rawDataFiles;
    
    @ManyToOne
    private Datasource datasource;
    
    @ManyToOne
    @JoinColumn(name="graphtemplate_id")
    private GraphTemplate graphTemplate;
    
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

	public long getEntryCount() {
		return entryCount;
	}
	
	public void setEntryCount(long entryCount) {
		this.entryCount = entryCount;
	}
	
	public long getFeatureCount() {
		return featureCount;
	}

	public void setFeatureCount(long featureCount) {
		this.featureCount= featureCount;
	}
	
	public long getTripleCount() {
		return tripleCount;
	}

	public void setTripleCount(long tripleCount) {
		this.tripleCount= tripleCount;
	}
	
	public String getFileType() {
		return fileType;
	}
	public void setFileType (String fileType) {
		this.fileType = fileType;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getSpecies() {
		return species;
	}
	
	public void setSpecies(String species) {
		this.species = species;
	}
	
	public String getAssembly() {
		return assembly;
	}
	
	public void setAssembly(String assembly){
		this.assembly = assembly;
	}
	
	public String getContigPrefix() {
		return contigPrefix;
	}

	public void setContigPrefix(String contigPrefix){
		this.contigPrefix = contigPrefix;
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

	public GraphTemplate getGraphTemplate() {
		return graphTemplate;
	}

	public void setGraphTemplate(GraphTemplate graphTemplate) {
		this.graphTemplate = graphTemplate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setSupportedFilters(List<Map<String, String>> supportedFilters) {
		this.supportedFilters = supportedFilters;
	}
	
	public List<Map<String,String>> getSupportedFilters() {
		return supportedFilters;
	}

	public Map<String,String> getSupportedFeatureTypes() {
		return supportedFeatureTypes;
	}

	public void setSupportedFeatureTypes(Map<String,String> supportedFeatureTypes) {
		this.supportedFeatureTypes = supportedFeatureTypes;
	}

	public Map<Node, Node> getReferenceMap() {
		return referenceMap;
	}

	public void setReferenceMap(Map<Node, Node> referenceMap) {
		this.referenceMap = referenceMap;
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
                ", entryCount=" + entryCount +
                ", featureCount=" + featureCount +
                ", tripleCount=" + tripleCount +
                ", fileType=" + fileType + 
                ", name='" + name + "'" +
                ", type='" + type + "'" +
                ", species='" + species + "'" +
                ", assembly='" + assembly + "'" +
                ", contigPrefix=" + contigPrefix + "'" +
                ", rawDataFiles = [" + rawDataFilesString + "]" +
                ", supportedFilters = [" + supportedFilters + "]" +
                ", supportedConnectors = [" + supportedConnectors + "]" +
                '}';
    }

    public List<Map<String, String>> getSupportedConnectors() {
    	return this.supportedConnectors;
    }
    
	public void setSupportedConnectors(List<Map<String, String>> connectors) {
		this.supportedConnectors = connectors;
	}
}
