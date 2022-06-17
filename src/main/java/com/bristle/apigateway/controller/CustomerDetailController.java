package com.bristle.apigateway.controller;

import com.bristle.apigateway.converter.CustomerDetailEntityConverter;
import com.bristle.apigateway.model.CustomerEntity;
import com.bristle.apigateway.model.ResponseWrapper;
import com.bristle.apigateway.service.CustomerDetailService;
import com.bristle.apigateway.util.ProtobufUtil;
import com.bristle.proto.common.ApiError;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.common.ResponseContext;
import com.bristle.proto.customer_detail.Customer;
import com.bristle.proto.customer_detail.GetAllCustomersResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(path = "api/v1/customer-detail")
@RestController
public class CustomerDetailController {

    CustomerDetailService m_customerDetailService;

    CustomerDetailEntityConverter m_customerConverter;

    @Autowired
    public CustomerDetailController(CustomerDetailService m_customerDetailService, CustomerDetailEntityConverter m_customerConverter) {
        this.m_customerDetailService = m_customerDetailService;
        this.m_customerConverter = m_customerConverter;
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<ResponseWrapper<List<CustomerEntity>>> getAllCustomers(HttpServletRequest httpRequest) {
        GetAllCustomersResponse response = m_customerDetailService.getAllCustomers();
        ResponseContext responseContext = response.getResponseContext();
        if (responseContext.hasError()) {
            ApiError error = responseContext.getError();
            return new ResponseEntity<>(new ResponseWrapper<>(
                    httpRequest.getRequestURI(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    error.getErrorMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<Customer> customerProtoList = response.getCustomerList();
        List<CustomerEntity> result = customerProtoList.stream()
                .map(customer -> m_customerConverter.protoToEntity(customer))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ResponseWrapper<>(
                httpRequest.getRequestURI(),
                HttpStatus.OK.value(),
                "sucess",
                result
        ), HttpStatus.OK);
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<?> addCustomer(
            @RequestParam(name = "customerId", required = true) String customerId,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "contactName", required = false) String contactName,
            @RequestParam(name = "contactNumber", required = false) String contactNumber,
            @RequestParam(name = "contactMobileNumber", required = false) String contactMobileNumber,
            @RequestParam(name = "faxNumber", required = false) String faxNumber,
            @RequestParam(name = "postalCode", required = false) String postalCode,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "taxId", required = false) String taxId,
            @RequestParam(name = "receiver", required = false) String receiver,
            @RequestParam(name = "note", required = false) String note
    ) {
        try {
            m_customerDetailService.addCustomer(new CustomerEntity(
                    customerId,
                    name,
                    contactName,
                    contactNumber,
                    contactMobileNumber,
                    faxNumber,
                    postalCode,
                    address,
                    taxId,
                    receiver,
                    note
            ));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
