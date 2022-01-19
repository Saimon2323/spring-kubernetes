package com.saimon.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement  // for generating result in xml format. eta dile o chole abar na dile problem nai.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "patient_info")
public class PatientEntity {

    @Id
    @GeneratedValue
    @NotNull
    private int patientId;

    @NotNull
    private String patientName;

    @NotNull
    private String patientAddress;

    @NotNull
    private int patientAge;

    @NotNull
    private String patientContactNo;

    @NotNull
    private String patientEmail;

}
