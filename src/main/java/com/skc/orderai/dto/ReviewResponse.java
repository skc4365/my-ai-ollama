package com.skc.orderai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class ReviewResponse {


    private Long reviewId;


    private String sentiment;


    private String summary;


}