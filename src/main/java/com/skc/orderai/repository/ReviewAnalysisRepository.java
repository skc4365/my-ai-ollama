package com.skc.orderai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skc.orderai.entity.ReviewAnalysis;

/**
 * qwen_review_analysis 테이블(AI 감정 및 요약 분석 데이터)에 접근하기 위한 Spring Data JPA Repository 인터페이스입니다.
 * JpaRepository를 상속받아 기본적인 등록, 조회, 수정, 삭제(CRUD) 및 페이징 처리를 별도 구현 없이 제공합니다.
 */
public interface ReviewAnalysisRepository
        extends JpaRepository<ReviewAnalysis, Long> {

}