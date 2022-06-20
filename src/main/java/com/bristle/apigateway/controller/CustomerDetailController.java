package com.bristle.apigateway.controller;

import com.bristle.apigateway.model.CustomerEntity;
import com.bristle.apigateway.model.ResponseWrapper;
import com.bristle.apigateway.service.CustomerDetailService;
import com.bristle.proto.common.RequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "api/v1/customer-detail")
@RestController
public class CustomerDetailController {

    Logger log = LoggerFactory.getLogger(CustomerDetailController.class);


    CustomerDetailService m_customerDetailService;

    @Autowired
    public CustomerDetailController(CustomerDetailService m_customerDetailService) {
        this.m_customerDetailService = m_customerDetailService;
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<ResponseWrapper<List<CustomerEntity>>> getAllCustomers(
            HttpServletRequest httpRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: "+requestId + "getAllCustomers request received");
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            List<CustomerEntity> resultList= m_customerDetailService.getAllCustomers(requestContextBuilder);
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    resultList
            ), HttpStatus.OK);

        } catch (Exception exception){
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upsertCustomer")
    public ResponseEntity<ResponseWrapper<CustomerEntity>> upsertCustomer(
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
            @RequestParam(name = "note", required = false) String note,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: "+requestId + "getAllCustomers request received");
        RequestContext.Builder requestContext = RequestContext.newBuilder();
        requestContext.setRequestId(requestId);
        CustomerEntity customerEntity = new CustomerEntity(
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
        );

        try {
            CustomerEntity addedCustomer = m_customerDetailService.upsertCustomer(requestContext,customerEntity);
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),

                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    addedCustomer
            ), HttpStatus.OK);

        } catch (Exception exception){
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteCustomer")
    public ResponseEntity<ResponseWrapper<CustomerEntity>> deleteCustomer(
            @RequestParam(name = "customerId", required = true) String customerId,
            HttpServletRequest httpRequest ){

        String requestId = UUID.randomUUID().toString();
        log.info("Request id: "+requestId + "getAllCustomers request received");
        RequestContext.Builder requestContext = RequestContext.newBuilder();
        requestContext.setRequestId(requestId);

        try {
            CustomerEntity deletedCustomer = m_customerDetailService.deleteCustomerById(requestContext, customerId);
            if(deletedCustomer == null){
                return new ResponseEntity<>(new ResponseWrapper<>(
                        LocalDateTime.now(),
                        httpRequest.getRequestURI(),
                        requestId,
                        HttpStatus.ACCEPTED.value(),
                        "Customer with id " + customerId + " does not exist.",
                        deletedCustomer
                ), HttpStatus.ACCEPTED);
            }

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    deletedCustomer
            ), HttpStatus.OK);
        }catch (Exception e){

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
