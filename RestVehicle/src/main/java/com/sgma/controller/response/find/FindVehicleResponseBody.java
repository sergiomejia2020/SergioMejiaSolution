package com.sgma.controller.response.find;

import com.google.gson.Gson;
import com.sgma.controller.response.ResponseStatus;
import com.sgma.jpa.domains.VehicleEntity;

/**
 * This class is used to send back as a response vehicle information and the request processing status.
 * Contains {@link VehicleEntity} and {@link ResponseStatus}
 * @author sgma
 *
 */
public class FindVehicleResponseBody {
	private VehicleEntity vehicle;
	private ResponseStatus responseStatus;

	public VehicleEntity getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleEntity vehicle) {
		this.vehicle = vehicle;
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
