package com.bristle.apigateway.service;


import com.bristle.apigateway.converter.order.OrderEntityConverter;
import com.bristle.apigateway.model.order.OrderEntity;
import com.bristle.apigateway.model.order.ProductEntryEntity;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.customer_detail.UpsertCustomerRequest;
import com.bristle.proto.order.GetOrderRequest;
import com.bristle.proto.order.GetOrderResponse;
import com.bristle.proto.order.Order;
import com.bristle.proto.order.OrderFilter;
import com.bristle.proto.order.OrderServiceGrpc;
import com.bristle.proto.order.UpsertOrderRequest;
import com.bristle.proto.order.UpsertOrderResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    Logger log = LoggerFactory.getLogger(OrderService.class);
    @GrpcClient("order_grpc_service")
    OrderServiceGrpc.OrderServiceBlockingStub m_orderGrpcService;

    private final OrderEntityConverter m_orderConverter;

    OrderService(OrderEntityConverter orderConverter) {
        this.m_orderConverter = orderConverter;
    }

    public OrderEntity upsertOrder(RequestContext.Builder requestContext, OrderEntity orderEntity) throws Exception {
        // if the product entry is being inserted the first time
        // the id column will be null, thus give it an uuid, otherwise keep it
        List<ProductEntryEntity> productList = orderEntity.getProductEntries();
        for (ProductEntryEntity item : productList) {
            if (item.getProductEntryId() == null) {
                item.setProductEntryId(UUID.randomUUID().toString());
            }
        }

        // if this is the first time issuing this order
        // put current time as issued_at
        if (orderEntity.getIssuedAt() == null) {
            orderEntity.setIssuedAt(LocalDateTime.now());
        }

        UpsertOrderRequest request = UpsertOrderRequest.newBuilder()
                .setRequestContext(requestContext)
                .setOrder(m_orderConverter.entityToProto(orderEntity)).build();
        UpsertOrderResponse response = m_orderGrpcService.upsertOrder(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return m_orderConverter.protoToEntity(response.getOrder());
    }

    public List<OrderEntity> getOrders(RequestContext.Builder requestContext,
                                       Integer orderId,
                                       String customerOrderId,
                                       String customerId,
                                       Date dueDateFrom,
                                       Date dueDateTo,
                                       LocalDateTime issuedAfter) throws Exception {

        // put parameters into proto "OrderFilter" message
        // params are verified in controller layer, only need to do null check here
        OrderFilter.Builder filter = OrderFilter.newBuilder();

        if(orderId!= null){
            filter.setOrderId(orderId);
        }

        if(customerOrderId != null ){
            filter.setCustomerOrderId(customerOrderId);
        }

        if(customerId != null){
            filter.setCustomerId(customerId);
        }

        if(dueDateFrom!= null){
            filter.setDueDateFrom(dueDateFrom.getTime());
        }

        if(dueDateTo!=null){
            filter.setDueDateTo(dueDateTo.getTime());
        }

        if(issuedAfter != null){
            filter.setIssuedAfter(issuedAfter.toEpochSecond(ZoneOffset.UTC));
        }

        GetOrderRequest request = GetOrderRequest.newBuilder()
                .setRequestContext(requestContext)
                .setFilter(filter).build();
        GetOrderResponse response = m_orderGrpcService.getOrders(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return response.getOrderList().stream()
                .map(m_orderConverter::protoToEntity).collect(Collectors.toList());
    }
}
