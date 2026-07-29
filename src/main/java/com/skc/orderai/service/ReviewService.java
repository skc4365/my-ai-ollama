package com.skc.orderai.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skc.orderai.dto.ReviewRequest;
import com.skc.orderai.dto.ReviewResponse;
import com.skc.orderai.entity.Review;
import com.skc.orderai.entity.ReviewAnalysis;
import com.skc.orderai.repository.ReviewAnalysisRepository;
import com.skc.orderai.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


/**
 * 고객의 리뷰(후기) 정보를 관리하고, AI 분석 서비스(Ollama)를 호출하여 
 * 감정 및 요약 결과를 데이터베이스에 함께 저장하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class ReviewService {


	private final ReviewRepository reviewRepository;
    private final ReviewAnalysisRepository reviewAnalysisRepository;
    private final ReviewAiService reviewAiService;
    private final ObjectMapper objectMapper;



    /**
     * 고객 후기를 저장하고 AI를 통해 감정 및 요약을 분석하여 저장합니다.
     *
     * @param request 주문 번호와 리뷰 내용이 포함된 DTO
     * @return 리뷰 ID, AI 감정 분석 결과(긍정/부정 등), 요약 내용을 담은 응답 DTO
     */
    public ReviewResponse save(ReviewRequest request) {

        // 1. 요청 데이터를 기반으로 새로운 Review 엔티티 객체 생성
        Review review = new Review();
        review.setOrderId(request.getOrderId());
        review.setContent(request.getContent());

        // 2. 고객의 리뷰 내용을 데이터베이스(qwen_review 테이블)에 일차 저장
        Review saved = reviewRepository.save(review);

        // 3. Spring AI + Ollama 연동 서비스를 호출하여 리뷰 텍스트 분석 요청 (JSON String 수신)
        String aiResult = reviewAiService.analyze(saved.getContent());

        try {
            // 4. AI로부터 전달된 JSON 형식의 문자열 데이터를 Jackson 라이브러리를 사용해 파싱
            JsonNode json = objectMapper.readTree(aiResult);

            // 5. JSON 데이터 노드로부터 감정 분석 결과(sentiment)와 요약(summary) 텍스트 추출
            String sentiment = json.get("sentiment").asText();
            String summary = json.get("summary").asText();

            // 6. 분석 결과를 관리할 ReviewAnalysis 엔티티 객체 생성 및 리뷰 식별자(PK) 매핑
            ReviewAnalysis analysis = new ReviewAnalysis();
            analysis.setReviewId(saved.getId());
            analysis.setSentiment(sentiment);
            analysis.setSummary(summary);

            // 7. AI 분석 및 요약 데이터를 데이터베이스(qwen_review_analysis 테이블)에 최종 저장
            reviewAnalysisRepository.save(analysis);

            // 8. 클라이언트에 반환할 최종 DTO 생성 및 리턴
            return new ReviewResponse(
                    saved.getId(),
                    sentiment,
                    summary
            );

        } catch (Exception e) {
            // AI 응답 형식이 올바르지 않거나 JSON 파싱 처리 중 예외 발생 시 예외 전파
            throw new RuntimeException("AI 응답 파싱 실패", e);
        }
    }



    /**
     * 데이터베이스에 저장된 모든 고객 후기(리뷰) 목록을 가져옵니다.
     *
     * @return 전체 리뷰 엔티티 리스트
     */
    public List<Review> findAll(){

        return reviewRepository.findAll();

    }




    /**
     * 특정 리뷰 식별자(ID)에 해당하는 후기 상세 내용을 단건 조회합니다.
     *
     * @param id 조회하고자 하는 리뷰의 일련번호 (PK)
     * @return 해당 리뷰 엔티티 객체
     * @throws RuntimeException 지정한 ID에 부합하는 데이터가 없을 경우 예외 유발
     */
    public Review findById(Long id){

        return reviewRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Review not found : "
                        + id
                    )
                );

    }

}