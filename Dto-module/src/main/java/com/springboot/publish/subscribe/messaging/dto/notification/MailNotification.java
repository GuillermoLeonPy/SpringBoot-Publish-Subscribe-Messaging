package com.springboot.publish.subscribe.messaging.dto.notification;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Data
public class MailNotification {

    private Integer id;
    private String to;
    private String from;
    private String subject;
    private String body;
}
