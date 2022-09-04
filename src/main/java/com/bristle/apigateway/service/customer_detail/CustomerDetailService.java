package com.bristle.apigateway.service.customer_detail;

import com.bristle.apigateway.converter.customer_detail.CustomerDetailConverter;
import com.bristle.apigateway.model.dto.customer_detail.CustomerDto;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.common.ResponseContext;
import com.bristle.proto.customer_detail.CustomerDetailServiceGrpc;
import com.bristle.proto.customer_detail.CustomerFilter;
import com.bristle.proto.customer_detail.DeleteCustomerRequest;
import com.bristle.proto.customer_detail.DeleteCustomerResponse;
import com.bristle.proto.customer_detail.GetCustomersRequest;
import com.bristle.proto.customer_detail.GetCustomersResponse;
import com.bristle.proto.customer_detail.UpsertCustomerRequest;
import com.bristle.proto.customer_detail.UpsertCustomerResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerDetailService {

    Logger log = LoggerFactory.getLogger(CustomerDetailService.class);
    @GrpcClient("customer_detail_grpc_service")
    CustomerDetailServiceGrpc.CustomerDetailServiceBlockingStub m_customerDetailGrpcClient;

    CustomerDetailConverter m_customerConverter;

    public CustomerDetailService(CustomerDetailConverter customerConverter) {
        this.m_customerConverter = customerConverter;
    }

    public Optional<CustomerDto> getCustomerById(RequestContext.Builder requestContext,
                                                 String customerId) throws Exception {
        // grpc request to customer-detail
        GetCustomersRequest request = GetCustomersRequest.newBuilder()
                .setRequestContext(requestContext)
                .setFilter(CustomerFilter.newBuilder()
                        .setMatchingCustomerId(customerId)
                        .build())
                .setPageIndex(0)
                .setPageSize(1)
                .build();

        GetCustomersResponse response = m_customerDetailGrpcClient.getCustomers(request);
        log.info("Request id: " + request.getRequestContext().getRequestId()
                + "getCustomerById request sent through getAllCustomers grpc request to customer-detail-service sent");

        ResponseContext responseContext = response.getResponseContext();
        if (responseContext.hasError()) {
            throw new Exception(responseContext.getError().getErrorMessage());
        }

        return response.getCustomerList().isEmpty() ? Optional.empty() : Optional.ofNullable(m_customerConverter.protoToDto(response.getCustomerList().get(0)));
    }

    public List<CustomerDto> getCustomers(RequestContext.Builder requestContext,
                                          CustomerFilter filter,
                                          Integer pageSize,
                                          Integer pageIndex) throws Exception {
        // grpc request to customer-detail
        GetCustomersRequest request = GetCustomersRequest.newBuilder()
                .setRequestContext(requestContext)
                .setFilter(filter)
                .setPageIndex(pageIndex)
                .setPageSize(pageSize)
                .build();

        GetCustomersResponse response = m_customerDetailGrpcClient.getCustomers(request);
        log.info("Request id: " + request.getRequestContext().getRequestId()
                + "getAllCustomers grpc request to customer-detail-service sent");

        ResponseContext responseContext = response.getResponseContext();
        if (responseContext.hasError()) {
            throw new Exception(responseContext.getError().getErrorMessage());
        }
        return response.getCustomerList().stream().map(m_customerConverter::protoToDto).collect(Collectors.toList());
    }

    public CustomerDto upsertCustomer(RequestContext.Builder requestContext, CustomerDto customerDto) throws Exception {
        // grpc request to customer-detail
        UpsertCustomerRequest.Builder requestBuilder = UpsertCustomerRequest.newBuilder();
        requestBuilder.setRequestContext(requestContext);
        requestBuilder.setCustomer(m_customerConverter.dtoToProto(customerDto));

        UpsertCustomerResponse response = m_customerDetailGrpcClient.upsertCustomer(requestBuilder.build());
        log.info("Request id: " + requestBuilder.getRequestContext().getRequestId()
                + "upsert grpc request to customer-detail-service sent. " + customerDto.toString());
        ResponseContext responseContext = response.getResponseContext();
        if (responseContext.hasError()) {
            throw new Exception(responseContext.getError().getErrorMessage());
        }
        return m_customerConverter.protoToDto(response.getCustomer());
    }

    public CustomerDto deleteCustomerById(RequestContext.Builder requestContext, String customerId) throws Exception {
        DeleteCustomerRequest request = DeleteCustomerRequest.newBuilder()
                .setRequestContext(requestContext)
                .setCustomerId(customerId).build();
        DeleteCustomerResponse response = m_customerDetailGrpcClient.deleteCustomer(request);

        ResponseContext responseContext = response.getResponseContext();

        // Make sure to check if there is error first
        // cuz when there is error there is no customer either
        if (responseContext.hasError()) {
            throw new Exception(responseContext.getError().getErrorMessage());
        }

        if (!response.hasDeletedCustomer()) {
            // only reach here when the customerId provided does not map to any customer in db
            return null;
        }
        return m_customerConverter.protoToDto(response.getDeletedCustomer());
    }
}
