package com.sgma;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.sgma.controller.request.VehicleRequest;
import com.sgma.jpa.VehiclesRepository;
import com.sgma.jpa.domains.VehicleEntity;
import com.sgma.services.utils.VehicleUUIDTool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class VehicleRepositoryApplicationTest {

	@Autowired
	private VehiclesRepository vehicleRepository;
	
	private VehicleEntity vehicleEntity;
	
	private String vehicleUUID;

	/**
	 * Creates an instance of a valid vehicle entity
	 * @return A valid request, see {@link VehicleEntity}
	 */
	public VehicleEntity createVehicleRequest() {
		VehicleRequest vehicleRqt = new VehicleRequest();
		vehicleRqt.setVin("1A4AABBC5KD501999");
		vehicleRqt.setYear(2000);
		vehicleRqt.setMake("FGF");
		vehicleRqt.setModel("REF");
		vehicleRqt.setTransmissionType("Manual");
		
		vehicleEntity = new VehicleEntity(vehicleUUID, 
				vehicleRqt.getVin(), vehicleRqt.getYear(), vehicleRqt.getMake(),
				vehicleRqt.getModel(), vehicleRqt.getTransmissionType());
		return vehicleEntity;
	}
	
	@BeforeAll
	public void createCorrectRequest() {
		vehicleUUID = VehicleUUIDTool.getVehicleUUID();
		createVehicleRequest();
	}
	
	@Test
	public void saveNewVehicleTest() {
		VehicleEntity vehicle = vehicleRepository.save(vehicleEntity);
		assertThat(vehicle.getUUID()).isNotNull();
		assertThat(vehicle.getId()).isGreaterThan(0);
	}
	
	@Test
	public void findVehicleByUUIDAndByIdTest() {
		VehicleEntity vehicle = vehicleRepository.save(vehicleEntity);
		Optional<VehicleEntity> vehicle1 = vehicleRepository.findByUuid(vehicle.getUUID());
		assertThat(vehicle1.get()).isNotNull();
		Optional<VehicleEntity> vehicle2 = vehicleRepository.findById(vehicle.getId());
		assertThat(vehicle2.get()).isNotNull();
		assertThat(vehicle1.get()).isEqualTo(vehicle2.get());
	}
}
