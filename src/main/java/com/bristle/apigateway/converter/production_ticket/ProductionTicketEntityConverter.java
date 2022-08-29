package com.bristle.apigateway.converter.production_ticket;

import com.bristle.apigateway.model.production_ticket.ProductionTicketEntity;
import com.bristle.proto.production_ticket.ProductionTicket;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
public class ProductionTicketEntityConverter {

    public ProductionTicketEntity protoToEntity(ProductionTicket proto) {

        return new ProductionTicketEntity(
                proto.getTicketId() == Integer.MIN_VALUE ? null : proto.getTicketId(),
                proto.getCustomerId().equals("") ? null : proto.getCustomerId(),
                proto.getDueDate() == Long.MIN_VALUE ? null : Instant.ofEpochMilli(proto.getDueDate()*1000).atZone(ZoneId.of("UTC")).toLocalDate(),
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

    public ProductionTicket entityToProto(ProductionTicketEntity entity) {
        return ProductionTicket.newBuilder()
                .setTicketId(entity.getTicketId() == null ? Integer.MIN_VALUE : entity.getTicketId())
                .setCustomerId(entity.getCustomerId() == null ? "" : entity.getCustomerId())
                .setDueDate(entity.getDueDate()==null? Long.MIN_VALUE : entity.getDueDate().atStartOfDay().toEpochSecond(ZoneOffset.UTC))
                .setProductName(entity.getProductName()==null ? "" : entity.getProductName())
                .setBristleType(entity.getBristleType() == null?"": entity.getBristleType())
                .setModel(entity.getModel()==null?"":entity.getModel())
                .setInnerTubeType(entity.getInnerTubeType() == null?"":entity.getInnerTubeType())
                .setBristleDiameter(entity.getBristleDiameter() == null? Float.MIN_VALUE : entity.getBristleDiameter())
                .setQuantity(entity.getQuantity() == null ? Integer.MIN_VALUE : entity.getQuantity())
                .setAlumTubeType(entity.getAlumTubeType() == null?"":entity.getAlumTubeType())
                .setAlumRimType(entity.getAlumRimType() == null ? "":entity.getAlumRimType())
                .setModelNote(entity.getModelNote() == null ? "" : entity.getModelNote())
                .setProductionNote1(entity.getProductionNote1() == null ? "" : entity.getProductionNote1())
                .setProductionNote2(entity.getProductionNote2() == null ? "" : entity.getProductionNote2())
                .setProductionNote3(entity.getProductionNote3() == null ? "" : entity.getProductionNote3())
                .setProductionNote4(entity.getProductionNote4() == null ? "" : entity.getProductionNote4())
                .setProductionNote5(entity.getProductionNote5() == null ? "" : entity.getProductionNote5())
                .setProductionNote6(entity.getProductionNote6() == null ? "" : entity.getProductionNote6())
                .setDonePreparingAt(entity.getDonePreparingAt() == null ? Long.MIN_VALUE : entity.getDonePreparingAt().toEpochSecond(ZoneOffset.UTC))
                .setPreparedBy(entity.getPreparedBy() == null ? "":entity.getPreparedBy())
                .setDoneTwiningAt(entity.getDoneTwiningAt() == null ? Long.MIN_VALUE : entity.getDoneTwiningAt().toEpochSecond(ZoneOffset.UTC))
                .setTwinedBy(entity.getTwinedBy() == null ? "":entity.getTwinedBy())
                .setDoneTrimmingAt(entity.getDoneTrimmingAt() == null ? Long.MIN_VALUE : entity.getDoneTrimmingAt().toEpochSecond(ZoneOffset.UTC))
                .setTrimmedBy(entity.getTrimmedBy() == null ? "":entity.getTrimmedBy())
                .setDonePackagingAt(entity.getDonePackagingAt() == null ? Long.MIN_VALUE : entity.getDonePackagingAt().toEpochSecond(ZoneOffset.UTC))
                .setPackagedBy(entity.getPackagedBy() == null ? "" : entity.getPackagedBy())
                .setIssuedAt(entity.getIssuedAt() == null ? Long.MIN_VALUE : entity.getIssuedAt().toEpochSecond(ZoneOffset.UTC))
                .build();
    }
}
