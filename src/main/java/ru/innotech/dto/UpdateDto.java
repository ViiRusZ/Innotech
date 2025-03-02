package ru.innotech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.innotech.entity.TaskStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateDto {

    @NotBlank(message = "title can`t be blank")
    private String title;

    private String description;

    private Long userId;

    @JsonProperty("status")
    @NotNull(message = "status can`t be blank")
    private TaskStatus taskStatus;
}
