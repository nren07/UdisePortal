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
        portMap.put(51475, false);
        portMap.put(44439, false);
        portMap.put(55564, false);
        portMap.put(51043, false);
        portMap.put(54186, false);
        portMap.put(57012, false);
        portMap.put(55612, false);
        portMap.put(55897, false);
        portMap.put(53765, false);
        portMap.put(42034, false);
        portMap.put(47213, false);
        portMap.put(47385, false);
        portMap.put(43611, false);
        portMap.put(45294, false);
        portMap.put(49167, false);
        portMap.put(44127, false);
        portMap.put(48674, false);
        portMap.put(44052, false);
        portMap.put(50265, false);
        portMap.put(55134, false);
        portMap.put(58678, false);
        portMap.put(52238, false);
        portMap.put(54328, false);
        portMap.put(54503, false);
        portMap.put(47342, false);
        portMap.put(53662, false);
        portMap.put(49415, false);
        portMap.put(46288, false);
        portMap.put(52492, false);
        portMap.put(49613, false);
        portMap.put(58123, false);
        portMap.put(49311, false);
        portMap.put(46972, false);
        portMap.put(40215, false);
        portMap.put(44758, false);
        portMap.put(49031, false);
        portMap.put(42059, false);
        portMap.put(56351, false);
        portMap.put(52511, false);
        portMap.put(43094, false);
        portMap.put(49812, false);
        portMap.put(41603, false);
        portMap.put(46234, false);
        portMap.put(53982, false);
        portMap.put(58196, false);
        portMap.put(45498, false);
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

