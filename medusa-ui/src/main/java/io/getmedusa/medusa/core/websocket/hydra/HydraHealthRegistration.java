package io.getmedusa.medusa.core.websocket.hydra;

import java.util.*;

public class HydraHealthRegistration {

    private int port;
    private final String name;
    private final String secret;
    private Set<String> endpoints = new HashSet<>();
    private Set<String> websockets = new HashSet<>();
    private Set<String> staticResources = new HashSet<>();
    private Map<String, Set<HydraMenuItem>> menuItems = new HashMap<>();

    public HydraHealthRegistration(String name, String secret) {
        this.name = name;
        this.secret = secret;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public Set<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Set<String> endpoints) {
        this.endpoints = endpoints;
    }

    public Set<String> getWebsockets() {
        return websockets;
    }

    public void setWebsockets(Set<String> websockets) {
        this.websockets = websockets;
    }

    public Set<String> getStaticResources() {
        return staticResources;
    }

    public void setStaticResources(Set<String> staticResources) {
        this.staticResources = staticResources;
    }

    public Map<String, Set<HydraMenuItem>> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Map<String, Set<HydraMenuItem>> menuItems) {
        this.menuItems = menuItems;
    }

    public String getSecret() {
        return secret;
    }
}
