package com.springboot.publish.subscribe.messaging.producer.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.publish.subscribe.messaging.dto.notification.MailNotification;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class MailNotificationSerializer implements Serializer<MailNotification> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, MailNotification mailNotification) {
        try {
            if (mailNotification == null){
                System.out.println("Null received at serializing");
                return null;
            }

            return objectMapper.writeValueAsBytes(mailNotification);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing mailNotification to byte[]");
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, MailNotification data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
