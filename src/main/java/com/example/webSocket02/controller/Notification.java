package com.example.webSocket02.controller;

import com.example.webSocket02.entities.ClientMessageDTO;
import com.example.webSocket02.entities.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Notification {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/notification")
    public void sendNotification(@RequestBody MessageDTO messageDTO){
        simpMessagingTemplate.convertAndSend("/broadcast/broadcast-message", messageDTO);
    }

    @MessageMapping("/client-message")
    @SendTo("/broadcast/broadcast-message")
    public MessageDTO handleMessageWebSocket(@RequestBody ClientMessageDTO clientMessage){
        System.out.println("Arrived something on /app/client-message" + clientMessage.toString());
        return new MessageDTO("Message from: " + clientMessage.getClientName() +
                " - message: " + clientMessage.getClientMsg() + " alert: " + clientMessage.getClientAlert());
    }

}
