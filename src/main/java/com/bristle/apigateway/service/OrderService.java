package com.bristle.apigateway.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

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
