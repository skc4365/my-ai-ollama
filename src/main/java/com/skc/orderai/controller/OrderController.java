package com.skc.orderai.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skc.orderai.entity.Order;
import com.skc.orderai.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderRepository repository;

	@PostMapping
	public Order save( @RequestBody Order order) {

		return repository.save(order);

	}

	@GetMapping
	public List<Order> list() {

		return repository.findAll();

	}

}
