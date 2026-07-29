package com.skc.orderai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "qwen_review_analysis")
@Getter
@Setter
@NoArgsConstructor
public class ReviewAnalysis {
	
	@Id
    @GeneratedValue
    private Long id;

    private Long reviewId;

    private String sentiment;

    @Column(length = 2000)
    private String summary;

}
