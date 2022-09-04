package com.bristle.apigateway.model.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    private Integer orderId;

    private String customerOrderId;

    private String customerId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private String note;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deliveredAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime issuedAt;

    private List<ProductEntryDto> productEntries;

    @Override
    public String toString() {
        return "OrderDto{" +
                "orderId=" + orderId +
                ", customerOrderId='" + customerOrderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", dueDate=" + dueDate +
                ", note='" + note + '\'' +
                ", deliveredAt=" + deliveredAt +
                ", issuedAt=" + issuedAt +
                ", productEntries=" + productEntries +
                '}';
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(String customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public List<ProductEntryDto> getProductEntries() {
        return productEntries;
    }

    public void setProductEntries(List<ProductEntryDto> productEntries) {
        this.productEntries = productEntries;
    }

    public OrderDto(Integer orderId, String customerOrderId, String customerId, LocalDate dueDate, String note, LocalDateTime deliveredAt, LocalDateTime issuedAt, List<ProductEntryDto> productEntries) {
        this.orderId = orderId;
        this.customerOrderId = customerOrderId;
        this.customerId = customerId;
        this.dueDate = dueDate;
        this.note = note;
        this.deliveredAt = deliveredAt;
        this.issuedAt = issuedAt;
        this.productEntries = productEntries;
    }
}
