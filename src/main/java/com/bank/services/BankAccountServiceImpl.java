package com.bank.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.dtos.AccountHistoriqueDTO;
import com.bank.dtos.AccountOperationDTO;
import com.bank.dtos.BankAccountDTO;
import com.bank.dtos.CurrentBankAccountDTO;
import com.bank.dtos.CustomerDTO;
import com.bank.dtos.SavingBankAccountDTO;
import com.bank.entities.AccountOperation;
import com.bank.entities.BankAccount;
import com.bank.entities.CurrentAccount;
import com.bank.entities.Customer;
import com.bank.entities.SavingAccount;
import com.bank.enums.OperationType;

import com.bank.exceptions.BalanceNotSufficientException;
import com.bank.exceptions.BankAccountNotFoundException;
import com.bank.exceptions.CustomerNotFoundException;
import com.bank.mappers.BankAccountMapperImpl;
import com.bank.repositories.AccountOperationRepository;
import com.bank.repositories.BankAccountRepository;
import com.bank.repositories.CustomerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

	private CustomerRepository customerRepository;

	private BankAccountRepository bankAccountRepository;

	private AccountOperationRepository accountOperationRepository;

	private BankAccountMapperImpl dtoMapper;

	@Override
	public CustomerDTO saveCustomer(CustomerDTO customerDTO) {

		log.info("saving new customer");

		Customer customer = dtoMapper.fromCustomerDTO(customerDTO);

		Customer savedCustomer = customerRepository.save(customer);

		return dtoMapper.fromCustomer(savedCustomer);
	}

	@Override
	public CurrentBankAccountDTO saveCurrentBankAccount(double initialeBalance, double overDraft, Long customerId)
			throws CustomerNotFoundException {

		Customer customer = customerRepository.findById(customerId).orElse(null);

		if (customer == null) {
			throw new CustomerNotFoundException("Customer not found");
		}
		CurrentAccount currentAccount = new CurrentAccount();

		currentAccount.setId(UUID.randomUUID().toString());
		currentAccount.setBalance(initialeBalance);
		currentAccount.setCreateAcc(new Date());
		currentAccount.setCustomer(customer);
		currentAccount.setOverDraft(overDraft);

		CurrentAccount saveBankAccount = bankAccountRepository.save(currentAccount);

		return dtoMapper.fromCurrentAccount(saveBankAccount);
	}

	@Override
	public SavingBankAccountDTO saveSavingBankAccount(double initialeBalance, double interestRate, Long customerId)
			throws CustomerNotFoundException {
		Customer customer = customerRepository.findById(customerId).orElse(null);

		if (customer == null) {
			throw new CustomerNotFoundException("Customer not found");
		}
		SavingAccount savingAccount = new SavingAccount();

		savingAccount.setId(UUID.randomUUID().toString());
		savingAccount.setBalance(initialeBalance);
		savingAccount.setCreateAcc(new Date());
		savingAccount.setCustomer(customer);
		savingAccount.setInterestRate(interestRate);

		SavingAccount saveBankAccount = bankAccountRepository.save(savingAccount);

		return dtoMapper.fromSavingBankAccount(saveBankAccount);
	}

	@Override
	public List<CustomerDTO> listCustomers() {

		List<Customer> customers = customerRepository.findAll();

		List<CustomerDTO> customerDTOs = customers.stream().map(cust -> dtoMapper.fromCustomer(cust))
				.collect(Collectors.toList());

		return customerDTOs;

	}

	@Override
	public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {

		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

		if (bankAccount instanceof SavingAccount) {

			SavingAccount savingAccount = (SavingAccount) bankAccount;
			return dtoMapper.fromSavingBankAccount(savingAccount);

		} else {

			CurrentAccount currentAccount = (CurrentAccount) bankAccount;
			return dtoMapper.fromCurrentAccount(currentAccount);

		}

	}

	@Override
	public void debit(String accountId, double amount, String description)
			throws BankAccountNotFoundException, BalanceNotSufficientException {

		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

		if (bankAccount.getBalance() < amount) {
			throw new BalanceNotSufficientException("Balance not sufficient");
		}

		AccountOperation accountOperation = new AccountOperation();

		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(description);
		accountOperation.setOperationDate(new Date());
		accountOperation.setBankAccount(bankAccount);

		accountOperationRepository.save(accountOperation);

		bankAccount.setBalance(bankAccount.getBalance() - amount);

		bankAccountRepository.save(bankAccount);

	}

	@Override
	public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {

		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

		AccountOperation accountOperation = new AccountOperation();

		accountOperation.setType(OperationType.CREDIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(description);
		accountOperation.setOperationDate(new Date());
		accountOperation.setBankAccount(bankAccount);

		accountOperationRepository.save(accountOperation);

		bankAccount.setBalance(bankAccount.getBalance() + amount);

		bankAccountRepository.save(bankAccount);

	}

	@Override
	public void transfer(String accountIdSource, String accountIdDestination, double amount)
			throws BankAccountNotFoundException, BalanceNotSufficientException {

		debit(accountIdSource, amount, "Tansfert to " + accountIdDestination);
		credit(accountIdDestination, amount, "Tansfert from " + accountIdSource);

	}

	@Override

	public List<BankAccountDTO> lisBankAccounts() {

		List<BankAccount> bankAccounts = bankAccountRepository.findAll();

		List<BankAccountDTO> bankAccountDTOs = bankAccounts.stream().map(bankAccount -> {
			if (bankAccount instanceof SavingAccount) {

				SavingAccount savingAccount = (SavingAccount) bankAccount;
				return dtoMapper.fromSavingBankAccount(savingAccount);

			} else {

				CurrentAccount currentAccount = (CurrentAccount) bankAccount;
				return dtoMapper.fromCurrentAccount(currentAccount);

			}
		}).collect(Collectors.toList());

		return bankAccountDTOs;
	}

	@Override
	public CustomerDTO getCustumer(Long customerId) throws CustomerNotFoundException {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

		return dtoMapper.fromCustomer(customer);
	}

	@Override
	public CustomerDTO updateCustomer(CustomerDTO customerDTO) {

		log.info("saving new customer");

		Customer customer = dtoMapper.fromCustomerDTO(customerDTO);

		Customer savedCustomer = customerRepository.save(customer);

		return dtoMapper.fromCustomer(savedCustomer);
	}

	@Override
	public void deleteCustomer(long customerId) {
		customerRepository.deleteById(customerId);
	}

	@Override
	public List<AccountOperationDTO> accountHistorique(String accountId) {

		List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);

		return accountOperations.stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());

	}

	@Override
	public AccountHistoriqueDTO getAccountHistorique(String accountId, int page, int size) throws BankAccountNotFoundException{
		
		BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
		
		if(bankAccount==null) {
			throw new BankAccountNotFoundException("Account not found");
		}

		Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId,
				PageRequest.of(page, size));

		AccountHistoriqueDTO accountHistoriqueDTO = new AccountHistoriqueDTO();

		List<AccountOperationDTO> accountOperationDTOs = accountOperations.getContent().stream()
				.map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());

		accountHistoriqueDTO.setAccountOperationDTOs(accountOperationDTOs);
		accountHistoriqueDTO.setAccountId(bankAccount.getId());
		accountHistoriqueDTO.setBalance(bankAccount.getBalance());
		accountHistoriqueDTO.setCurrentPage(page);
		accountHistoriqueDTO.setPageSize(size);
		accountHistoriqueDTO.setTotalPages(accountOperations.getTotalPages());
		
		return accountHistoriqueDTO;
	}

}
