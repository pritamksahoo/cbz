package com.pks.cbz.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaPredicateUtil
{
    @Autowired
    private ObjectMapper objectMapper;

    public boolean createPredicate(byte[] payload, String key, String value)
    {
        try {
            return value.equals(objectMapper.readValue(payload, JSONObject.class).getString(key));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
