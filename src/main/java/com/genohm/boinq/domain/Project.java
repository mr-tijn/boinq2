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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "T_PROJECT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Project implements Serializable {

	private static final long serialVersionUID = 7693586791588617522L;

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;
	
    @ManyToMany(fetch = FetchType.EAGER,targetEntity=Track.class)
    private Set<Track> tracks;
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    
    @Size(min = 1, max = 50)
    @Column(name = "title")
    private String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Track> getTracks() {
		return tracks;
	}

	public void setTracks(Set<Track> tracks) {
		this.tracks = tracks;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Project project = (Project) o;

        if (id != project.id) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
       	String tracksString = "";
    	for (Track track : tracks) {
    		tracksString += track + ",";
    	}
    	if (tracksString.length() > 0) {
    		tracksString = tracksString.substring(0,tracksString.length() - 1);
    	}
   	    	
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", tracks= " + tracksString + "]" +
                '}';
    }
}
