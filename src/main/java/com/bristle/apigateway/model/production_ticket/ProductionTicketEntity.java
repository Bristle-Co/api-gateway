package com.bristle.apigateway.model.production_ticket;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(name = "production_tickets")
public class ProductionTicketEntity {

    public static final String TABLE_NAME = "production_tickets";

    // I use static final variables instead of enums on purpose
    // so I can reference these strings in annotations
    public static final String TICKET_ID = "ticket_id";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String DUE_DATE = "due_date";
    public static final String PRODUCT_NAME = "product_name";
    public static final String BRISTLE_TYPE = "bristle_type";
    public static final String MODEL = "model";
    public static final String INNER_TUBE_TYPE = "inner_tube_type";
    public static final String BRISTLE_DIAMETER = "bristle_diameter";
    public static final String QUANTITY = "quantity";
    public static final String ALUM_TUBE_TYPE = "aluminium_tube_type";
    public static final String ALUM_RIM_TYPE = "aluminium_tim_type";
    public static final String MODEL_NOTE = "model_note";
    public static final String PRODUCTION_NOTE_1 = "production_note_1";
    public static final String PRODUCTION_NOTE_2 = "production_note_2";
    public static final String PRODUCTION_NOTE_3 = "production_note_3";
    public static final String PRODUCTION_NOTE_4 = "production_note_4";
    public static final String PRODUCTION_NOTE_5 = "production_note_5";
    public static final String PRODUCTION_NOTE_6 = "production_note_6";
    public static final String DONE_PREPARING_AT = "done_preparing_at";
    public static final String PREPARED_BY = "prepared_by";
    public static final String DONE_TWINING_AT = "done_twining_at";
    public static final String TWINED_BY = "twined_by";
    public static final String DONE_TRIMMING_AT = "done_trimming_at";
    public static final String TRIMMED_BY = "trimmed_by";
    public static final String DONE_PACKAGING_AT = "done_packaging_at";
    public static final String PACKAGED_BY = "packaged_by";
    public static final String ISSUED_AT = "issued_at";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TICKET_ID, nullable = false)
    private Integer ticketId;

    @Column(name = CUSTOMER_ID, nullable = true)
    private String customerId;

    @Column(name = DUE_DATE, nullable = true)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;

    @Column(name = PRODUCT_NAME, nullable = true)
    private String productName;
    @Column(name = BRISTLE_TYPE, nullable = true)
    private String bristleType;

    @Column(name = MODEL, nullable = true)
    private String model;

    @Column(name = INNER_TUBE_TYPE, nullable = true)
    private String innerTubeType;

    @Column(name = BRISTLE_DIAMETER, nullable = true)
    private Float bristleDiameter;

    @Column(name = QUANTITY, nullable = true)
    private Integer quantity;

    @Column(name = ALUM_TUBE_TYPE, nullable = true)
    private String alumTubeType;

    @Column(name = ALUM_RIM_TYPE, nullable = true)
    private String alumRimType;

    @Column(name = MODEL_NOTE, nullable = true)
    private String modelNote;

    @Column(name = PRODUCTION_NOTE_1, nullable = true)
    private String ProductionNote1;

    @Column(name = PRODUCTION_NOTE_2, nullable = true)
    private String ProductionNote2;

    @Column(name = PRODUCTION_NOTE_3, nullable = true)
    private String ProductionNote3;

    @Column(name = PRODUCTION_NOTE_4, nullable = true)
    private String ProductionNote4;

    @Column(name = PRODUCTION_NOTE_5, nullable = true)
    private String ProductionNote5;

    @Column(name = PRODUCTION_NOTE_6, nullable = true)
    private String ProductionNote6;

    @Column(name = DONE_PREPARING_AT, nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime donePreparingAt;

    @Column(name = PREPARED_BY, nullable = true)
    private String preparedBy;

    @Column(name = DONE_TWINING_AT, nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime doneTwiningAt;

    @Column(name = TWINED_BY, nullable = true)
    private String twinedBy;

    @Column(name = DONE_TRIMMING_AT, nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime doneTrimmingAt;

    @Column(name = TRIMMED_BY, nullable = true)
    private String trimmedBy;

    @Column(name = DONE_PACKAGING_AT, nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime donePackagingAt;

    @Column(name = PACKAGED_BY, nullable = true)
    private String packagedBy;

    @Column(name = ISSUED_AT, nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime issuedAt;

    public ProductionTicketEntity() {}

    public ProductionTicketEntity(Integer ticketId, String customerId, Date dueDate, String productName, String bristleType, String model, String innerTubeType, Float bristleDiameter, Integer quantity, String alumTubeType, String alumRimType, String modelNote, String productionNote1, String productionNote2, String productionNote3, String productionNote4, String productionNote5, String productionNote6, LocalDateTime donePreparingAt, String preparedBy, LocalDateTime doneTwiningAt, String twinedBy, LocalDateTime doneTrimmingAt, String trimmedBy, LocalDateTime donePackagingAt, String packagedBy, LocalDateTime issuedAt) {
        this.ticketId = ticketId;
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

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
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
}
