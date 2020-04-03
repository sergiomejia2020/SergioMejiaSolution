package com.sgma.controller.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import com.google.gson.Gson;

/**
 * This class contains the information that is used in the request.
 * As was mentioned in the test file, there is just a validation for transmission type, the others properties I just added
 * not null validation and size for String object and Min validation for Year
 * @author sgma
 *
 */
public class VehicleRequest {
	@NotEmpty(message="Vin property must contain information at least 1 character")
	@Size(min=1, max=25, message= "Vin property must be between 1 - 25 characters")
	private String vin;
	
	@Min(value = 2000, message = "Year should not be less than 2000")
	private int year;
	
	@NotEmpty(message="Make property must contain information at least 1 character")
	@Size(min=1, max=4, message= "Make property must be between 1 - 10 characters")
	private String make;
	
	@NotEmpty(message="Model property must contain information at least 1 character")
	@Size(min=1, max=4, message= "Model property must be between 1 - 10 characters")
	private String model;
	
	@NotEmpty(message="Transmission type property must contain information like Manual or Auto")
    @Size(min=4, max=10, message="Transmission type must be between 4 - 10 characters")
	private String transmissionType;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTransmissionType() {
		return transmissionType;
	}

	public void setTransmissionType(String transmissionType) {
		this.transmissionType = transmissionType;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
