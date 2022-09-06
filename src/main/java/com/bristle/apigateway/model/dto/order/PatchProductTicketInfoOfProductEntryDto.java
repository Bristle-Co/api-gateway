package com.bristle.apigateway.model.dto.order;

public class PatchProductTicketInfoOfProductEntryDto {

    private String productEntryId;

    private Integer productTicketId;

    private boolean resetToNull = false; // false as default

    public PatchProductTicketInfoOfProductEntryDto(String productEntryId, Integer productTicketId) {
        this.productEntryId = productEntryId;
        this.productTicketId = productTicketId;
    }

    public String getProductEntryId() {
        return productEntryId;
    }

    public void setProductEntryId(String productEntryId) {
        this.productEntryId = productEntryId;
    }

    public Integer getProductTicketId() {
        return productTicketId;
    }

    public void setProductTicketId(Integer productTicketId) {
        this.productTicketId = productTicketId;
    }

    public boolean isResetToNull() {
        return resetToNull;
    }

    public void setResetToNull(boolean resetToNull) {
        this.resetToNull = resetToNull;
    }

    @Override
    public String toString() {
        return "PatchProductTicketInfoOfProductEntryDto{" +
                "productEntryId='" + productEntryId + '\'' +
                ", productTicketId='" + productTicketId + '\'' +
                ", resetToNull=" + resetToNull +
                '}';
    }
}
