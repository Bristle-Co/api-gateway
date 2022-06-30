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
import java.time.LocalTime;
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
        log.info("Request id: " + requestId + "upsertOrder request received. \n" + orderEntity.toString() + " ProductEntryList: " +
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
            @RequestParam(name = "issuedAtFrom", required = false) String issuedAtFrom,
            @RequestParam(name = "issuedAtTo", required = false) String issuedAtTo,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "upsertOrder request received. " +
                "olderId: " + orderId + " customerOrderId: " + customerOrderId +
                " customerId: " + customerId + " dueDateFrom: " + dueDateFrom +
                " dueDateTo: " + dueDateTo + " issuedAtFrom: " + issuedAtFrom +
                " issuedAtTo: " + issuedAtTo);
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            SimpleDateFormat yearMonthDate = new SimpleDateFormat("yyyy-MM-dd");

            // even tho the issued_after field is a timestamp, we don't need such precise search,
            // search by date for simplicity
            DateTimeFormatter yearMonthDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Date dateFrom = null;
            Date dateTo = null;
            LocalDateTime issuedAtFromDateTime = null;
            LocalDateTime issuedAtToDateTime = null;

            // validate parameters
            if (dueDateFrom != null) {
                dateFrom = yearMonthDate.parse(dueDateFrom);
            }
            if (dueDateTo != null) {
                dateTo = yearMonthDate.parse(dueDateTo);
            }
            if (issuedAtFrom != null) {
                issuedAtFromDateTime = LocalDate.parse(issuedAtFrom, yearMonthDateFormatter).atStartOfDay();
            }
            if (issuedAtTo != null) {
                // make this go to the end of day
                issuedAtToDateTime = LocalDate.parse(issuedAtTo, yearMonthDateFormatter).atTime(LocalTime.MAX);
            }
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    m_orderService.getOrders(
                            requestContextBuilder,
                            orderId,
                            customerOrderId,
                            customerId,
                            dateFrom,
                            dateTo,
                            issuedAtFromDateTime,
                            issuedAtToDateTime)
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
            @RequestParam(name = "orderId", required = true) Integer orderId,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "deleteOrder request received. orderId: " + orderId);
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            if (orderId <= 0) throw new IllegalArgumentException("orderId must > 0");

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    m_orderService.deleteOrder(requestContextBuilder, orderId)
            ), HttpStatus.OK);

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
