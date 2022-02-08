package com.saimon.dynamodbkubernetes.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.saimon.dynamodbkubernetes.entity.CustomerSync;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public CustomerRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public CustomerSync saveCustomer(CustomerSync customer) {
        dynamoDBMapper.save(customer);
        return customer;
    }

    public CustomerSync getCustomerById(String customerId) {
        return dynamoDBMapper.load(CustomerSync.class, customerId);
    }

    public List<CustomerSync> getCustomers() {
        return dynamoDBMapper.scan(CustomerSync.class, new DynamoDBScanExpression());
    }

    public String deleteCustomerById(String customerId) {
        dynamoDBMapper.delete(dynamoDBMapper.load(CustomerSync.class, customerId));
        return "Customer Id : "+ customerId +" Deleted!";
    }

    public String updateCustomer(String customerId, CustomerSync customer) {
        dynamoDBMapper.save(customer,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("customerId",
                                new ExpectedAttributeValue(
                                        new AttributeValue().withS(customerId)
                                )));
        return customerId;
    }
}