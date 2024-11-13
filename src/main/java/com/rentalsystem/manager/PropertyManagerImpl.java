package com.rentalsystem.manager;

import com.rentalsystem.model.Property;
import com.rentalsystem.util.FileHandler;
import java.util.*;
import java.util.stream.Collectors;

public class PropertyManagerImpl implements PropertyManager {
    private Map<String, Property> properties;
    private FileHandler fileHandler;

    public PropertyManagerImpl(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.properties = new HashMap<>();
        loadProperties();
    }

    private void loadProperties() {
        List<Property> loadedProperties = fileHandler.loadProperties();
        for (Property property : loadedProperties) {
            properties.put(property.getPropertyId(), property);
        }
    }

    @Override
    public void addProperty(Property property) {
        if (properties.containsKey(property.getPropertyId())) {
            throw new IllegalArgumentException("Property with ID " + property.getPropertyId() + " already exists.");
        }
        properties.put(property.getPropertyId(), property);
        saveProperties();
    }

    @Override
    public void updateProperty(Property property) {
        if (!properties.containsKey(property.getPropertyId())) {
            throw new IllegalArgumentException("Property with ID " + property.getPropertyId() + " does not exist.");
        }
        properties.put(property.getPropertyId(), property);
        saveProperties();
    }

    @Override
    public void deleteProperty(String propertyId) {
        if (!properties.containsKey(propertyId)) {
            throw new IllegalArgumentException("Property with ID " + propertyId + " does not exist.");
        }
        properties.remove(propertyId);
        saveProperties();
    }

    @Override
    public Property getProperty(String propertyId) {
        Property property = properties.get(propertyId);
        if (property == null) {
            throw new IllegalArgumentException("Property with ID " + propertyId + " does not exist.");
        }
        return property;
    }

    @Override
    public List<Property> getAllProperties() {
        return new ArrayList<>(properties.values());
    }

    @Override
    public int getTotalProperties() {
        return properties.size();
    }

    @Override
    public int getOccupiedProperties() {
        return (int) properties.values().stream()
            .filter(p -> p.getStatus() == Property.PropertyStatus.RENTED)
            .count();
    }

    @Override
    public List<Property> searchProperties(String keyword) {
        final String lowercaseKeyword = keyword.toLowerCase();
        return properties.values().stream()
                .filter(property -> property.getPropertyId().toLowerCase().contains(lowercaseKeyword) ||
                        property.getAddress().toLowerCase().contains(lowercaseKeyword) ||
                        property.getOwner().getFullName().toLowerCase().contains(lowercaseKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Property> getAvailableProperties() {
        return properties.values().stream()
                .filter(property -> property.getStatus() == Property.PropertyStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    private void saveProperties() {
        fileHandler.saveProperties(new ArrayList<>(properties.values()));
    }
}