package ru.innotech.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "spring.mail")
public class MailConfigurationProperties {
    private String host;
    private int port;
    private String username;
    private String password;
}
