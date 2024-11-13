package com.rentalsystem.manager;

import com.rentalsystem.model.Host;
import com.rentalsystem.util.FileHandler;
import java.util.*;
import java.util.stream.Collectors;

public class HostManagerImpl implements HostManager {
    private Map<String, Host> hosts;
    private FileHandler fileHandler;

    public HostManagerImpl(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.hosts = new HashMap<>();
        loadHosts();
    }

    private void loadHosts() {
        List<Host> loadedHosts = fileHandler.loadHosts();
        for (Host host : loadedHosts) {
            hosts.put(host.getId(), host);
        }
    }

    @Override
    public void addHost(Host host) {
        if (hosts.containsKey(host.getId())) {
            throw new IllegalArgumentException("Host with ID " + host.getId() + " already exists.");
        }
        hosts.put(host.getId(), host);
        saveHosts();
    }

    @Override
    public void updateHost(Host host) {
        if (!hosts.containsKey(host.getId())) {
            throw new IllegalArgumentException("Host with ID " + host.getId() + " does not exist.");
        }
        hosts.put(host.getId(), host);
        saveHosts();
    }

    @Override
    public void deleteHost(String hostId) {
        if (!hosts.containsKey(hostId)) {
            throw new IllegalArgumentException("Host with ID " + hostId + " does not exist.");
        }
        hosts.remove(hostId);
        saveHosts();
    }

    @Override
    public Host getHost(String hostId) {
        Host host = hosts.get(hostId);
        if (host == null) {
            throw new IllegalArgumentException("Host with ID " + hostId + " does not exist.");
        }
        return host;
    }

    @Override
    public List<Host> getAllHosts() {
        return new ArrayList<>(hosts.values());
    }

    @Override
    public List<Host> searchHosts(String keyword) {
        final String lowercaseKeyword = keyword.toLowerCase();
        return hosts.values().stream()
                .filter(host -> host.getFullName().toLowerCase().contains(lowercaseKeyword) ||
                        host.getId().toLowerCase().contains(lowercaseKeyword) ||
                        host.getContactInformation().toLowerCase().contains(lowercaseKeyword))
                .collect(Collectors.toList());
    }

    private void saveHosts() {
        fileHandler.saveHosts(new ArrayList<>(hosts.values()));
    }
}