package com.bristle.apigateway.model.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class ProductEntryDto {

    private String productEntryId;

    private String model;

    private Integer quantity;

    private Integer price;

    private String productTicketId;

    private Integer orderId;

    @JsonInclude(Include.NON_NULL)
    private String customerId;

    @Override
    public String toString() {
        return "ProductEntryDto{" +
                "productEntryId='" + productEntryId + '\'' +
                ", model='" + model + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", productTicketId='" + productTicketId + '\'' +
                ", orderId=" + orderId +
                ", customerId='" + customerId + '\'' +
                '}';
    }

    public String getProductEntryId() {
        return productEntryId;
    }

    public void setProductEntryId(String productEntryId) {
        this.productEntryId = productEntryId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getProductTicketId() {
        return productTicketId;
    }

    public void setProductTicketId(String productTicketId) {
        this.productTicketId = productTicketId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ProductEntryDto() {
    }

    public ProductEntryDto(String productEntryId, String model, Integer quantity, Integer price, String productTicketId, Integer orderId) {
        this.productEntryId = productEntryId;
        this.model = model;
        this.quantity = quantity;
        this.price = price;
        this.productTicketId = productTicketId;
        this.orderId = orderId;
    }

    public ProductEntryDto(String productEntryId, String model, Integer quantity, Integer price, String productTicketId, Integer orderId, String customerId) {
        this.productEntryId = productEntryId;
        this.model = model;
        this.quantity = quantity;
        this.price = price;
        this.productTicketId = productTicketId;
        this.orderId = orderId;
        this.customerId = customerId;
    }
}
