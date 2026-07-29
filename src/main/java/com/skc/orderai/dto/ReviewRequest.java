package com.skc.orderai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewRequest {


    @Schema(
        description="주문번호",
        example="1"
    )
    private Long orderId;



    @Schema(
        description="고객 후기",
        example="배송이 빠르고 제품이 좋아요"
    )
    private String content;


}