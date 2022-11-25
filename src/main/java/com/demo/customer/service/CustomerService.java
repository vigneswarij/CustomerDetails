package com.demo.customer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.customer.dao.CustomerRepository;
import com.demo.customer.entity.Customers;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public Customers createCustomer(Customers cust) {
		return customerRepository.save(cust);
	}

	public List<Customers> getAllCustomers() {
		return customerRepository.findAll();
	}

	public Customers getCustomerById(int Id) {
		return customerRepository.findById(Id).orElse(null);
	}

	public Customers updateCustomer(Customers customer) {
		Customers oldCustomer;
		Optional<Customers> optCustomer = customerRepository.findById(customer.getId());
		
		if (optCustomer.isPresent()) {
			oldCustomer = optCustomer.get();
			oldCustomer.setAddress(customer.getAddress());
			customerRepository.save(oldCustomer);
		} else {
			return new Customers();
		}
		return oldCustomer;
	}
	
	public String deleteCustomerById(int id)
	{
		customerRepository.deleteById(id);
		return "Customer got deleted";
	} 

}
