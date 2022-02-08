package com.saimon.dynamodbkubernetes.controller;

import com.saimon.dynamodbkubernetes.entity.CustomerSync;
import com.saimon.dynamodbkubernetes.repository.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping("/add/customer")
    public CustomerSync saveCustomer(@RequestBody CustomerSync customer) {
        return customerRepository.saveCustomer(customer);
    }

    @GetMapping("/get/customer/{id}")
    public CustomerSync getCustomerById(@PathVariable("id") String customerId) {
        return customerRepository.getCustomerById(customerId);
    }

    @GetMapping("/get/customers")
    public List<CustomerSync> getCustomers() {
        return customerRepository.getCustomers();
    }

    @DeleteMapping("/delete/customer/{id}")
    public String deleteCustomerById(@PathVariable("id") String customerId) {
        return  customerRepository.deleteCustomerById(customerId);
    }

    @PutMapping("/update/customer/{id}")
    public String updateCustomer(@PathVariable("id") String customerId, @RequestBody CustomerSync customer) {
        return customerRepository.updateCustomer(customerId,customer);
    }
}