package com.udise.portal.vo.docker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

public class DockerVo {
    private String containerId;
    private int hostPort;
    private int vncPort;
    private String containerName;

    public DockerVo() {
    }

    public DockerVo(String containerId, int hostPort, int vncPort, String containerName) {
        this.containerId = containerId;
        this.hostPort = hostPort;
        this.vncPort = vncPort;
        this.containerName = containerName;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public int getVncPort() {
        return vncPort;
    }

    public void setVncPort(int vncPort) {
        this.vncPort = vncPort;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }
}
