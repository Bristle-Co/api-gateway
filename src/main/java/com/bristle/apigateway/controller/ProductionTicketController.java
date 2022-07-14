package com.bristle.apigateway.controller;

import com.bristle.apigateway.model.ResponseWrapper;
import com.bristle.apigateway.model.order.OrderEntity;
import com.bristle.apigateway.model.order.ProductEntryEntity;
import com.bristle.apigateway.model.production_ticket.ProductionTicketEntity;
import com.bristle.apigateway.service.OrderService;
import com.bristle.apigateway.service.ProductionTicketService;
import com.bristle.proto.common.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/upsertProductionTicket")
    public ResponseEntity<ResponseWrapper<ProductionTicketEntity>> upsertProductionTicket(
            @RequestBody ProductionTicketEntity ticketEntity,
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "upsertProductionTicket request received. \n" + ticketEntity.toString());
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
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

    @DeleteMapping("/deleteProductionTicket")
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

    @GetMapping("/getProductionTickets")
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
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    m_productionTicketService.getProductionTicket(
                            requestContextBuilder,
                            pageIndex,
                            pageSize,
                            ticketId,
                            customerId,
                            bristleType,
                            model,
                            productName,
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

}
