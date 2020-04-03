package com.sgma.controller;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.sgma.controller.request.VehicleRequest;
import com.sgma.controller.response.create.CreateVehicleResponseBody;
import com.sgma.controller.response.find.FindVehicleResponseBody;
import com.sgma.services.interfaces.VehicleServiceInterface;
import com.sgma.services.utils.CreateVehicleErrorResponse;
import com.sgma.services.utils.VehicleUUIDTool;

/**
 * This class is used as a Controller that is going to receive any request to execute an operation related to Vehicle. 
 * For example, create new vehicle
 * @author sgma
 *
 */
@RestController
public class VehicleRestController {

	@Autowired
	private VehicleServiceInterface vehicleService;
	
	private static final String BAD_REQUEST_NO_VALID_DATA = "Request does not contains valid data";

	/**
	 * Handlers create new vehicle, inside this method is called {@link VehicleServiceInterface#saveNewVehicle}
	 * @param vehicleRqtOptional Is the request that contains vehicle information see {@link VehicleRequest]
	 * @param responseHeaders HttpServletResponse that is used to add parameters into the header
	 * @return Information that contains the response data and response status see {@link CreateVehicleResponseBody}
	 */
	@PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateVehicleResponseBody> createVehicle(
			@NotNull @Valid @RequestBody Optional<VehicleRequest> vehicleRqtOptional, HttpServletResponse responseHeaders) {

		if (!vehicleRqtOptional.isPresent()) {
			return new ResponseEntity<>(CreateVehicleErrorResponse.createVehicleErrorResponse(400,
					"The request body can not be empty", 
					BAD_REQUEST_NO_VALID_DATA),
					HttpStatus.BAD_REQUEST);
		}

		VehicleRequest vehicleRqt = vehicleRqtOptional.get();
		if (!validateTransmitionType(vehicleRqt.getTransmissionType())) {
			return new ResponseEntity<>(CreateVehicleErrorResponse.createVehicleErrorResponse(400,
					"Transmission type must be Manual or Auto", 
					BAD_REQUEST_NO_VALID_DATA),
					HttpStatus.BAD_REQUEST);
		}
		
		/*
		 * I added HttpServletResponse responseHeaders because I was not sure if I need to add something in the header.
		 * If necessary, is as simple like responseHeaders.addHeader(name, value) or responseHeaders.setHeader(name, value)
		 * 
		 */
		CreateVehicleResponseBody response = vehicleService.saveNewVehicle(vehicleRqt);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseStatus().getHttpStatus()));
	}
	

	/**
	 * Handlers find a vehicle by uuid, inside this method is called {@link VehicleServiceInterface#findVehicleById(String)}
	 * @param vehicleIdOptional String that contains vehicle uuid
	 * @param responseHeaders HttpServletResponse that is used to add parameters into the header
	 * @return Information that contains the response data and response status see {@link FindVehicleResponseBody}
	 */
	@GetMapping(value = "/registration/id/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FindVehicleResponseBody> getVehicleById(
			@NotNull @PathVariable("uuid") Optional<String> vehicleIdOptional, HttpServletResponse responseHeaders) {
		
		if (!vehicleIdOptional.isPresent()) {
			return new ResponseEntity<>(CreateVehicleErrorResponse.findVehicleErrorResponse(400,
					"The request body can not be empty", 
					BAD_REQUEST_NO_VALID_DATA),
					HttpStatus.BAD_REQUEST);
		}
		
		if (!VehicleUUIDTool.verifyVehicleUUID(vehicleIdOptional.get())) {
			return new ResponseEntity<>(CreateVehicleErrorResponse.findVehicleErrorResponse(400,
					"The request is not valid", 
					BAD_REQUEST_NO_VALID_DATA),
					HttpStatus.BAD_REQUEST);
		}
		
		FindVehicleResponseBody response = vehicleService.findVehicleById(vehicleIdOptional.get());
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseStatus().getHttpStatus()));
	}

	/**
	 * Validates if Transmission type contains valid information
	 * 
	 * @param transmitionType can be manual or auto
	 * @return True if Transmission type is manual or auto
	 */
	private boolean validateTransmitionType(String transmissionType) {
		return transmissionType.equalsIgnoreCase("manual") || transmissionType.equalsIgnoreCase("auto");
	}
}
