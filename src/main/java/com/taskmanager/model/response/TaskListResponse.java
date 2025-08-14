package com.taskmanager.model.response;

import com.taskmanager.entity.TaskList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskListResponse {
    private Long id;
    private String name;
    private List<TaskResponse> tasks;

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
