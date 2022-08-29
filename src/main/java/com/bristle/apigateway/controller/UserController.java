package com.bristle.apigateway.controller;

import com.bristle.apigateway.model.ResponseWrapper;
import com.bristle.apigateway.model.production_ticket.ProductionTicketEntity;
import com.bristle.apigateway.model.user.UserEntity;
import com.bristle.apigateway.service.ProductionTicketService;
import com.bristle.apigateway.service.UserService;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.production_ticket.ProductionTicketFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RequestMapping(path = "api/v1/user")
@RestController
public class UserController {

    UserService m_userService;

    Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService m_userService) {
        this.m_userService = m_userService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<UserEntity>>> getUsers(
            HttpServletRequest httpRequest
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Request id: " + requestId + "getUser request received.");
        RequestContext.Builder requestContextBuilder = RequestContext.newBuilder().setRequestId(requestId);

        try {
            return new ResponseEntity<>(new ResponseWrapper<>(
                    LocalDateTime.now(),
                    httpRequest.getRequestURI(),
                    requestId,
                    HttpStatus.OK.value(),
                    "success",
                    m_userService.getUsers(requestContextBuilder)
            ), HttpStatus.OK);

        } catch (Exception exception) {
            log.error("Request id: " + requestId + "getUsers failed. " + exception.getMessage());
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