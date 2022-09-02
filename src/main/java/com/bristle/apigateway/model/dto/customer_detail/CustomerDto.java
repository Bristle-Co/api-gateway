package com.bristle.apigateway.model.dto.customer_detail;

import javax.persistence.Column;
import javax.persistence.Id;

public class CustomerDto {

    String customerId;

    String name;

    String contactName;

    String contactNumber;

    String contactMobileNumber;

    String faxNumber;

    String postalCode;

    String address;

    String taxId;

    String receiver;

    String note;

    @Override
    public String toString() {
        return "CustomerDto{" +
                "customerId='" + customerId + '\'' +
                ", name='" + name + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", contactMobileNumber='" + contactMobileNumber + '\'' +
                ", faxNumber='" + faxNumber + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", address='" + address + '\'' +
                ", taxId='" + taxId + '\'' +
                ", receiver='" + receiver + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public CustomerDto(String customerId,
                       String name,
                       String contactName,
                       String contactNumber,
                       String contactMobileNumber,
                       String faxNumber,
                       String postalCode,
                       String address,
                       String taxId,
                       String receiver,
                       String note) {

        this.customerId = customerId;
        this.name = name;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.contactMobileNumber = contactMobileNumber;
        this.faxNumber = faxNumber;
        this.postalCode = postalCode;
        this.address = address;
        this.taxId = taxId;
        this.receiver = receiver;
        this.note = note;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactMobileNumber() {
        return contactMobileNumber;
    }

    public void setContactMobileNumber(String contactMobileNumber) {
        this.contactMobileNumber = contactMobileNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
