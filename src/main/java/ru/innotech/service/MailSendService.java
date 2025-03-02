package ru.innotech.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.innotech.configuration.properties.MailSenderProperties;
import ru.innotech.dto.KafkaDto;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(MailSenderProperties.class)
public class MailSendService {
    private final MailSenderProperties mailSenderProperties;
    private final JavaMailSender mailSender;

    public void sendEmail(KafkaDto fromKafka) {
        try {
            String forEmail = String.format("Задача: %s\nОписание: %s\nID пользователя: %d\nСтатус: %s",
                    fromKafka.title(), fromKafka.description(), fromKafka.userId(), fromKafka.status());
            log.info("Sending email: {}, to {}", mailSenderProperties.getSubjectForEmail(), mailSenderProperties.getMailTo());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(mailSenderProperties.getMailTo());
            helper.setSubject(mailSenderProperties.getSubjectForEmail());
            helper.setText(forEmail);

            mailSender.send(message);
        } catch (MessagingException | MailAuthenticationException e) {
            log.error("Failed to send email {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
