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


@Service
@RequiredArgsConstructor
public class ReviewService {


	private final ReviewRepository reviewRepository;
    private final ReviewAnalysisRepository reviewAnalysisRepository;
    private final ReviewAiService reviewAiService;
    private final ObjectMapper objectMapper;



    public ReviewResponse save(ReviewRequest request) {

        Review review = new Review();
        review.setOrderId(request.getOrderId());
        review.setContent(request.getContent());

        Review saved = reviewRepository.save(review);

        String aiResult = reviewAiService.analyze(saved.getContent());

        try {

            JsonNode json = objectMapper.readTree(aiResult);

            String sentiment = json.get("sentiment").asText();
            String summary = json.get("summary").asText();

            ReviewAnalysis analysis = new ReviewAnalysis();
            analysis.setReviewId(saved.getId());
            analysis.setSentiment(sentiment);
            analysis.setSummary(summary);

            reviewAnalysisRepository.save(analysis);

            return new ReviewResponse(
                    saved.getId(),
                    sentiment,
                    summary
            );

        } catch (Exception e) {
            throw new RuntimeException("AI 응답 파싱 실패", e);
        }
    }



    public List<Review> findAll(){

        return reviewRepository.findAll();

    }




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