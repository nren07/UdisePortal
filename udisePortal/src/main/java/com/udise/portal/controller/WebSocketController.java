package com.udise.portal.controller;

import com.udise.portal.vo.job.SocketResponseVo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/emit")
    public void sendEvent() {
        System.out.println("emit event triggered");
            // Send message to the specific user
//        messagingTemplate.convertAndSendToUser(
//                "nren", "/queue/messages", new JobEvent("JOB_STARTED", 1L)
//        );

        messagingTemplate.convertAndSend("/topic/1", new SocketResponseVo("JOB_STARTED", "job started testing"));


    }
}


