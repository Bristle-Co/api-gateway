package com.bristle.apigateway.model.order;

import java.io.Serializable;
import java.util.Objects;

public class ProductEntryPK implements Serializable {

    private String model;
    private OrderEntity order;

    public ProductEntryPK() {
    }

    public ProductEntryPK(String model, OrderEntity order) {
        this.model = model;
        this.order = order;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntryPK that = (ProductEntryPK) o;
        return Objects.equals(model, that.model) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, order);
    }
}
