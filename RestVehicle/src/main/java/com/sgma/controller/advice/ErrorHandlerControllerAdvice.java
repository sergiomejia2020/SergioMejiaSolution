package com.sgma.controller.advice;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.sgma.controller.response.create.CreateVehicleResponseBody;
import com.sgma.services.utils.CreateVehicleErrorResponse;

/**
 * This class catches any unexpected RuntimeException or the exception that is thrown is a parameter does not contains valid data.
 * This is the object that is returned in any method in this class see {@link CreateVehicleResponseBody}
 * Here we can add more exceptions.
 * @author sgma
 *
 */
@ControllerAdvice
public class ErrorHandlerControllerAdvice  extends ResponseEntityExceptionHandler {
	
	/**
	 * Handlers the exception that is thrown in parameters validation
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
	    MethodArgumentNotValidException ex, HttpHeaders headers, 
	    HttpStatus status, WebRequest request) {
	
		List<String> errors = ex.getBindingResult()
			    .getFieldErrors()
			    .stream()
			    .map(x -> x.getDefaultMessage())
			    .collect(Collectors.toList());
		
		return new ResponseEntity<>(CreateVehicleErrorResponse.createVehicleErrorResponse(
				400, 
				"The request does not contains valid data",
				errors), 
				HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Catches a RuntimeException
	 * @param ex RuntimeException instance that contains a description about an exception
	 * @param request Contains general request data
	 * @return Instance of {@link CreateVehicleResponseBody}}
	 */
	@ExceptionHandler(value = {RuntimeException.class})
	public ResponseEntity<CreateVehicleResponseBody> createCustomVehicleResponse(RuntimeException ex, WebRequest request) {
		
		return new ResponseEntity<>(CreateVehicleErrorResponse.createVehicleErrorResponse(
				500, 
				"Is not possible to processed the request.",
				"An exception occurred processing the request. Try again later."), 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
