package com.rentalsystem.model;

import java.util.*;

public class RentalAgreement {
    private String agreementId;
    private Property property;
    private Tenant mainTenant;
    private List<Tenant> subTenants;
    private Owner owner;
    private Host host;
    private Date startDate;
    private Date endDate;
    private double rentAmount;
    private RentalPeriod rentalPeriod;
    private Status status;

    public enum RentalPeriod {
        DAILY, WEEKLY, FORTNIGHTLY, MONTHLY
    }

    public enum Status {
        NEW, ACTIVE, COMPLETED
    }

    public String getAgreementId() { return agreementId; }
    public Property getProperty() { return property; }
    public Tenant getMainTenant() { return mainTenant; }
    public Owner getOwner() { return owner; }
    public Host getHost() { return host; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public double getRentAmount() { return rentAmount; }
    public RentalPeriod getRentalPeriod() { return rentalPeriod; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public List<Tenant> getSubTenants() { return subTenants; }
    public Tenant getTenant() { return mainTenant; }

    public RentalAgreement(String agreementId, Property property, Tenant mainTenant, Owner owner, Host host,
                           Date startDate, Date endDate, double rentAmount, RentalPeriod rentalPeriod) {
        this.agreementId = agreementId;
        this.property = property;
        this.mainTenant = mainTenant;
        this.owner = owner;
        this.host = host;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentAmount = rentAmount;
        this.rentalPeriod = rentalPeriod;
        this.subTenants = new ArrayList<>();
        this.status = Status.NEW;
    }

    // Getters and setters for all fields

    public void setRentAmount(double rentAmount) {
    this.rentAmount = rentAmount;
    }

    public void addSubTenant(Tenant subTenant) {
        if (!subTenants.contains(subTenant)) {
            subTenants.add(subTenant);
        }
    }

    public void removeSubTenant(String subTenantId) {
        subTenants.removeIf(tenant -> tenant.getId().equals(subTenantId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalAgreement that = (RentalAgreement) o;
        return Objects.equals(agreementId, that.agreementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agreementId);
    }

    @Override
    public String toString() {
        return "RentalAgreement{" +
                "agreementId='" + agreementId + '\'' +
                ", property=" + property.getPropertyId() +
                ", mainTenant=" + mainTenant.getFullName() +
                ", subTenants=" + subTenants.size() +
                ", owner=" + owner.getFullName() +
                ", host=" + host.getFullName() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", rentAmount=" + rentAmount +
                ", rentalPeriod=" + rentalPeriod +
                ", status=" + status +
                '}';
    }
}