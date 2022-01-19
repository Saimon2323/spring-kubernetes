package com.saimon.service;

import com.saimon.models.PatientEntity;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    PatientEntity addPatient(PatientEntity patientEntity);

    PatientEntity updatePatient(PatientEntity patientEntity);

    Optional<PatientEntity> getPatientById(int id);

    List<PatientEntity> getAllPatients();

    Optional<PatientEntity> getPatientByPatientName(String name);

    List<String> getAllSeparatelyByColumnName(String patientName);

    void deletePatient(int id);
}
