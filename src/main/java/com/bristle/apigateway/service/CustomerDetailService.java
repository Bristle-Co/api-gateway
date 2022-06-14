package com.bristle.apigateway.service;

import com.bristle.apigateway.model.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerDetailService {

    @Transactional(readOnly = true)
    public List<CustomerEntity> getAllCustomers() throws Exception {
        // grpc request to customer-detail
        return null;
    }

    @Transactional
    public void addCustomer(CustomerEntity customerEntity) throws Exception {
        // grpc request to customer-detail
    }
}
