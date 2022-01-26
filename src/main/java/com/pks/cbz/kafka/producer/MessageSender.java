package com.pks.cbz.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Autowired
    private KafkaProducer kafkaProducer;

    public void sendMessage(String message, String type)
    {
        try {
            kafkaProducer.getCbzSource().output().send(MessageBuilder.withPayload(message).build());
            System.out.println("Message Sent");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
