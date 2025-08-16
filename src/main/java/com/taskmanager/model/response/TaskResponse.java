package com.taskmanager.model.response;

import com.taskmanager.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a response DTO for {@link Task} entities.
 * Contains the task's ID, name, and description.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    /**
     * Unique identifier of the task.
     */
    private Long id;

    /**
     * Name of the task.
     */
    private String name;

    /**
     * Description of the task.
     */
    private String description;

    /**
     * Converts a {@link Task} entity to a {@link TaskResponse} DTO.
     * This allows for safe exposure of task data in API responses.
     *
     * @param task the {@link Task} entity to convert
     * @return a {@link TaskResponse} DTO representing the task
     */
    public static TaskResponse from(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .build();
    }
}
