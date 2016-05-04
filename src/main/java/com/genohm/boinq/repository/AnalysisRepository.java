package com.genohm.boinq.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.jobs.analysis.GenomeAnalysis;

public interface AnalysisRepository extends JpaRepository<GenomeAnalysis, Long> {

}
