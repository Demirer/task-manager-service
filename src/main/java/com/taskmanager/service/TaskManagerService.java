package com.taskmanager.service;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskList;
import com.taskmanager.entity.TaskListRepository;
import com.taskmanager.entity.TaskRepository;
import com.taskmanager.model.request.TaskListRequest;
import com.taskmanager.model.request.TaskRequest;
import com.taskmanager.model.response.TaskListResponse;
import com.taskmanager.model.response.TaskResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service layer for managing task lists and tasks.
 * Handles business logic such as creating, updating, deleting, and moving tasks and task lists.
 */
@Service
@RequiredArgsConstructor
public class TaskManagerService {

    private final TaskListRepository taskListRepository;
    private final TaskRepository taskRepository;

    /**
     * Retrieves all task lists along with their tasks.
     *
     * @return list of {@link TaskListResponse} representing all task lists
     */
    public List<TaskListResponse> getAllLists() {
        return taskListRepository.findAllWithTasks().stream()
                .map(TaskListResponse::from)
                .toList();
    }

    /**
     * Creates a new task list.
     *
     * @param request the {@link TaskListRequest} containing the name of the new list
     * @return the created {@link TaskListResponse}
     */
    @Transactional
    public TaskListResponse createList(TaskListRequest request) {
        TaskList list = new TaskList();
        list.setName(request.getName());
        TaskList saved = taskListRepository.save(list);
        return TaskListResponse.from(saved);
    }

    /**
     * Deletes a task list by its ID.
     *
     * @param listId the ID of the task list to delete
     */
    @Transactional
    public void deleteList(Long listId) {
        taskListRepository.delete(findTaskListById(listId));
    }

    /**
     * Adds a new task to a specific task list.
     *
     * @param listId  the ID of the task list
     * @param request the {@link TaskRequest} containing task details
     * @return the created {@link TaskResponse}
     */
    @Transactional
    public TaskResponse addTaskToList(Long listId, TaskRequest request) {
        TaskList list = findTaskListById(listId);

        Task task = new Task();
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setTaskList(list);

        Task saved = taskRepository.save(task);

        // Add task to list's collection
        list.getTasks().add(saved);

        return TaskResponse.from(saved);
    }

    /**
     * Updates an existing task.
     *
     * @param taskId  the ID of the task to update
     * @param request the {@link TaskRequest} containing updated task details
     * @return the updated {@link TaskResponse}
     */
    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest request) {
        Task task = findTaskById(taskId);
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        return TaskResponse.from(taskRepository.save(task));
    }

    /**
     * Deletes a task from a task list.
     *
     * @param listId the ID of the task list
     * @param taskId the ID of the task to delete
     */
    @Transactional
    public void deleteTask(Long listId, Long taskId) {
        TaskList list = findTaskListById(listId);
        Task task = findTaskById(taskId);

        if (!task.getTaskList().getId().equals(list.getId())) {
            throw new IllegalArgumentException("Task does not belong to the specified list");
        }

        taskRepository.delete(task);
    }

    /**
     * Moves a task from one list to another.
     *
     * @param fromListId the ID of the source task list
     * @param taskId     the ID of the task to move
     * @param toListId   the ID of the target task list
     * @return the moved {@link TaskResponse}
     */
    @Transactional
    public TaskResponse moveTask(Long fromListId, Long taskId, Long toListId) {
        TaskList fromList = findTaskListById(fromListId);
        TaskList toList = findTaskListById(toListId);
        Task task = findTaskById(taskId);

        if (!task.getTaskList().getId().equals(fromList.getId())) {
            throw new IllegalArgumentException("Task does not belong to the source list");
        }

        task.setTaskList(toList);
        return TaskResponse.from(taskRepository.save(task));
    }

    /**
     * Finds a task list by its ID or throws an {@link EntityNotFoundException}.
     *
     * @param id the ID of the task list
     * @return the {@link TaskList}
     */
    private TaskList findTaskListById(Long id) {
        return taskListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("List not found with id " + id));
    }

    /**
     * Finds a task by its ID or throws an {@link EntityNotFoundException}.
     *
     * @param id the ID of the task
     * @return the {@link Task}
     */
    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id " + id));
    }
}

