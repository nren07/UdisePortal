package com.udise.portal.service.port_manager;

import com.udise.portal.common.PortAllocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface PortManager {
    int getSeleniumHostPort();
     int getVncHostPort();
     void releasePort(int port);
}
