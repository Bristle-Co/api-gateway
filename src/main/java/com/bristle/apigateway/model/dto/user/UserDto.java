package com.bristle.apigateway.model.dto.user;

public class UserDto {

    String userId;

    String name;

    public UserDto(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public UserDto() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "user{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
