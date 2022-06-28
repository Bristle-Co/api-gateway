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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @GetMapping("/getOrders")
    public ResponseEntity<ResponseWrapper<List<OrderEntity>>> getOrders(
            @RequestParam(name = "orderId", required = false) Integer orderId,
            @RequestParam(name = "customerOrderId", required = false) String customerOrderId,
            @RequestParam(name = "customerId", required = false) String customerId,
            @RequestParam(name = "dueDateFrom", required = false) String dueDateFrom,
            @RequestParam(name = "dueDateTo", required = false) String dueDateTo,
            @RequestParam(name = "issuedAfter", required = false) String issuedAfter,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "upsertOrder request received. ");
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            SimpleDateFormat yearMonthDate = new SimpleDateFormat("yyyy-MM-dd");

            // even tho the issued_after field is a timestamp, we don't need such precise search,
            // search by date for simplicity
            DateTimeFormatter yearMonthDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Date dateFrom = null;
            Date dateTo = null;
            LocalDateTime issuedAfterDateTime = null;

            // validate parameters
            if (dueDateFrom != null) {
                dateFrom = yearMonthDate.parse(dueDateFrom);
            }
            if (dueDateTo != null) {
                dateTo = yearMonthDate.parse(dueDateTo);
            }
            if (issuedAfter != null) {
                issuedAfterDateTime = LocalDate.parse(issuedAfter, yearMonthDateFormatter).atStartOfDay();
            }
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.ACCEPTED.value(),
                    "success",
                    m_orderService.getOrders(
                            requestContextBuilder,
                            orderId,
                            customerOrderId,
                            customerId,
                            dateFrom,
                            dateTo,
                            issuedAfterDateTime)
            ), HttpStatus.OK);

        } catch (ParseException exception) {
            log.error("Request id: " + requestId + "time parse failed. " + exception.getMessage());
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception exception) {
            log.error("Request id: " + requestId + "getOrders failed. " + exception.getMessage());
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @DeleteMapping("/deleteOrder")
    public ResponseEntity<ResponseWrapper<OrderEntity>> getOrders(
            @RequestParam(name = "orderId", required = true) Integer orderId
            ,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "deleteOrder request received. ");
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            if (orderId == null) throw new IllegalArgumentException("orderId can not be null");

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.ACCEPTED.value(),
                    "success",
                    m_orderService.deleteOrder(requestContextBuilder, orderId)
            ), HttpStatus.OK);

        } catch (IllegalArgumentException exception) {
            log.error("Request id: " + requestId + "deleteOrder request failed. " + exception.getMessage());
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "orderId is invalid. Received: " + orderId
            ), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception exception) {
            log.error("Request id: " + requestId + "getOrders failed. " + exception.getMessage());
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
