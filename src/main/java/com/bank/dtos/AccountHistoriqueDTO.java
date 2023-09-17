package com.bank.dtos;

import java.util.List;

import lombok.Data;


@Data
public class AccountHistoriqueDTO {
	
	private String accountId;
	
	private double balance;
	
	private int currentPage;
	
	private int totalPages;
	
	private int pageSize;
	
	private List<AccountOperationDTO> accountOperationDTOs;

}
