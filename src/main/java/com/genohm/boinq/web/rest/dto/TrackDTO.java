package com.genohm.boinq.web.rest.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.jena.graph.Node;

import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.domain.Track;

public class TrackDTO {

	private long id;
	private int status;
	private String graphName;
	private String fileType;
	private String name;
	private String type;
	private String species;
	private String assembly;
	private String contigPrefix;
	private long entryCount;
	private long featureCount;
	private long tripleCount;
	private long datasourceId;
	private Set<RawDataFileDTO> rawDataFiles;
	private Map<String, Map<String, String>> supportedOperators;
	private Map<String, String> supportedFeatureTypes;
	private Map<String, String> referenceMap;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
		this.featureCount = featureCount;
	}

	public long getTripleCount() {
		return tripleCount;
	}

	public void setTripleCount(long tripleCount) {
		this.tripleCount = tripleCount;
	}

	public String getGraphName() {
		return graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
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

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getAssembly() {
		return assembly;
	}

	public void setAssembly(String assembly) {
		this.assembly = assembly;
	}

	public String getContigPrefix() {
		return contigPrefix;
	}

	public void setContigPrefix(String contigPrefix) {
		this.contigPrefix = contigPrefix;
	}

	public long getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(long datasourceId) {
		this.datasourceId = datasourceId;
	}

	public Set<RawDataFileDTO> getRawDataFiles() {
		return rawDataFiles;
	}

	public void setRawDataFiles(Set<RawDataFileDTO> rawDataFiles) {
		this.rawDataFiles = rawDataFiles;
	}

	public Map<String, Map<String, String>> getSupportedOperators() {
		return supportedOperators;
	}

	public void setSupportedOperators(Map<String, Map<String, String>> supportedOperators) {
		this.supportedOperators = supportedOperators;
	}

	public Map<String, String> getSupportedFeatureTypes() {
		return supportedFeatureTypes;
	}

	public void setSupportedFeatureTypes(Map<String, String> supportedFeatureTypes) {
		this.supportedFeatureTypes = supportedFeatureTypes;
	}

	public Map<String, String> getReferenceMap() {
		return referenceMap;
	}

	public void setReferenceMap(Map<String, String> referenceMap) {
		this.referenceMap = referenceMap;
	}

	public TrackDTO() {
	}

	public TrackDTO(Track track) {
		this.id = track.getId();
		this.status = track.getStatus();
		this.entryCount = track.getEntryCount();
		this.featureCount = track.getFeatureCount();
		this.tripleCount = track.getTripleCount();
		this.name = track.getName();
		this.graphName = track.getGraphName();
		this.fileType = track.getFileType();
		this.type = track.getType();
		this.species = track.getSpecies();
		this.assembly = track.getAssembly();
		this.contigPrefix = track.getContigPrefix();
		this.rawDataFiles = new HashSet<RawDataFileDTO>();
		for (RawDataFile dataFile : track.getRawDataFiles()) {
			rawDataFiles.add(new RawDataFileDTO(dataFile));
		}
		if (track.getSupportedOperators() != null) {
			this.supportedOperators = new HashMap<>();
			for (String operator : track.getSupportedOperators().keySet()) {
				Map<String, String> parameters = new HashMap<>();
				for (String parameter : track.getSupportedOperators().get(operator).keySet()) {
					parameters.put(parameter, track.getSupportedOperators().get(operator).get(parameter));
				}
				supportedOperators.put(operator, parameters);
			}
		}
		if (track.getSupportedFeatureTypes() != null) {
			this.supportedFeatureTypes = new HashMap<>();
			for (String typeName : track.getSupportedFeatureTypes().keySet()) {
				this.supportedFeatureTypes.put(typeName, track.getSupportedFeatureTypes().get(typeName));
			}
		}
		if (track.getReferenceMap() != null) {
			this.referenceMap = new HashMap<>();
			for (Node ref : track.getReferenceMap().keySet()) {
				this.referenceMap.put(ref.getURI(), track.getReferenceMap().get(ref).getURI());
			}
		}
	}
}
