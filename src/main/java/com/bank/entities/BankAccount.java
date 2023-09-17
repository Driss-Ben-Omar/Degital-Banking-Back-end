package com.bank.entities;

import java.util.Date;
import java.util.List;


import com.bank.enums.AccountStatus;

import jakarta.persistence.DiscriminatorColumn;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE",length = 4)
@Data @NoArgsConstructor @AllArgsConstructor
public class BankAccount {

	@Id
	private String id;
	
	private double balance;
	
	private Date createAcc;
	
	@Enumerated(EnumType.STRING)
	private AccountStatus status;
	
	@ManyToOne
	private Customer customer;
	
	@OneToMany(mappedBy = "bankAccount",fetch = FetchType.EAGER)
	private List<AccountOperation> accountOperations;
	
}
