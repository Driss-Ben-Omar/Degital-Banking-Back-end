package com.bank.web;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dtos.CustomerDTO;
import com.bank.exceptions.CustomerNotFoundException;
import com.bank.services.BankAccountService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {

	private BankAccountService bankAccountService;
	
	@GetMapping("/customers")
	public List<CustomerDTO> customers(){
		
		return bankAccountService.listCustomers();
		
	}
	
	@GetMapping("/customers/{id}")
	public CustomerDTO getCustomer(@PathVariable(name="id") Long customerId) throws CustomerNotFoundException {
		
		return bankAccountService.getCustumer(customerId);
		
	}
	
	@PostMapping("/customers")
	public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
		
		return bankAccountService.saveCustomer(customerDTO);
		
	}
	
	@PutMapping("/customers/{customerId}")
	public CustomerDTO updateCustomer(@PathVariable Long customerId,@RequestBody CustomerDTO customerDTO) {
		
		customerDTO.setId(customerId);
		
		return bankAccountService.updateCustomer(customerDTO);
		
	}
	
	@DeleteMapping("/customers/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		
		bankAccountService.deleteCustomer(id);
		
	}
	
}
