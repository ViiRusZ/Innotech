package ru.innotech.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.innotech.aspect.annotation.AspectAfterReturning;
import ru.innotech.aspect.annotation.AspectAfterThrowing;
import ru.innotech.aspect.annotation.LoggingAspectAfterMethod;
import ru.innotech.aspect.annotation.LoggingAspectBeforeMethod;
import ru.innotech.dto.KafkaDto;
import ru.innotech.dto.TaskDto;
import ru.innotech.dto.TaskResponseDto;
import ru.innotech.dto.UpdateDto;
import ru.innotech.entity.Task;
import ru.innotech.exception.TaskNotFoundException;
import ru.innotech.kafka.KafkaClientProducer;
import ru.innotech.mapper.TaskMapper;
import ru.innotech.repository.TaskRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final KafkaClientProducer kafkaClientProducer;

    @Transactional
    @AspectAfterReturning
    public TaskResponseDto createNewTask(TaskDto taskDto) {
        Task newTask = taskMapper.toEntityTask(taskDto);
        return taskMapper.toTaskResponseDto(taskRepository.save(newTask));
    }

    @LoggingAspectBeforeMethod
    @LoggingAspectAfterMethod
    @AspectAfterReturning
    public TaskDto getTaskById(Long taskId) {
        Task task = taskRepository.findTaskById(taskId).orElseThrow(() -> new EntityNotFoundException("Task with id: " + taskId + " was not found"));
        return taskMapper.toTaskDto(task);
    }

    @AspectAfterThrowing
    @Transactional
    public void deleteTaskById(Long taskId) {
        int deletedTasks = taskRepository.deleteTaskById(taskId);
        if (deletedTasks == 0) {
            throw new TaskNotFoundException(taskId);
        }
    }

    @AspectAfterThrowing
    @Transactional
    @AspectAfterReturning
    public TaskResponseDto updateTask(UpdateDto updateDto, Long id) {
        Task taskUpdated = taskRepository.updateTaskById(id, updateDto.getTitle(),
                updateDto.getDescription(),
                updateDto.getUserId(),
                updateDto.getTaskStatus().toString()).orElseThrow(() ->
                new EntityNotFoundException(String.format("Task not found with ID:%d", id)));

        KafkaDto forKafka = taskMapper.toKafkaDto(taskUpdated);
        kafkaClientProducer.send(forKafka);

        return taskMapper.toTaskResponseDto(taskUpdated);
    }

    @LoggingAspectBeforeMethod
    @AspectAfterReturning
    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAllTasks(pageable);
    }
}
