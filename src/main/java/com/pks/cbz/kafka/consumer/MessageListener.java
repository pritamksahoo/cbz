package com.pks.cbz.kafka.consumer;

import java.util.function.Predicate;
// import com.fasterxml.jackson.databind.ObjectMapper;
import com.pks.cbz.kafka.producer.MessageSender;
// import org.json.JSONObject;
import com.pks.cbz.util.KafkaPredicateUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@EnableBinding(Sink.class)
public class MessageListener {
    @Autowired
    private MessageSender messageSender;

    @Autowired
    private KafkaPredicateUtil kafkaPredicateUtil;

    @Value("${kafka-connect-zeebe.dominos.global-ref}")
    private String KAFKA_CONNECT_ZEEBE_SINK_DOMINOS_GLOBAL_REF;

    @Value("${kafka-connect-zeebe.pizzaHut.global-ref}")
    private String KAFKA_CONNECT_ZEEBE_SINK_PIZZAHUT_GLOBAL_REF;

    @StreamListener(target = Sink.INPUT, condition = "@dominosOrderCheck.test(payload)")
    @Transactional
    public void placeDominosOrder(@Payload JSONObject message)
    {
        try {
            System.out.println("og payload - " + message);
            JSONObject variables = message.getJSONObject("variablesAsMap");

            JSONObject response = new JSONObject();
            response.put("name", KAFKA_CONNECT_ZEEBE_SINK_DOMINOS_GLOBAL_REF);
            response.put("key", variables.getString("key"));
            response.put("ttl", 100000);

            System.out.println("respJson - " + response.toString());

            messageSender.sendMessage(response.toString(), "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @StreamListener(target = Sink.INPUT, condition = "@pizzaHutOrderCheck.test(payload)")
    @Transactional
    public void placePizzaHutOrder(@Payload JSONObject message)
    {
        try {
            System.out.println("og payload - " + message);
            JSONObject variables = message.getJSONObject("variablesAsMap");

            JSONObject response = new JSONObject();
            response.put("name", KAFKA_CONNECT_ZEEBE_SINK_PIZZAHUT_GLOBAL_REF);
            response.put("key", variables.getString("key"));
            response.put("ttl", 100000);

            System.out.println("respJson - " + response.toString());

            messageSender.sendMessage(response.toString(), "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @StreamListener(target = Sink.INPUT, condition = "@procTerminateSignalCheck.test(payload)")
    @Transactional
    public void terminateProcessSignal(@Payload JSONObject message)
    {
        try {
            System.out.println("og payload - " + message);
            System.out.println("Process terminates here!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Bean
	public Predicate<byte[]> dominosOrderCheck() {
        return obj -> kafkaPredicateUtil.createPredicate(obj, "elementId", "Send_DominosOrder");
	}

    @Bean
	public Predicate<byte[]> pizzaHutOrderCheck() {
        return obj -> kafkaPredicateUtil.createPredicate(obj, "elementId", "Send_PizzaHutOrder");
	}

    @Bean
	public Predicate<byte[]> procTerminateSignalCheck() {
        return obj -> kafkaPredicateUtil.createPredicate(obj, "elementId", "Send_ProcTerminateSignal");
	}

}
