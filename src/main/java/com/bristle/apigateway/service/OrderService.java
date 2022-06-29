package com.bristle.apigateway.service;


import com.bristle.apigateway.converter.order.OrderEntityConverter;
import com.bristle.apigateway.model.order.OrderEntity;
import com.bristle.apigateway.model.order.ProductEntryEntity;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.customer_detail.UpsertCustomerRequest;
import com.bristle.proto.order.DeleteOrderRequest;
import com.bristle.proto.order.DeleteOrderResponse;
import com.bristle.proto.order.GetOrdersRequest;
import com.bristle.proto.order.GetOrdersResponse;
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
                                       LocalDateTime issuedAtFrom,
                                       LocalDateTime issuedAtTo
                                       ) throws Exception {

        // put parameters into proto "OrderFilter" message
        // params are verified in controller layer, only need to do null check here
        OrderFilter.Builder filter = OrderFilter.newBuilder();

        filter.setOrderId(orderId==null ? Integer.MIN_VALUE : orderId);
        filter.setCustomerOrderId(customerOrderId==null ? "" :customerOrderId);
        filter.setCustomerId(customerId== null?"":customerId);
        filter.setDueDateFrom(dueDateFrom == null? Long.MIN_VALUE : dueDateFrom.getTime());
        filter.setDueDateTo(dueDateFrom == null? Long.MIN_VALUE : dueDateTo.getTime());
        filter.setIssuedAtFrom(issuedAtFrom == null?Long.MIN_VALUE : issuedAtFrom.toEpochSecond(ZoneOffset.UTC));
        filter.setIssuedAtTo(issuedAtTo == null?Long.MIN_VALUE : issuedAtTo.toEpochSecond(ZoneOffset.UTC));

        GetOrdersRequest request = GetOrdersRequest.newBuilder()
                .setRequestContext(requestContext)
                .setFilter(filter).build();
        GetOrdersResponse response = m_orderGrpcService.getOrders(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return response.getOrderList().stream()
                .map(m_orderConverter::protoToEntity).collect(Collectors.toList());
    }

    public OrderEntity deleteOrder(RequestContext.Builder requestContext, Integer orderId) throws Exception{
        DeleteOrderRequest request = DeleteOrderRequest.newBuilder()
                .setRequestContext(requestContext)
                .setOrderId(orderId).build();
        DeleteOrderResponse response = m_orderGrpcService.deleteOrder(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return m_orderConverter.protoToEntity(response.getDeletedOrder());
    }
}
