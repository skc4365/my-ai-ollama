package com.skc.orderai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skc.orderai.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
