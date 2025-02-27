package ru.innotech.dto;

import ru.innotech.entity.TaskStatus;

public record KafkaDto(Long id, TaskStatus status) {
}
