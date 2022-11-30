package com.rebobank.customer.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rebobank.customer.entity.Customers;
import com.rebobank.customer.exception.CustomerDetailsException;
import com.rebobank.customer.service.CustomerService;

@RestController
@Validated
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	Logger logger = LoggerFactory.getLogger(CustomerController.class);

	/**
	 * This endpoint is used to create the new customer.
	 * 
	 * @param customer
	 * @return
	 * @throws CustomerDetailsException
	 */
	@PostMapping("/addCustomer")
	public ResponseEntity<Customers> addCustomer(@RequestBody @Valid Customers customer)
			throws CustomerDetailsException {
		Customers CustomerDetail = customerService.createCustomer(customer);
		if (null != CustomerDetail) {
			logger.info("New customer details are created");
			return new ResponseEntity<>(CustomerDetail, HttpStatus.CREATED);
		} else {
			logger.error("Problem to Create Customer details");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * 
	 * @return
	 * @throws CustomerDetailsException 
	 * This endpoint to fetch all the customers
	 */
	@GetMapping("/Customers")
	public ResponseEntity<List<Customers>> getAllCutomers() throws CustomerDetailsException {
		logger.info("Inside getAllCutomers() to fetch all the Customers");
		return ResponseEntity.ok(customerService.getAllCustomers());
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws CustomerDetailsException 
	 * This endpoint used to fetch the customer details which we have requested by id
	 */
	@GetMapping("/customer/{id}")
	public ResponseEntity<Customers> getCustomerById(
			@PathVariable(value = "id", required = true) @NotNull @Min(0) int id) throws CustomerDetailsException {
		logger.info("Inside getCustomerById() to get the record of Id :" + id);
		return ResponseEntity.ok(customerService.getCustomerById(id));
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws CustomerDetailsException 
	 * This endpoint to fetch the customer which we have requested by firstName / lastName
	 */
	@GetMapping(value = "/customer")
	public ResponseEntity<List<Customers>> searchCustomerByName(
			@RequestParam(name = "firstName", required = true) @NotNull @Size(min = 1, max = 60) @Pattern(regexp = "^[a-zA-Z]*$", message = "FirstName should be a string") String firstName,
			@RequestParam(name = "lastName", required = false) @Size(min = 1, max = 60) @Pattern(regexp = "^[a-zA-Z]*$", message = "LastName should be a string") String lastName)
			throws CustomerDetailsException {
		logger.info("Inside searchCustomerByName() to get record Name");
		return ResponseEntity.ok(customerService.searchCustomerByName(firstName, lastName));
	}

	/**
	 * 
	 * @param customer
	 * @return 
	 * This endpoint used to update the Address of the customer.
	 */
	@PutMapping("/updateCustomer")
	public ResponseEntity<Customers> updateCustomerAddress(@RequestBody Customers customer)
			throws CustomerDetailsException {
		logger.info("Inside updateCustomerAddress() to update the Address of Customer");
		return ResponseEntity.ok(customerService.updateCustomer(customer));
	}
}
