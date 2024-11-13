package com.rentalsystem.model;

import java.util.Objects;

public abstract class Property {
    private String propertyId;
    private String address;
    private double price;
    private PropertyStatus status;
    private Owner owner;

    public enum PropertyStatus {
        AVAILABLE, RENTED, UNDER_MAINTENANCE
    }

    public Property(String propertyId, String address, double price, PropertyStatus status, Owner owner) {
        this.propertyId = propertyId;
        this.address = address;
        this.price = price;
        this.status = status;
        this.owner = owner;
    }

    // Getters and setters
    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return Objects.equals(propertyId, property.propertyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyId);
    }

    @Override
    public String toString() {
        return "Property{" +
                "propertyId='" + propertyId + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", owner=" + owner.getFullName() +
                '}';
    }
}