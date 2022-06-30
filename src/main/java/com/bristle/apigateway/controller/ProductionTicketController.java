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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    ){
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "upsertProductionTicket request received. " + ticketEntity.toString());
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
}
