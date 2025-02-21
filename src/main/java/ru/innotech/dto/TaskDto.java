package ru.innotech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskDto {
    @NotBlank(message = "title can`t be blank")
    @Length(min = 1, max = 128, message = "title must be between 1 and 128 characters")
    private String title;

    private String description;

    @Positive(message = "user_id must start from 1")
    @NotNull
    private Long userId;
}
