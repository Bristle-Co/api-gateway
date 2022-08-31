package com.bristle.apigateway.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

// This table has many-to-one relationship with the orders table
// One order could have many product entries(規格)

// Note that @IdClass is used instead of @EmbeddedId because hibernate @EmbeddedId
// doesn't play well with a column being foreign key and primary key at the same time
@Entity(name = "product_entries")
public class ProductEntryEntity {

    public static final String TABLE_NAME = "product_entries";

    public static final String COLM_PRODUCT_ENTRY_ID = "product_entry_id";
    public static final String COLM_QUANTITY = "quantity";
    public static final String COLM_PRICE= "price";
    public static final String COLM_MODEL = "model";
    public static final String COLM_PRODUCT_TICKET_ID= "product_ticket_id";

    public static final String COLM_ORDER_ID_FK= "order_id_fk";


    @Id
    @Column(name = COLM_PRODUCT_ENTRY_ID)
    private String productEntryId;

    @Column(name = COLM_MODEL, nullable = true)
    private String model;

    @Column(name = COLM_QUANTITY, nullable = true)
    private Integer quantity;

    @Column(name = COLM_PRICE, nullable = true)
    private Integer price;

    // If this field is null that means this product entry
    // has not been made into a product ticket
    @Column(name = COLM_PRODUCT_TICKET_ID, nullable = true)
    private String productTicketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLM_ORDER_ID_FK, referencedColumnName = OrderEntity.COLM_ORDER_ID)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private OrderEntity order;

    @Transient
    @JsonInclude(Include.NON_NULL)
    private Integer orderId;

    public ProductEntryEntity() {
    }

    public ProductEntryEntity(String productEntryId, String model, Integer quantity, Integer price, String productTicketId, OrderEntity order, Integer orderId) {
        this.productEntryId = productEntryId;
        this.model = model;
        this.quantity = quantity;
        this.price = price;
        this.productTicketId = productTicketId;
        this.order = order;
        this.orderId = orderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public void setProductTicketId(String productTicket_id) {
        this.productTicketId = productTicket_id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "ProductEntryEntity{" +
                "productEntryId='" + productEntryId + '\'' +
                ", model='" + model + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", productTicket_id='" + productTicketId + '\'' +
                '}';
    }
}