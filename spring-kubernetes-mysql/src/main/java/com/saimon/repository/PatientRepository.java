package com.saimon.repository;

import com.saimon.models.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//JpaRepository extends CrudRepository. So its better to use JpaRepository and also efficient.
public interface PatientRepository extends JpaRepository<PatientEntity, Integer> {
    Optional<PatientEntity> findByPatientName(String patientName);
}
