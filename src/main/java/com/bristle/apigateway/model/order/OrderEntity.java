package com.bristle.apigateway.model.order;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

// This class is used for database definition in relational database
// The protobuf generated class "Customer" shoud map to this class
// then stored into MySQL/MariaDB

// Note that only the  customer_id(客戶代號) which is the primary key
// has to be non-null

@Entity(name = "orders")
public class OrderEntity {

    // Table name
    public static final String TABLE_NAME = "orders";

    // Column names, reusable from outside of class
    // COLM for column
    public static final String COLM_ORDER_ID = "order_id";
    public static final String COLM_CUSTOMER_ORDER_ID = "customer_order_id";
    public static final String COLM_CUSTOMER_ID= "customer_id";
    public static final String COLM_DUE_DATE = "due_date";
    public static final String COLM_NOTE = "note";

    public static final String COLM_DELIVERED_AT = "delivered_at";

    public static final String COLM_ISSUED_AT = "issued_at";


    // This is simply a auto incrementing integer
    // When displaying we concatenate it with the prefix "BR"
    // ex: BR1, BR5, BR888
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = COLM_ORDER_ID)
    private Integer orderID;

    // There is a unique order Id we get from customer in a order
    // however the format of this string is different from customer to customer
    // thus we can not use it as primary key
    @Column(name = COLM_CUSTOMER_ORDER_ID, nullable = true)
    private String customerOrderId;

    // Foreign key to customers table
    @Column(name = COLM_CUSTOMER_ID, nullable = true)
    private String customerId;

    // This is the initial estimated due date
    // I use java.util.Date + @Temporal
    // because I want to use the constructor that takes epoch long
    @Column(name = COLM_DUE_DATE, nullable = true)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;

    @Column(name = COLM_NOTE, nullable = true)
    private String note;

    @Column(name = COLM_DELIVERED_AT, nullable = true)
    private LocalDateTime deliveredAt;

    @Column(name = COLM_ISSUED_AT, nullable = true)
    private LocalDateTime issuedAt;

    // The name of this mappedBy attribute is the name of the variable
    // that is annotated with @JoinColumn on the owning side
    // this is the referencing side
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<ProductEntryEntity> productEntries;

    public OrderEntity() {
    }

    public OrderEntity(Integer orderID,
                       String customerOrderId,
                       String customerId,
                       Date dueDate,
                       String note,
                       LocalDateTime deliveredAt,
                       LocalDateTime issuedAt,
                       List<ProductEntryEntity> productEntries) {
        this.orderID = orderID;
        this.customerOrderId = customerOrderId;
        this.customerId = customerId;
        this.dueDate = dueDate;
        this.note = note;
        this.deliveredAt = deliveredAt;
        this.issuedAt = issuedAt;
        this.productEntries = productEntries;
    }


    // Lombok could work well here but I don't wanna use it lol
    // It doesn't support new version of intellij

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public String getcustomerOrderId() {
        return customerOrderId;
    }

    public void setcustomerOrderId(String customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
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

    public List<ProductEntryEntity> getProductEntries() {
        return productEntries;
    }

    public void setProductEntries(List<ProductEntryEntity> productEntries) {
        this.productEntries = productEntries;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "orderID=" + orderID +
                ", customerOrderId='" + customerOrderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", dueDate=" + dueDate +
                ", note='" + note + '\'' +
                ", deliveredAt=" + deliveredAt +
                ", issuedAt=" + issuedAt +
                ", productEntries=" + productEntries +
                '}';
    }
}


