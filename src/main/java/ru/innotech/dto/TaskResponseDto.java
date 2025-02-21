package ru.innotech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskResponseDto {
    private Long id;

    private String title;

    private String description;

    private Long userId;
}
