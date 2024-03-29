package com.bristle.apigateway.service.order;


import com.bristle.apigateway.converter.order.OrderConverter;
import com.bristle.apigateway.converter.order.ProductEntryConverter;
import com.bristle.apigateway.model.dto.order.OrderDto;
import com.bristle.apigateway.model.dto.order.ProductEntryDto;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.order.DeleteOrderRequest;
import com.bristle.proto.order.DeleteOrderResponse;
import com.bristle.proto.order.GetOrdersRequest;
import com.bristle.proto.order.GetOrdersResponse;
import com.bristle.proto.order.GetProductEntriesRequest;
import com.bristle.proto.order.GetProductEntriesResponse;
import com.bristle.proto.order.OrderFilter;
import com.bristle.proto.order.OrderServiceGrpc;
import com.bristle.proto.order.PatchProductionTicketInfoRequest;
import com.bristle.proto.order.PatchProductionTicketInfoResponse;
import com.bristle.proto.order.ProductEntryFilter;
import com.bristle.proto.order.ProductEntryServiceGrpc;
import com.bristle.proto.order.UpsertOrderRequest;
import com.bristle.proto.order.UpsertOrderResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    Logger log = LoggerFactory.getLogger(OrderService.class);
    @GrpcClient("order_grpc_service")
    OrderServiceGrpc.OrderServiceBlockingStub m_orderGrpcService;

    // use same config in application.properties because it's
    @GrpcClient("order_grpc_service")
    ProductEntryServiceGrpc.ProductEntryServiceBlockingStub m_productEntryService;

    private final OrderConverter m_orderConverter;

    private final ProductEntryConverter m_productEntryConverter;

    OrderService(OrderConverter orderConverter, ProductEntryConverter productEntryConverter) {
        this.m_orderConverter = orderConverter;
        this.m_productEntryConverter = productEntryConverter;
    }

    public OrderDto upsertOrder(RequestContext.Builder requestContext, OrderDto orderDto) throws Exception {
        // if the product entry is being inserted the first time
        // the id column will be null, thus give it an uuid, otherwise keep it
        List<ProductEntryDto> productList = orderDto.getProductEntries();
        for (ProductEntryDto item : productList) {
            if (item.getProductEntryId() == null) {
                item.setProductEntryId(UUID.randomUUID().toString());
            }
        }

        // if this is the first time issuing this order
        // put current time as issued_at
        if (orderDto.getIssuedAt() == null) {
            orderDto.setIssuedAt(LocalDateTime.now());
        }

        UpsertOrderRequest request = UpsertOrderRequest.newBuilder()
                .setRequestContext(requestContext)
                .setOrder(m_orderConverter.dtoToProto(orderDto)).build();
        UpsertOrderResponse response = m_orderGrpcService.upsertOrder(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return m_orderConverter.protoToDto(response.getOrder());
    }

    public List<OrderDto> getOrders(RequestContext.Builder requestContext,
                                    Integer pageIndex,
                                    Integer pageSize,
                                    OrderFilter filter
    ) throws Exception {


        GetOrdersRequest request = GetOrdersRequest.newBuilder()
                .setRequestContext(requestContext)
                .setFilter(filter)
                .setPageIndex(pageIndex == null ? 0 : pageIndex)
                .setPageSize(pageSize == null ? 20 : pageSize).build();
        GetOrdersResponse response = m_orderGrpcService.getOrders(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return response.getOrderList().stream()
                .map(m_orderConverter::protoToDto).collect(Collectors.toList());
    }

    public OrderDto deleteOrder(RequestContext.Builder requestContext, Integer orderId) throws Exception {
        DeleteOrderRequest request = DeleteOrderRequest.newBuilder()
                .setRequestContext(requestContext)
                .setOrderId(orderId).build();
        DeleteOrderResponse response = m_orderGrpcService.deleteOrder(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return m_orderConverter.protoToDto(response.getDeletedOrder());
    }

    // I didn't separate these product entry endpoints into separate service
    // because they are related to orders and should be a part of order operations
    public List<ProductEntryDto> getProductEntries(RequestContext.Builder requestContext,
                                                   ProductEntryFilter filter) throws Exception {
        GetProductEntriesRequest request =
                GetProductEntriesRequest.newBuilder()
                        .setRequestContext(requestContext)
                        .setFilter(filter)
                        .build();
        GetProductEntriesResponse response
                = m_productEntryService.getProductEntries(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return response.getProductEntryList().stream().map(m_productEntryConverter::protoToDto).collect(Collectors.toList());
    }


    public ProductEntryDto patchProductionTicketInfo(RequestContext.Builder requestContext,
                                                     String productEntryId,
                                                     Integer productTicketId,
                                                     boolean resetToNull) throws Exception {
        PatchProductionTicketInfoRequest.Builder request =
                PatchProductionTicketInfoRequest.newBuilder()
                        .setRequestContext(requestContext)
                        .setProductEntryId(productEntryId);
        if (resetToNull) {
            request.setIsResetToNull(true);
        } else {
            request.setProductionTicketId(productTicketId);
            request.setIsResetToNull(false);
        }

        PatchProductionTicketInfoResponse response
                = m_productEntryService.patchProductionTicketInfo(request.build());

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return m_productEntryConverter.protoToDto(response.getProductEntry());
    }
}
