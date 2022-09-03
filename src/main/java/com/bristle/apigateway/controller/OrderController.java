package com.bristle.apigateway.controller;


import com.bristle.apigateway.model.ResponseWrapper;
import com.bristle.apigateway.model.dto.order.OrderDto;
import com.bristle.apigateway.model.dto.order.ProductEntryDto;
import com.bristle.apigateway.service.OrderService;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.order.OrderFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RequestMapping(path = "api/v1/order")
@RestController
public class OrderController {

    OrderService m_orderService;

    Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService m_orderService) {
        this.m_orderService = m_orderService;
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<OrderDto>> createOrder(
            @RequestBody OrderDto orderDto,
            HttpServletRequest httpRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "createOrder request received. \n" + orderDto.toString() + " ProductEntryList: " +
                orderDto.getProductEntries().stream().map(ProductEntryDto::toString));
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            validateToBeCreatedOrder(orderDto);

            OrderDto upsertedOrder = m_orderService.upsertOrder(requestContextBuilder, orderDto);
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
            exception.printStackTrace();

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper<OrderDto>> updateOrder(
            @RequestBody OrderDto orderDto,
            HttpServletRequest httpRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "upsertOrder request received. \n" + orderDto.toString() + " ProductEntryList: " +
                orderDto.getProductEntries().stream().map(ProductEntryDto::toString));
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            validateToBeUpdatedOrder(orderDto);

            // check that order exists
            List<OrderDto> existingOrderList
                    = m_orderService.getOrders(requestContextBuilder,
                    0,
                    1,
                    OrderFilter.newBuilder()
                            .setOrderId(orderDto.getOrderId())
                            .build()
            );
            if (existingOrderList.isEmpty()) {
                throw new Exception("Order with id " + orderDto.getOrderId() + "  does not exist");
            }

            OrderDto upsertedOrder = m_orderService.upsertOrder(requestContextBuilder, orderDto);
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
            exception.printStackTrace();

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<OrderDto>>> getOrders(
            @RequestParam(name = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
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
                "pageIndex: " + pageIndex + " pageSize: " + pageSize +
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
            if (StringUtils.hasText(dueDateFrom)) {
                dateFrom = yearMonthDate.parse(dueDateFrom);
            }
            if (StringUtils.hasText(dueDateTo)) {
                dateTo = yearMonthDate.parse(dueDateTo);
            }
            if (StringUtils.hasText(issuedAtFrom)) {
                issuedAtFromDateTime = LocalDate.parse(issuedAtFrom, yearMonthDateFormatter).atStartOfDay();
            }
            if (StringUtils.hasText(issuedAtTo)) {
                // make this go to the end of day
                issuedAtToDateTime = LocalDate.parse(issuedAtTo, yearMonthDateFormatter).atTime(LocalTime.MAX);
            }

            // construct filter
            // put parameters into proto "OrderFilter" message
            // params are verified in controller layer, only need to do null check here
            OrderFilter.Builder filter = OrderFilter.newBuilder();

            filter.setOrderId(orderId == null ? Integer.MIN_VALUE : orderId);
            filter.setCustomerOrderId(customerOrderId == null ? "" : customerOrderId);
            filter.setCustomerId(customerId == null ? "" : customerId);
            filter.setDueDateFrom(dueDateFrom == null ? Long.MIN_VALUE : dateFrom.getTime());
            filter.setDueDateTo(dueDateFrom == null ? Long.MIN_VALUE : dateTo.getTime());
            filter.setIssuedAtFrom(issuedAtFrom == null ? Long.MIN_VALUE : issuedAtFromDateTime.toEpochSecond(ZoneOffset.UTC));
            filter.setIssuedAtTo(issuedAtTo == null ? Long.MIN_VALUE : issuedAtToDateTime.toEpochSecond(ZoneOffset.UTC));

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    m_orderService.getOrders(
                            requestContextBuilder,
                            pageIndex,
                            pageSize,
                            filter.build())
            ), HttpStatus.OK);

        } catch (ParseException exception) {
            log.error("Request id: " + requestId + "time parse failed. " + exception.getMessage());
            exception.printStackTrace();

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception exception) {
            log.error("Request id: " + requestId + "getOrders failed. " + exception.getMessage());
            exception.printStackTrace();

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseWrapper<OrderDto>> deleteOrder(
            @RequestParam(name = "orderId", required = true) Integer orderId,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "deleteOrder request received. orderId: " + orderId);
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            if (orderId <= 0) throw new IllegalArgumentException("orderId must > 0");

            // check that order exists
            List<OrderDto> existingOrderList
                    = m_orderService.getOrders(requestContextBuilder,
                    0,
                    1,
                    OrderFilter.newBuilder()
                            .setOrderId(orderId)
                            .build()
            );
            if (existingOrderList.isEmpty()) {
                throw new Exception("Order with id " + orderId + " does not exist");
            }

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
            exception.printStackTrace();

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/unassigned_product_entry")
    public ResponseEntity<ResponseWrapper<List<ProductEntryDto>>> getUnAssignedProductEntries(
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "getUnAssignedProductEntries request received.");
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    m_orderService.getUnAssignedProductEntries(requestContextBuilder)
            ), HttpStatus.OK);

        } catch (Exception exception) {
            log.error("Request id: " + requestId + "getUnAssignedProductEntries failed. " + exception.getMessage());
            exception.printStackTrace();

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    private void validateToBeCreatedOrder(OrderDto dto) throws Exception {
        if (dto.getOrderId() != null) {
            // orderId is assigned by db
            throw new Exception("orderId must be null when creating order");
        }

        if (dto.getIssuedAt() != null) {
            // a newly created order must let server log current timestamp
            throw new Exception("issuedAt must be null when creating order");
        }

        List<ProductEntryDto> productEntryEntities = dto.getProductEntries();
        for (int i = 0; i < productEntryEntities.size(); i++) {
            if (productEntryEntities.get(i).getOrderId() != null) {
                throw new Exception("orderId in product entry must be null when creating order, index " + i);
            }
        }
    }

    private void validateToBeUpdatedOrder(OrderDto dto) throws Exception {
        if (dto.getOrderId() == null) {
            throw new Exception("orderId must NOT be null");
        }

        List<ProductEntryDto> productEntryDtos = dto.getProductEntries();
        for (int i = 0; i < productEntryDtos.size(); i++) {
            if (dto.getOrderId().equals(productEntryDtos.get(i).getOrderId())) {
                throw new Exception("orderId in ProductEntry must match parent order's id, fail at ProductEntry index " + i);
            }
        }
    }
}
