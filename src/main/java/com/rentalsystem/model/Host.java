package com.rentalsystem.model;

import java.util.*;

public class Host extends Person {
    private List<Property> managedProperties;
    private Set<Owner> cooperatingOwners;
    private List<RentalAgreement> managedAgreements;

    public Host(String id, String fullName, Date dateOfBirth, String contactInformation) {
        super(id, fullName, dateOfBirth, contactInformation);
        this.managedProperties = new ArrayList<>();
        this.cooperatingOwners = new HashSet<>();
        this.managedAgreements = new ArrayList<>();
    }

    public List<Property> getManagedProperties() {
        return new ArrayList<>(managedProperties);
    }

    public void addManagedProperty(Property property) {
        if (!managedProperties.contains(property)) {
            managedProperties.add(property);
        }
    }

    public void removeManagedProperty(Property property) {
        managedProperties.remove(property);
    }

    public Set<Owner> getCooperatingOwners() {
        return new HashSet<>(cooperatingOwners);
    }

    public void addCooperatingOwner(Owner owner) {
        cooperatingOwners.add(owner);
    }

    public void removeCooperatingOwner(Owner owner) {
        cooperatingOwners.remove(owner);
    }

    public List<RentalAgreement> getManagedAgreements() {
        return new ArrayList<>(managedAgreements);
    }

    public void addManagedAgreement(RentalAgreement agreement) {
        if (!managedAgreements.contains(agreement)) {
            managedAgreements.add(agreement);
        }
    }

    public void removeManagedAgreement(RentalAgreement agreement) {
        managedAgreements.remove(agreement);
    }

    @Override
    public String toString() {
        return "Host{" +
                "id='" + getId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", dateOfBirth=" + getDateOfBirth() +
                ", contactInformation='" + getContactInformation() + '\'' +
                ", managedProperties=" + managedProperties.size() +
                ", cooperatingOwners=" + cooperatingOwners.size() +
                ", managedAgreements=" + managedAgreements.size() +
                '}';
    }
}