import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskList;
import com.taskmanager.entity.TaskListRepository;
import com.taskmanager.entity.TaskRepository;
import com.taskmanager.model.request.TaskListRequest;
import com.taskmanager.model.request.TaskRequest;
import com.taskmanager.model.response.TaskListResponse;
import com.taskmanager.service.TaskManagerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskManagerServiceTests {

    private TaskListRepository taskListRepository;
    private TaskRepository taskRepository;
    private TaskManagerService service;

    @BeforeEach
    void setUp() {
        taskListRepository = mock(TaskListRepository.class);
        taskRepository = mock(TaskRepository.class);
        service = new TaskManagerService(taskListRepository, taskRepository);
    }

    @Test
    @DisplayName("Delete list should throw if list not found")
    void testDeleteListNotFound() {
        when(taskListRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.deleteList(1L));
        assertEquals("List not found with id 1", ex.getMessage());
    }

    @Test
    @DisplayName("Move task should throw if task does not belong to source list")
    void testMoveTaskInvalidSource() {
        TaskList fromList = new TaskList(); fromList.setId(1L);
        TaskList toList = new TaskList(); toList.setId(2L);

        TaskList differentList = new TaskList();
        differentList.setId(99L); // ID not equal to fromList

        Task task = new Task();
        task.setId(3L);
        task.setTaskList(differentList); // Different list

        when(taskListRepository.findById(1L)).thenReturn(Optional.of(fromList));
        when(taskListRepository.findById(2L)).thenReturn(Optional.of(toList));
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.moveTask(1L, 3L, 2L));
        assertEquals("Task does not belong to the source list", ex.getMessage());
    }

    @Test
    @DisplayName("Adding a task to a non-existing list should throw EntityNotFoundException with proper message")
    void addTaskToListInvalidListThrowsException() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("Task 1");
        taskRequest.setDescription("Desc 1");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                service.addTaskToList(999L, taskRequest) // 999L does not exist
        );

        assertEquals("List not found with id 999", exception.getMessage());
    }

    @Test
    @DisplayName("Finding a non-existing task should throw EntityNotFoundException with proper message")
    void findTaskByIdInvalidTaskThrowsException() {
        Long invalidTaskId = 999L; // non-existing task ID

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                service.updateTask(invalidTaskId, new TaskRequest()) // or any method that calls findTaskById
        );

        assertEquals("Task not found with id " + invalidTaskId, exception.getMessage());
    }

}
