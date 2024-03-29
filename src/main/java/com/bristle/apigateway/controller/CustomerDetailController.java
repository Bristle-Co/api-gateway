package com.bristle.apigateway.controller;

import com.bristle.apigateway.model.ResponseWrapper;
import com.bristle.apigateway.model.dto.customer_detail.CustomerDto;
import com.bristle.apigateway.service.customer_detail.CustomerDetailService;
import com.bristle.proto.common.RequestContext;

import com.bristle.proto.customer_detail.CustomerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import java.util.Optional;
import java.util.UUID;

// must have @CrossOrigin else browser can't receive any payload
@CrossOrigin
@RequestMapping(path = "api/v1/customer_detail")
@RestController
public class CustomerDetailController {

    Logger log = LoggerFactory.getLogger(CustomerDetailController.class);
    CustomerDetailService m_customerDetailService;

    public CustomerDetailController(CustomerDetailService m_customerDetailService) {
        this.m_customerDetailService = m_customerDetailService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<CustomerDto>>> getCustomers(
            @RequestParam(name = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "customerId", required = false) String customerId,
            @RequestParam(name = "matchingCustomerId", required = false) String matchingCustomerId,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "contactName", required = false) String contactName,
            @RequestParam(name = "contactNumber", required = false) String contactNumber,
            @RequestParam(name = "address", required = false) String address,
            HttpServletRequest httpRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "getAllCustomers request received. pageIndex: " + pageIndex
                + ", pageSize: " + pageSize + ", customerId: " + customerId + ", matchingCustomerId: "
                + matchingCustomerId + ", name: " + name
                + ", contactName: " + contactName + ", contactNumber: " + contactNumber
                + ", address: " + address);
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        // construct filter
        CustomerFilter filter = CustomerFilter.newBuilder()
                .setCustomerId(customerId == null ? "" : customerId)
                .setName(name == null ? "" : name)
                .setContactName(contactName == null ? "" : contactName)
                .setContactNumber(contactNumber == null ? "" : contactNumber)
                .setAddress(address == null ? "" : address)
                .setMatchingCustomerId(matchingCustomerId == null ? "" : matchingCustomerId)
                .build();

        try {
            List<CustomerDto> resultList = m_customerDetailService.getCustomers(requestContextBuilder,
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
            exception.printStackTrace();
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper<CustomerDto>> updateCustomer(
            @RequestBody CustomerDto customerDto,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "updateCustomer request received. \n" + customerDto.toString());
        RequestContext.Builder requestContext = RequestContext.newBuilder();
        requestContext.setRequestId(requestId);

        try {
            if (customerDto.getCustomerId().equals("")) {
                throw new Exception("customer id can't be empty");
            }
            // get customer by id to make sure customer exists
            Optional<CustomerDto> existingCustomer =
                    m_customerDetailService.getCustomerById(requestContext, customerDto.getCustomerId());
            if (!existingCustomer.isPresent()) {
                throw new Exception("Customer with id " + customerDto.getCustomerId() + "  does not exist");
            }
            CustomerDto editedCustomer = m_customerDetailService.upsertCustomer(requestContext, customerDto);
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    editedCustomer
            ), HttpStatus.OK);

        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<CustomerDto>> createCustomer(
            @RequestBody CustomerDto customerDto,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "createCustomer request received. \n" + customerDto.toString());
        RequestContext.Builder requestContext = RequestContext.newBuilder();
        requestContext.setRequestId(requestId);

        try {
            if (customerDto.getCustomerId().equals("")) {
                throw new Exception("customer id can't be empty");
            }
            // get customer by id to make sure customer does not exists
            // we're not getting customer by id and check if one already exist since
            Optional<CustomerDto> existingCustomer =
                    m_customerDetailService.getCustomerById(requestContext, customerDto.getCustomerId());
            if (existingCustomer.isPresent()) {
                throw new Exception("Customer with id" + customerDto.getCustomerId() + " already exist");
            }
            CustomerDto addedCustomer = m_customerDetailService.upsertCustomer(requestContext, customerDto);
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    addedCustomer
            ), HttpStatus.OK);

        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseWrapper<CustomerDto>> deleteCustomer(
            @RequestParam(name = "customerId", required = true) String customerId,
            HttpServletRequest httpRequest) {

        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "getAllCustomers request received");
        RequestContext.Builder requestContext = RequestContext.newBuilder();
        requestContext.setRequestId(requestId);

        try {
            CustomerDto deletedCustomer = m_customerDetailService.deleteCustomerById(requestContext, customerId);
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
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
