package com.bristle.apigateway.controller;

import com.bristle.apigateway.model.customer_detail.CustomerEntity;
import com.bristle.apigateway.model.ResponseWrapper;
import com.bristle.apigateway.service.CustomerDetailService;
import com.bristle.proto.common.RequestContext;

import com.bristle.proto.customer_detail.CustomerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "api/v1/customer_detail")
@RestController
public class CustomerDetailController {

    Logger log = LoggerFactory.getLogger(CustomerDetailController.class);
    CustomerDetailService m_customerDetailService;

    public CustomerDetailController(CustomerDetailService m_customerDetailService) {
        this.m_customerDetailService = m_customerDetailService;
    }

    @GetMapping("/getCustomers")
    public ResponseEntity<ResponseWrapper<List<CustomerEntity>>> getCustomers(
            @RequestParam(name = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "customerId", required = false) String customerId,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "contactName", required = false) String contactName,
            @RequestParam(name = "contactNumber", required = false) String contactNumber,
            @RequestParam(name = "address", required = false) String address,
            HttpServletRequest httpRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "getAllCustomers request received");
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        // construct filter
        CustomerFilter filter = CustomerFilter.newBuilder()
                .setCustomerId(customerId == null ? "" : customerId)
                .setName(name == null ? "" : name)
                .setContactName(contactName == null ? "" : contactName)
                .setContactNumber(contactName == null ? "" : contactNumber)
                .setAddress(address == null ? "" : address)
                .build();

        try {
            List<CustomerEntity> resultList = m_customerDetailService.getCustomers(requestContextBuilder,
                    filter,
                    pageSize == null ? 20 : pageSize,
                    pageIndex == null ? 0 : pageIndex);
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    resultList
            ), HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateCustomer")
    public ResponseEntity<ResponseWrapper<CustomerEntity>> updateCustomer(
            @RequestBody CustomerEntity customerEntity,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "updateCustomer request received. \n" + customerEntity.toString());
        RequestContext.Builder requestContext = RequestContext.newBuilder();
        requestContext.setRequestId(requestId);

        try {
            if (customerEntity.getCustomerId().equals("")) {
                throw new Exception("customer id can't be empty");
            }
            // get customer by id to make sure customer exists
            List<CustomerEntity> existingCustomer =
                    m_customerDetailService.getCustomers(requestContext,
                            CustomerFilter.newBuilder()
                                    .setCustomerId(customerEntity.getCustomerId()).build(),
                            1,
                            0);
            if (existingCustomer.isEmpty()) {
                throw new Exception("Customer with id " + customerEntity.getCustomerId() + "  does not exist");
            }
            CustomerEntity editedCustomer = m_customerDetailService.upsertCustomer(requestContext, customerEntity);
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    editedCustomer
            ), HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createCustomer")
    public ResponseEntity<ResponseWrapper<CustomerEntity>> createCustomer(
            @RequestBody CustomerEntity customerEntity,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "createCustomer request received. \n" + customerEntity.toString());
        RequestContext.Builder requestContext = RequestContext.newBuilder();
        requestContext.setRequestId(requestId);

        try {
            if (customerEntity.getCustomerId().equals("")) {
                throw new Exception("customer id can't be empty");
            }
            // get customer by id to make sure customer does not exists
            List<CustomerEntity> existingCustomer =
                    m_customerDetailService.getCustomers(requestContext,
                            CustomerFilter.newBuilder()
                                    .setCustomerId(customerEntity.getCustomerId()).build(),
                            1,
                            0);
            if (!existingCustomer.isEmpty()) {
                throw new Exception("Customer with id" + customerEntity.getCustomerId() + " already exist");
            }
            CustomerEntity addedCustomer = m_customerDetailService.upsertCustomer(requestContext, customerEntity);
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    addedCustomer
            ), HttpStatus.OK);

        } catch (Exception exception) {
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
            HttpServletRequest httpRequest) {

        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "getAllCustomers request received");
        RequestContext.Builder requestContext = RequestContext.newBuilder();
        requestContext.setRequestId(requestId);

        try {
            CustomerEntity deletedCustomer = m_customerDetailService.deleteCustomerById(requestContext, customerId);
            if (deletedCustomer == null) {
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
        } catch (Exception e) {

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
