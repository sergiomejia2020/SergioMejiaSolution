package com.sgma.controller.response;

import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import com.google.gson.Gson;

/**
 * This class is used to store response information like Http status, a brief description of the status and
 * a list of errors if any error occurs processing the request.
 * @author sgma
 *
 */
public class ResponseStatus {
	private int httpStatus;
	private String timeStamp;
	private String description;
	private List<String> errors;

	public ResponseStatus() {
		this.httpStatus = HttpStatus.OK.value();
		this.timeStamp = "";
		this.description = "The request was processed successfully";
		this.errors = Collections.emptyList();
	}
	
	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
