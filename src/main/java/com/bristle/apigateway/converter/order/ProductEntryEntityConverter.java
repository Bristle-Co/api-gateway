package com.bristle.apigateway.converter.order;


import com.bristle.apigateway.model.order.OrderEntity;
import com.bristle.apigateway.model.order.ProductEntryEntity;
import com.bristle.proto.order.ProductEntry;
import org.springframework.stereotype.Component;

@Component
public class ProductEntryEntityConverter {

    public ProductEntryEntity protoToEntity(ProductEntry productEntryProto, OrderEntity orderEntity) {
        // whenever we load proto back to entity we need to make sure
        // each item in ProductEntryEntityList references the OrderEntity object
        // this way Hibernate knows what the foreign key is

        return new ProductEntryEntity(
                productEntryProto.getProductEntryId(),
                productEntryProto.getModel().equals("") ? null : productEntryProto.getModel(),
                // protobuf3 numeric type default value is 0
                // but there might be times where we actually want to 0 for special cases and store it in db
                // thus we define -2,147,483,648 ( 0x80000000 ) to be null
                productEntryProto.getQuantity() == Integer.MIN_VALUE ? null : productEntryProto.getQuantity(),
                productEntryProto.getPrice() == Integer.MIN_VALUE ? null : productEntryProto.getPrice(),
                productEntryProto.getProductTicketId().equals("") ? null : productEntryProto.getProductTicketId(),
                orderEntity,
                orderEntity.getOrderId() == Integer.MIN_VALUE ? null : orderEntity.getOrderId()
        );
    }

    public ProductEntry entityToProto(ProductEntryEntity productEntryEntity) {

        return ProductEntry.newBuilder()
                // product entry id is never ever null
                .setProductEntryId(productEntryEntity.getProductEntryId())
                .setModel(productEntryEntity.getModel() == null ? "" : productEntryEntity.getModel())
                .setQuantity(productEntryEntity.getQuantity() == null ? Integer.MIN_VALUE : productEntryEntity.getQuantity())
                .setPrice(productEntryEntity.getPrice() == null ? Integer.MIN_VALUE : productEntryEntity.getPrice())
                .setProductTicketId(productEntryEntity.getProductTicketId() == null ? "" : productEntryEntity.getProductTicketId())
                // order Id can be null when order with product entry is first inserted
                // the id foreign key column is then set when we do JPARepository.save(orderEntity)
                .setOrderId(productEntryEntity.getOrderId() == null ? Integer.MIN_VALUE : productEntryEntity.getOrderId())
                .build();
    }
}
