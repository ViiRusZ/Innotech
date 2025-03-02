package ru.innotech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.innotech.dto.TaskDto;
import ru.innotech.dto.TaskResponseDto;
import ru.innotech.dto.UpdateDto;
import ru.innotech.entity.Task;
import ru.innotech.entity.TaskStatus;
import ru.innotech.service.TaskService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {TaskController.class})
class TaskControllerTest {

    private final static String URL = "/tasks";
    private final static String PUT_URL = "/tasks/{id}";
    private final static String DELETE_URL = "/tasks/{id}";
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void positiveCreateNewTask() throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .title("Task 1")
                .description("Here task 1")
                .userId(1L)
                .status(TaskStatus.NEW)
                .build();

        TaskResponseDto expectedResponseDto = TaskResponseDto.builder()
                .id(1L)
                .title("Task 1")
                .description("Here task 1")
                .userId(1L)
                .status(TaskStatus.NEW)
                .build();

        when(taskService.createNewTask(taskDto)).thenReturn(expectedResponseDto);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(taskDto)))
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(expectedResponseDto)))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("invalidTaskDto")
    void negativeCreateNewTask(TaskDto taskDto) throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(taskDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void positiveFindTaskById() throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .title("Task 1")
                .description("Here task 1")
                .userId(1L)
                .build();


        when(taskService.getTaskById(1L)).thenReturn(taskDto);

        mockMvc.perform(get(URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(taskDto)));

    }

    @Test
    void positiveDeleteTaskById() throws Exception {

        doNothing().when(taskService).deleteTaskById(1L);

        mockMvc.perform(delete(DELETE_URL, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void positiveUpdateTask() throws Exception {
        UpdateDto updateDto = UpdateDto.builder()
                .title("Task 1")
                .description("Here task 1")
                .userId(1L)
                .taskStatus(TaskStatus.NEW)
                .build();

        TaskResponseDto expectedResponseDto = TaskResponseDto.builder()
                .id(2L)
                .title("Task 2")
                .description("Here task 2")
                .userId(2L)
                .status(TaskStatus.NEW)
                .build();

        when(taskService.updateTask(updateDto, 1L)).thenReturn(expectedResponseDto);

        mockMvc.perform(put(PUT_URL, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(updateDto)))
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(expectedResponseDto)))
                .andExpect(status().isOk());

    }

    @ParameterizedTest
    @MethodSource("invalidUpdateDto")
    void negativeUpdateTask(UpdateDto updateDto) throws Exception {
        mockMvc.perform(put(PUT_URL, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTasks() throws Exception {
        List<Task> taskList = Arrays.asList(
                new Task(1L, "Task 1", "Description 1", 1L, TaskStatus.NEW),
                new Task(2L, "Task 2", "Description 2", 1L, TaskStatus.NEW)
        );

        Page<Task> page = new PageImpl<>(taskList);
        when(taskService.getAllTasks(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(URL)
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(taskList)));
    }

    static Stream<Object[]> invalidUpdateDto() {
        return Stream.of(
                new Object[]{UpdateDto.builder()
                        .description("Here task 1")
                        .userId(1L)
                        .build()},
                new Object[]{UpdateDto.builder()
                        .title("")
                        .description("Here task 1")
                        .userId(1L)
                        .build()},
                new Object[]{UpdateDto.builder()
                        .title("   ")
                        .description("Here task 1")
                        .userId(1L)
                        .build()}
        );
    }

    static Stream<Object[]> invalidTaskDto() {
        return Stream.of(
                new Object[]{TaskDto.builder()
                        .description("Here task 1")
                        .userId(1L)
                        .build()},
                new Object[]{TaskDto.builder()
                        .title("")
                        .description("Here task 1")
                        .userId(1L)
                        .build()},
                new Object[]{TaskDto.builder()
                        .title("Task 1")
                        .description("Here task 1")
                        .userId(0L)
                        .build()},
                new Object[]{TaskDto.builder()
                        .title("Task 1")
                        .description("Here task 1")
                        .userId(null)
                        .build()}
        );
    }
}