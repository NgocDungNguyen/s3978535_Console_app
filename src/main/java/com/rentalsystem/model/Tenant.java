package com.rentalsystem.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tenant extends Person {
    private List<RentalAgreement> rentalAgreements;
    private List<Payment> paymentTransactions;

    public Tenant(String id, String fullName, Date dateOfBirth, String contactInformation) {
        super(id, fullName, dateOfBirth, contactInformation);
        this.rentalAgreements = new ArrayList<>();
        this.paymentTransactions = new ArrayList<>();
    }

    public List<RentalAgreement> getRentalAgreements() {
        return rentalAgreements;
    }

    public void addRentalAgreement(RentalAgreement agreement) {
        rentalAgreements.add(agreement);
    }

    public List<Payment> getPaymentTransactions() {
        return paymentTransactions;
    }

    public void addPaymentTransaction(Payment payment) {
        paymentTransactions.add(payment);
    }

    @Override
    public String toString() {
        return String.join(",",
            getId(),
            getFullName(),
            new SimpleDateFormat("yyyy-MM-dd").format(getDateOfBirth()),
            getContactInformation()
        );
    }

    public static Tenant fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid tenant data: " + line);
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return new Tenant(
                parts[0],
                parts[1],
                dateFormat.parse(parts[2]),
                parts[3]
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format in tenant data: " + line, e);
        }
    }
}
