package com.springboot.publish.subscribe.messaging.producer.configuration;

import com.springboot.publish.subscribe.messaging.dto.notification.MailNotification;
import com.springboot.publish.subscribe.messaging.producer.serializers.MailNotificationSerializer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config {
    public static final String TOPIC_NAME = "ewallet.notification.email";

    @Value(value = "${kafka.bootstrap-server}")
    private String kafkaBootstrapServer;

    @Value(value = "${spring.application.name}")
    private String springApplicationName;

    @Bean
    public NewTopic createTopic(){
        Map<String,String> topicConfig = new HashMap<>();
        topicConfig.put(TopicConfig.CLEANUP_POLICY_CONFIG,TopicConfig.CLEANUP_POLICY_DELETE);
        topicConfig.put(TopicConfig.RETENTION_MS_CONFIG,"1800000");//HALF HOUR
        topicConfig.put(TopicConfig.SEGMENT_BYTES_CONFIG,"67108864");//~67 mb
        topicConfig.put(TopicConfig.MAX_MESSAGE_BYTES_CONFIG,"1000012");//1 MB
        return TopicBuilder.name(TOPIC_NAME)
                .configs(topicConfig)
                .build();
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer);
        configs.put(AdminClientConfig.CLIENT_ID_CONFIG, springApplicationName);
        return new KafkaAdmin(configs);
    }

    @Bean
    public MailNotification mailNotification(){
        return MailNotification.builder().
                to("test@test.com")
                .from("info@test.com")
                .subject("Service payment notification")
                .body("Your credit card XXXX XXXX XXXX XXXX payment has been completed, transaction #123456789")
                .build();
    }


    @Bean
    public ProducerFactory<String,MailNotification> producerFactory(){
        Map<String,Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaBootstrapServer);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MailNotificationSerializer.class);
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String,MailNotification> kafkaTemplate(ProducerFactory<String,MailNotification> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }
}
