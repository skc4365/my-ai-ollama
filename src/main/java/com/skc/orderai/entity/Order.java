package com.skc.orderai.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "qwen_order")
@Getter
@Setter
@NoArgsConstructor
public class Order {
	
	@Id
	@GeneratedValue
	private Long id;

	private String customer;

	private String product;

}
