package com.udise.portal.service.port_manager.impl;

import com.udise.portal.common.PortAllocator;
import com.udise.portal.service.port_manager.PortManager;
import org.springframework.stereotype.Service;

@Service
public class PortManagerImpl implements PortManager {
    private final PortAllocator portAllocator;
    private int seleniumHostPort;
    private int vncHostPort;
    public PortManagerImpl(PortAllocator portAllocator) {
        this.portAllocator = portAllocator;
    }

    public void allocateVNCport() {
        // Dynamically allocate free ports on the host
        for (Integer port : portAllocator.getPortMap().keySet()) {
            if (Boolean.FALSE.equals(portAllocator.getPortMap().get(port)) && portAllocator.isPortAvailable(port)) {
                vncHostPort = port;
                portAllocator.allocatePort(port);
                break;
            }
        }
    }
    public void allocateSeleniumPort(){
        for (Integer port : portAllocator.getPortMap().keySet()) {
            if (Boolean.FALSE.equals(portAllocator.getPortMap().get(port)) && portAllocator.isPortAvailable(port)) {
                seleniumHostPort = port;
                portAllocator.allocatePort(port);
                break;
            }
        }
    }

    public void releasePort(int port){
        PortAllocator.getPortMap().put(port,false);
    }

    public int getSeleniumHostPort() {
        allocateSeleniumPort();
        return seleniumHostPort;
    }

    public int getVncHostPort() {
        allocateVNCport();
        return vncHostPort;
    }
}
