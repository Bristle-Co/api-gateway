package com.bristle.apigateway.model;

public class ResponseWrapper<T> {
    // This is implemented because I want the api response to have same structure
    // regardless of successful request or not
    // also reserved for future in case any information should be added to the response json structure

    String path;

    String requestId;
    int status;
    String message;
    T data;

    public ResponseWrapper() {
    }

    public ResponseWrapper(String path, String requestId, int status, String message) {
        this.path = path;
        this.requestId = requestId;
        this.status = status;
        this.message = message;
    }

    public ResponseWrapper(String path, String requestId, int status, String message, T data) {
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
}
