package com.rebobank.customer.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebobank.customer.config.CustomerDetailsConstantTests;
import com.rebobank.customer.controller.CustomerController;
import com.rebobank.customer.entity.Customers;
import com.rebobank.customer.exception.CustomerDetailsException;
import com.rebobank.customer.service.CustomerServiceImpl;

public class CustomerControllerTest {

	private MockMvc mockMvc;

	private static final String getAllCustomer = "/Customers";
	private static final String getCustomerById = "/customer/1";
	private static final String getCustomerByIdErrInput = "/customer/a";
	private static final String getCustomerByName = "/customer";
	private static final String createCustomer = "/addCustomer";
	private static final String updateCustomer = "/updateCustomer";

	ObjectMapper om = new ObjectMapper();

	@Mock
	private CustomerServiceImpl customerService;

	@InjectMocks
	private CustomerController customerController;

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this); 
		mockMvc = MockMvcBuilders.standaloneSetup(customerController).alwaysDo(MockMvcResultHandlers.print()).build();
	}

	@Test
	public void test_getAllCutomersSuccess() throws Exception {
		List<Customers> customer = prepareCustomerList();
		when(customerService.getAllCustomers()).thenReturn(customer);
		MvcResult result = mockMvc.perform(get(getAllCustomer)).andExpect(status().isOk()).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	public void test_getCutomerByIdSuccess() throws Exception {
		when(customerService.getCustomerById(CustomerDetailsConstantTests.id_1)).thenReturn(getMockFirstCustomer());
		MvcResult result = mockMvc.perform(get(getCustomerById)).andExpect(status().isOk()).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	public void test_getCutomerByIdFailure() throws Exception {
		when(customerService.getCustomerById(CustomerDetailsConstantTests.id_1)).thenReturn(null);
		MvcResult result = mockMvc.perform(get(getCustomerByIdErrInput)).andExpect(status().isBadRequest()).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(CustomerDetailsConstantTests.BAD_STATUS, response.getStatus());
	}
 
	@Test
	public void test_searchCustomerByNameSuccess() throws Exception {
		List<Customers> customer = prepareCustomerList();
		when(customerService.searchCustomerByName(CustomerDetailsConstantTests.firstName_1,
				CustomerDetailsConstantTests.lastName_1)).thenReturn(customer);
		MvcResult result = mockMvc
				.perform(get(getCustomerByName).param("firstName", CustomerDetailsConstantTests.firstName_1)
						.param("lastName", CustomerDetailsConstantTests.lastName_1))
				.andExpect(status().isOk()).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	public void test_searchCustomerByNameSuccess_SingleParam() throws Exception {
		List<Customers> customer = prepareCustomerList();
		when(customerService.searchCustomerByName(CustomerDetailsConstantTests.firstName_1, null)).thenReturn(customer);
		MvcResult result = mockMvc
				.perform(get(getCustomerByName).param("firstName", CustomerDetailsConstantTests.firstName_1))
				.andExpect(status().isOk()).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	public void test_addCustomerSuccess() throws Exception {
		Customers customer = getMockFirstCustomer();
		String jsonRequest = om.writeValueAsString(customer);
		when(customerService.createCustomer(customer)).thenReturn(customer);
		MvcResult result = mockMvc
				.perform(post(createCustomer).content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated()).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}

	// Exception Scenario to Create New Customer
	@Test
	public void test_addWrongInputCustomerException() throws Exception {
		try {
			Customers customer = getMockErrorCustomer();
			String jsonRequest = om.writeValueAsString(customer);
			when(customerService.createCustomer(customer)).thenReturn(customer);
			MvcResult result = mockMvc
					.perform(post(createCustomer).content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isBadRequest()).andReturn();
			MockHttpServletResponse response = result.getResponse();
			assertEquals(CustomerDetailsConstantTests.BAD_STATUS, response.getStatus());
		} catch (MethodArgumentNotValidException e) {
			e.printStackTrace();
		}
	}

	// Exception Scenario to Create New Customer
	@Test
	public void test_addCustomerFailure() throws Exception {
		Customers customer = getMockFirstCustomer();
		String jsonRequest = om.writeValueAsString(customer);
		when(customerService.isDataAlreadyAvailable(customer)).thenReturn(false);
		MvcResult result = mockMvc
				.perform(post(createCustomer).content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is4xxClientError()).andReturn();
	}
	
	@Test
	public void test_updateCustomerSuccess() throws Exception {
		Customers customer = getMockFirstCustomer();
		String jsonRequest = om.writeValueAsString(customer);
		when(customerService.updateCustomer(customer)).thenReturn(customer);
		MvcResult result = mockMvc
				.perform(put(updateCustomer).content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());

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

	private Customers getMockErrorCustomer() {
		Customers expCustomer = new Customers();
		expCustomer.setFirstName(CustomerDetailsConstantTests.errFirstName);
		expCustomer.setLastName(CustomerDetailsConstantTests.lastName_2);
		expCustomer.setId(CustomerDetailsConstantTests.id_2);
		expCustomer.setAge(CustomerDetailsConstantTests.Age_2);
		expCustomer.setAddress(CustomerDetailsConstantTests.Address_2);
		return expCustomer;
	}

}
