package com.sgma.jpa;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.sgma.jpa.domains.VehicleEntity;

public interface VehiclesRepository extends CrudRepository<VehicleEntity, Long>{
	Optional<VehicleEntity> findByUuid(String uuid);
}
