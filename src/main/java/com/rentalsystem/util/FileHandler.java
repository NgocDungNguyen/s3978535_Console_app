package com.rentalsystem.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rentalsystem.model.CommercialProperty;
import com.rentalsystem.model.Host;
import com.rentalsystem.model.Owner;
import com.rentalsystem.model.Property;
import com.rentalsystem.model.RentalAgreement;
import com.rentalsystem.model.ResidentialProperty;
import com.rentalsystem.model.Tenant;

public class FileHandler {
    private static final String DATA_DIRECTORY = "resources/data/";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public List<String> readLines(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIRECTORY + filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void writeLines(String filename, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_DIRECTORY + filename))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveRentalAgreements(List<RentalAgreement> agreements) {
        List<String> lines = new ArrayList<>();
        for (RentalAgreement agreement : agreements) {
            lines.add(String.join(",",
                    agreement.getAgreementId(),
                    agreement.getProperty().getPropertyId(),
                    agreement.getMainTenant().getId(),
                    agreement.getOwner().getId(),
                    agreement.getHost().getId(),
                    DATE_FORMAT.format(agreement.getStartDate()),
                    DATE_FORMAT.format(agreement.getEndDate()),
                    String.valueOf(agreement.getRentAmount()),
                    agreement.getRentalPeriod().toString(),
                    agreement.getStatus().toString()
            ));
            
            for (Tenant subTenant : agreement.getSubTenants()) {
                lines.add("SUB," + agreement.getAgreementId() + "," + subTenant.getId());
            }
        }
        writeLines("rental_agreements.txt", lines);
    }

    public List<RentalAgreement> loadRentalAgreements() {
        List<RentalAgreement> agreements = new ArrayList<>();
        Map<String, RentalAgreement> agreementMap = new HashMap<>();

        List<String> lines = readLines("rental_agreements.txt");
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts[0].equals("SUB")) {
                RentalAgreement agreement = agreementMap.get(parts[1]);
                if (agreement != null) {
                    agreement.addSubTenant(getTenantById(parts[2]));
                }
            } else {
                try {
                    RentalAgreement agreement = new RentalAgreement(
                            parts[0],
                            getPropertyById(parts[1]),
                            getTenantById(parts[2]),
                            getOwnerById(parts[3]),
                            getHostById(parts[4]),
                            DATE_FORMAT.parse(parts[5]),
                            DATE_FORMAT.parse(parts[6]),
                            Double.parseDouble(parts[7]),
                            RentalAgreement.RentalPeriod.valueOf(parts[8])
                    );
                    agreement.setStatus(RentalAgreement.Status.valueOf(parts[9]));
                    agreements.add(agreement);
                    agreementMap.put(agreement.getAgreementId(), agreement);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return agreements;
    }

    public void saveProperties(List<Property> properties) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIRECTORY + "properties.txt"))) {
            for (Property property : properties) {
                if (property instanceof ResidentialProperty) {
                    ResidentialProperty rp = (ResidentialProperty) property;
                    writer.println(String.join(",",
                            "RESIDENTIAL",
                            property.getPropertyId(),
                            property.getAddress(),
                            String.valueOf(property.getPrice()),
                            property.getStatus().toString(),
                            property.getOwner().getId(),
                            String.valueOf(rp.getNumberOfBedrooms()),
                            String.valueOf(rp.hasGarden()),
                            String.valueOf(rp.isPetFriendly())
                    ));
                } else if (property instanceof CommercialProperty) {
                    CommercialProperty cp = (CommercialProperty) property;
                    writer.println(String.join(",",
                            "COMMERCIAL",
                            property.getPropertyId(),
                            property.getAddress(),
                            String.valueOf(property.getPrice()),
                            property.getStatus().toString(),
                            property.getOwner().getId(),
                            cp.getBusinessType(),
                            String.valueOf(cp.getParkingSpaces()),
                            String.valueOf(cp.getSquareFootage())
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Property> loadProperties() {
        List<Property> properties = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIRECTORY + "properties.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals("RESIDENTIAL")) {
                    properties.add(new ResidentialProperty(
                            parts[1], parts[2], Double.parseDouble(parts[3]),
                            Property.PropertyStatus.valueOf(parts[4]),
                            getOwnerById(parts[5]),
                            Integer.parseInt(parts[6]),
                            Boolean.parseBoolean(parts[7]),
                            Boolean.parseBoolean(parts[8])
                    ));
                } else if (parts[0].equals("COMMERCIAL")) {
                    properties.add(new CommercialProperty(
                            parts[1], parts[2], Double.parseDouble(parts[3]),
                            Property.PropertyStatus.valueOf(parts[4]),
                            getOwnerById(parts[5]),
                            parts[6],
                            Integer.parseInt(parts[7]),
                            Double.parseDouble(parts[8])
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public void saveTenants(List<Tenant> tenants) {
        List<String> lines = new ArrayList<>();
        for (Tenant tenant : tenants) {
            lines.add(tenant.toString());
        }
        writeLines("tenants.txt", lines);
    }

    public List<Tenant> loadTenants() {
        List<Tenant> tenants = new ArrayList<>();
        List<String> lines = readLines("tenants.txt");
        for (String line : lines) {
            tenants.add(Tenant.fromString(line));
        }
        return tenants;
    }

    public void saveOwners(List<Owner> owners) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIRECTORY + "owners.txt"))) {
            for (Owner owner : owners) {
                writer.println(String.join(",",
                        owner.getId(),
                        owner.getFullName(),
                        DATE_FORMAT.format(owner.getDateOfBirth()),
                        owner.getContactInformation()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Owner> loadOwners() {
        List<Owner> owners = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIRECTORY + "owners.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                owners.add(new Owner(
                        parts[0],
                        parts[1],
                        DATE_FORMAT.parse(parts[2]),
                        parts[3]
                ));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return owners;
    }

    public void saveHosts(List<Host> hosts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIRECTORY + "hosts.txt"))) {
            for (Host host : hosts) {
                writer.println(String.join(",",
                        host.getId(),
                        host.getFullName(),
                        DATE_FORMAT.format(host.getDateOfBirth()),
                        host.getContactInformation()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Host> loadHosts() {
        List<Host> hosts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIRECTORY + "hosts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                hosts.add(new Host(
                        parts[0],
                        parts[1],
                        DATE_FORMAT.parse(parts[2]),
                        parts[3]
                ));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return hosts;
    }

    private Property getPropertyById(String propertyId) {
        List<Property> properties = loadProperties();
        return properties.stream()
                .filter(p -> p.getPropertyId().equals(propertyId))
                .findFirst()
                .orElse(null);
    }

    private Tenant getTenantById(String tenantId) {
        List<Tenant> tenants = loadTenants();
        return tenants.stream()
                .filter(t -> t.getId().equals(tenantId))
                .findFirst()
                .orElse(null);
    }

    private Owner getOwnerById(String ownerId) {
        List<Owner> owners = loadOwners();
        return owners.stream()
                .filter(o -> o.getId().equals(ownerId))
                .findFirst()
                .orElse(null);
    }

    private Host getHostById(String hostId) {
        List<Host> hosts = loadHosts();
        return hosts.stream()
                .filter(h -> h.getId().equals(hostId))
                .findFirst()
                .orElse(null);
    }
}