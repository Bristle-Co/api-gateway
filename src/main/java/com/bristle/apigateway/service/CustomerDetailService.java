package com.bristle.apigateway.service;

import com.bristle.apigateway.model.CustomerEntity;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.customer_detail.CustomerDetailServiceGrpc;
import com.bristle.proto.customer_detail.GetAllCustomersRequest;
import com.bristle.proto.customer_detail.GetAllCustomersResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerDetailService {

    @GrpcClient("customer_detail_grpc_service")
    CustomerDetailServiceGrpc.CustomerDetailServiceBlockingStub m_customerDetailGrpcClient;

    @Transactional(readOnly = true)
    public GetAllCustomersResponse getAllCustomers(){
        // grpc request to customer-detail
        String requestId = UUID.randomUUID().toString();
        RequestContext requestContext = RequestContext.newBuilder().setRequestId(requestId).build();
        GetAllCustomersRequest request = GetAllCustomersRequest.newBuilder().setRequestContext(requestContext).build();
        return m_customerDetailGrpcClient.getAllCustomers(request);
    }

    @Transactional
    public void addCustomer(CustomerEntity customerEntity) throws Exception {
        // grpc request to customer-detail
    }
}
