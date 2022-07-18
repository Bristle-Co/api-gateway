package com.bristle.apigateway.converter.customer_detail;

import com.bristle.apigateway.model.customer_detail.CustomerEntity;
import com.bristle.proto.customer_detail.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDetailEntityConverter {
    public CustomerEntity protoToEntity(Customer customerProto){
        // I don't want getters to get called twice in ternary operator
        // which is why I declare explicit variables
        // trading memory space for speed
        String name = customerProto.getName();
        String contactName = customerProto.getContactName();
        String contactNumber = customerProto.getContactNumber();
        String contactMobileNumber = customerProto.getContactMobileNumber();
        String faxNumber = customerProto.getFaxNumber();
        String postalCode = customerProto.getPostalCode();
        String address = customerProto.getAddress();
        String taxId = customerProto.getTaxId();
        String receiver = customerProto.getReceiver();
        String note = customerProto.getNote();

        return new CustomerEntity(customerProto.getCustomerId(),
                name.equals("") ? null : name,
                contactName.equals("") ? null : contactName,
                contactNumber.equals("") ? null : contactNumber,
                contactMobileNumber.equals("") ? null : contactMobileNumber,
                faxNumber.equals("") ? null : faxNumber,
                postalCode.equals("") ? null : postalCode,
                address.equals("") ? null : address,
                taxId.equals("") ? null : taxId,
                receiver.equals("") ? null : receiver,
                note.equals("") ? null : note);
    }

    public Customer entityToProto(CustomerEntity customerEntity){
        // Caution! Protobuf message setters don't take null!
        String name = customerEntity.getName();
        String contactName = customerEntity.getContactName();
        String contactNumber = customerEntity.getContactNumber();
        String contactMobileNumber = customerEntity.getContactMobileNumber();
        String faxNumber = customerEntity.getFaxNumber();
        String postalCode = customerEntity.getPostalCode();
        String address = customerEntity.getAddress();
        String taxId = customerEntity.getTaxId();
        String receiver = customerEntity.getReceiver();
        String note = customerEntity.getNote();

        return Customer.newBuilder()
                .setCustomerId(customerEntity.getCustomerId())
                .setName(name == null? "" : name)
                .setContactName(contactName == null ? "" : contactName)
                .setContactNumber(contactNumber == null ? "" : contactNumber)
                .setContactMobileNumber(contactMobileNumber == null ? "" : contactMobileNumber)
                .setFaxNumber(faxNumber == null ? "" : faxNumber)
                .setPostalCode(postalCode == null ? "" : postalCode)
                .setAddress(address == null ? "" : address)
                .setTaxId(taxId == null ? "" : taxId)
                .setReceiver(receiver ==  null ? "" : receiver)
                .setNote(note == null ? "" : note).build();
    }
}
