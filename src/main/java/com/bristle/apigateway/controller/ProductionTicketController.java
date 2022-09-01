package com.bristle.apigateway.controller;

import com.bristle.apigateway.model.ResponseWrapper;
import com.bristle.apigateway.model.production_ticket.ProductionTicketEntity;
import com.bristle.apigateway.service.ProductionTicketService;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.production_ticket.ProductionTicket;
import com.bristle.proto.production_ticket.ProductionTicketFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RequestMapping(path = "api/v1/production_ticket")
@RestController
public class ProductionTicketController {

    ProductionTicketService m_productionTicketService;

    Logger log = LoggerFactory.getLogger(ProductionTicketController.class);

    public ProductionTicketController(ProductionTicketService productionTicketService) {
        this.m_productionTicketService = productionTicketService;
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper<ProductionTicketEntity>> updateProductionTicket(
            @RequestBody ProductionTicketEntity ticketEntity,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "upsertProductionTicket request received. \n" + ticketEntity.toString());
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            if (ticketEntity.getTicketId() == null) {
                throw new Exception("ticketId must NOT be null");
            }

            validateInComingProductTicket(ticketEntity);

            // check that order exists
            List<ProductionTicketEntity> existingProductTicket
                    = m_productionTicketService.getProductionTicket(requestContextBuilder,
                    0,
                    1,
                    ProductionTicketFilter.newBuilder()
                            .setTicketId(ticketEntity.getTicketId())
                            .build()
            );
            if (existingProductTicket.isEmpty()) {
                throw new Exception("Ticket with id " + ticketEntity.getTicketId() + "  does not exist");
            }

            ProductionTicketEntity upsertedTicket = m_productionTicketService.upsertProductionTicket(requestContextBuilder, ticketEntity);
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    upsertedTicket
            ), HttpStatus.OK);

        } catch (Exception exception) {
            log.error("Request id: " + requestId + "upsertProductionTicket failed. " + exception.getMessage());

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<ProductionTicketEntity>> createProductionTicket(
            @RequestBody ProductionTicketEntity ticketEntity,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "upsertProductionTicket request received. \n" + ticketEntity.toString());
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            if (ticketEntity.getTicketId() != null) {
                throw new Exception("ticketId must be null");
            }
            validateInComingProductTicket(ticketEntity);

            ProductionTicketEntity upsertedTicket = m_productionTicketService.upsertProductionTicket(requestContextBuilder, ticketEntity);
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    upsertedTicket
            ), HttpStatus.OK);

        } catch (Exception exception) {
            log.error("Request id: " + requestId + "upsertProductionTicket failed. " + exception.getMessage());

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
    public ResponseEntity<ResponseWrapper<ProductionTicketEntity>> deleteProductionTicket(
            @RequestParam(name = "ticketId", required = true) Integer ticketId,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "deleteProductionTicket request received. Id: " + ticketId);
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            if (ticketId <= 0) throw new IllegalArgumentException("ticketId must > 0");

            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    m_productionTicketService.deleteProductionTicket(requestContextBuilder, ticketId)
            ), HttpStatus.OK);

        } catch (Exception exception) {
            log.error("Request id: " + requestId + "deleteProdctionTicket failed. " + exception.getMessage());
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
    public ResponseEntity<ResponseWrapper<List<ProductionTicketEntity>>> getProductionTickets(
            @RequestParam(name = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "ticketId", required = false) Integer ticketId,
            @RequestParam(name = "customerId", required = false) String customerId,
            @RequestParam(name = "bristleType", required = false) String bristleType,
            @RequestParam(name = "model", required = false) String model,
            @RequestParam(name = "productName", required = false) String productName,
            @RequestParam(name = "dueDateFrom", required = false) String dueDateFrom,
            @RequestParam(name = "dueDateTo", required = false) String dueDateTo,
            @RequestParam(name = "issuedAtFrom", required = false) String issuedAtFrom,
            @RequestParam(name = "issuedAtTo", required = false) String issuedAtTo,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "upsertOrder request received. " +
                "pageIndex" + pageIndex + "pageSize" + pageSize +
                "ticketId: " + ticketId + " customerId: " + customerId +
                " bristleType: " + bristleType + " model: " + model +
                " productName: " + productName + " dueDateFrom: " + dueDateFrom +
                " dueDateTo: " + dueDateTo + " issuedAtFrom: " + issuedAtFrom + " issuedAtTo: " + issuedAtTo);
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            // parse time stamp here to validate in format of input
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

            // Construct filter
            ProductionTicketFilter.Builder filter = ProductionTicketFilter.newBuilder();

            filter.setTicketId(ticketId == null ? Integer.MIN_VALUE : ticketId);
            filter.setCustomerId(customerId == null ? "" : customerId);
            filter.setBristleType(bristleType == null ? "" : bristleType);
            filter.setModel(model == null ? "" : model);
            filter.setProductName(productName == null ? "" : productName);
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
                    m_productionTicketService.getProductionTicket(
                            requestContextBuilder,
                            pageIndex == null ? 0 : pageIndex,
                            pageSize == null ? 20 : pageSize,
                            filter.build())
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

    public void validateInComingProductTicket(ProductionTicketEntity entity) throws Exception{
        // don't validate ticketId here since this function is reused for update and create endpoint
        if (entity.getOrderId() == null) {
            throw new Exception("orderId must NOT be null");
        }
        if (entity.getProductEntryId() == null) {
            throw new Exception("productEntryId must NOT be null");
        }
    }
}
