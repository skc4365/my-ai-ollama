package com.skc.orderai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skc.orderai.entity.ReviewAnalysis;

public interface ReviewAnalysisRepository
        extends JpaRepository<ReviewAnalysis, Long> {

}