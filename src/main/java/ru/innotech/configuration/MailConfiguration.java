package ru.innotech.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ru.innotech.configuration.properties.MailConfigurationProperties;

import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MailConfigurationProperties.class)
public class MailConfiguration {
    private final MailConfigurationProperties mailConfigurationProperties;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfigurationProperties.getHost());
        mailSender.setPort(mailConfigurationProperties.getPort());
        mailSender.setUsername(mailConfigurationProperties.getUsername());
        mailSender.setPassword(mailConfigurationProperties.getPassword());
        mailSender.setJavaMailProperties(getMailProperties());
        return mailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.debug", "true");
        return properties;
    }
}
