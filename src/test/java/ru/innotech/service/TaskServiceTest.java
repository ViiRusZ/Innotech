package ru.innotech.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.innotech.dto.TaskDto;
import ru.innotech.dto.TaskResponseDto;
import ru.innotech.dto.UpdateDto;
import ru.innotech.entity.Task;
import ru.innotech.exception.TaskNotFoundException;
import ru.innotech.mapper.TaskMapper;
import ru.innotech.repository.TaskRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private final static Long TASK_ID = 1L;
    private final static int PAGE_NUMBER = 0;
    private final static int PAGE_SIZE = 2;

    @Mock
    TaskMapper taskMapper;

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskService taskService;

    @Test
    void positiveCreateNewTask() {
        TaskDto newTaskDto = createNewTask();
        TaskResponseDto expectedTaskDto = newTaskResponseDto();
        Task expectedTask = newTaskEntity();

        when(taskMapper.toEntityTask(newTaskDto)).thenReturn(expectedTask);
        when(taskRepository.save(expectedTask)).thenReturn(expectedTask);
        when(taskMapper.toTaskResponseDto(expectedTask)).thenReturn(expectedTaskDto);

        TaskResponseDto result = taskService.createNewTask(newTaskDto);

        verify(taskMapper, times(1)).toEntityTask(newTaskDto);
        verify(taskRepository, times(1)).save(expectedTask);
        verify(taskMapper, times(1)).toTaskResponseDto(expectedTask);

        assertEquals(expectedTaskDto, result);
    }

    @Test
    void negativeCreateNewTask() {
        TaskDto newTaskDto = createNewTask();
        Task expectedTask = newTaskEntity();

        when(taskMapper.toEntityTask(newTaskDto)).thenReturn(expectedTask);
        when(taskRepository.save(expectedTask)).thenThrow(new RuntimeException("Ошибка при сохранении задачи"));

        assertThrows(RuntimeException.class, () -> taskService.createNewTask(newTaskDto));

        verify(taskMapper, times(1)).toEntityTask(newTaskDto);
        verify(taskRepository, times(1)).save(expectedTask);
        verify(taskMapper, times(0)).toTaskResponseDto(expectedTask);
    }

    @Test
    void positiveGetTaskById() {
        Task expectedTask = newTaskEntity();
        TaskDto newTaskDto = createNewTask();

        when(taskRepository.findTaskById(TASK_ID)).thenReturn(Optional.of(expectedTask));
        when(taskMapper.toTaskDto(expectedTask)).thenReturn(newTaskDto);

        TaskDto result = taskService.getTaskById(TASK_ID);

        verify(taskRepository, times(1)).findTaskById(TASK_ID);
        verify(taskMapper, times(1)).toTaskDto(expectedTask);
        assertEquals(newTaskDto, result);
    }

    @Test
    void negativeGetTaskById() {
        Task expectedTask = newTaskEntity();
        String message = "Task with id: " + TASK_ID + " was not found";

        when(taskRepository.findTaskById(TASK_ID)).thenThrow(new EntityNotFoundException(message));

        Exception ex = assertThrows(EntityNotFoundException.class, () -> taskService.getTaskById(TASK_ID));

        verify(taskRepository, times(1)).findTaskById(TASK_ID);
        verify(taskMapper, times(0)).toTaskDto(expectedTask);
        assertEquals(message, ex.getMessage());
    }

    @Test
    void positiveDeleteTaskById() {
        when(taskRepository.deleteTaskById(TASK_ID)).thenReturn(1);

        taskService.deleteTaskById(TASK_ID);

        verify(taskRepository, times(1)).deleteTaskById(TASK_ID);
    }

    @Test
    void negativeDeleteTaskById() {
        String messageException = "Task with id: " + TASK_ID + " not found";

        when(taskRepository.deleteTaskById(TASK_ID)).thenReturn(0);

        Exception ex = assertThrows(TaskNotFoundException.class, () -> taskService.deleteTaskById(TASK_ID));

        verify(taskRepository, times(1)).deleteTaskById(TASK_ID);
        assertEquals(messageException, ex.getMessage());
    }

    @Test
    void positiveUpdateTask() {
        UpdateDto updateTaskDto = createTaskUpdateDto();

        TaskResponseDto expectedTaskResponseDto = TaskResponseDto.builder().id(TASK_ID)
                .title("Вторая задача")
                .description("Тут вторая задача")
                .userId(2L)
                .build();

        Task updatedTask = new Task(TASK_ID, "Вторая задача", "Тут вторая задача", 2L);

        when(taskRepository.updateTaskById(TASK_ID, updateTaskDto.getTitle(), updateTaskDto.getDescription(), updateTaskDto.getUserId())).thenReturn(Optional.of(updatedTask));
        when(taskMapper.toTaskResponseDto(updatedTask)).thenReturn(expectedTaskResponseDto);

        TaskResponseDto result = taskService.updateTask(updateTaskDto, TASK_ID);

        verify(taskRepository, times(1)).updateTaskById(TASK_ID, updateTaskDto.getTitle(), updateTaskDto.getDescription(), updateTaskDto.getUserId());
        verify(taskMapper, times(1)).toTaskResponseDto(updatedTask);

        assertEquals(result, expectedTaskResponseDto);
    }

    @Test
    void negativeUpdateTask() {
        String messageException = "Task with id: " + TASK_ID + " not found";
        UpdateDto updateTaskDto = createTaskUpdateDto();

        when(taskRepository.updateTaskById(TASK_ID, updateTaskDto.getTitle(), updateTaskDto.getDescription(), updateTaskDto.getUserId())).thenReturn(Optional.empty());

        Exception ex = assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(updateTaskDto, TASK_ID));

        verify(taskRepository, times(1)).updateTaskById(TASK_ID, updateTaskDto.getTitle(), updateTaskDto.getDescription(), updateTaskDto.getUserId());

        assertEquals(messageException, ex.getMessage());
    }

    @Test
    void positiveGetAllTasks() {
        Task task1 = new Task(1L, "Задача 1", "Описание 1", 1L);
        Task task2 = new Task(2L, "Задача 2", "Описание 2", 1L);
        List<Task> tasks = Arrays.asList(task1, task2);
        Page<Task> page = new PageImpl<>(tasks, PageRequest.of(PAGE_NUMBER, PAGE_SIZE), tasks.size());

        when(taskRepository.findAllTasks(any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Page<Task> result = taskService.getAllTasks(pageable);

        verify(taskRepository, times(1)).findAllTasks(pageable);

        assertEquals(page, result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Задача 1", result.getContent().get(0).getTitle());
        assertEquals("Задача 2", result.getContent().get(1).getTitle());
    }

    @Test
    void negativeGetAllTasks() {
        Page<Task> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(PAGE_NUMBER, PAGE_SIZE), 0);

        when(taskRepository.findAllTasks(any(Pageable.class))).thenReturn(emptyPage);

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Page<Task> result = taskService.getAllTasks(pageable);

        verify(taskRepository, times(1)).findAllTasks(pageable);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    private TaskDto createNewTask() {
        return TaskDto.builder().title("Первая задача").description("Первая задача для тестов").userId(1L).build();
    }

    private UpdateDto createTaskUpdateDto() {
        return UpdateDto.builder().title("Вторая задача").description("Тут вторая задача").userId(2L).build();
    }

    private TaskResponseDto newTaskResponseDto() {
        return TaskResponseDto.builder().title("Первая задача").description("Первая задача для тестов").userId(1L).build();
    }

    private Task newTaskEntity() {
        return new Task(TASK_ID, "Первая задача", "Первая задача для тестов", 1L);
    }

}