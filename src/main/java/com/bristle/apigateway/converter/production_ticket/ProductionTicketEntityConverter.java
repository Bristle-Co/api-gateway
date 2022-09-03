package com.bristle.apigateway.converter.production_ticket;

import com.bristle.apigateway.model.dto.production_ticket.ProductionTicketDto;
import com.bristle.proto.production_ticket.ProductionTicket;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
public class ProductionTicketEntityConverter {

    public ProductionTicketDto protoToDto(ProductionTicket proto) {
        return new ProductionTicketDto(
                proto.getTicketId() == Integer.MIN_VALUE ? null : proto.getTicketId(),
//                no null check for orderId and productEntryId needed since they always have to be defined
                proto.getOrderId(),
                proto.getProductEntryId(),
                proto.getCustomerId().equals("") ? null : proto.getCustomerId(),
                proto.getDueDate() == Long.MIN_VALUE ? null : Instant.ofEpochMilli(proto.getDueDate() * 1000).atZone(ZoneId.of("UTC")).toLocalDate(),
                proto.getProductName().equals("") ? null : proto.getProductName(),
                proto.getBristleType().equals("") ? null : proto.getBristleType(),
                proto.getModel().equals("") ? null : proto.getModel(),
                proto.getInnerTubeType().equals("") ? null : proto.getInnerTubeType(),
                proto.getBristleDiameter() == Float.MIN_VALUE ? null : proto.getBristleDiameter(),
                proto.getQuantity() == Integer.MIN_VALUE ? null : proto.getQuantity(),
                proto.getAlumTubeType().equals("") ? null : proto.getAlumTubeType(),
                proto.getAlumRimType().equals("") ? null : proto.getAlumRimType(),
                proto.getModelNote().equals("") ? null : proto.getModelNote(),
                proto.getProductionNote1().equals("") ? null : proto.getProductionNote1(),
                proto.getProductionNote2().equals("") ? null : proto.getProductionNote2(),
                proto.getProductionNote3().equals("") ? null : proto.getProductionNote3(),
                proto.getProductionNote4().equals("") ? null : proto.getProductionNote4(),
                proto.getProductionNote5().equals("") ? null : proto.getProductionNote5(),
                proto.getProductionNote6().equals("") ? null : proto.getProductionNote6(),
                proto.getDonePreparingAt() == Long.MIN_VALUE ? null : LocalDateTime.ofEpochSecond(proto.getDonePreparingAt(), 0, ZoneOffset.UTC),
                proto.getPreparedBy().equals("") ? null : proto.getPreparedBy(),
                proto.getDoneTwiningAt() == Long.MIN_VALUE ? null : LocalDateTime.ofEpochSecond(proto.getDoneTwiningAt(), 0, ZoneOffset.UTC),
                proto.getTwinedBy().equals("") ? null : proto.getTwinedBy(),
                proto.getDoneTrimmingAt() == Long.MIN_VALUE ? null : LocalDateTime.ofEpochSecond(proto.getDoneTrimmingAt(), 0, ZoneOffset.UTC),
                proto.getTrimmedBy().equals("") ? null : proto.getTrimmedBy(),
                proto.getDonePackagingAt() == Long.MIN_VALUE ? null : LocalDateTime.ofEpochSecond(proto.getDonePackagingAt(), 0, ZoneOffset.UTC),
                proto.getPackagedBy().equals("") ? null : proto.getPackagedBy(),
                proto.getIssuedAt() == Long.MIN_VALUE ? null : LocalDateTime.ofEpochSecond(proto.getIssuedAt(), 0, ZoneOffset.UTC)
        );
    }

    public ProductionTicket dtoToProto(ProductionTicketDto dto) {

        return ProductionTicket.newBuilder()
                .setTicketId(dto.getTicketId() == null ? Integer.MIN_VALUE : dto.getTicketId())
                .setCustomerId(dto.getCustomerId() == null ? "" : dto.getCustomerId())
                .setDueDate(dto.getDueDate() == null ? Long.MIN_VALUE : dto.getDueDate().atStartOfDay().toEpochSecond(ZoneOffset.UTC))
                .setProductName(dto.getProductName() == null ? "" : dto.getProductName())
                .setBristleType(dto.getBristleType() == null ? "" : dto.getBristleType())
                .setModel(dto.getModel() == null ? "" : dto.getModel())
                .setInnerTubeType(dto.getInnerTubeType() == null ? "" : dto.getInnerTubeType())
                .setBristleDiameter(dto.getBristleDiameter() == null ? Float.MIN_VALUE : dto.getBristleDiameter())
                .setQuantity(dto.getQuantity() == null ? Integer.MIN_VALUE : dto.getQuantity())
                .setAlumTubeType(dto.getAlumTubeType() == null ? "" : dto.getAlumTubeType())
                .setAlumRimType(dto.getAlumRimType() == null ? "" : dto.getAlumRimType())
                .setModelNote(dto.getModelNote() == null ? "" : dto.getModelNote())
                .setProductionNote1(dto.getProductionNote1() == null ? "" : dto.getProductionNote1())
                .setProductionNote2(dto.getProductionNote2() == null ? "" : dto.getProductionNote2())
                .setProductionNote3(dto.getProductionNote3() == null ? "" : dto.getProductionNote3())
                .setProductionNote4(dto.getProductionNote4() == null ? "" : dto.getProductionNote4())
                .setProductionNote5(dto.getProductionNote5() == null ? "" : dto.getProductionNote5())
                .setProductionNote6(dto.getProductionNote6() == null ? "" : dto.getProductionNote6())
                .setDonePreparingAt(dto.getDonePreparingAt() == null ? Long.MIN_VALUE : dto.getDonePreparingAt().toEpochSecond(ZoneOffset.UTC))
                .setPreparedBy(dto.getPreparedBy() == null ? "" : dto.getPreparedBy())
                .setDoneTwiningAt(dto.getDoneTwiningAt() == null ? Long.MIN_VALUE : dto.getDoneTwiningAt().toEpochSecond(ZoneOffset.UTC))
                .setTwinedBy(dto.getTwinedBy() == null ? "" : dto.getTwinedBy())
                .setDoneTrimmingAt(dto.getDoneTrimmingAt() == null ? Long.MIN_VALUE : dto.getDoneTrimmingAt().toEpochSecond(ZoneOffset.UTC))
                .setTrimmedBy(dto.getTrimmedBy() == null ? "" : dto.getTrimmedBy())
                .setDonePackagingAt(dto.getDonePackagingAt() == null ? Long.MIN_VALUE : dto.getDonePackagingAt().toEpochSecond(ZoneOffset.UTC))
                .setPackagedBy(dto.getPackagedBy() == null ? "" : dto.getPackagedBy())
                .setIssuedAt(dto.getIssuedAt() == null ? Long.MIN_VALUE : dto.getIssuedAt().toEpochSecond(ZoneOffset.UTC))
//                no null check for order id and product entry id needed since they always have to be defined
                .setOrderId(dto.getOrderId())
                .setProductEntryId(dto.getProductEntryId())
                .build();
    }
}
