package ru.innotech.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateDto {

    @NotBlank(message = "title can`t be blank")
    private String title;

    private String description;

    private Long userId;
}
