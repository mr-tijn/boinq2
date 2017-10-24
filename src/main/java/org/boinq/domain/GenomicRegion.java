package org.boinq.domain;

public class GenomicRegion {
	public Long start;
	public Long end;
	public String assemblyURI; 
	public Boolean strand;
	
	public Boolean overlaps(GenomicRegion target) {
		if (!assemblyURI.equals(target.assemblyURI)) return false;
		if (strand != null) {
			if (target.strand != null && target.strand != strand) return false;
		}
		return (start <= target.end && end >= target.start);
	}
	
	public GenomicRegion intersect(GenomicRegion target) {
		if (overlaps(target)) {
			GenomicRegion intersect = new GenomicRegion();
			intersect.assemblyURI = assemblyURI;
			if (strand != null && target.strand != null) {
				intersect.strand = strand;
			}
			intersect.start = Math.max(start, target.start);
			intersect.end = Math.min(end, target.end);
			return intersect;
		} else {
			return null;
		}
	}
}
