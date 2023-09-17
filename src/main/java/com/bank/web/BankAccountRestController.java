package com.bank.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dtos.AccountHistoriqueDTO;
import com.bank.dtos.AccountOperationDTO;
import com.bank.dtos.BankAccountDTO;
import com.bank.exceptions.BankAccountNotFoundException;
import com.bank.services.BankAccountService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class BankAccountRestController {

	private BankAccountService bankAccountService;

	@GetMapping("/accounts/{accountId}")
	public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {

		return bankAccountService.getBankAccount(accountId);

	}

	@GetMapping("/accounts")
	public List<BankAccountDTO> listAccounts() {

		return bankAccountService.lisBankAccounts();

	}

	@GetMapping("/accounts/{accountId}/operations")
	public List<AccountOperationDTO> getHistorique(@PathVariable String accountId) {

		return bankAccountService.accountHistorique(accountId);

	}

	@GetMapping("/accounts/{accountId}/pageOperations")
	public AccountHistoriqueDTO getAccountHistorique(@PathVariable String accountId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException{

		return bankAccountService.getAccountHistorique(accountId,page,size);

	}

}
