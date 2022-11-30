package com.rebobank.customer.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.rebobank.customer.config.CustomerDetailsConstantTests;
import com.rebobank.customer.entity.Customers;
import com.rebobank.customer.exception.CustomerDetailsException;
import com.rebobank.customer.exception.ResourceNotFoundException;
import com.rebobank.customer.repository.CustomerRepository;
import com.rebobank.customer.service.CustomerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTests {

	@Autowired
	private CustomerService service;

	@MockBean
	private CustomerRepository repository;

	// Positive Scenario to getAllCustomers
	@Test
	public void test_getAllCustomersSuccess() throws ResourceNotFoundException {
		when(repository.findAll()).thenReturn(prepareCustomerList());
		assertEquals(CustomerDetailsConstantTests.size, service.getAllCustomers().size());
	}

	// Exception Scenario to getAllCustomers
	@Test(expected = ResourceNotFoundException.class)
	public void test_getAllCustomersException() throws ResourceNotFoundException {
		when(repository.findAll()).thenReturn(new ArrayList<>());
		service.getAllCustomers();
	}

	// Postive Scenario to getCustomerById
	@Test
	public void test_getCustomerByIdSuccess() throws ResourceNotFoundException {
		when(repository.findById(CustomerDetailsConstantTests.id_1)).thenReturn(Optional.of(getMockFirstCustomer()));
		assertEquals(CustomerDetailsConstantTests.id_1,
				service.getCustomerById(CustomerDetailsConstantTests.id_1).getId());
	}

	// Exception Scenario to getCustomerById
	@Test(expected = ResourceNotFoundException.class)
	public void test_getCustomerByIdException() throws ResourceNotFoundException {
		when(repository.findById(CustomerDetailsConstantTests.id_1)).thenReturn(Optional.empty());
		service.getCustomerById(CustomerDetailsConstantTests.id_1);
	}

	// Exception Scenario to getCustomerById
	@Test(expected = ResourceNotFoundException.class) 
	public void test_getCustomerByIdBadInputException() throws ResourceNotFoundException {
		when(repository.findById(CustomerDetailsConstantTests.id_1)).thenReturn(Optional.empty());
		service.getCustomerById(CustomerDetailsConstantTests.id_1);
	}

	// Positive Scenario to searchByCustomerName
	@Test
	public void test_searchCustomerByNameSuccess() throws ResourceNotFoundException {
		when(repository.findByFirstName(CustomerDetailsConstantTests.firstName_1)).thenReturn(prepareCustomerList());
		when(repository.findByFirstNameAndLastName(CustomerDetailsConstantTests.firstName_1, CustomerDetailsConstantTests.lastName_1))
				.thenReturn(prepareCustomerList());
		assertEquals(CustomerDetailsConstantTests.size, service
				.searchCustomerByName(CustomerDetailsConstantTests.firstName_1, CustomerDetailsConstantTests.lastName_1)
				.size());
	}

	// Exception Scenario to searchByCustomerName
	@Test(expected = ResourceNotFoundException.class)
	public void test_searchCustomerByNameException() throws ResourceNotFoundException {
		when(repository.findByFirstName(CustomerDetailsConstantTests.firstName_1)).thenReturn(new ArrayList<>());
		when(repository.findByFirstNameAndLastName(CustomerDetailsConstantTests.firstName_1, CustomerDetailsConstantTests.lastName_1))
				.thenReturn(new ArrayList<>());
		service.searchCustomerByName(CustomerDetailsConstantTests.firstName_1, CustomerDetailsConstantTests.lastName_1);
	}

	// Exception Scenario to searchByCustomerName
	@Test(expected = ResourceNotFoundException.class)
	public void test_searchCustomerByNameException_singleParam() throws ResourceNotFoundException {
		when(repository.findByFirstName(CustomerDetailsConstantTests.firstName_1)).thenReturn(new ArrayList<>());
		when(repository.findByFirstNameAndLastName(CustomerDetailsConstantTests.firstName_1, CustomerDetailsConstantTests.lastName_1))
				.thenReturn(new ArrayList<>());
		service.searchCustomerByName(CustomerDetailsConstantTests.firstName_1, null);
	}

	// Positive Scenario to Create New Customer
	@Test
	public void test_createCustomerTestsSuccess() throws CustomerDetailsException {
		Customers customer = getMockFirstCustomer();
		when(repository.findByFirstNameAndLastNameAndAgeAndAddress(CustomerDetailsConstantTests.firstName_1, CustomerDetailsConstantTests.lastName_1,
				CustomerDetailsConstantTests.Age_1, CustomerDetailsConstantTests.Address_2))
				.thenReturn(Optional.of(getMockFirstCustomer()));
		when(repository.save(customer)).thenReturn(getMockFirstCustomer());
		assertEquals(customer, service.createCustomer(customer));
	}

	// Exception Scenario to Create New Customer
	@Test(expected = CustomerDetailsException.class)
	public void test_createCustomerException() throws CustomerDetailsException {
		Customers customer = getMockFirstCustomer();
		when(repository.findByFirstNameAndLastNameAndAgeAndAddress(CustomerDetailsConstantTests.firstName_1, CustomerDetailsConstantTests.lastName_1,
				CustomerDetailsConstantTests.Age_1, CustomerDetailsConstantTests.Address_1))
				.thenReturn(Optional.of(getMockFirstCustomer()));
		when(repository.save(customer)).thenReturn(getMockFirstCustomer());
		service.createCustomer(customer);
		
	}

	// Positive Scenario to updateCustomer details
	@Test
	public void test_updateCustomerSuccess() throws ResourceNotFoundException {
		Customers customer = getMockFirstCustomer();
		when(repository.findById(CustomerDetailsConstantTests.id_1)).thenReturn(Optional.of(getMockFirstCustomer()));
		when(repository.save(customer)).thenReturn(getMockFirstCustomer());
		assertEquals(customer, service.updateCustomer(customer));
	}

	// Exception Scenario to updateCustomer details
	@Test(expected = ResourceNotFoundException.class)
	public void test_updateCustomerException() throws ResourceNotFoundException {
		Customers customer = getMockFirstCustomer();
		when(repository.findById(CustomerDetailsConstantTests.id_1)).thenReturn(Optional.empty());
		when(repository.save(customer)).thenReturn(getMockFirstCustomer());
		service.updateCustomer(customer);
	}

	private List<Customers> prepareCustomerList() {
		List<Customers> customerList = new ArrayList<>();
		Customers custDetail = getMockFirstCustomer();
		customerList.add(custDetail);
		custDetail = getMockSecondCustomer();
		customerList.add(custDetail);
		return customerList;
	}

	private Customers getMockFirstCustomer() {
		Customers firstCustomer = new Customers();
		firstCustomer.setFirstName(CustomerDetailsConstantTests.firstName_1);
		firstCustomer.setLastName(CustomerDetailsConstantTests.lastName_1);
		firstCustomer.setId(CustomerDetailsConstantTests.id_1);
		firstCustomer.setAge(CustomerDetailsConstantTests.Age_1);
		firstCustomer.setAddress(CustomerDetailsConstantTests.Address_1);
		return firstCustomer;
	}

	private Customers getMockSecondCustomer() {
		Customers secCustomer = new Customers();
		secCustomer.setFirstName(CustomerDetailsConstantTests.firstName_2);
		secCustomer.setLastName(CustomerDetailsConstantTests.lastName_2);
		secCustomer.setId(CustomerDetailsConstantTests.id_2);
		secCustomer.setAge(CustomerDetailsConstantTests.Age_2);
		secCustomer.setAddress(CustomerDetailsConstantTests.Address_2);
		return secCustomer;
	}

}
