package ru.innotech.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.innotech.dto.KafkaDto;
import ru.innotech.dto.TaskDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSendService {
    @Value("${spring.mail.mailTo}")
    private String mailTo;
    @Value("${spring.mail.subjectForEmail}")
    private String subjectForEmail;

    private final TaskService taskService;
    private final JavaMailSender mailSender;

    public void sendEmail(KafkaDto fromKafka) {
        try {
            TaskDto taskToSendEmail = taskService.getTaskById(fromKafka.id());

            String forEmail = String.format("Задача: %s\nОписание: %s\nID пользователя: %d\nСтатус: %s",
                    taskToSendEmail.getTitle(), taskToSendEmail.getDescription(), taskToSendEmail.getUserId(), taskToSendEmail.getStatus());
            log.info("Sending email: {}, to {}", subjectForEmail, mailTo);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(mailTo);
            helper.setSubject(subjectForEmail);
            helper.setText(forEmail);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
