package com.rentalsystem.manager;

import com.rentalsystem.model.Owner;
import com.rentalsystem.util.FileHandler;
import java.util.*;
import java.util.stream.Collectors;

public class OwnerManagerImpl implements OwnerManager {
    private Map<String, Owner> owners;
    private FileHandler fileHandler;

    public OwnerManagerImpl(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.owners = new HashMap<>();
        loadOwners();
    }

    private void loadOwners() {
        List<Owner> loadedOwners = fileHandler.loadOwners();
        for (Owner owner : loadedOwners) {
            owners.put(owner.getId(), owner);
        }
    }

    @Override
    public void addOwner(Owner owner) {
        if (owners.containsKey(owner.getId())) {
            throw new IllegalArgumentException("Owner with ID " + owner.getId() + " already exists.");
        }
        owners.put(owner.getId(), owner);
        saveOwners();
    }

    @Override
    public void updateOwner(Owner owner) {
        if (!owners.containsKey(owner.getId())) {
            throw new IllegalArgumentException("Owner with ID " + owner.getId() + " does not exist.");
        }
        owners.put(owner.getId(), owner);
        saveOwners();
    }

    @Override
    public void deleteOwner(String ownerId) {
        if (!owners.containsKey(ownerId)) {
            throw new IllegalArgumentException("Owner with ID " + ownerId + " does not exist.");
        }
        owners.remove(ownerId);
        saveOwners();
    }

    @Override
    public Owner getOwner(String ownerId) {
        Owner owner = owners.get(ownerId);
        if (owner == null) {
            throw new IllegalArgumentException("Owner with ID " + ownerId + " does not exist.");
        }
        return owner;
    }

    @Override
    public List<Owner> getAllOwners() {
        return new ArrayList<>(owners.values());
    }

    @Override
    public List<Owner> searchOwners(String keyword) {
        final String lowercaseKeyword = keyword.toLowerCase();
        return owners.values().stream()
                .filter(owner -> owner.getFullName().toLowerCase().contains(lowercaseKeyword) ||
                        owner.getId().toLowerCase().contains(lowercaseKeyword) ||
                        owner.getContactInformation().toLowerCase().contains(lowercaseKeyword))
                .collect(Collectors.toList());
    }

    private void saveOwners() {
        fileHandler.saveOwners(new ArrayList<>(owners.values()));
    }
}