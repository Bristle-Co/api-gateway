package com.bristle.apigateway.converter.user;

import com.bristle.apigateway.model.dto.user.UserDto;
import com.bristle.proto.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserEntityConverter {

    public User dtoToProto(UserDto dto) {
        return User.newBuilder()
                .setId(dto.getUserId())
                .setName(dto.getName())
                .build();
    }

    public UserDto protoToDto(User proto) {
        return new UserDto(proto.getId(), proto.getName());
    }
}
