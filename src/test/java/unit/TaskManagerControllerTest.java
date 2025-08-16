package unit;

import com.taskmanager.model.request.TaskListRequest;
import com.taskmanager.model.request.TaskRequest;
import com.taskmanager.model.response.TaskListResponse;
import com.taskmanager.model.response.TaskResponse;
import com.taskmanager.service.TaskManagerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class TaskManagerControllerTest {

    @Mock
    private TaskManagerService service;

    @InjectMocks
    private com.taskmanager.rest.TaskManagerController controller;

    private TaskListResponse sampleList;
    private TaskResponse sampleTask;

    @BeforeEach
    void setUp() {
        sampleList = new TaskListResponse();
        sampleList.setId(1L);
        sampleList.setName("Sample List");
        sampleList.setTasks(List.of());

        sampleTask = new TaskResponse();
        sampleTask.setId(1L);
        sampleTask.setName("Sample Task");
        sampleTask.setDescription("Sample Desc");
    }

    @Test
    @DisplayName("GET /lists returns all lists")
    void testGetLists() {
        Mockito.when(service.getAllLists()).thenReturn(List.of(sampleList));

        ResponseEntity<List<TaskListResponse>> response = controller.getLists();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().getFirst().getId()).isEqualTo(sampleList.getId());
        assertThat(response.getBody().getFirst().getName()).isEqualTo(sampleList.getName());
    }

    @Test
    @DisplayName("POST /lists creates a new list")
    void testCreateList() {
        TaskListRequest request = new TaskListRequest();
        request.setName("New List");

        Mockito.when(service.createList(any(TaskListRequest.class))).thenReturn(sampleList);

        ResponseEntity<TaskListResponse> response = controller.createList(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(sampleList.getId());
        assertThat(response.getBody().getName()).isEqualTo(sampleList.getName());
    }

    @Test
    @DisplayName("POST /lists/{listId}/tasks adds a task to list")
    void testAddTaskToList() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("New Task");
        taskRequest.setDescription("Desc");

        Mockito.when(service.addTaskToList(anyLong(), any(TaskRequest.class))).thenReturn(sampleTask);

        ResponseEntity<TaskResponse> response = controller.addTaskToList(1L, taskRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(sampleTask.getId());
        assertThat(response.getBody().getName()).isEqualTo(sampleTask.getName());
        assertThat(response.getBody().getDescription()).isEqualTo(sampleTask.getDescription());
    }

    @Test
    @DisplayName("POST /lists/{listId}/tasks throws 404 if list not found")
    void testAddTaskToNonExistingList() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("Task");
        taskRequest.setDescription("Desc");

        Mockito.when(service.addTaskToList(anyLong(), any(TaskRequest.class)))
                .thenThrow(new EntityNotFoundException("List not found with id 999"));

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> controller.addTaskToList(999L, taskRequest));

        assertThat(ex.getMessage()).contains("List not found");
    }

    @Test
    @DisplayName("PUT /tasks/{taskId} updates a task")
    void testUpdateTask() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("Updated Task");
        taskRequest.setDescription("Updated Desc");

        Mockito.when(service.updateTask(anyLong(), any(TaskRequest.class))).thenReturn(sampleTask);

        ResponseEntity<TaskResponse> response = controller.updateTask(1L, taskRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(sampleTask.getId());
        assertThat(response.getBody().getName()).isEqualTo(sampleTask.getName());
        assertThat(response.getBody().getDescription()).isEqualTo(sampleTask.getDescription());
    }

    @Test
    @DisplayName("DELETE /lists/{listId}/tasks/{taskId} deletes a task")
    void testDeleteTask() {
        Mockito.doNothing().when(service).deleteTask(anyLong(), anyLong());

        ResponseEntity<Void> response = controller.deleteTask(1L, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("DELETE /lists/{listId} deletes a list")
    void testDeleteList() {
        Mockito.doNothing().when(service).deleteList(anyLong());

        ResponseEntity<Void> response = controller.deleteList(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("PUT /lists/{fromListId}/tasks/{taskId}/move/{toListId} moves a task")
    void testMoveTask() {
        TaskResponse movedTask = new TaskResponse();
        movedTask.setId(1L);
        movedTask.setName("Moved Task");
        movedTask.setDescription("Moved Desc");

        Mockito.when(service.moveTask(anyLong(), anyLong(), anyLong())).thenReturn(movedTask);

        ResponseEntity<TaskResponse> response = controller.moveTask(1L, 1L, 2L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(movedTask.getId());
        assertThat(response.getBody().getName()).isEqualTo(movedTask.getName());
        assertThat(response.getBody().getDescription()).isEqualTo(movedTask.getDescription());
    }
}
