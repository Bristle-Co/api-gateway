package com.bristle.apigateway.service;



import com.bristle.apigateway.converter.order.OrderEntityConverter;
import com.bristle.apigateway.model.order.OrderEntity;
import com.bristle.apigateway.model.order.ProductEntryEntity;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.customer_detail.UpsertCustomerRequest;
import com.bristle.proto.order.OrderServiceGrpc;
import com.bristle.proto.order.UpsertOrderRequest;
import com.bristle.proto.order.UpsertOrderResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    Logger log = LoggerFactory.getLogger(OrderService.class);
    @GrpcClient("order_grpc_service")
    OrderServiceGrpc.OrderServiceBlockingStub m_orderGrpcService;

    private final OrderEntityConverter m_orderConverter;

    OrderService(OrderEntityConverter orderConverter){
        this.m_orderConverter = orderConverter;
    }

    public OrderEntity upsertOrder(RequestContext.Builder requestContext, OrderEntity orderEntity) throws Exception {
        // if the product entry is being inserted the first time
        // the id column will be null, thus give it an uuid, otherwise keep it
        List<ProductEntryEntity> productList = orderEntity.getProductEntries();
        for(ProductEntryEntity item: productList){
            if(item.getProductEntryId() == null){
                item.setProductEntryId(UUID.randomUUID().toString());
            }
        }

        UpsertOrderRequest request = UpsertOrderRequest.newBuilder()
                        .setRequestContext(requestContext)
                .setOrder(m_orderConverter.entityToProto(orderEntity)).build();
        UpsertOrderResponse response = m_orderGrpcService.upsertOrder(request);

        if(response.getResponseContext().hasError()){
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return m_orderConverter.protoToEntity(response.getOrder());
    }

//    private final OrderRepository m_orderRepository;
//    Logger LOG = LoggerFactory.getLogger(OrderService.class);
//
//    @Autowired
//    public OrderService(OrderRepository m_orderRepository) {
//        this.m_orderRepository = m_orderRepository;
//    }
//
//    @Transactional
//    public void addOrder(OrderEntity orderEntity) throws Exception {
//        m_orderRepository.save(orderEntity);
//
//    }
//
//    @Transactional(readOnly = true)
//    public List<OrderEntity> getOrdersByLimitAndOffset(int limit, int offset) throws Exception {
//        // Note that the orders are put into descending order first
//        // and then offset is applied
//
//        // LIMIT 0 simply returns an empty list, absolutely useless
//        // thus we make default limit 20
//        // 20 is about the max number of orders that mom will be tracking at once
//        limit = limit <= 0 ? 20 : limit;
//        return m_orderRepository.getOrdersByLimitAndOffset(limit, offset);
//    }
}
