package com.saimon.webfluxDynamoDBKubernetes.entity;

import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@ToString
public class Customer {
    private String customerID;
    private String firstName;
    private String lastName;
    private String contactNo;
    private Address address;
    private String createdTimeStamp;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("CustomerID")
    public String getCustomerID() {
        return customerID;
    }

    @DynamoDbAttribute("CustomerFirstName")
    public String getFirstName() {
        return firstName;
    }

    @DynamoDbAttribute("CustomerLastName")
    public String getLastName() {
        return lastName;
    }

    @DynamoDbAttribute("CustomerContactNumber")
    public String getContactNo() {
        return contactNo;
    }

    @DynamoDbAttribute("CustomerAddress")
    public Address getAddress() {
        return address;
    }

    @DynamoDbAttribute("CustomerCreatedTime")
    public String getCreatedTimeStamp() { return createdTimeStamp; }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setCreatedTimeStamp(String createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }
}
