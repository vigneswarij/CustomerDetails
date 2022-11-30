package com.rebobank.customer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rebobank.customer.constant.CustomerDetailConstant;
import com.rebobank.customer.controller.CustomerController;
import com.rebobank.customer.entity.Customers;
import com.rebobank.customer.exception.CustomerDetailsException;
import com.rebobank.customer.exception.ResourceNotFoundException;
import com.rebobank.customer.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	Logger logger = LoggerFactory.getLogger(CustomerController.class);


	/**
	 * It creates new customer and it does not allow to save duplicates record.
	 * @param cust
	 * @return
	 * @throws CustomerDetailsException
	 */
	public Customers createCustomer(Customers cust) throws CustomerDetailsException {
		if (isDataAlreadyAvailable(cust)) {
			return customerRepository.save(cust);
		} else {
			throw new CustomerDetailsException(CustomerDetailConstant.createErrMessage);
		}
	}

	/**
	 * This method returns all the Customer without any filter
	 * 
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public List<Customers> getAllCustomers() throws ResourceNotFoundException {
		List<Customers> customerList = customerRepository.findAll();
		if (!customerList.isEmpty()) {
			logger.info("Data retrieval succesully and the details are: {} ", customerList);
			return customerList;
		} else {
			throw new ResourceNotFoundException(CustomerDetailConstant.notFound);
		}
	}

	/**
	 * It filter the record by Id.
	 * @param Id
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public Customers getCustomerById(int Id) throws ResourceNotFoundException {
		return customerRepository.findById(Id)
				.orElseThrow(() -> new ResourceNotFoundException(CustomerDetailConstant.notFoundBySpecfic));
	}

	/**
	 * This method can search the customer by firstName and lastName. firstName is Mandatory But lastname is optional
	 * @param firstName
	 * @param lastName
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public List<Customers> searchCustomerByName(String firstName, String lastName) throws ResourceNotFoundException {
		List<Customers> customList = new ArrayList<>();
		if (lastName == null) {
			customList = customerRepository.findByFirstName(firstName);
		} else {
			customList = customerRepository.findByFirstNameAndLastName(firstName, lastName);
		}
		if (!customList.isEmpty()) {
			logger.info("Data retrieval succesully and the details are: {} ", customList);
			return customList;
		} else {
			lastName = (lastName == null) ? "" : lastName;
			throw new ResourceNotFoundException(CustomerDetailConstant.name + firstName  + lastName + CustomerDetailConstant.notFoundSpecific);
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
			throw new ResourceNotFoundException(CustomerDetailConstant.errMsgSpecficById + customer.getId());
		}
		return oldCustomer;
	}

	public Boolean isDataAlreadyAvailable(Customers cust) throws CustomerDetailsException {
		String firstName = cust.getFirstName();
		String LastName = cust.getLastName();
		String Address = cust.getAddress();
		int Age = cust.getAge();

		Optional<Customers> custDetail = customerRepository.findByFirstNameAndLastNameAndAgeAndAddress(firstName, LastName, Age, Address);
		if (custDetail.isPresent()) {
			throw new CustomerDetailsException(CustomerDetailConstant.createErrMessage);
		}
		return true;
	}

}
