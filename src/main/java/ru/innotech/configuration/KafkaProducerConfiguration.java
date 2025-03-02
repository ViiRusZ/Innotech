package ru.innotech.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.innotech.dto.KafkaDto;
import ru.innotech.kafka.KafkaClientProducer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfiguration {
    @Value("${kafka.bootstrap-servers}")
    private String server;

    @Value("${kafka.topic}")
    private String emailClientTopic;

    @Bean(name = "emailKafkaTemplate")
    public KafkaTemplate<String, KafkaDto> kafkaTemplate(ProducerFactory<String, KafkaDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "kafka.producer.enable", havingValue = "true", matchIfMissing = true)
    public KafkaClientProducer producerClient(@Qualifier("emailKafkaTemplate") KafkaTemplate template) {
        template.setDefaultTopic(emailClientTopic);
        return new KafkaClientProducer(template);
    }

    @Bean
    public ProducerFactory<String, KafkaDto> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }
}
