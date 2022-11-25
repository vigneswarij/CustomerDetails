package com.demo.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.customer.entity.Customers;
import com.demo.customer.service.CustomerService;

@RestController
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping("/addCustomer")
	public Customers addCustomer(@RequestBody Customers customer) {
		return customerService.createCustomer(customer);
	}

	@GetMapping("/Customers")
	public List<Customers> getAllCutomers() {
		return customerService.getAllCustomers();
	}

	@GetMapping("/customer/{id}")
	public Customers getCustomerById(@PathVariable int id) {
		return customerService.getCustomerById(id);
	}

	@PutMapping("/updateCustomer")
	public Customers updateCustomerAddress(@RequestBody Customers customer) {
		return customerService.updateCustomer(customer);
	}
	
	@DeleteMapping("/Customer/{id}")
	public String deleteCustomer(@PathVariable int id)
	{
		return customerService.deleteCustomerById(id);
	}

}
