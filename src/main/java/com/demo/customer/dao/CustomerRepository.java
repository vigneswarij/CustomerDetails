package com.demo.customer.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.customer.entity.Customers;


public interface CustomerRepository extends JpaRepository<Customers, Integer> {
	
	public List<Customers> findByFirstNameAndLastName(String firstName, String lastName);
	
	public List<Customers> findByFirstName(String firstName);
	
	public Optional<Customers> findByFirstNameAndLastNameAndAgeAndAddress(String firstName, String lastName, int age, String address);
}


