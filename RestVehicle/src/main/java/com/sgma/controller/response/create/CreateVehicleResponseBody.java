package com.sgma.controller.response.create;

import com.google.gson.Gson;
import com.sgma.controller.response.ResponseStatus;

/**
 * This class is used to send back a response for create vehicle request,
 * contains {@link CreateVehicleResponse} and {@link ResponseStatus}
 * 
 * @author sgma
 *
 */
public class CreateVehicleResponseBody {
	private CreateVehicleResponse vehicleData;
	private ResponseStatus responseStatus;

	public CreateVehicleResponse getVehicleData() {
		return vehicleData;
	}

	public void setVehicleData(CreateVehicleResponse vehicleData) {
		this.vehicleData = vehicleData;
	}

	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
