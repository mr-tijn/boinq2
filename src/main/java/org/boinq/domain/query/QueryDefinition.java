package org.boinq.domain.query;

import java.beans.Transient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
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

import org.boinq.domain.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "T_QUERYDEFINITION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QueryDefinition implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="status")
	private Integer status;
	
	@Column(name="name")
	private String name;
	@Column(name="description")
	private String description;
	@Column(name="species")
	private String species;
	@Column(name="assembly")
	private String assembly;
	@ManyToOne
	@JoinColumn(name="owner_id")
	private User owner;

	@Column(name="result_as_table")
	private Boolean resultAsTable;
	@Column(name="sparql_query")
	private String sparqlQuery;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="querydefinition_id")
	private Set<QueryBridge> queryBridges;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="querydefinition_id")
	private Set<QueryGraph> queryGraphs;

	@Column(name="target_graph")
	private String targetGraph;
	@Column(name="target_file")
	private String targetFile;
	
	
	public QueryDefinition() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public Boolean getResultAsTable() {
		return resultAsTable;
	}
	public void setResultAsTable(Boolean resultAsTable) {
		this.resultAsTable = resultAsTable;
	}
	public Set<QueryBridge> getQueryBridges() {
		return queryBridges;
	}
	public void setQueryBridges(Set<QueryBridge> queryBridges) {
		this.queryBridges = queryBridges;
	}
	public Set<QueryGraph> getQueryGraphs() {
		return queryGraphs;
	}
	public void setQueryGraphs(Set<QueryGraph> queryGraphs) {
		this.queryGraphs = queryGraphs;
	}
	public String getTargetGraph() {
		return targetGraph;
	}
	public void setTargetGraph(String targetGraph) {
		this.targetGraph = targetGraph;
	}
	public String getTargetFile() {
		return targetFile;
	}
	public void setTargetFile(String targetFile) {
		this.targetFile = targetFile;
	}
	public String getSparqlQuery() {
		return sparqlQuery;
	}
	public void setSparqlQuery(String sparqlQuery) {
		this.sparqlQuery = sparqlQuery;
	}
	public Set<Set<QueryEdge>> findClusters() {
		Set<QueryEdge> allEdges = new HashSet<>();
		Set<Set<QueryEdge>> clusters = new HashSet<>();
		for (QueryGraph graph: getQueryGraphs()) {
			allEdges.addAll(graph.getQueryEdges());
		}
		Set<QueryEdge> candidates = new HashSet<>();
		candidates.addAll(allEdges);
		for (QueryEdge edge: allEdges) {
			if (clusters.stream().noneMatch(c -> c.contains(edge))) {
				Set<QueryEdge> cluster = new HashSet<>();
				cluster.addAll(getAdjoiningEdges(candidates,edge));
				clusters.add(cluster);
			}
		}
		return clusters;
	}
	
	@Transient
	public Set<Set<QueryBridge>> findBridgeHubs() {
		Set<Set<QueryBridge>> clusters = new HashSet<>();
		for (QueryBridge bridge: getQueryBridges()) {
			 List<Set<QueryBridge>> containing = clusters.stream().filter(cluster -> cluster.contains(bridge)).collect(Collectors.toList());
			 if (containing.isEmpty()) {
				 Set<QueryBridge> newCluster = new HashSet<>();
				 Set<QueryBridge> candidates = new HashSet<>();
				 candidates.addAll(getQueryBridges());
				 newCluster.addAll(getAdjoiningBridges(candidates, bridge));
				 clusters.add(newCluster);
			 }
		}
		return clusters;
	}
	
	@Transient
	private Set<QueryEdge> getAdjoiningEdges(Set<QueryEdge> candidates, QueryEdge edge) {
		Set<QueryEdge> adjoiningEdges = new HashSet<>();
		adjoiningEdges.add(edge);
		candidates.remove(edge); 
		Set<QueryEdge> neighbours = getNeighbouringEdges(candidates, edge);
		neighbours.retainAll(candidates);
		for (QueryEdge neighbour: neighbours) {
			adjoiningEdges.addAll(getAdjoiningEdges(candidates, neighbour));
		}
		return adjoiningEdges;
	}
	
	@Transient
	private Set<QueryEdge> getNeighbouringEdges(Set<QueryEdge> candidates, QueryEdge edge) {
		// direct neighbours
		Set<QueryEdge> neighbouringEdges = candidates.stream()
				.filter(candidate -> candidate.adjoins(edge))
				.collect(Collectors.toSet());
		// adjoining bridges
		Set<QueryBridge> neighbouringBridges = getQueryBridges().stream()
//				.filter(bridge -> QueryBridge.BRIDGE_TYPE_LITERAL_TO_LITERAL != bridge.getType())
//				.filter(bridge -> QueryBridge.BRIDGE_TYPE_LOCATION != bridge.getType())
				.filter(bridge -> bridge.adjoins(edge))
				.collect(Collectors.toSet());
		// bridged edges
		Set<QueryEdge> bridgedEdges = candidates.stream()
				.filter(candidate -> candidate.adjoins(neighbouringBridges))
				.collect(Collectors.toSet());
		neighbouringEdges.addAll(bridgedEdges);
		return neighbouringEdges;
	}
	
	
	@Transient
	private Set<QueryBridge> getAdjoiningBridges(Set<QueryBridge> candidates, QueryBridge bridge) {
		Set<QueryBridge> adjoiningBridges = new HashSet<>();
		adjoiningBridges.add(bridge);
		candidates.remove(bridge);
		Set<QueryBridge> neighbours = getNeighbouringBridges(candidates, bridge);
		neighbours.retainAll(candidates);
		for (QueryBridge neighbour: neighbours) {
			adjoiningBridges.addAll(getNeighbouringBridges(candidates, neighbour));
		}
		return adjoiningBridges;
	}
	
	@Transient
	private Set<QueryBridge> getNeighbouringBridges(Set<QueryBridge> candidates, QueryBridge bridge) {
		return candidates.stream()
				.filter(candidate -> candidate.adjoins(bridge))
				.collect(Collectors.toSet());
	}

	
}