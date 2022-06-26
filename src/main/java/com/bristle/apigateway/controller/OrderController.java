package com.bristle.apigateway.controller;


import com.bristle.apigateway.model.ResponseWrapper;
import com.bristle.apigateway.model.customer_detail.CustomerEntity;
import com.bristle.apigateway.model.order.OrderEntity;
import com.bristle.apigateway.model.order.ProductEntryEntity;
import com.bristle.apigateway.service.OrderService;
import com.bristle.proto.common.RequestContext;
import org.hibernate.internal.CriteriaImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "api/v1/order")
@RestController
public class OrderController {

    OrderService m_orderService;

    Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService m_orderService) {
        this.m_orderService = m_orderService;
    }

    @PostMapping("/upsertOrder")
    public ResponseEntity<ResponseWrapper<OrderEntity>> upsertOrder(
            @RequestBody OrderEntity orderEntity,
            HttpServletRequest httpRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "upsertOrder request received. " + orderEntity.toString() + " ProductEntryList: " +
                orderEntity.getProductEntries().stream().map(ProductEntryEntity::toString));
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            OrderEntity upsertedOrder = m_orderService.upsertOrder(requestContextBuilder, orderEntity);
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    upsertedOrder
            ), HttpStatus.OK);

        } catch (Exception exception) {
            log.error("Request id: " + requestId + "upsertOrder failed. " + exception.getMessage());

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//
//    @GetMapping("/getOrdersByLimitAndOffset")
//    public ResponseEntity<ResponseWrapper<List<OrderEntity>>> getOrdersByLimitAndOffset(
//            @RequestParam(name = "limit", required = false) int limit,
//            @RequestParam(name = "offset", required = false) int offset,
//            HttpServletRequest request
//    ) {
//        // This order table is eventually going to be huge
//        // we be careful when fetching all rows without a limit
//        try {
//            return new ResponseEntity<>(new ResponseWrapper<>(
//                    request.getRequestURI(),
//                    HttpStatus.ACCEPTED.value(),
//                    HttpStatus.ACCEPTED.getReasonPhrase(),
//                    m_orderService.getOrdersByLimitAndOffset(limit, offset))
//                    , HttpStatus.ACCEPTED);
//
//        } catch (Exception e) {
//            LOG.error(e.getMessage());
//            return new ResponseEntity<>(new ResponseWrapper<>(
//                    request.getRequestURI(),
//                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                    e.getMessage()
//            ), HttpStatus.INTERNAL_SERVER_ERROR);
//
//        }
//    }
//
//    @PostMapping("/addOrder")
//    public ResponseEntity<ResponseWrapper<OrderEntity>> addCustomer(
//            @RequestParam(name = COLM_CUSTOMER_ORDER_ID, required = false)
//            String customerOderId,
//            @RequestParam(name = COLM_CUSTOMER_ID, required = false)
//            String customerId,
//            @RequestParam(name = COLM_DUE_DATE, required = false)
//            String dueDateString,
//            @RequestParam(name = COLM_NOTE, required = false)
//            String note,
//            @RequestParam(name = COLM_FINISHED_ISSUING_TICKETS_AT, required = false)
//            LocalDateTime finishedIssuingTicketsAt,
//            HttpServletRequest request
//    ) {
//        try {
//            // due date should be passed in with the format 2022-03-30
//            Date dueDate = new Date(new SimpleDateFormat("yyyy-dd-MM").parse(dueDateString).getTime());
//            OrderEntity order = new OrderEntity(customerOderId, customerId, dueDate, note, finishedIssuingTicketsAt);
//            m_orderService.addOrder(order);
//            return new ResponseEntity<>(new ResponseWrapper<>(
//                    request.getRequestURI(),
//                    HttpStatus.CREATED.value(),
//                    HttpStatus.CREATED.getReasonPhrase(),
//                    order
//            ), HttpStatus.CREATED);
//        } catch (ParseException parseException) {
//            // Catch ParseException separately cuz I want to add customer message
//            return new ResponseEntity<>(new ResponseWrapper<>(
//                            request.getRequestURI(),
//                            HttpStatus.BAD_REQUEST.value(),
//                            "Incorrect date format"
//                    ), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>(new ResponseWrapper<>(
//                    request.getRequestURI(),
//                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                    e.getMessage()
//            ), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
