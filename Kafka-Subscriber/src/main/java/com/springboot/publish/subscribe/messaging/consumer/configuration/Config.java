package com.springboot.publish.subscribe.messaging.consumer.configuration;

import com.springboot.publish.subscribe.messaging.consumer.deserializers.MailNotificationDeserializer;
import com.springboot.publish.subscribe.messaging.dto.notification.MailNotification;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableKafka
public class Config {
    public static final String TOPIC_NAME = "ewallet.notification.email";
    public static final String GROUP_ID = "ewallet";

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
    public ConsumerFactory<String, MailNotification> consumerFactory(){
        Map<String,Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, springApplicationName);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MailNotificationDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MailNotification>
    kafkaListenerContainerFactory(ConsumerFactory<String, MailNotification> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, MailNotification> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @KafkaListener(topics = {TOPIC_NAME}, groupId = GROUP_ID)
    public void listener(MailNotification message){
        System.out.println("message received:" + message);

    }

}
