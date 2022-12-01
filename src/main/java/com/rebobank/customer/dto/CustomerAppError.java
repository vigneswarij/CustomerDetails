package com.rebobank.customer.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerAppError {
	
	private String message;
	private HttpStatus httpStatus;

}
