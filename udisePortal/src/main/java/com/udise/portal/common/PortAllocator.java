package com.udise.portal.common;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

@Component
public final  class PortAllocator {
    // Find a free port on the host machine
    private static final Map<Integer, Boolean> portMap = new HashMap<>();

    public PortAllocator() {
        portMap.put(5000, false);
        portMap.put(6000, false);
        portMap.put(7000, false);
        portMap.put(8000, false);
        portMap.put(9000, false);
        portMap.put(10000, false);
        portMap.put(11000, false);
        portMap.put(12000, false);
        portMap.put(13000, false);
        portMap.put(14000, false);
        portMap.put(15000, false);
        portMap.put(16000, false);
        portMap.put(17000, false);
        portMap.put(18000, false);
        portMap.put(19000, false);
        portMap.put(20000, false);
        portMap.put(21000, false);
        portMap.put(22000, false);
    }

    public static Map<Integer, Boolean> getPortMap() {
        return portMap;
    }

    // Method to set a specific port to true
    public static void allocatePort(int port) {
        if (portMap.containsKey(port)) {
            portMap.put(port, true);
        } else {
            throw new IllegalArgumentException("Port not available in the map: " + port);
        }
    }

    // Method to set a specific port back to false
    public static void releasePort(int port) {
        if (portMap.containsKey(port)) {
            portMap.put(port, false);
        } else {
            throw new IllegalArgumentException("Port not available in the map: " + port);
        }
    }

    public static boolean isPortAvailable(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            socket.setReuseAddress(true);
            return true; // Port is available if no exception occurs
        } catch (BindException e) {
            return false; // Port is busy if BindException is thrown
        } catch (IOException e) {
            throw new RuntimeException("Unexpected error occurred while checking port availability", e);
        }
    }
}

