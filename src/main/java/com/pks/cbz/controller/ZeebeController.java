package com.pks.cbz.controller;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("/zeebe")
public class ZeebeController
{

    @Value("${kafka-connect-zeebe.connectors.base-url}")
    private String zeebeKafkaConnectorUri;

    @PostMapping("/connectors/deploy/source")
    public ResponseEntity<Object> deploySourceConnector(@RequestParam("sourceFile") MultipartFile file)
    {
        try {
            String sourceStr = new String(file.getBytes());
            JSONObject sourceJson = new JSONObject(sourceStr);
            System.out.println("source connector config - " + sourceJson);

            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(zeebeKafkaConnectorUri);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(sourceStr));

            HttpResponse response = client.execute(httpPost);
            HttpEntity respEtity = response.getEntity();

            return new ResponseEntity<>(IOUtils.toString(respEtity.getContent(), Charset.defaultCharset()), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getLocalizedMessage(), new HttpHeaders(), HttpStatus.OK);
        }
    }
}
