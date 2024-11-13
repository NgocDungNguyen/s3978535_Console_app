package com.rentalsystem.manager;

import com.rentalsystem.model.Tenant;
import java.util.List;

public interface TenantManager {
    void addTenant(Tenant tenant);
    void updateTenant(Tenant tenant);
    void deleteTenant(String tenantId);
    Tenant getTenant(String tenantId);
    List<Tenant> getAllTenants();
    List<Tenant> searchTenants(String keyword);
}