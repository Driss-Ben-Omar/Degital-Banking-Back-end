package com.bank.services;

import java.util.List;

import com.bank.dtos.AccountHistoriqueDTO;
import com.bank.dtos.AccountOperationDTO;
import com.bank.dtos.BankAccountDTO;
import com.bank.dtos.CurrentBankAccountDTO;
import com.bank.dtos.CustomerDTO;
import com.bank.dtos.SavingBankAccountDTO;
import com.bank.entities.BankAccount;
import com.bank.entities.CurrentAccount;
import com.bank.entities.SavingAccount;
import com.bank.exceptions.BankAccountNotFoundException;
import com.bank.exceptions.BalanceNotSufficientException;
import com.bank.exceptions.CustomerNotFoundException;

public interface BankAccountService {

	public CustomerDTO saveCustomer(CustomerDTO customer);

	CurrentBankAccountDTO saveCurrentBankAccount(double initialeBalance, double overDraft, Long customerId)
			throws CustomerNotFoundException;

	SavingBankAccountDTO saveSavingBankAccount(double initialeBalance, double interestRate, Long customerId)
			throws CustomerNotFoundException;

	List<CustomerDTO> listCustomers();

	BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

	void debit(String accountId, double amount, String description)
			throws BankAccountNotFoundException, BalanceNotSufficientException;

	void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

	void transfer(String accountIdSource, String accountIdDestination, double amount)
			throws BankAccountNotFoundException, BalanceNotSufficientException;
	
	List<BankAccountDTO> lisBankAccounts();

	CustomerDTO getCustumer(Long customerId) throws CustomerNotFoundException;

	CustomerDTO updateCustomer(CustomerDTO customerDTO);

	void deleteCustomer(long customerId);

	List<AccountOperationDTO> accountHistorique(String accountId);

	AccountHistoriqueDTO getAccountHistorique(String accountId, int page, int size) throws BankAccountNotFoundException;

}
