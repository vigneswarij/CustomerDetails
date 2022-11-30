package com.rebobank.customer.service;

import java.util.List;

import com.rebobank.customer.entity.Customers;
import com.rebobank.customer.exception.CustomerDetailsException;
import com.rebobank.customer.exception.ResourceNotFoundException;

public interface CustomerService {

	// Create new customer
	Customers createCustomer(Customers customer) throws CustomerDetailsException;

	// Fetch all the Customer Details.
	List<Customers> getAllCustomers() throws ResourceNotFoundException;

	// Fetch Customer detail by Id
	Customers getCustomerById(int id) throws ResourceNotFoundException;

	// Fetch Customer detail by Name
	List<Customers> searchCustomerByName(String firstName, String lastName) throws ResourceNotFoundException;

	// Update Customer Address based on Id
	Customers updateCustomer(Customers customer) throws ResourceNotFoundException;
}
