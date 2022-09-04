package com.bristle.apigateway.converter.order;

import com.bristle.apigateway.model.dto.order.OrderDto;
import com.bristle.apigateway.model.dto.order.ProductEntryDto;
import com.bristle.proto.order.Order;
import com.bristle.proto.order.ProductEntry;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverter {

    private final ProductEntryConverter m_productEntryConverter;

    OrderConverter(ProductEntryConverter productEntryConverter) {
        m_productEntryConverter = productEntryConverter;
    }

    public OrderDto protoToDto(Order orderProto) {

        OrderDto result = new OrderDto(
                orderProto.getOrderId() == Integer.MIN_VALUE ? null : orderProto.getOrderId(),
                orderProto.getCustomerOrderId().equals("") ? null : orderProto.getCustomerOrderId(),
                orderProto.getCustomerId().equals("") ? null : orderProto.getCustomerId(),
                orderProto.getDueDate() == Long.MIN_VALUE ? null : Instant.ofEpochMilli(orderProto.getDueDate() * 1000).atZone(ZoneId.of("UTC")).toLocalDate(),
                orderProto.getNote().equals("") ? null : orderProto.getNote(),
                orderProto.getDeliveredAt() == Long.MIN_VALUE ? null : LocalDateTime.ofEpochSecond(orderProto.getDeliveredAt(), 0, ZoneOffset.UTC),
                orderProto.getIssuedAt() == Long.MIN_VALUE ? null : LocalDateTime.ofEpochSecond(orderProto.getIssuedAt(), 0, ZoneOffset.UTC),
                null);

        if (orderProto.getProductEntryCount() > 0) {
            List<ProductEntryDto> entityList
                    = orderProto.getProductEntryList()
                    .stream()
                    .map(proto -> m_productEntryConverter.protoToDto(proto, result))
                    .collect(Collectors.toList());
            result.setProductEntries(entityList);
        } else {
            result.setProductEntries(Collections.emptyList());
        }

        return result;
    }

    public Order dtoToProto(OrderDto dto) {
        Order.Builder result = Order.newBuilder()
                .setOrderId(dto.getOrderId() == null ? Integer.MIN_VALUE : dto.getOrderId())
                .setCustomerOrderId(dto.getCustomerOrderId() == null ? "" : dto.getCustomerOrderId())
                .setCustomerId(dto.getCustomerId() == null ? "" : dto.getCustomerId())
                .setDueDate(dto.getDueDate() == null ? Long.MIN_VALUE : dto.getDueDate().atStartOfDay().toEpochSecond(ZoneOffset.UTC))
                .setNote(dto.getNote() == null ? "" : dto.getNote())
                .setDeliveredAt(dto.getDeliveredAt() == null ? Long.MIN_VALUE : dto.getDeliveredAt().toEpochSecond(ZoneOffset.UTC))
                .setIssuedAt(dto.getIssuedAt() == null ? Long.MIN_VALUE : dto.getIssuedAt().toEpochSecond(ZoneOffset.UTC));

        if (dto.getProductEntries().size() > 0) {
            List<ProductEntry> productEntriesList = dto.getProductEntries().stream()
                    .map(item -> {
                        // when inserting always have the orderId in each ProductEntryDto follow the order's Id in OrderDto
                        // regardless of it is null or not
                        return m_productEntryConverter.dtoToProto(item, dto);
                    }).collect(Collectors.toList());
            result.addAllProductEntry(productEntriesList);
        }

        return result.build();
    }
}
