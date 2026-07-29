package com.skc.orderai.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skc.orderai.dto.ReviewRequest;
import com.skc.orderai.dto.ReviewResponse;
import com.skc.orderai.entity.Review;
import com.skc.orderai.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 후기 등록 + AI 분석
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> save( @RequestBody ReviewRequest request ){

        ReviewResponse response = reviewService.save(request);

        return ResponseEntity.ok(response);

    }


    /**
     * 전체 후기 조회
     */
    @GetMapping
    public ResponseEntity<List<Review>> findAll(){

        return ResponseEntity.ok( reviewService.findAll() );

    }


    /**
     * 후기 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Review> findById( @PathVariable("id") Long id ) {
    	
        return ResponseEntity.ok( reviewService.findById(id) );
        
    }

}