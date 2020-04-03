package com.sgma.services.utils;

import java.util.ArrayList;
import java.util.List;
import com.sgma.controller.response.ResponseStatus;
import com.sgma.controller.response.create.CreateVehicleResponse;
import com.sgma.controller.response.create.CreateVehicleResponseBody;
import com.sgma.controller.response.find.FindVehicleResponseBody;
import java.sql.Timestamp;

/**
 * This class is used to create custom error responses for operation like create Vehicle or find a Vehicle
 * @author sgma
 *
 */
public class CreateVehicleErrorResponse {
	/**
	 * Creates a response to use it when the request does not contains correct information
	 * @param httpStatus Http status code
	 * @param description A simple explanation about the bad request
	 * @param error A list of strings that contains a more that one error description
	 * @return See {@link CreateVehicleResponseBody}
	 */
	public static CreateVehicleResponseBody createVehicleErrorResponse(int httpStatus, String description, List<String> error) {
		ResponseStatus status = new ResponseStatus();
		status.setHttpStatus(httpStatus);
		status.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());
		status.setDescription(description);
		status.setErrors(error);

		CreateVehicleResponseBody response = new CreateVehicleResponseBody();
		response.setResponseStatus(status);
		response.setVehicleData(new CreateVehicleResponse());
		return response;
	}
	
	/**
	 * This method overloads {@link #createVehicleErrorResponse(int, String, List)}, this is used when error parameters is not a list,
	 * is just a simple string
	 * @param httpStatus Http status code
	 * @param description A simple explanation about the bad request
	 * @param error A simple description about the error
	 * @return See {@link CreateVehicleResponseBody}
	 */
	public static CreateVehicleResponseBody createVehicleErrorResponse(int httpStatus, String description, String error) {
		List<String> errors = new ArrayList<>(1);
		errors.add(error);
		return createVehicleErrorResponse(httpStatus, description, errors);
	}
	
	/**
	 * This method creates an error response for find vehicle operation,
	 * @param httpStatus Http status code
	 * @param description A simple explanation about the bad request
	 * @param error A simple description about the error
	 * @return  See {@link FindVehicleResponseBody}
	 */
	public static FindVehicleResponseBody findVehicleErrorResponse(int httpStatus, String description, String error) {
		List<String> errors = new ArrayList<>(1);
		errors.add(error);
		ResponseStatus status = new ResponseStatus();
		status.setHttpStatus(httpStatus);
		status.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());
		status.setDescription(description);
		status.setErrors(errors);

		FindVehicleResponseBody response = new FindVehicleResponseBody();
		response.setResponseStatus(status);
		response.setVehicle(null);
		return response;
	}
}
