package com.demo.customer.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.customer.entity.Customers;


public interface CustomerRepository extends JpaRepository<Customers, Integer> {
	
	@Query(value = "SELECT c FROM Customers c WHERE c.firstName LIKE ?1 and c.lastName LIKE ?2")
	public List<Customers> findAll(String firstName, String lastName);
	
	@Query(value = "SELECT c FROM Customers c WHERE c.firstName LIKE ?1")
	public List<Customers> findAll(String firstName);
	
	@Query(value = "SELECT c FROM Customers c WHERE c.firstName LIKE ?1 and c.lastName LIKE ?2 and c.age like ?3 and c.address LIKE ?4")
	public Optional<Customers> findAll(String firstName, String lastName, int age, String address);
}

