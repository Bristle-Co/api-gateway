package com.bristle.apigateway.model.dto.production_ticket;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProductionTicketDto {

    private Integer ticketId;

    private Integer orderId;

    private String productEntryId;

    private String customerId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private String productName;

    private String bristleType;

    private String model;

    private String innerTubeType;

    private Float bristleDiameter;

    private Integer quantity;

    private String alumTubeType;

    private String alumRimType;

    private String modelNote;

    private String ProductionNote1;

    private String ProductionNote2;

    private String ProductionNote3;

    private String ProductionNote4;

    private String ProductionNote5;

    private String ProductionNote6;

    // @JsonFormat is only applied on deserialization (json -> LocalDateTime)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime donePreparingAt;

    private String preparedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime doneTwiningAt;

    private String twinedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime doneTrimmingAt;

    private String trimmedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime donePackagingAt;

    private String packagedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime issuedAt;

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBristleType() {
        return bristleType;
    }

    public void setBristleType(String bristleType) {
        this.bristleType = bristleType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getInnerTubeType() {
        return innerTubeType;
    }

    public void setInnerTubeType(String innerTubeType) {
        this.innerTubeType = innerTubeType;
    }

    public Float getBristleDiameter() {
        return bristleDiameter;
    }

    public void setBristleDiameter(Float bristleDiameter) {
        this.bristleDiameter = bristleDiameter;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getAlumTubeType() {
        return alumTubeType;
    }

    public void setAlumTubeType(String alumTubeType) {
        this.alumTubeType = alumTubeType;
    }

    public String getAlumRimType() {
        return alumRimType;
    }

    public void setAlumRimType(String alumRimType) {
        this.alumRimType = alumRimType;
    }

    public String getModelNote() {
        return modelNote;
    }

    public void setModelNote(String modelNote) {
        this.modelNote = modelNote;
    }

    public String getProductionNote1() {
        return ProductionNote1;
    }

    public void setProductionNote1(String productionNote1) {
        ProductionNote1 = productionNote1;
    }

    public String getProductionNote2() {
        return ProductionNote2;
    }

    public void setProductionNote2(String productionNote2) {
        ProductionNote2 = productionNote2;
    }

    public String getProductionNote3() {
        return ProductionNote3;
    }

    public void setProductionNote3(String productionNote3) {
        ProductionNote3 = productionNote3;
    }

    public String getProductionNote4() {
        return ProductionNote4;
    }

    public void setProductionNote4(String productionNote4) {
        ProductionNote4 = productionNote4;
    }

    public String getProductionNote5() {
        return ProductionNote5;
    }

    public void setProductionNote5(String productionNote5) {
        ProductionNote5 = productionNote5;
    }

    public String getProductionNote6() {
        return ProductionNote6;
    }

    public void setProductionNote6(String productionNote6) {
        ProductionNote6 = productionNote6;
    }

    public LocalDateTime getDonePreparingAt() {
        return donePreparingAt;
    }

    public void setDonePreparingAt(LocalDateTime donePreparingAt) {
        this.donePreparingAt = donePreparingAt;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public LocalDateTime getDoneTwiningAt() {
        return doneTwiningAt;
    }

    public void setDoneTwiningAt(LocalDateTime doneTwiningAt) {
        this.doneTwiningAt = doneTwiningAt;
    }

    public String getTwinedBy() {
        return twinedBy;
    }

    public void setTwinedBy(String twinedBy) {
        this.twinedBy = twinedBy;
    }

    public LocalDateTime getDoneTrimmingAt() {
        return doneTrimmingAt;
    }

    public void setDoneTrimmingAt(LocalDateTime doneTrimmingAt) {
        this.doneTrimmingAt = doneTrimmingAt;
    }

    public String getTrimmedBy() {
        return trimmedBy;
    }

    public void setTrimmedBy(String trimmedBy) {
        this.trimmedBy = trimmedBy;
    }

    public LocalDateTime getDonePackagingAt() {
        return donePackagingAt;
    }

    public void setDonePackagingAt(LocalDateTime donePackagingAt) {
        this.donePackagingAt = donePackagingAt;
    }

    public String getPackagedBy() {
        return packagedBy;
    }

    public void setPackagedBy(String packagedBy) {
        this.packagedBy = packagedBy;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public ProductionTicketDto(Integer ticketId,
                               Integer orderId,
                               String productEntryId,
                               String customerId,
                               LocalDate dueDate,
                               String productName,
                               String bristleType,
                               String model,
                               String innerTubeType,
                               Float bristleDiameter,
                               Integer quantity,
                               String alumTubeType,
                               String alumRimType,
                               String modelNote,
                               String productionNote1,
                               String productionNote2,
                               String productionNote3,
                               String productionNote4,
                               String productionNote5,
                               String productionNote6,
                               LocalDateTime donePreparingAt,
                               String preparedBy,
                               LocalDateTime doneTwiningAt,
                               String twinedBy,
                               LocalDateTime doneTrimmingAt,
                               String trimmedBy,
                               LocalDateTime donePackagingAt,
                               String packagedBy,
                               LocalDateTime issuedAt) {

        this.ticketId = ticketId;
        this.orderId = orderId;
        this.productEntryId = productEntryId;
        this.customerId = customerId;
        this.dueDate = dueDate;
        this.productName = productName;
        this.bristleType = bristleType;
        this.model = model;
        this.innerTubeType = innerTubeType;
        this.bristleDiameter = bristleDiameter;
        this.quantity = quantity;
        this.alumTubeType = alumTubeType;
        this.alumRimType = alumRimType;
        this.modelNote = modelNote;
        ProductionNote1 = productionNote1;
        ProductionNote2 = productionNote2;
        ProductionNote3 = productionNote3;
        ProductionNote4 = productionNote4;
        ProductionNote5 = productionNote5;
        ProductionNote6 = productionNote6;
        this.donePreparingAt = donePreparingAt;
        this.preparedBy = preparedBy;
        this.doneTwiningAt = doneTwiningAt;
        this.twinedBy = twinedBy;
        this.doneTrimmingAt = doneTrimmingAt;
        this.trimmedBy = trimmedBy;
        this.donePackagingAt = donePackagingAt;
        this.packagedBy = packagedBy;
        this.issuedAt = issuedAt;
    }

    @Override
    public String toString() {
        return "ProductionTicketDto{" +
                "ticketId=" + ticketId +
                ", orderId=" + orderId +
                ", productEntryId='" + productEntryId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", dueDate=" + dueDate +
                ", productName='" + productName + '\'' +
                ", bristleType='" + bristleType + '\'' +
                ", model='" + model + '\'' +
                ", innerTubeType='" + innerTubeType + '\'' +
                ", bristleDiameter=" + bristleDiameter +
                ", quantity=" + quantity +
                ", alumTubeType='" + alumTubeType + '\'' +
                ", alumRimType='" + alumRimType + '\'' +
                ", modelNote='" + modelNote + '\'' +
                ", ProductionNote1='" + ProductionNote1 + '\'' +
                ", ProductionNote2='" + ProductionNote2 + '\'' +
                ", ProductionNote3='" + ProductionNote3 + '\'' +
                ", ProductionNote4='" + ProductionNote4 + '\'' +
                ", ProductionNote5='" + ProductionNote5 + '\'' +
                ", ProductionNote6='" + ProductionNote6 + '\'' +
                ", donePreparingAt=" + donePreparingAt +
                ", preparedBy='" + preparedBy + '\'' +
                ", doneTwiningAt=" + doneTwiningAt +
                ", twinedBy='" + twinedBy + '\'' +
                ", doneTrimmingAt=" + doneTrimmingAt +
                ", trimmedBy='" + trimmedBy + '\'' +
                ", donePackagingAt=" + donePackagingAt +
                ", packagedBy='" + packagedBy + '\'' +
                ", issuedAt=" + issuedAt +
                '}';
    }
}
