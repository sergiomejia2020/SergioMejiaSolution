package com.sgma.controller.interfaces;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import com.sgma.controller.request.VehicleRequest;
import com.sgma.controller.response.create.CreateVehicleResponseBody;

/**
 * This interface defines the behavior of the controller about the operations related to Vehicle
 * @author sgma
 *
 */
public interface VehicleRestControllerInterface {
	/**
	 * This method is used to save a new vehicle
	 * @param vehicleRqtOptional Is the request that contains vehicle information see {@link VehicleRequest]
	 * @param responseHeaders HttpServletResponse that is used to add parameters into the header
	 * @return Information that contains the response data and response status see {@link CreateVehicleResponseBody}
	 */
	ResponseEntity<CreateVehicleResponseBody> createVehicle(Optional<VehicleRequest> vehicleRqtOptional, HttpServletResponse responseHeaders);
}
