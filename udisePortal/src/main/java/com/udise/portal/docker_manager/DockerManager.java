package com.udise.portal.docker_manager;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.udise.portal.vo.docker.DockerVo;

import java.io.IOException;

public interface DockerManager {
    public DockerVo createAndStartContainer(Long jobId) throws IOException, InterruptedException;
    public void stopAndRemoveContainer(String containerId);

    public void waitForContainerReady(String url) throws InterruptedException;
}
