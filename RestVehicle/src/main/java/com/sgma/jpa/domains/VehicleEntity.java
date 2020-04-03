package com.sgma.jpa.domains;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import com.google.gson.Gson;

/**
 * This class is an Entity class to map a POJO to a database table.
 * 
 * I decide to add id as Long because we can use this long value if we need to store many vehicles in a HashMap, long value has a better performance that the default hash code value
 * but an int hash code value has better performance that a long value. If we decide to use custom has code value is necessary to override getHashcode() and equals method in this class or
 * in another class. I do not return long id in the response when we are create a new vehicle because is not part of the requirement.
 * @author sgma
 *
 */
@Entity
public class VehicleEntity {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message="UUID property must contain information")
	private String uuid;
	
	@NotEmpty(message="Vin property must contain information at least 1 character")
	@Size(min=1, max=25, message= "Vin property must be between 1 - 25 characters")
	private String vin;
	
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

	public VehicleEntity() {
		this.uuid = "ADFGE23423";
		this.vin = "A";
		this.year = 2000;
		this.make = "M";
		this.model = "T";
		this.transmissionType = "AUTO";
	}

	public VehicleEntity(String uuid, String vin, int year, String make, String model, String transmissionType) {
		this.uuid = uuid;
		this.vin = vin;
		this.year = year;
		this.make = make;
		this.model = model;
		this.transmissionType = transmissionType;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

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
