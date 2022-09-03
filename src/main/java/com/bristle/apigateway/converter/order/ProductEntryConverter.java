package com.bristle.apigateway.converter.order;


import com.bristle.apigateway.model.dto.order.OrderDto;
import com.bristle.apigateway.model.dto.order.ProductEntryDto;
import com.bristle.proto.order.ProductEntry;
import org.springframework.stereotype.Component;

@Component
public class ProductEntryConverter {

    public ProductEntryDto protoToDto(ProductEntry productEntryProto) {
        // this conversion is used when productEntry is fetched from db alone
        // without attaching to an order

        return new ProductEntryDto(
                productEntryProto.getProductEntryId(),
                productEntryProto.getModel().equals("") ? null : productEntryProto.getModel(),
                // protobuf3 numeric type default value is 0
                // but there might be times where we actually want to 0 for special cases and store it in db
                // thus we define -2,147,483,648 ( 0x80000000 ) to be null
                productEntryProto.getQuantity() == Integer.MIN_VALUE ? null : productEntryProto.getQuantity(),
                productEntryProto.getPrice() == Integer.MIN_VALUE ? null : productEntryProto.getPrice(),
                productEntryProto.getProductTicketId().equals("") ? null : productEntryProto.getProductTicketId(),
                productEntryProto.getOrderId() == Integer.MIN_VALUE ? null : productEntryProto.getOrderId()
        );
    }

    public ProductEntryDto protoToDto(ProductEntry productEntryProto, OrderDto orderEntity) {
        // whenever we load proto back to entity we need to make sure
        // each item in ProductEntryEntityList references the OrderEntity object
        // this way Hibernate knows what the foreign key is

        return new ProductEntryDto(
                productEntryProto.getProductEntryId(),
                productEntryProto.getModel().equals("") ? null : productEntryProto.getModel(),
                // protobuf3 numeric type default value is 0
                // but there might be times where we actually want to 0 for special cases and store it in db
                // thus we define -2,147,483,648 ( 0x80000000 ) to be null
                productEntryProto.getQuantity() == Integer.MIN_VALUE ? null : productEntryProto.getQuantity(),
                productEntryProto.getPrice() == Integer.MIN_VALUE ? null : productEntryProto.getPrice(),
                productEntryProto.getProductTicketId().equals("") ? null : productEntryProto.getProductTicketId(),
                orderEntity.getOrderId() == Integer.MIN_VALUE ? null : orderEntity.getOrderId()
        );
    }

    public ProductEntry dtoToProto(ProductEntryDto productEntryDto, OrderDto oderDto) {

        return ProductEntry.newBuilder()
                // product entry id is never ever null
                .setProductEntryId(productEntryDto.getProductEntryId())
                .setModel(productEntryDto.getModel() == null ? "" : productEntryDto.getModel())
                .setQuantity(productEntryDto.getQuantity() == null ? Integer.MIN_VALUE : productEntryDto.getQuantity())
                .setPrice(productEntryDto.getPrice() == null ? Integer.MIN_VALUE : productEntryDto.getPrice())
                .setProductTicketId(productEntryDto.getProductTicketId() == null ? "" : productEntryDto.getProductTicketId())
                // order Id can be null when order with product entry is first inserted
                // the id foreign key column is then set when we do JPARepository.save(orderEntity)
                .setOrderId(oderDto.getOrderId() == null ? Integer.MIN_VALUE : oderDto.getOrderId())
                .build();
    }
}