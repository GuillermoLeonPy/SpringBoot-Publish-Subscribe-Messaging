package com.springboot.publish.subscribe.messaging.producer.application;

import com.springboot.publish.subscribe.messaging.dto.notification.MailNotification;
import com.springboot.publish.subscribe.messaging.producer.configuration.Config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
@ComponentScan(basePackages = {"com.springboot.publish.subscribe.messaging.producer"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    @Bean
    CommandLineRunner publisher(KafkaTemplate<String,MailNotification> kafkaTemplate, MailNotification mailNotification){
        return args -> {
            int id=0;
            while (true){
                mailNotification.setId(id++);
                kafkaTemplate.send(Config.TOPIC_NAME,mailNotification);
                Thread.sleep(5000);
            }
        };
    }


}
