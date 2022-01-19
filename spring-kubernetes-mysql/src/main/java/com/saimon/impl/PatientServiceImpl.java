package com.saimon.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saimon.models.PatientEntity;
import com.saimon.repository.PatientRepository;
import com.saimon.service.PatientService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PatientEntity addPatient(PatientEntity patientEntity) {
        return patientRepository.save(patientEntity);
    }

    @Override
    public PatientEntity updatePatient(PatientEntity patientEntity) {
        return patientRepository.save(patientEntity);
    }

    @Override
    public Optional<PatientEntity> getPatientById(int id) {
        return patientRepository.findById(id);
    }

    @Override
    public List<PatientEntity> getAllPatients() {
        return patientRepository.findAll(Sort.by("patientId").descending());
/*
        return patientRepository.findAll(Sort.by(Sort.Direction.DESC, "patientId"));
        return patientRepository.findAll(Sort.by(Sort.Order.desc("patientId")));
*/

//        List<PatientEntity> patients = new ArrayList<>();
//        patientRepository.findAll().forEach(patients::add);
//        return patients;
    }

    @Override
    public Optional<PatientEntity> getPatientByPatientName(String patientName) {
        return patientRepository.findByPatientName(patientName);
    }

    @Override
    public List<String> getAllSeparatelyByColumnName(String patientName) {
        List<PatientEntity> patients = patientRepository.findAll();

//        JSONArray jsonArray = new JSONArray(patients);
//        return IntStream.range(0, jsonArray.length())
//                .mapToObj(index -> ((JSONObject)jsonArray.get(index)).optString(patientName))
//                .collect(Collectors.toList());

        return patients.stream().map(p ->
                String.valueOf(objectMapper.convertValue(p, Map.class).get(patientName))).collect(Collectors.toList());
    }

    @Override
    public void deletePatient(int id) {
        patientRepository.deleteById(id);
    }

}
