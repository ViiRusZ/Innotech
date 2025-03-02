package ru.innotech.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.mail.sender")
public class MailSenderProperties {
    private String mailTo;
    private String subjectForEmail;
}
