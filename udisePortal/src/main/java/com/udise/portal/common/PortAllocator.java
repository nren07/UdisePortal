package com.udise.portal.common;

import java.io.IOException;
import java.net.ServerSocket;

public final class PortAllocator {
    // Find a free port on the host machine
    public static int findFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        }
    }
}

