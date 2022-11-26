package com.demo.customer.exception;

public class CustomerDetailsException extends Exception {
	public CustomerDetailsException(String message) {
		super(message);
	}

	public CustomerDetailsException(String message, Throwable cause) {
		super(message, cause);
	}
}
