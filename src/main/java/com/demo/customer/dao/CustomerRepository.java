package com.demo.customer.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.customer.entity.Customers;

public interface CustomerRepository extends JpaRepository<Customers, Integer> {

}
