package com.bristle.apigateway.converter;

import com.bristle.apigateway.model.CustomerEntity;
import com.bristle.proto.customer_detail.Customer;
import com.bristle.proto.customer_detail.CustomerDetail;
import org.springframework.stereotype.Component;

@Component
public class CustomerDetailEntityConverter {
    public CustomerEntity protoToEntity(Customer customerProto){
        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setCustomerId(customerProto.getCustomerId());
        customerEntity.setName(customerProto.getName());
        customerEntity.setContactName(customerProto.getContactName());
        customerEntity.setContactNumber(customerProto.getContactNumber());
        customerEntity.setContactMobileNumber(customerProto.getContactMobileNumber());
        customerEntity.setFaxNumber(customerProto.getFaxNumber());
        customerEntity.setPostalCode(customerProto.getPostalCode());
        customerEntity.setAddress(customerProto.getAddress());
        customerEntity.setTaxId(customerProto.getTaxId());
        customerEntity.setReceiver(customerProto.getReceiver());
        customerEntity.setNote(customerProto.getNote());
        return customerEntity;
    }
}
