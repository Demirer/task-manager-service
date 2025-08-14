import com.taskmanager.TaskManagerApplication;
import com.taskmanager.entity.TaskList;
import com.taskmanager.entity.TaskListRepository;
import com.taskmanager.entity.TaskRepository;
import com.taskmanager.model.request.TaskListRequest;
import com.taskmanager.model.request.TaskRequest;
import com.taskmanager.model.response.TaskListResponse;
import com.taskmanager.model.response.TaskResponse;
import com.taskmanager.service.TaskManagerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TaskManagerApplication.class)
@Transactional
class TaskManagerServiceIT {

    @Autowired
    private TaskManagerService service;

    @Autowired
    private TaskRepository taskRepository; // or TaskListRepository

    @Autowired
    private TaskListRepository taskListRepository;

    @Test
    @DisplayName("Create list should save and return TaskListResponse")
    void testCreateListIntegration() {
        TaskListRequest request = new TaskListRequest();
        request.setName("My List");

        TaskListResponse response = service.createList(request);

        assertNotNull(response.getId());
        assertEquals("My List", response.getName());
    }

    @Test
    @DisplayName("Add task to list should save task and return TaskResponse")
    void testAddTaskToListIntegration() {
        // First create a list
        TaskListRequest listRequest = new TaskListRequest();
        listRequest.setName("List 1");
        TaskListResponse listResponse = service.createList(listRequest);

        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("Task 1");
        taskRequest.setDescription("Desc 1");

        TaskResponse taskResponse = service.addTaskToList(listResponse.getId(), taskRequest);

        assertNotNull(taskResponse.getId());
        assertEquals("Task 1", taskResponse.getName());
        assertEquals("Desc 1", taskResponse.getDescription());
    }

    @Test
    @DisplayName("Fetch TaskList with tasks directly from repository")
    void testGetAllListsWithTasks() {
        // create list + task via service
        TaskListRequest listRequest = new TaskListRequest();
        listRequest.setName("List 1");
        TaskListResponse listResponse = service.createList(listRequest);

        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("Task 1");
        taskRequest.setDescription("Desc 1");
        service.addTaskToList(listResponse.getId(), taskRequest);

        // Directly load entities with tasks
        TaskList listEntity = taskListRepository.findById(listResponse.getId())
                .orElseThrow(() -> new RuntimeException("TaskList not found"));

        // Convert manually to DTO in test
        List<TaskResponse> tasks = listEntity.getTasks().stream()
                .map(t -> new TaskResponse(t.getId(), t.getName(), t.getDescription()))
                .toList();

        assertEquals(1, tasks.size());
        assertEquals("Task 1", tasks.getFirst().getName());
        assertEquals("Desc 1", tasks.getFirst().getDescription());
    }

    @Test
    @DisplayName("Move task from one list to another")
    void testMoveTask() {
        // Create source list
        TaskListRequest sourceRequest = new TaskListRequest();
        sourceRequest.setName("Source List");
        TaskListResponse sourceList = service.createList(sourceRequest);

        // Create destination list
        TaskListRequest destRequest = new TaskListRequest();
        destRequest.setName("Destination List");
        TaskListResponse destList = service.createList(destRequest);

        // Add task to source
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("Task 1");
        taskRequest.setDescription("Desc 1");
        TaskResponse task = service.addTaskToList(sourceList.getId(), taskRequest);

        // Move task
        TaskResponse movedTask = service.moveTask(sourceList.getId(), task.getId(), destList.getId());

        assertEquals(destList.getId(), taskRepository.findById(movedTask.getId())
                .orElseThrow().getTaskList().getId());
    }

    @Test
    @DisplayName("Delete task from list")
    void testDeleteTask() {
        // Create list
        TaskListRequest listRequest = new TaskListRequest();
        listRequest.setName("List 1");
        TaskListResponse list = service.createList(listRequest);

        // Add task
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("Task 1");
        taskRequest.setDescription("Desc 1");
        TaskResponse task = service.addTaskToList(list.getId(), taskRequest);

        // Delete task
        service.deleteTask(list.getId(), task.getId());

        assertFalse(taskRepository.findById(task.getId()).isPresent());
    }
}

