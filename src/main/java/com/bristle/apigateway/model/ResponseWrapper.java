package com.bristle.apigateway.model;

import java.time.LocalDateTime;

public class ResponseWrapper<T> {
    // This is implemented because I want the api response to have same structure
    // regardless of successful request or not
    // also reserved for future in case any information should be added to the response json structure

    LocalDateTime timeStamp;
    String path;
    String requestId;
    int status;
    String message;
    T data;




    public ResponseWrapper() {
    }

    public ResponseWrapper(LocalDateTime timeStamp, String path, String requestId, int status, String message) {
        this.timeStamp = timeStamp;
        this.path = path;
        this.requestId = requestId;
        this.status = status;
        this.message = message;
    }

    public ResponseWrapper(LocalDateTime timeStamp, String path, String requestId, int status, String message, T data) {
        this.timeStamp = timeStamp;
        this.path = path;
        this.requestId = requestId;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
