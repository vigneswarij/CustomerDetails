package com.demo.customer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.customer.dao.CustomerRepository;
import com.demo.customer.entity.Customers;
import com.demo.customer.exception.CustomerDetailsException;
import com.demo.customer.exception.ResourceNotFoundException;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;


	/**
	 * This service method does not allows to save duplicate records. 
	 * @param cust
	 * @return
	 * @throws CustomerDetailsException
	 */
	public Customers createCustomer(Customers cust) throws CustomerDetailsException {
		if (isDataAlreadyAvailable(cust)) {
			return customerRepository.save(cust);
		} else {
			throw new CustomerDetailsException("Same Customer Detail Already Exist");
		}
	}

	/**
	 * This method returns all the Customer without any filter
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public List<Customers> getAllCustomers() throws ResourceNotFoundException {
		List<Customers> customerList = customerRepository.findAll();
		if (!customerList.isEmpty()) {
			return customerList;
		} else {
			throw new ResourceNotFoundException("Resource Not Found");
		}
	}

	/**
	 * This method filter the record based on ID.
	 * @param Id
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public Customers getCustomerById(int Id) throws ResourceNotFoundException {
		return customerRepository.findById(Id)
				.orElseThrow(() -> new ResourceNotFoundException("Requested resource is not found"));
	}

	/**
	 * This method can search the customer by firstName and lastName. firstName is Mandatory But lastname is optional
	 * @param firstName
	 * @param lastName
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public List<Customers> searchCustomerByName(String firstName, String lastName) throws ResourceNotFoundException {
		List<Customers> customer = new ArrayList<>();
		if (lastName == null) {
			customer = customerRepository.findAll(firstName);
		} else {
			customer = customerRepository.findAll(firstName, lastName);
		}
		if (!customer.isEmpty()) {
			return customer;
		} else {
			lastName = (lastName == null) ? "" : lastName;
			throw new ResourceNotFoundException("The Name " + firstName + " " + lastName + " Not Found");
		}

	}

	/**
	 * This method used to update the Address by ID.
	 * @param customer
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public Customers updateCustomer(Customers customer) throws ResourceNotFoundException {
		Customers oldCustomer = new Customers();
		Optional<Customers> optCustomer = customerRepository.findById(customer.getId());
		if (optCustomer.isPresent()) {
			oldCustomer = optCustomer.get();
			oldCustomer.setAddress(customer.getAddress());
			customerRepository.save(oldCustomer);
		} else {
			throw new ResourceNotFoundException("There is no customer with id " + customer.getId());
		}
		return oldCustomer;
	}

	public Boolean isDataAlreadyAvailable(Customers cust) throws CustomerDetailsException {
		String firstName = cust.getFirstName();
		String LastName = cust.getLastName();
		String Address = cust.getAddress();
		int Age = cust.getAge();

		Optional<Customers> custDetail = customerRepository.findAll(firstName, LastName, Age, Address);
		if (custDetail.isPresent()) {
			throw new CustomerDetailsException("Same Customer Detail Already Exist");
		}
		return true;
	}

}
