package com.ayuan.mobilesafe.vo;

import java.util.Objects;

public class contact {
    private String contactId;
    private String contactName;
    private String contactNumber;

    public contact() {
    }

    public contact(String contactId, String contactName, String contactNumber) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
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

    @Override
    public String toString() {
        return "contact{" +
                "contactId='" + contactId + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        contact contact = (contact) o;
        return Objects.equals(contactId, contact.contactId) &&
                Objects.equals(contactName, contact.contactName) &&
                Objects.equals(contactNumber, contact.contactNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(contactId, contactName, contactNumber);
    }
}

