package com.udise.portal.service.docker_manager;

import com.udise.portal.vo.docker.DockerVo;

import java.io.IOException;

public interface DockerManager {
    public DockerVo createAndStartContainer(Long jobId) throws IOException, InterruptedException;
    public void stopAndRemoveContainer(String containerId,DockerVo obj);

    public void waitForContainerReady(String url) throws InterruptedException, IOException;
}
