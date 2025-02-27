package ru.innotech.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.innotech.dto.KafkaDto;
import ru.innotech.dto.TaskDto;
import ru.innotech.dto.TaskResponseDto;
import ru.innotech.entity.Task;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "status", source = "status")
    Task toEntityTask(TaskDto taskDto);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "status", source = "status")
    TaskDto toTaskDto(Task task);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "status", source = "status")
    TaskResponseDto toTaskResponseDto(Task task);

    KafkaDto toKafkaDto(Task task);
}
