package com.pks.cbz.controller;

import java.util.UUID;

import com.pks.cbz.kafka.producer.KafkaProducer;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/bpmn-process")
public class BpmnProcessController
{

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping("/startProcess/{processSignalId}")
    public ResponseEntity<String> startBpmnProcess(@PathVariable("processSignalId") String processSignalId, @RequestBody String variables)
    {
        JSONObject var = new JSONObject(variables);

        String key = UUID.randomUUID().toString();
        var.put("key", key);

        JSONObject signal = new JSONObject();
        signal.put("name", processSignalId);
        signal.put("key", key);
        signal.put("variablesAsMap", var);
        signal.put("ttl", 10000);

        kafkaProducer.getCbzSource().output().send(MessageBuilder.withPayload(signal.toString()).build());
        return new ResponseEntity<>("Message sent successfully to " + processSignalId, new HttpHeaders(), HttpStatus.OK);
    }
}
