package com.bristle.apigateway.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "users")
public class UserEntity {

    // Table name
    public static final String TABLE_NAME = "users";

    // Column names, reusable from outside of class
    public static final String COLM_ID = "user_id";
    public static final String COLM_NAME = "name";

    @Id
    @Column(name = COLM_ID, nullable = false)
    String userId; // this is a unique UUID

    @Column(name = COLM_NAME, nullable = false)
    String name;

    public UserEntity() {

    }

    public UserEntity(String id, String name) {
        this.userId = id;
        this.name = name;
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
}
