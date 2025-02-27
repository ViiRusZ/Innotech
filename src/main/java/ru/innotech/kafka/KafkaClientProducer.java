package ru.innotech.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaClientProducer {

    private final KafkaTemplate kafkaTemplate;

    @Retryable(retryFor = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 3))
    public void send(Object o) {
        try {
            log.info("Отправка сообщения в Kafka: {}", o);
            kafkaTemplate.sendDefault(o);
        } catch (Exception ex) {
            log.error("Ошибка при отправке сообщения в Kafka: {} ", o, ex);
            throw ex;
        }
    }

    @Recover
    public void recover(Exception ex, Object o) {
        log.error("Не удалось отправить сообщение в Kafka после всех попыток: {}", o, ex);
    }
}
