package com.ayuan.mobilesafe.vo;

import java.util.Objects;

public class contact {
    private String contactName;
    private String contactNumber;

    public contact() {
    }

    public contact(String contactName, String contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
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
                "contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        contact contact = (contact) o;
        return Objects.equals(contactName, contact.contactName) &&
                Objects.equals(contactNumber, contact.contactNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactName, contactNumber);
    }
}
