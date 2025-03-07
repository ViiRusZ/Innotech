package ru.innotech.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import ru.innotech.dto.KafkaDto;
import ru.innotech.service.MailSendService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaClientConsumer {
    private final MailSendService mailSendService;

    @Retryable(retryFor = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Payload List<KafkaDto> messages, Acknowledgment ack) {
        try {
            log.info("СООБЩЕНИЕ ПОЛУЧЕНО {}", messages);
            ack.acknowledge();

            messages.forEach(mailSendService::sendEmail);
        } catch (Exception ex) {
            log.error("Ошибка при обработке сообщения: {}", ex.getMessage());
            throw ex;
        }
    }

    @Recover
    public void recover(Exception ex, Object o) {
        log.error("Не удалось получить сообщение из Kafka после всех попыток: {}", o, ex);
    }
}
