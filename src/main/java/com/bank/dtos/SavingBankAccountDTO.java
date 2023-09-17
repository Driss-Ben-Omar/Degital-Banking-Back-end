package com.bank.dtos;

import java.util.Date;

import com.bank.enums.AccountStatus;


import lombok.Data;



@Data 
public class SavingBankAccountDTO extends BankAccountDTO{

	private String id;
	
	private double balance;
	
	private Date createAcc;

	private AccountStatus status;
	
	private CustomerDTO customerDTO;
	
	private String overDraft;
	
}
