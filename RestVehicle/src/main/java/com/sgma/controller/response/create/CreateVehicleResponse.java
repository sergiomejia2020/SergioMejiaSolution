package com.sgma.controller.response.create;

import com.google.gson.Gson;

/**
 * This class is used to send back a response for create vehicle request.
 * Contains vehicle id. This class can contain more information but because the
 * is a test I was not sure what information to add
 * 
 * @author sgma
 *
 */
public class CreateVehicleResponse {
	private String vehicleId;

	public CreateVehicleResponse() {
		this.vehicleId = "";
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
