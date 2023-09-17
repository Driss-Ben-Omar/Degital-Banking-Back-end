package com.bank.entities;

import java.util.Date;

import org.hibernate.annotations.ManyToAny;

import com.bank.enums.OperationType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class AccountOperation {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Date operationDate;
	
	private double amount;
	
	@Enumerated(EnumType.STRING)
	private OperationType type;
	
	@ManyToOne
	private BankAccount bankAccount;
	
	private String description;
	
}
