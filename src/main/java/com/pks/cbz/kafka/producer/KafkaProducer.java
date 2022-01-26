package com.pks.cbz.kafka.producer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@EnableBinding(Source.class)
public class KafkaProducer 
{
    private Source cbzSource;

    public KafkaProducer(Source cbzSource)
    {
        super();
        this.cbzSource = cbzSource;
    }

    public void setCbzSource(Source cbzSource)
    {
        this.cbzSource = cbzSource;
    }

    public Source getCbzSource()
    {
        return this.cbzSource;
    }
}
