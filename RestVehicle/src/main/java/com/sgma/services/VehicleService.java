package com.sgma.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.sgma.controller.request.VehicleRequest;
import com.sgma.controller.response.ResponseStatus;
import com.sgma.controller.response.create.CreateVehicleResponse;
import com.sgma.controller.response.create.CreateVehicleResponseBody;
import com.sgma.controller.response.find.FindVehicleResponseBody;
import com.sgma.jpa.VehiclesRepository;
import com.sgma.jpa.domains.VehicleEntity;
import com.sgma.services.interfaces.VehicleServiceInterface;
import com.sgma.services.utils.VehicleUUIDTool;

/**
 * This class in used as a Service that is going to process any request about
 * create a vehicle. The scope is prototype because in the case that we have
 * many requests in 100 milliseconds we can start to have concurrency issues
 * 
 * @author sgma
 *
 */
@Service
@Scope("prototype")
public class VehicleService implements VehicleServiceInterface {

	@Autowired
	private DBServiceA dbServiceA;

	@Autowired
	private HttpServiceB httpServiceB;

	@Autowired
	private VehiclesRepository vehicleRepository;

	/**
	 * This method is used to create a vehicle
	 * 
	 * @param vehicleRqt A request that contains vehicle data see {@link CreateVehicleResponseBody}
	 * @return Information about the processed request like status details and vehicle id
	 */
	@Override
	public CreateVehicleResponseBody saveNewVehicle(VehicleRequest vehicleRqt) {
		CreateVehicleResponseBody response = new CreateVehicleResponseBody();
		CreateVehicleResponse vehicleRpe = new CreateVehicleResponse();
		ResponseStatus status = new ResponseStatus();
		try {
			// The method inside may catch the exception save the information about the
			// exception and then throw the same Exception
			// or a custom Exception if occurs
			vehicleRpe.setVehicleId(VehicleUUIDTool.getVehicleUUID());
			
			//The new vehicle is stored in the database
			vehicleRepository.save(new VehicleEntity(vehicleRpe.getVehicleId(), 
					vehicleRqt.getVin(), vehicleRqt.getYear(), vehicleRqt.getMake(),
					vehicleRqt.getModel(), vehicleRqt.getTransmissionType()));
			
			// This if statement is just to tell dbService.getdata to throw an exception to
			// test AOP Afterthrowing and a test case with Internal Server Error
			int id = 10;
			if (vehicleRqt.getMake().equalsIgnoreCase("FFF")) {
				id = 0;
			}

			//Simple example to about asynchronous process with Future tasks
			//Lets say that after the new vehicle is stored in the database, is necessary to retrieve data from another place and send a message to a messaging service
			//I'm using this classes just to show an asynchronous processing
			CompletableFuture<String> dataString = dbServiceA.getData(id);
			CompletableFuture<String> serviceMessage = httpServiceB.sendMessage("Simple message");

			CompletableFuture.allOf(dataString, serviceMessage).join();			
		} catch (Exception ex) {
			status.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			status.setDescription("Is not possible to processed the request");
			List<String> errors = new ArrayList<>();
			errors.add("Error processing the request saveNewVehicle ".concat(vehicleRqt.toString()));
			status.setErrors(errors);
		} finally {
			status.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());
			response.setResponseStatus(status);
			response.setVehicleData(vehicleRpe);
		}

		return response;
	}
	
	/**
	 * Finds a vehicle by uuid
	 * @param uuid Valid vehicle uuid like 89faf622-622c-4443-baf6-22622c8443e8
	 * @return Information about the vehicle if found, see {@link FindVehicleResponseBody}
	 */
	@Override
	public FindVehicleResponseBody findVehicleById(String uuid) {
		FindVehicleResponseBody response = new FindVehicleResponseBody();
		response.setVehicle(null);
		ResponseStatus status = new ResponseStatus();
		try {
			Optional<VehicleEntity> vehicle = vehicleRepository.findByUuid(uuid);
			if(vehicle.isPresent()) {
				response.setVehicle(vehicle.get());
			} else {
				status.setHttpStatus(HttpStatus.NOT_FOUND.value());
				status.setDescription("Vehicle not found whith the given id");
			}
		} catch (Exception ex) {
			status.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			status.setDescription("Is not possible to processed the request");
			List<String> errors = new ArrayList<>();
			errors.add("Error processing the request findVehicleById ".concat(uuid));
			status.setErrors(errors);
		} finally {
			status.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());
			response.setResponseStatus(status);
		}

		return response;
	}
}
