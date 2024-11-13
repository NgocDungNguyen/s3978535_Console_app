package com.rentalsystem.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.rentalsystem.model.Tenant;
import com.rentalsystem.util.FileHandler;
import com.rentalsystem.util.InputValidator;

public class TenantManagerImpl implements TenantManager {
    private Map<String, Tenant> tenants;
    private FileHandler fileHandler;

    public TenantManagerImpl(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.tenants = new HashMap<>();
        loadTenants();
    }

    private void loadTenants() {
        List<String> lines = fileHandler.readLines("tenants.txt");
        for (String line : lines) {
            Tenant tenant = Tenant.fromString(line);
            tenants.put(tenant.getId(), tenant);
        }
    }

    private void saveTenants() {
        List<String> lines = tenants.values().stream()
                .map(Tenant::toString)
                .collect(Collectors.toList());
        fileHandler.writeLines("tenants.txt", lines);
    }

    @Override
    public void addTenant(Tenant tenant) {
        if (!InputValidator.isValidEmail(tenant.getContactInformation())) {
            throw new IllegalArgumentException("Invalid email format for tenant: " + tenant.getContactInformation());
        }
        if (isEmailTaken(tenant.getContactInformation())) {
            throw new IllegalArgumentException("Email already in use: " + tenant.getContactInformation());
        }
        tenants.put(tenant.getId(), tenant);
        saveTenants();
    }

    @Override
    public void updateTenant(Tenant tenant) {
        if (!InputValidator.isValidEmail(tenant.getContactInformation())) {
            throw new IllegalArgumentException("Invalid email format for tenant: " + tenant.getContactInformation());
        }
        Tenant existingTenant = tenants.get(tenant.getId());
        if (existingTenant == null) {
            throw new IllegalArgumentException("Tenant with ID " + tenant.getId() + " does not exist.");
        }
        if (!existingTenant.getContactInformation().equals(tenant.getContactInformation()) && isEmailTaken(tenant.getContactInformation())) {
            throw new IllegalArgumentException("Email already in use: " + tenant.getContactInformation());
        }
        tenants.put(tenant.getId(), tenant);
        saveTenants();
    }

    @Override
    public void deleteTenant(String id) {
        if (tenants.remove(id) != null) {
            saveTenants();
        } else {
            throw new IllegalArgumentException("Tenant with ID " + id + " does not exist.");
        }
    }

    @Override
    public Tenant getTenant(String id) {
        return tenants.get(id);
    }

    @Override
    public List<Tenant> getAllTenants() {
        return new ArrayList<>(tenants.values());
    }

    @Override
    public List<Tenant> searchTenants(String keyword) {
        final String lowercaseKeyword = keyword.toLowerCase();
        return tenants.values().stream()
                .filter(tenant -> 
                    tenant.getFullName().toLowerCase().contains(lowercaseKeyword) ||
                    tenant.getId().toLowerCase().contains(lowercaseKeyword) ||
                    tenant.getContactInformation().toLowerCase().contains(lowercaseKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isEmailTaken(String email) {
        final String lowercaseEmail = email.toLowerCase();
        return tenants.values().stream()
                .anyMatch(tenant -> tenant.getContactInformation().toLowerCase().equals(lowercaseEmail));
    }
}