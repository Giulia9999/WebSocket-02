package com.example.webSocket02.controller;

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
    public void sendNotification(@RequestBody MessageMapping messageDTO){
        simpMessagingTemplate.convertAndSend("/broadcast/broadcast-message", messageDTO);
    }

    //MANDARE MESSAGGIO DAL CLIENT AL SERVER
    //Per inviare sul brocker in questo caso ci vuole /app perchè stiamo inviando dal client al server(quindi sonno messaggi in entrata)
    @MessageMapping("/hello") //Non è un mapping su una chiamata http ma su una WS
    @SendTo("/topic/client-message")
    public SimpleMessageDTO handleMessageWebSocket(@RequestBody MessageFromClientDTO clientMessage){
        System.out.println("Arrived something on /app/hello" + clientMessage.toString());
        return new SimpleMessageDTO("Message from: " + clientMessage.getFrom() + " - " + clientMessage.getText());
    }

}
