package integration;

import com.taskmanager.TaskManagerApplication;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskListRepository;
import com.taskmanager.entity.TaskRepository;
import com.taskmanager.model.request.TaskListRequest;
import com.taskmanager.model.request.TaskRequest;
import com.taskmanager.model.response.TaskListResponse;
import com.taskmanager.model.response.TaskResponse;
import com.taskmanager.service.TaskManagerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = TaskManagerApplication.class)
@Transactional
class TaskManagerServiceIT {

    @Autowired
    TaskManagerService service;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskListRepository taskListRepository;

    @Test
    @DisplayName("Create list and retrieve all lists from endpoint")
    void createAndGetListsTest() {
        TaskListRequest request = new TaskListRequest("List 1");
        TaskListResponse created = service.createList(request);

        assertNotNull(created.getId());

        List<TaskListResponse> allLists = service.getAllLists();
        assertEquals(1, allLists.size());
        assertEquals("List 1", allLists.getFirst().getName());
    }

    @Test
    @DisplayName("Add, update and delete task operation in sequence")
    void addUpdateDeleteTaskTest() {
        TaskListResponse taskList = service.createList(new TaskListRequest("List 1"));
        TaskRequest taskRequest = new TaskRequest("Task 1", "Desc 1");

        TaskResponse task = service.addTaskToList(taskList.getId(), taskRequest);

        TaskRequest update = new TaskRequest("Updated", "Updated Description");
        TaskResponse updated = service.updateTask(task.getId(), update);
        assertEquals("Updated", updated.getName());
        assertEquals("Updated Description", updated.getDescription());

        service.deleteTask(taskList.getId(), task.getId());
        assertFalse(taskRepository.findById(task.getId()).isPresent());
    }

    @Test
    @DisplayName("Delete list removes tasks and list")
    void deleteListTest() {
        TaskListResponse list = service.createList(new TaskListRequest("List 1"));
        TaskResponse task = service.addTaskToList(list.getId(), new TaskRequest("Task 1", "Desc 1"));

        service.deleteList(list.getId());

        assertFalse(taskListRepository.findById(list.getId()).isPresent());
        assertFalse(taskRepository.findById(task.getId()).isPresent());
    }

    @Test
    @DisplayName("Moving task between lists")
    void moveTaskTest() {
        TaskListResponse list1 = service.createList(new TaskListRequest("List 1"));
        TaskListResponse list2 = service.createList(new TaskListRequest("List 2"));
        TaskResponse task = service.addTaskToList(list1.getId(), new TaskRequest("Task", "Description"));

        TaskResponse moved = service.moveTask(list1.getId(), task.getId(), list2.getId());

        assertNotNull(moved);

        // Fetch actual entity from DB to check list.
        Task movedEntity = taskRepository.findById(moved.getId()).orElseThrow();
        assertEquals(list2.getId(), movedEntity.getTaskList().getId());
    }

    @Test
    @DisplayName("Operations on non-existing entities throw EntityNotFoundException")
    void nonExistingEntitiesTest() {
        TaskRequest taskRequest = new TaskRequest("Task", "Description");

        assertThrows(EntityNotFoundException.class, () -> service.addTaskToList(999L, taskRequest));
        assertThrows(EntityNotFoundException.class, () -> service.updateTask(999L, taskRequest));
        assertThrows(EntityNotFoundException.class, () -> service.deleteTask(999L, 999L));
        assertThrows(EntityNotFoundException.class, () -> service.deleteList(999L));
        assertThrows(EntityNotFoundException.class, () -> service.moveTask(999L, 999L, 999L));
    }
}
