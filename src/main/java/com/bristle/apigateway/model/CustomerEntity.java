package com.bristle.apigateway.model;

import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

// This class is used for database definition in relational database
// The protobuf generated class "Customer" shoud map to this class
// then stored into MySQL/MariaDB

// Note that only the  customer_id(客戶代號) which is the primary key
// has to be non-null

@Entity(name = "customers")
public class CustomerEntity {

    // Table name
    public static final String CUSTOMER_TABLE_NAME = "customers";

    // Column names, reusable from outside of class
    public static final String CUSTOMERS_COLM_CUSTOMER_ID = "customer_id";
    public static final String CUSTOMERS_COLM_NAME = "name";
    public static final String CUSTOMERS_COLM_CONTACT_NAME = "contact_name";
    public static final String CUSTOMERS_COLM_CONTACT_NUMBER = "contact_number";
    public static final String CUSTOMERS_COLM_CONTACT_MOBILE_NUMBER = "contact_mobile_number";
    public static final String CUSTOMERS_COLM_FAX_NUMBER = "fax_number";
    public static final String CUSTOMERS_COLM_POSTAL_CODE = "postal_code";
    public static final String CUSTOMERS_COLM_ADDRESS = "address";
    public static final String CUSTOMERS_COLM_TAX_ID = "tax_id";
    public static final String CUSTOMERS_COLM_RECEIVER = "receiver";
    public static final String CUSTOMERS_COLM_NOTE = "note";


    @Id
    @NonNull
    @Column(name = CUSTOMERS_COLM_CUSTOMER_ID)
    String customerId;

    @Column(name = CUSTOMERS_COLM_NAME)
    String name;

    @Column(name = CUSTOMERS_COLM_CONTACT_NAME)
    String contactName;

    @Column(name = CUSTOMERS_COLM_CONTACT_NUMBER)
    String contactNumber;

    @Column(name = CUSTOMERS_COLM_CONTACT_MOBILE_NUMBER)
    String contactMobileNumber;

    @Column(name = CUSTOMERS_COLM_FAX_NUMBER)
    String faxNumber;

    @Column(name = CUSTOMERS_COLM_POSTAL_CODE)
    String postalCode;

    @Column(name = CUSTOMERS_COLM_ADDRESS)
    String address;

    @Column(name = CUSTOMERS_COLM_TAX_ID)
    String taxId;

    @Column(name = CUSTOMERS_COLM_RECEIVER)
    String receiver;

    @Column(name = CUSTOMERS_COLM_NOTE)
    String note;

    public CustomerEntity(@NonNull String customerId, String name, String contactName, String contactNumber, String contactMobileNumber, String faxNumber, String postalCode, String address, String taxId, String receiver, String note) {
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

    public CustomerEntity() {
    }

    @NonNull
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(@NonNull String customerId) {
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

    @Override
    public String toString() {
        return "CustomerEntity{" +
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
}

