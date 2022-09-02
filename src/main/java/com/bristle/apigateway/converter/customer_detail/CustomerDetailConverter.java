package com.bristle.apigateway.converter.customer_detail;

import com.bristle.apigateway.model.customer_detail.CustomerEntity;
import com.bristle.apigateway.model.dto.customer_detail.CustomerDto;
import com.bristle.proto.customer_detail.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDetailConverter {
    public CustomerDto protoToDto(Customer customerProto){

        return new CustomerDto(customerProto.getCustomerId(),
                customerProto.getName().equals("") ? null : customerProto.getName(),
                customerProto.getContactName().equals("") ? null : customerProto.getContactName(),
                customerProto.getContactNumber().equals("") ? null : customerProto.getContactNumber(),
                customerProto.getContactMobileNumber().equals("") ? null : customerProto.getContactMobileNumber(),
                customerProto.getFaxNumber().equals("") ? null : customerProto.getFaxNumber(),
                customerProto.getPostalCode().equals("") ? null : customerProto.getPostalCode(),
                customerProto.getAddress().equals("") ? null : customerProto.getAddress(),
                customerProto.getTaxId().equals("") ? null : customerProto.getTaxId(),
                customerProto.getReceiver().equals("") ? null : customerProto.getReceiver(),
                customerProto.getNote().equals("") ? null : customerProto.getNote());
    }

    public Customer dtoToProto(CustomerDto customerDto){

        return Customer.newBuilder()
                .setCustomerId(customerDto.getCustomerId())
                .setName(customerDto.getName() == null? "" : customerDto.getName())
                .setContactName(customerDto.getContactName() == null ? "" : customerDto.getContactName())
                .setContactNumber(customerDto.getContactNumber() == null ? "" : customerDto.getContactNumber())
                .setContactMobileNumber(customerDto.getContactMobileNumber() == null ? "" : customerDto.getContactMobileNumber())
                .setFaxNumber(customerDto.getFaxNumber() == null ? "" : customerDto.getFaxNumber())
                .setPostalCode(customerDto.getPostalCode() == null ? "" : customerDto.getPostalCode())
                .setAddress(customerDto.getAddress() == null ? "" : customerDto.getAddress())
                .setTaxId(customerDto.getTaxId() == null ? "" : customerDto.getTaxId())
                .setReceiver(customerDto.getReceiver() ==  null ? "" : customerDto.getReceiver())
                .setNote(customerDto.getNote() == null ? "" : customerDto.getNote()).build();
    }
}
