package com.taskmanager.service;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskList;
import com.taskmanager.entity.TaskListRepository;
import com.taskmanager.entity.TaskRepository;
import com.taskmanager.exception.InvalidRequestException;
import com.taskmanager.exception.RecordNotFoundException;
import com.taskmanager.model.request.TaskListRequest;
import com.taskmanager.model.request.TaskRequest;
import com.taskmanager.model.response.TaskListResponse;
import com.taskmanager.model.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskManagerService {

    private final TaskListRepository taskListRepository;
    private final TaskRepository taskRepository;

    public List<TaskListResponse> getAllLists() {
        return taskListRepository.findAllWithTasks().stream()
                .map(TaskListResponse::fromEntity)
                .toList();
    }

    @Transactional
    public TaskListResponse createList(TaskListRequest request) {
        TaskList list = new TaskList();
        list.setName(request.getName());
        TaskList saved = taskListRepository.save(list);
        return TaskListResponse.fromEntity(saved);
    }

    @Transactional
    public void deleteList(Long listId) {
        taskListRepository.delete(findTaskListById(listId));
    }

    @Transactional
    public TaskResponse addTaskToList(Long listId, TaskRequest request) {
        TaskList list = findTaskListById(listId);

        Task task = new Task();
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setTaskList(list);

        return TaskResponse.fromEntity(taskRepository.save(task));
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest request) {
        Task task = findTaskById(taskId);
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        return TaskResponse.fromEntity(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long listId, Long taskId) {
        TaskList list = findTaskListById(listId);
        Task task = findTaskById(taskId);

        if (!task.getTaskList().getId().equals(list.getId())) {
            throw new InvalidRequestException("Task does not belong to the specified list");
        }

        taskRepository.delete(task);
    }

    @Transactional
    public TaskResponse moveTask(Long fromListId, Long taskId, Long toListId) {
        TaskList fromList = findTaskListById(fromListId);
        TaskList toList = findTaskListById(toListId);
        Task task = findTaskById(taskId);

        if (!task.getTaskList().getId().equals(fromList.getId())) {
            throw new InvalidRequestException("Task does not belong to the source list");
        }

        task.setTaskList(toList);
        return TaskResponse.fromEntity(taskRepository.save(task));
    }

    private TaskList findTaskListById(Long id) {
        return taskListRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("List not found with id " + id));
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Task not found with id " + id));
    }
}
