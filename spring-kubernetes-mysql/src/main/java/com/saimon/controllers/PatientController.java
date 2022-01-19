package com.saimon.controllers;

import com.saimon.models.PatientEntity;
import com.saimon.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping(value = "/patients/add")
    public PatientEntity addPatient(@RequestBody PatientEntity patientEntity) {
        return patientService.addPatient(patientEntity);
    }

    @GetMapping(value = "/patients")
    public List<PatientEntity> getAllPatients() {
        // these lines(26-29) are testing purposes. to check individual items.
        List<PatientEntity> patients = patientService.getAllPatients();
        patients.forEach(patient -> {
            System.out.println(patient.getPatientName());
        });
        return patientService.getAllPatients(); //this line is enough.
    }
    /*
     Upore method a annotation normally eta @GetMapping(value = "/patients") and eta dile default vabe result JSON format a dibe.
     But amra jodi XML format a results generate korte chai, then @GetMapping(value = "/patients", produces = {"application/xml"}) likhte hobe.
     er jonno must ekta dependency add korte hobe pom.xml file a(Given in POM)
     and Database connection cls er upore @XmlRootElement annotation add korte hobe (jodio na korle o kaj kore)
     */

    @GetMapping(value = "/patients/only/{columnName}") //key like patientName, patientContactNo etc.
    public List<String> getAllSeparatelyByColumnName(@PathVariable String columnName) {
        return patientService.getAllSeparatelyByColumnName(columnName);
    }

    @GetMapping(value = "/patients/{id}")
    public Optional<PatientEntity> getPatientById(@PathVariable int id) {
        return patientService.getPatientById(id);
    }

    @GetMapping(value = "/patients/name/{patientName}")
    public Optional<PatientEntity> getPatientByPatientName(@PathVariable String patientName) {
        return patientService.getPatientByPatientName(patientName);
    }

    @PutMapping(value = "/patients/update")
    public PatientEntity updatePatient(@RequestBody PatientEntity patientEntity) {

        Optional<PatientEntity> patient = patientService.getPatientById(patientEntity.getPatientId());

        if (patient.isPresent()) {
            return patientService.updatePatient(patientEntity);
        }

        return null;
    }

    @DeleteMapping(value = "/patients/{id}")
    public void deletePatient(@PathVariable int id) {
        patientService.deletePatient(id);
    }

}
