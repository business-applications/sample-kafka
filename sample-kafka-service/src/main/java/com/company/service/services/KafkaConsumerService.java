package com.company.service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    SimpMessagingTemplate template;

    @Value("${websocket.topic}")
    public String websocketTopic;

    @Value("${websocket.destinationPrefix}")
    public String websocketDestinationPrefix;

    @KafkaListener(topics = "${kafka.topic}")
    public void consume(@Payload String message) {
        template.convertAndSend(websocketDestinationPrefix + "/" + websocketTopic,
                                message);
    }
}
