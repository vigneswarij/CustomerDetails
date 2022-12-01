package com.rebobank.customer.exception;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.rebobank.customer.constant.CustomerDetailConstant;
import com.rebobank.customer.dto.CustomerAppError;

@RestControllerAdvice
public class CustomerExceptionHandler {

	String errMsg;
	/**
	 * If Invalid payload request this error message will return.
	 * @param ex
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomerAppError> handleInvalidArgument(MethodArgumentNotValidException ex) {
		errMsg=CustomerDetailConstant.empty;
		ex.getBindingResult().getFieldErrors().forEach(error -> { 
			errMsg = errMsg + error.getDefaultMessage()+". ";
		});
		CustomerAppError error = new CustomerAppError(errMsg, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST); 
	}
	
	/**
	 * This message will return if any mismatched RequestParam/PathVariable
	 * @param ex
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<CustomerAppError> handleInvalidArgument(MethodArgumentTypeMismatchException ex) {
		CustomerAppError error = new CustomerAppError(CustomerDetailConstant.idErrorMessage, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	} 
	
	/**This error message will return when try to create duplicate records.
	 * @param ex
	 * @return
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(CustomerDetailsException.class)
	public ResponseEntity<CustomerAppError> handleresourceNotFound(CustomerDetailsException ex) {
		CustomerAppError error = new CustomerAppError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	} 
	
	/**
	 * This will return if requested resource not found in database
	 * @param ex
	 * @return
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<CustomerAppError> handleresourceNotFound(ResourceNotFoundException ex) {
		CustomerAppError error = new CustomerAppError(ex.getMessage(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	/**
	 * This error message will return if missed any mandatory input params.
	 * @param ex
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<CustomerAppError> handleresourceNotFound(MissingServletRequestParameterException ex) {
		CustomerAppError error = new CustomerAppError(ex.getMessage(), HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * This will handle be constraintViolation(Name should be String(0-60))|Age should be number|Address size should be 300)
	 * @param ex
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<CustomerAppError> handleresourceNotFound(ConstraintViolationException ex) {
		CustomerAppError error = new CustomerAppError(ex.getMessage(), HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * This will return when syntax error in payload request
	 * @param ex
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<CustomerAppError> handleInputsInNotFormat(HttpMessageNotReadableException ex) {
		CustomerAppError error = new CustomerAppError(CustomerDetailConstant.inputDataErrorMessage, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
}
