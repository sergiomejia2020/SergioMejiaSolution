package com.sgma;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.sgma.controller.VehicleRestController;
import com.sgma.controller.request.VehicleRequest;
import com.sgma.controller.response.ResponseStatus;
import com.sgma.controller.response.create.CreateVehicleResponse;
import com.sgma.controller.response.create.CreateVehicleResponseBody;
import com.sgma.services.VehicleService;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * this class executes tests with the Controller {@link VehicleRestController}  
 * @author sgma
 *
 */
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class RestVehicleApplicationTests {
	
	private String createVehiclePath = "/vehicles/registration";
	private String serverPort = "8085";
	private String serverIp = "http://localhost:".concat(serverPort);
	
	@Autowired
	private VehicleRestController vehicleController;

	@Autowired
	private TestRestTemplate restTemplate;

	private VehicleRequest vechileRequest;
	
	@Test
	void contextLoads() {
		assertThat(vehicleController).isNotNull();
	}
	
	@MockBean
	private VehicleService vehicleService;
	
	@BeforeAll
	public void createCorrectRequest() {
		vechileRequest = createVehicleRequest();
	}
	
	@BeforeEach
	public void setUpMockService(){
		Mockito.when(vehicleService.saveNewVehicle(Mockito.any(VehicleRequest.class))).thenReturn(createSuccessResponse());
	}
	
	/**
	 * Creates an instance of a valid request to create a new vehicle
	 * @return A valid request, see {@link VehicleRequest}
	 */
	public VehicleRequest createVehicleRequest() {
		VehicleRequest vechileRequest = new VehicleRequest();
		vechileRequest.setVin("1A4AABBC5KD501999");
		vechileRequest.setYear(2000);
		vechileRequest.setMake("FGF");
		vechileRequest.setModel("REF");
		vechileRequest.setTransmissionType("Manual");
		return vechileRequest;
	}
	
	/**
	 * Creates an instance of a successful response after create a new vehicle
	 * @return A successful response, see {@link CreateVehicleResponseBody}
	 */
	private CreateVehicleResponseBody createSuccessResponse() {
		CreateVehicleResponseBody response = new CreateVehicleResponseBody();
		CreateVehicleResponse vehicleRpe = new CreateVehicleResponse();
		vehicleRpe.setVehicleId("23M1238-12312DA-12312VSDG");
		ResponseStatus status = new ResponseStatus();
		response.setResponseStatus(status);
		response.setVehicleData(vehicleRpe);
		return response;
	}

	@Test
	public void createVehicleResponseIsNotNUll() throws Exception {
		assertThat(this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest, CreateVehicleResponseBody.class)).isNotNull();
	}
	
	@Test
	public void createVehicleContainsId() throws Exception {
		ResponseEntity<CreateVehicleResponseBody> response = this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest, CreateVehicleResponseBody.class);
		assertThat(response.getBody().getVehicleData().getVehicleId()).isNotBlank();
	}
	
	@Test
	public void createVehicleContainsEmptyErrorList() throws Exception {
		ResponseEntity<CreateVehicleResponseBody> response = this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest, CreateVehicleResponseBody.class);
		assertThat(response.getBody().getResponseStatus().getErrors()).isEmpty();
	}
	
	@Test
	public void createVehicleHttpStatusOKManualTrasmission() throws Exception {
		vechileRequest.setTransmissionType("Manual");
		ResponseEntity<CreateVehicleResponseBody> response = this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest, CreateVehicleResponseBody.class);
		assertThat(response.getBody().getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	public void createVehicleHttpStatusOKAutoTrasmission() throws Exception {
		vechileRequest.setTransmissionType("Auto");
		ResponseEntity<CreateVehicleResponseBody> response = this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest, CreateVehicleResponseBody.class);
		assertThat(response.getBody().getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	public void createVehicleWrongTrasmission() throws Exception {
		VehicleRequest vechileRequest2 = createVehicleRequest();
		vechileRequest2.setTransmissionType("hybrid");
		ResponseEntity<CreateVehicleResponseBody> response = this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest2, CreateVehicleResponseBody.class);
		assertThat(response.getBody().getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getBody().getResponseStatus().getDescription()).contains("Transmission type must be Manual or Auto");
		assertThat(response.getBody().getResponseStatus().getErrors()).isNotEmpty();
	}
	
	@Test
	public void createVehicleEmptyMake() {
		VehicleRequest vechileRequest2 = createVehicleRequest();
		vechileRequest2.setMake("");
		ResponseEntity<CreateVehicleResponseBody> response = this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest2, CreateVehicleResponseBody.class);
		assertThat(response.getBody().getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getBody().getResponseStatus().getErrors()).contains("Make property must contain information at least 1 character");
	}
	
	@Test
	public void createVehicleLongStringMake() {
		VehicleRequest vechileRequest2 = createVehicleRequest();
		vechileRequest2.setMake("WMEIFNDHRYTUNSNDHDKJ");
		ResponseEntity<CreateVehicleResponseBody> response = this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest2, CreateVehicleResponseBody.class);
		assertThat(response.getBody().getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getBody().getResponseStatus().getErrors()).contains("Make property must be between 1 - 10 characters");
	}
	
	@Test
	public void createVehicleEmptyModel() {
		VehicleRequest vechileRequest2 = createVehicleRequest();
		vechileRequest2.setModel("");
		ResponseEntity<CreateVehicleResponseBody> response = this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest2, CreateVehicleResponseBody.class);
		assertThat(response.getBody().getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getBody().getResponseStatus().getErrors()).contains("Model property must contain information at least 1 character");
	}
	
	@Test
	public void createVehicleLongStringModel() {
		VehicleRequest vechileRequest2 = createVehicleRequest();
		vechileRequest2.setModel("MQWJENERIOANDJSLAHNFN");
		ResponseEntity<CreateVehicleResponseBody> response = this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest2, CreateVehicleResponseBody.class);
		assertThat(response.getBody().getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getBody().getResponseStatus().getErrors()).contains("Model property must be between 1 - 10 characters");
	}
	
	@Test
	public void createVehicleEmptyVin() {
		VehicleRequest vechileRequest2 = createVehicleRequest();
		vechileRequest2.setVin("");
		ResponseEntity<CreateVehicleResponseBody> response = this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest2, CreateVehicleResponseBody.class);
		assertThat(response.getBody().getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getBody().getResponseStatus().getErrors()).contains("Vin property must contain information at least 1 character");
	}
	
	@Test
	public void createVehicleLongStringVin() {
		VehicleRequest vechileRequest2 = createVehicleRequest();
		vechileRequest2.setVin("IOWMEURNPNFGUREIRNGOWEINEFIEWPWENERPWEUEN");
		ResponseEntity<CreateVehicleResponseBody> response = this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest2, CreateVehicleResponseBody.class);
		assertThat(response.getBody().getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getBody().getResponseStatus().getErrors()).contains("Vin property must be between 1 - 25 characters");
	}
	
	@Test
	public void createVehicleIncorrectYear() {
		VehicleRequest vechileRequest2 = createVehicleRequest();
		vechileRequest2.setYear(1990);
		ResponseEntity<CreateVehicleResponseBody> response = this.restTemplate.postForEntity(serverIp.concat(createVehiclePath),
				vechileRequest2, CreateVehicleResponseBody.class);
		assertThat(response.getBody().getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getBody().getResponseStatus().getErrors()).contains("Year should not be less than 2000");
	}
	
}
