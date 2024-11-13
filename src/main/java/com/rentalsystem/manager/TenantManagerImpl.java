package com.rentalsystem.manager;

import com.rentalsystem.model.Tenant;
import com.rentalsystem.util.FileHandler;
import java.util.*;
import java.util.stream.Collectors;

public class TenantManagerImpl implements TenantManager {
    private Map<String, Tenant> tenants;
    private FileHandler fileHandler;

    public TenantManagerImpl(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.tenants = new HashMap<>();
        loadTenants();
    }

    private void loadTenants() {
        List<Tenant> loadedTenants = fileHandler.loadTenants();
        for (Tenant tenant : loadedTenants) {
            tenants.put(tenant.getId(), tenant);
        }
    }

    @Override
    public void addTenant(Tenant tenant) {
        if (tenants.containsKey(tenant.getId())) {
            throw new IllegalArgumentException("Tenant with ID " + tenant.getId() + " already exists.");
        }
        tenants.put(tenant.getId(), tenant);
        saveTenants();
    }

    @Override
    public void updateTenant(Tenant tenant) {
        if (!tenants.containsKey(tenant.getId())) {
            throw new IllegalArgumentException("Tenant with ID " + tenant.getId() + " does not exist.");
        }
        tenants.put(tenant.getId(), tenant);
        saveTenants();
    }

    @Override
    public void deleteTenant(String tenantId) {
        if (!tenants.containsKey(tenantId)) {
            throw new IllegalArgumentException("Tenant with ID " + tenantId + " does not exist.");
        }
        tenants.remove(tenantId);
        saveTenants();
    }

    @Override
    public Tenant getTenant(String tenantId) {
        Tenant tenant = tenants.get(tenantId);
        if (tenant == null) {
            throw new IllegalArgumentException("Tenant with ID " + tenantId + " does not exist.");
        }
        return tenant;
    }

    @Override
    public List<Tenant> getAllTenants() {
        return new ArrayList<>(tenants.values());
    }

    @Override
    public List<Tenant> searchTenants(String keyword) {
        final String lowercaseKeyword = keyword.toLowerCase();
        return tenants.values().stream()
                .filter(tenant -> tenant.getFullName().toLowerCase().contains(lowercaseKeyword) ||
                        tenant.getId().toLowerCase().contains(lowercaseKeyword) ||
                        tenant.getContactInformation().toLowerCase().contains(lowercaseKeyword))
                .collect(Collectors.toList());
    }

    private void saveTenants() {
        fileHandler.saveTenants(new ArrayList<>(tenants.values()));
    }
}