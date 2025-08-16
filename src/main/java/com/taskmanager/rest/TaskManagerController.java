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

/**
 * REST controller for managing task lists and tasks.
 * Provides endpoints for creating, retrieving, updating, deleting, and moving tasks and task lists.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task-manager")
public class TaskManagerController {

    private final TaskManagerService taskManagerService;

    /**
     * Retrieves all task lists with their tasks.
     *
     * @return a list of {@link TaskListResponse} representing all task lists
     */
    @GetMapping("/lists")
    public ResponseEntity<List<TaskListResponse>> getLists() {
        return ResponseEntity.ok(taskManagerService.getAllLists());
    }

    /**
     * Creates a new task list.
     *
     * @param newList the {@link TaskListRequest} containing the name of the new list
     * @return the created {@link TaskListResponse}
     */
    @PostMapping("/lists")
    public ResponseEntity<TaskListResponse> createList(@RequestBody TaskListRequest newList) {
        newList.validate();
        return ResponseEntity.ok(taskManagerService.createList(newList));
    }

    /**
     * Adds a new task to an existing task list.
     *
     * @param listId  the ID of the task list
     * @param newTask the {@link TaskRequest} containing task details
     * @return the created {@link TaskResponse}
     */
    @PostMapping("/lists/{listId}/tasks")
    public ResponseEntity<TaskResponse> addTaskToList(@PathVariable("listId") Long listId,
                                                      @RequestBody TaskRequest newTask) {
        newTask.validate();
        return ResponseEntity.ok(taskManagerService.addTaskToList(listId, newTask));
    }

    /**
     * Updates an existing task.
     *
     * @param taskId      the ID of the task to update
     * @param updatedTask the {@link TaskRequest} containing updated task details
     * @return the updated {@link TaskResponse}
     */
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable("taskId") Long taskId,
                                                   @RequestBody TaskRequest updatedTask) {
        updatedTask.validate();
        return ResponseEntity.ok(taskManagerService.updateTask(taskId, updatedTask));
    }

    /**
     * Deletes a task from a task list.
     *
     * @param listId the ID of the task list
     * @param taskId the ID of the task to delete
     * @return an empty {@link ResponseEntity} with HTTP status 204 (No Content)
     */
    @DeleteMapping("/lists/{listId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("listId") Long listId,
                                           @PathVariable("taskId") Long taskId) {
        taskManagerService.deleteTask(listId, taskId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a task list and all its tasks.
     *
     * @param listId the ID of the task list to delete
     * @return an empty {@link ResponseEntity} with HTTP status 204 (No Content)
     */
    @DeleteMapping("/lists/{listId}")
    public ResponseEntity<Void> deleteList(@PathVariable("listId") Long listId) {
        taskManagerService.deleteList(listId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Moves a task from one list to another.
     *
     * @param fromListId the ID of the source task list
     * @param taskId     the ID of the task to move
     * @param toListId   the ID of the target task list
     * @return the moved {@link TaskResponse}
     */
    @PutMapping("/lists/{fromListId}/tasks/{taskId}/move/{toListId}")
    public ResponseEntity<TaskResponse> moveTask(@PathVariable("fromListId") Long fromListId,
                                                 @PathVariable("taskId") Long taskId,
                                                 @PathVariable("toListId") Long toListId) {
        return ResponseEntity.ok(taskManagerService.moveTask(fromListId, taskId, toListId));
    }
}
