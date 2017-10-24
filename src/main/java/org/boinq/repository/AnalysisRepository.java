package org.boinq.repository;

import org.boinq.domain.jobs.analysis.GenomeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisRepository extends JpaRepository<GenomeAnalysis, Long> {

}
