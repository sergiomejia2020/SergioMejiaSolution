package com.sgma;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import com.sgma.controller.request.VehicleRequest;
import com.sgma.controller.response.create.CreateVehicleResponseBody;
import com.sgma.jpa.VehiclesRepository;
import com.sgma.jpa.domains.VehicleEntity;
import com.sgma.services.VehicleService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class executes tests with the Service {@link VehicleService} 
 * @author sgma
 *
 */
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class ServiceVehicleApplicationTests {
	
	private VehicleEntity vehicleEntity = new VehicleEntity();
	
	@Autowired
	private VehicleService vehicleService;

	private VehicleRequest vehicleRequest;
	
	@MockBean
	private VehiclesRepository vehicleRepository;
	
	@BeforeEach
	public void setUpMockService(){
		Mockito.when(vehicleRepository.save(Mockito.any(VehicleEntity.class))).thenReturn(vehicleEntity);
	}
	
	@Test
	void contextLoads() {
		assertThat(vehicleService).isNotNull();
	}

	public VehicleRequest createVehicleRequest() {
		VehicleRequest vechileRequest = new VehicleRequest();
		vechileRequest.setVin("1A4AABBC5KD501999");
		vechileRequest.setYear(2000);
		vechileRequest.setMake("FGF");
		vechileRequest.setModel("REF");
		vechileRequest.setTransmissionType("Manual");
		return vechileRequest;
	}
	
	@BeforeAll
	public void createCorrectRequest() {
		vehicleRequest = createVehicleRequest();
	}

	@Test
	public void createVehicleResponseIsNotNUll() throws Exception {
		assertThat(vehicleService.saveNewVehicle(vehicleRequest)).isNotNull();
	}
	
	@Test
	public void createVehicleResponseStatusIsNotNull() throws Exception {
		assertThat(vehicleService.saveNewVehicle(vehicleRequest).getResponseStatus()).isNotNull();
	}
	
	@Test
	public void createVehicleResponseIsNotNull() throws Exception {
		assertThat(vehicleService.saveNewVehicle(vehicleRequest).getVehicleData()).isNotNull();
	}
	
	@Test
	public void createVehicleResponseHttpStatusOK() throws Exception {
		assertThat(vehicleService.saveNewVehicle(vehicleRequest).getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	public void createVehicleResponseVehicleIdIsNotNull() throws Exception {
		assertThat(vehicleService.saveNewVehicle(vehicleRequest).getVehicleData().getVehicleId()).isNotEmpty();
	}
	
	@Test
	public void findVehicleResponseEmpty() {
		assertThat(vehicleService.findVehicleById("89faf622-622c-4443-baf6-22622c8443e8").getVehicle()).isNull();
	}
	
	@Test
	public void findVehicleResponseHttpStatusNotFound() {
		assertThat(vehicleService.findVehicleById("89faf622-622c-4443-baf6-22622c8443e8")
				.getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void createVehicleResponseHttpStatus500() throws Exception {
		VehicleRequest vehicleRequest2 = createVehicleRequest();
		vehicleRequest2.setMake("FFF");
		CreateVehicleResponseBody response = vehicleService.saveNewVehicle(vehicleRequest2);
		assertThat(response.getResponseStatus().getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
		assertThat(response.getResponseStatus().getErrors()).isNotEmpty();
		assertThat(response.getResponseStatus().getDescription()).isEqualTo("Is not possible to processed the request");
	}
}
