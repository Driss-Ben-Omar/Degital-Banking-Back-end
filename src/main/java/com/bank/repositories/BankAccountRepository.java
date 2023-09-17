package com.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.entities.BankAccount;


public interface BankAccountRepository extends JpaRepository<BankAccount, String>{

}
