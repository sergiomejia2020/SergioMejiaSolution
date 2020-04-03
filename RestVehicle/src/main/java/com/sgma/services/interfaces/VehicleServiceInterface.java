package com.sgma.services.interfaces;

import com.sgma.controller.request.VehicleRequest;
import com.sgma.controller.response.create.CreateVehicleResponseBody;
import com.sgma.controller.response.find.FindVehicleResponseBody;

/**
 * This interface defines the behavior to handler incoming requests
 * @author sgma
 *
 */
public interface VehicleServiceInterface {
	/**
	 * This method saves a new vehicle
	 * @param vehicleRqt The request that contains vehicle data see {@link VehicleRequest}
	 * @return The response that contains vehicle information and response status information see {@link CreateVehicleResponseBody}
	 */
	CreateVehicleResponseBody saveNewVehicle(VehicleRequest vehicleRqt);
	
	/**
	 * Is used to find a vehicle by id
	 * @param id Valid vehicle id
	 * @return the response that contains vehicle information and response status information see {@link FindVehicleResponseBody}
	 */
	FindVehicleResponseBody findVehicleById(String id);
}
