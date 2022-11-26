package com.demo.customer.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customers {
	
	@Id
	@GeneratedValue
	private int id;
	
	@NotNull(message = "Firstname is required") 
	@Pattern(regexp = "^[a-zA-Z]*$", message = "firstName should be a string and It should not accept Specialcharacters|Numbers")
	@Size(min = 1, max=60)
	private String firstName;
	
	@NotNull(message = "Lastname is required")
	@Pattern(regexp = "^[a-zA-Z]*$", message = "lastName should be a string and It should not accept Specialcharacters|Numbers")
	@Size(min = 1, max=60)
	private String lastName;
	
	@NotNull(message = "Age is required")
	@Min(0)
	private int age;
	
	@NotNull(message ="Address is required")
	@Size(min = 1, max=300)
	private String address;
	
}
