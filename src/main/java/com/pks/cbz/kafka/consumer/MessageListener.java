package com.pks.cbz.kafka.consumer;

// import com.fasterxml.jackson.databind.ObjectMapper;
import com.pks.cbz.kafka.producer.MessageSender;
// import org.json.JSONObject;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@EnableBinding(Sink.class)
public class MessageListener {
    @Autowired
    private MessageSender messageSender;

    private final String KAFKA_CONNECT_ZEEBE_SINK_GLOBAL_REF = "ackFromApp";

    // @StreamListener(target = Sink.INPUT, condition = "headers['type'] == 'PizzaOrderEvent'")
    @StreamListener(target = Sink.INPUT)
    @Transactional
    public void placePizzaOrder(@Payload JSONObject message)
    {
        try {
            System.out.println("og payload - " + message);
            JSONObject variables = message.getJSONObject("variablesAsMap");

            JSONObject response = new JSONObject();
            response.put("name", KAFKA_CONNECT_ZEEBE_SINK_GLOBAL_REF);
            response.put("key", variables.getString("key"));
            response.put("ttl", 10000);

            System.out.println("respJson - " + response.toString());

            messageSender.sendMessage(response.toString(), "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
