package com.bristle.apigateway.service;


import com.bristle.apigateway.converter.order.OrderEntityConverter;
import com.bristle.apigateway.converter.order.ProductEntryEntityConverter;
import com.bristle.apigateway.model.order.OrderEntity;
import com.bristle.apigateway.model.order.ProductEntryEntity;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.customer_detail.UpsertCustomerRequest;
import com.bristle.proto.order.DeleteOrderRequest;
import com.bristle.proto.order.DeleteOrderResponse;
import com.bristle.proto.order.GetOrdersRequest;
import com.bristle.proto.order.GetOrdersResponse;
import com.bristle.proto.order.GetUnAssignedProductEntriesRequest;
import com.bristle.proto.order.GetUnAssignedProductEntriesResponse;
import com.bristle.proto.order.Order;
import com.bristle.proto.order.OrderFilter;
import com.bristle.proto.order.OrderServiceGrpc;
import com.bristle.proto.order.UpsertOrderRequest;
import com.bristle.proto.order.UpsertOrderResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    private final ProductEntryEntityConverter m_productEntryConverter;

    OrderService(OrderEntityConverter orderConverter, ProductEntryEntityConverter productEntryConverter)
    {
        this.m_orderConverter = orderConverter;
        this.m_productEntryConverter = productEntryConverter;
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
                                       Integer pageIndex,
                                       Integer pageSize,
                                       OrderFilter filter
                                       ) throws Exception {


        GetOrdersRequest request = GetOrdersRequest.newBuilder()
                .setRequestContext(requestContext)
                .setFilter(filter)
                .setPageIndex(pageIndex == null ? 0 : pageIndex)
                .setPageSize(pageSize == null? 20 : pageSize).build();
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

//    public List<ProductEntryEntity> getUnAssignedProductEntries(RequestContext.Builder requestContext) throws Exception{
//        GetUnAssignedProductEntriesRequest request =
//                GetUnAssignedProductEntriesRequest.newBuilder()
//                        .setRequestContext(requestContext)
//                        .build();
//        GetUnAssignedProductEntriesResponse response
//                = m_orderGrpcService.getUnAssignedProductEntries(request);
//
//        if (response.getResponseContext().hasError()) {
//            throw new Exception(response.getResponseContext().getError().getErrorMessage());
//        }
//
//        return response.getProductEntryList().stream().map(m_productEntryConverter::protoToEntity).collect(Collectors.toList());
//    }
}
