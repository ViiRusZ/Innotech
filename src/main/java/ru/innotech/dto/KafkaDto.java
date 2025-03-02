package ru.innotech.dto;

import ru.innotech.entity.TaskStatus;

public record KafkaDto(String title, String description, Long userId, TaskStatus status) {
}
