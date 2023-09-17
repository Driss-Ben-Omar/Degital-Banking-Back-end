package com.bank;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bank.dtos.BankAccountDTO;
import com.bank.dtos.CurrentBankAccountDTO;
import com.bank.dtos.CustomerDTO;
import com.bank.dtos.SavingBankAccountDTO;
import com.bank.entities.AccountOperation;
import com.bank.entities.BankAccount;
import com.bank.entities.CurrentAccount;
import com.bank.entities.Customer;
import com.bank.entities.SavingAccount;
import com.bank.enums.AccountStatus;
import com.bank.enums.OperationType;
import com.bank.exceptions.BankAccountNotFoundException;
import com.bank.exceptions.BalanceNotSufficientException;
import com.bank.exceptions.CustomerNotFoundException;
import com.bank.repositories.AccountOperationRepository;
import com.bank.repositories.BankAccountRepository;
import com.bank.repositories.CustomerRepository;
import com.bank.services.BankAccountService;

@SpringBootApplication
public class EBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBankApplication.class, args);
	}

//	@Bean
	CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository,
			AccountOperationRepository accountOperationRepository) {
		return args -> {
			Stream.of("Driss", "Omar", "Mouad").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name + "@gmail.com");
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(cust -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random() * 90000);
				currentAccount.setCreateAcc(new Date());
				currentAccount.setCustomer(cust);
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random() * 90000);
				savingAccount.setCreateAcc(new Date());
				savingAccount.setCustomer(cust);
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setInterestRate(5.5);
				bankAccountRepository.save(savingAccount);
			});
			bankAccountRepository.findAll().forEach(acc -> {
				for (int i = 0; i < 5; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random() * 12000);
					accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);
				}
			});
		};
	}

	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
		return args -> {
			Stream.of("Driss", "Mouad", "Omar").forEach(name -> {

				CustomerDTO customer = new CustomerDTO();

				customer.setName(name);
				customer.setEmail(name + "@gmail.com");

				bankAccountService.saveCustomer(customer);
			});

			bankAccountService.listCustomers().forEach(cust -> {

				try {

					bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 90000, cust.getId());
					bankAccountService.saveSavingBankAccount(Math.random() * 120000, 5.5, cust.getId());
					

				} catch (CustomerNotFoundException e) {

					e.printStackTrace();

				}

			});
			
			List<BankAccountDTO> bankAccounts = bankAccountService.lisBankAccounts();

			for (BankAccountDTO bankAccount : bankAccounts) {

				for (int i = 0; i < 5; i++) {
					
					String accountId;
					
					if(bankAccount instanceof SavingBankAccountDTO) {
						accountId=((SavingBankAccountDTO) bankAccount).getId();
					}else {
						accountId=((CurrentBankAccountDTO) bankAccount).getId();
					}

					bankAccountService.credit(accountId, 10000 + Math.random() * 120000, "Credit");
					bankAccountService.debit(accountId, 10000 + Math.random() * 9000, "Debit");
				}

			}
			
		};
	}
}
