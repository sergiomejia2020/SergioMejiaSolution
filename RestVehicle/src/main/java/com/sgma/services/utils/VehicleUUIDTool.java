package com.sgma.services.utils;

import java.util.regex.Pattern;
import com.fasterxml.uuid.Generators;


/**
 * This class is used to generate UUID identifier as is specified in the test file for this code challenge.
 * This API (Generators) provides a better performance in Multithreading scenarios
 * https://github.com/cowtowncoder/java-uuid-generator
 * @author sgma
 *
 */
public class VehicleUUIDTool {
	
	/**
	 * Defines UUID pattern 89faf622-622c-4443-baf6-22622c8443e8
	 */
	private static final Pattern UUID_PATTERN =
			  Pattern.compile("[a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12}");
	
	
	/**
	 * This methods create a UUID for vehicle id
	 * @return Unique UUID as String
	 */
	public static String getVehicleUUID() {
		return Generators.randomBasedGenerator().generate().toString();
	}

	/**
	 * Verifies that vehicle id is contains valid format, like 89faf622-622c-4443-baf6-22622c8443e8
	 * @param vvehicleUUID Vehicle id
	 * @return True if vehicle id is a valid format
	 */
	public static boolean verifyVehicleUUID(String vvehicleUUID) {
		return UUID_PATTERN.matcher(vvehicleUUID).find();
	}
}
