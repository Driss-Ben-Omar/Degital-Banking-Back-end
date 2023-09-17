package com.bank.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.entities.AccountOperation;


public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long>{

	public List<AccountOperation> findByBankAccountId(String accountId);//findByBankAccount_Id
	
	public Page<AccountOperation> findByBankAccountId(String accountId,Pageable pageable);
	
}
