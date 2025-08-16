package com.taskmanager.model.response;

import com.taskmanager.entity.TaskList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Represents a response DTO for {@link TaskList} entities.
 * Contains the task list's ID, name, and its associated {@link TaskResponse} objects.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskListResponse {

    /**
     * Unique identifier of the task list.
     */
    private Long id;

    /**
     * Name of the task list.
     */
    private String name;

    /**
     * The tasks that belong to this task list.
     */
    private List<TaskResponse> tasks;

    /**
     * Converts a {@link TaskList} entity to a {@link TaskListResponse} DTO.
     * Maps each {@link com.taskmanager.entity.Task} in the task list to a {@link TaskResponse}.
     *
     * @param list the {@link TaskList} entity to convert
     * @return a {@link TaskListResponse} DTO representing the task list and its tasks
     */
    public static TaskListResponse from(TaskList list) {
        List<TaskResponse> tasks = list.getTasks().stream()
                .map(TaskResponse::from)
                .toList();
        return TaskListResponse.builder()
                .id(list.getId())
                .name(list.getName())
                .tasks(tasks)
                .build();
    }
}