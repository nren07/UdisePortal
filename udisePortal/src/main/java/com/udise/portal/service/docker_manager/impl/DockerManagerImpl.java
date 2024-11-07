package com.udise.portal.service.docker_manager.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.udise.portal.common.PortAllocator;
import com.udise.portal.service.docker_manager.DockerManager;
import com.udise.portal.service.port_manager.PortManager;
import com.udise.portal.vo.docker.DockerVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sound.sampled.Port;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Slf4j
@Component
public class DockerManagerImpl implements DockerManager {

    private static final Logger log = LogManager.getLogger(DockerManagerImpl.class);

    @Autowired
    private DockerClient dockerClient;

    @Autowired
    private PortManager portManager;


    private static final String SELENIUM_IMAGE = "selenium/standalone-chrome"; // Change as needed
    private static final String NETWORK_NAME = "newNetwork"; // Replace with your actual network name

    @Override
    public DockerVo createAndStartContainer(Long jobId ) {
        try{
            // Generate a unique container name for each client
            String uniqueContainerName = "chrome-" + System.currentTimeMillis();

            // Define fixed internal ports for Selenium and VNC
            ExposedPort seleniumPort = ExposedPort.tcp(4444); // Internal Selenium port
            ExposedPort vncPort = ExposedPort.tcp(7900);
            // Internal VNC port
            // Dynamically allocate free ports on the host
            int seleniumHostPort=portManager.getSeleniumHostPort();
            int vncHostPort=portManager.getVncHostPort();


            log.info("Assigning Selenium host port: {} and VNC host port: {}", seleniumHostPort, vncHostPort);

            // Set up port bindings (Host port -> Container port)
            Ports portBindings = new Ports();
            portBindings.bind(seleniumPort, Ports.Binding.bindPort(seleniumHostPort));
            portBindings.bind(vncPort, Ports.Binding.bindPort(vncHostPort));

            // Create the container with appropriate configuration
            CreateContainerResponse container = dockerClient.createContainerCmd(SELENIUM_IMAGE)
                    .withName(uniqueContainerName) // Unique name for the container
                    .withExposedPorts(seleniumPort, vncPort) // Expose internal ports (4444, 7900)
                    .withHostConfig(
                            HostConfig.newHostConfig()
                                    .withPortBindings(portBindings) // Bind host and container port
                    )
                    .withNetworkMode(NETWORK_NAME) // Set the network mode
                    .exec();

            log.info("Container created with ID: {}", container.getId());

            // Start the container
            dockerClient.startContainerCmd(container.getId()).exec();
            return new DockerVo(container.getId(),seleniumHostPort,vncHostPort,uniqueContainerName);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    public void waitForContainerReady(String url) throws InterruptedException,RuntimeException {
        log.info("Waiting for Selenium container to be ready at {}", url);
        int loop_cnt=30;
        while (loop_cnt>=0) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    log.info("Selenium container is ready!");
                    break;
                }
            } catch (IOException e) {
                loop_cnt--;
                log.info("Selenium container not ready yet, retrying...");
            }
            Thread.sleep(1000);  // Wait 2 seconds before polling again
        }
        if(loop_cnt<0){
            throw new RuntimeException("Selenium container not ready to connect");
        }
    }




    @Override
    public void stopAndRemoveContainer(String containerId,DockerVo obj) {
        try{
            // Stop the container
            dockerClient.stopContainerCmd(containerId).exec();
            // Remove the container
            dockerClient.removeContainerCmd(containerId).exec();
            portManager.releasePort(obj.getHostPort());
            portManager.releasePort(obj.getVncPort());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Method to check if a container with a given name exists
    private boolean containerExists(String containerName) {
        // List all containers
        List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
        for (Container container : containers) {
            if (container.getNames()[0].equals("/" + containerName)) {
                return true; // Container with the given name exists
            }
        }
        return false; // Container does not exist
    }
}
