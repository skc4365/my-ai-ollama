package com.skc.orderai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skc.orderai.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
