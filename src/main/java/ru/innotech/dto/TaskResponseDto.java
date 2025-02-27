package ru.innotech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.innotech.entity.TaskStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskResponseDto {
    private Long id;

    private String title;

    private String description;

    private Long userId;

    private TaskStatus status;
}
