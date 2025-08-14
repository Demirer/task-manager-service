package com.taskmanager.rest;

import com.taskmanager.model.request.TaskListRequest;
import com.taskmanager.model.request.TaskRequest;
import com.taskmanager.model.response.TaskListResponse;
import com.taskmanager.model.response.TaskResponse;
import com.taskmanager.service.TaskManagerService;
import com.taskmanager.validation.ValidateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task-manager")
public class TaskManagerController {

    private final TaskManagerService taskManagerService;

    @ValidateRequest
    @GetMapping("/lists")
    public ResponseEntity<List<TaskListResponse>> getLists() {
        return ResponseEntity.ok(taskManagerService.getAllLists());
    }

    @ValidateRequest
    @PostMapping("/lists")
    public ResponseEntity<TaskListResponse> createList(@RequestBody TaskListRequest newList) {
        return ResponseEntity.ok(taskManagerService.createList(newList));
    }

    @ValidateRequest
    @PostMapping("/lists/{listId}/tasks")
    public ResponseEntity<TaskResponse> addTaskToList(@PathVariable Long listId, @RequestBody TaskRequest newTask) {
        return ResponseEntity.ok(taskManagerService.addTaskToList(listId, newTask));
    }

    @ValidateRequest
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId,
                                                   @RequestBody TaskRequest updatedTask) {
        return ResponseEntity.ok(taskManagerService.updateTask(taskId, updatedTask));
    }

    @ValidateRequest
    @DeleteMapping("/lists/{listId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long listId, @PathVariable Long taskId) {
        taskManagerService.deleteTask(listId, taskId);
        return ResponseEntity.noContent().build();
    }

    @ValidateRequest
    @DeleteMapping("/lists/{listId}")
    public ResponseEntity<Void> deleteList(@PathVariable Long listId) {
        taskManagerService.deleteList(listId);
        return ResponseEntity.noContent().build();
    }

    @ValidateRequest
    @PutMapping("/lists/{fromListId}/tasks/{taskId}/move/{toListId}")
    public ResponseEntity<TaskResponse> moveTask(@PathVariable Long fromListId,
                                                 @PathVariable Long taskId,
                                                 @PathVariable Long toListId) {
        return ResponseEntity.ok(taskManagerService.moveTask(fromListId, taskId, toListId));
    }
}