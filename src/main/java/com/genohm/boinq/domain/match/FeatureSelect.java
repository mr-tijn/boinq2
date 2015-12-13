package com.genohm.boinq.domain.match;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.tools.generators.QueryGenerator;

@Entity
@Table(name="T_FEATURESELECT")
public class FeatureSelect implements QueryGeneratorAcceptor {

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="track_id")
	private Track track;
    
    @Column(name="retrieve_feature_data")
	private Boolean retrieveFeatureData;
    
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="feature_select_id")
	private Set<FeatureSelectCriterion> criteria = new HashSet<>();
	
    //visualisation properties
    @Column(name="view_x")
    private int viewX;

    @Column(name="view_y")
    private int viewY;
    
	public FeatureSelect() {}
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getRetrieveFeatureData() {
		return retrieveFeatureData;
	}

	public void setRetrieveFeatureData(Boolean retrieveFeatureData) {
		this.retrieveFeatureData = retrieveFeatureData;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public void setViewX(int viewX) {
		this.viewX = viewX;
	}

	public void setViewY(int viewY) {
		this.viewY = viewY;
	}

	public Track getTrack() {
		return track;
	}
	public Boolean retrieveFeatureData() {
		return retrieveFeatureData;
	}
	
	public Set<FeatureSelectCriterion> getCriteria() {
		return criteria;
	}
	
	public int getViewX() {
		return viewX;
	}

	public int getViewY() {
		return viewY;
	}

	public void addCriteria(FeatureSelectCriterion criterion) {
		criteria.add(criterion);
	}
	
	public void accept(QueryGenerator qg, GenomicRegion region) {
		qg.visit(this, region);
	}
}
