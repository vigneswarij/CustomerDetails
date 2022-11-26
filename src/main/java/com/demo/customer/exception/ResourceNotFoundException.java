package com.demo.customer.exception;

public class ResourceNotFoundException extends CustomerDetailsException {
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
	
	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
