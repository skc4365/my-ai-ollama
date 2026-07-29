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
@Table(name = "qwen_review")
@Getter
@Setter
@NoArgsConstructor
public class Review {

	@Id
	@GeneratedValue
	private Long id;

	private Long orderId;

	@Column(length = 3000)
	private String content;
}
