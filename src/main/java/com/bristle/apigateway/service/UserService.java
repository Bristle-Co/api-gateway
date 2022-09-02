package com.bristle.apigateway.service;

import com.bristle.apigateway.converter.user.UserEntityConverter;
import com.bristle.apigateway.model.dto.user.UserDto;
import com.bristle.proto.common.RequestContext;
import com.bristle.proto.user.GetUsersRequest;
import com.bristle.proto.user.GetUsersResponse;
import com.bristle.proto.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    Logger log = LoggerFactory.getLogger(UserService.class);

    @GrpcClient("user_grpc_service")
    UserServiceGrpc.UserServiceBlockingStub m_userGrpcService;

    private final UserEntityConverter m_converter;

    public UserService(UserEntityConverter m_converter) {
        this.m_converter = m_converter;
    }

    public List<UserDto> getUsers(RequestContext.Builder requestContext) throws Exception{
        GetUsersRequest request = GetUsersRequest.newBuilder()
                .setRequestContext(requestContext)
                .build();
        GetUsersResponse response = m_userGrpcService.getUsers(request);

        if (response.getResponseContext().hasError()) {
            throw new Exception(response.getResponseContext().getError().getErrorMessage());
        }

        return response.getUserList().stream()
                .map(m_converter::protoToDto).collect(Collectors.toList());
    }
}
