package com.taskmanager.rest;

import com.taskmanager.model.request.TaskListRequest;
import com.taskmanager.model.request.TaskRequest;
import com.taskmanager.model.response.TaskListResponse;
import com.taskmanager.model.response.TaskResponse;
import com.taskmanager.service.TaskManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task-manager")
public class TaskManagerController {

    private final TaskManagerService taskManagerService;

    @GetMapping("/lists")
    public ResponseEntity<List<TaskListResponse>> getLists() {
        return ResponseEntity.ok(taskManagerService.getAllLists());
    }

    @PostMapping("/lists")
    public ResponseEntity<TaskListResponse> createList(@RequestBody TaskListRequest newList) {
        newList.validate();
        return ResponseEntity.ok(taskManagerService.createList(newList));
    }

    @PostMapping("/lists/{listId}/tasks")
    public ResponseEntity<TaskResponse> addTaskToList(@PathVariable("listId") Long listId, @RequestBody TaskRequest newTask) {
        newTask.validate();
        return ResponseEntity.ok(taskManagerService.addTaskToList(listId, newTask));
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable("taskId") Long taskId,
                                                   @RequestBody TaskRequest updatedTask) {
        updatedTask.validate();
        return ResponseEntity.ok(taskManagerService.updateTask(taskId, updatedTask));
    }

    @DeleteMapping("/lists/{listId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("listId") Long listId, @PathVariable("taskId") Long taskId) {
        taskManagerService.deleteTask(listId, taskId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lists/{listId}")
    public ResponseEntity<Void> deleteList(@PathVariable("listId") Long listId) {
        taskManagerService.deleteList(listId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/lists/{fromListId}/tasks/{taskId}/move/{toListId}")
    public ResponseEntity<TaskResponse> moveTask(@PathVariable("fromListId") Long fromListId,
                                                 @PathVariable("taskId") Long taskId,
                                                 @PathVariable("toListId") Long toListId) {
        return ResponseEntity.ok(taskManagerService.moveTask(fromListId, taskId, toListId));
    }
}