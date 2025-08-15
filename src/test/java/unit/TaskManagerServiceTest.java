package unit;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskList;
import com.taskmanager.entity.TaskListRepository;
import com.taskmanager.entity.TaskRepository;
import com.taskmanager.model.request.TaskListRequest;
import com.taskmanager.model.request.TaskRequest;
import com.taskmanager.model.response.TaskListResponse;
import com.taskmanager.model.response.TaskResponse;
import com.taskmanager.service.TaskManagerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskManagerServiceTest {

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
    @DisplayName("Create list should call repository and return correct response")
    void createListTest() {
        TaskListRequest request = new TaskListRequest("My List");
        TaskList savedList = new TaskList();
        savedList.setId(1L);
        savedList.setName("My List");

        when(taskListRepository.save(any(TaskList.class))).thenReturn(savedList);

        TaskListResponse response = service.createList(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("My List", response.getName());
        verify(taskListRepository, times(1)).save(any(TaskList.class));
    }

    @Test
    @DisplayName("Add task to list should save task and return response")
    void addTaskToListTest() {
        TaskList list = new TaskList();
        list.setId(1L);
        when(taskListRepository.findById(1L)).thenReturn(Optional.of(list));

        Task task = new Task();
        task.setId(100L);
        task.setName("Task 1");
        task.setDescription("Desc 1");
        task.setTaskList(list);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskRequest request = new TaskRequest("Task 1", "Desc 1");
        TaskResponse response = service.addTaskToList(1L, request);

        assertEquals(100L, response.getId());
        assertEquals("Task 1", response.getName());
        assertEquals("Desc 1", response.getDescription());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Update task should save updated task")
    void updateTaskTest() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Old");
        task.setDescription("Old Desc");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskRequest update = new TaskRequest("New", "New Desc");
        TaskResponse response = service.updateTask(1L, update);

        assertEquals("New", response.getName());
        assertEquals("New Desc", response.getDescription());
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Delete task should remove task if belongs to list")
    void deleteTaskTest() {
        TaskList list = new TaskList(); list.setId(1L);
        Task task = new Task(); task.setId(2L); task.setTaskList(list);

        when(taskListRepository.findById(1L)).thenReturn(Optional.of(list));
        when(taskRepository.findById(2L)).thenReturn(Optional.of(task));

        assertDoesNotThrow(() -> service.deleteTask(1L, 2L));
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    @DisplayName("Delete task throws exception if task not in list")
    void deleteTaskInvalidListTest() {
        TaskList list = new TaskList(); list.setId(1L);
        TaskList otherList = new TaskList(); otherList.setId(999L); // proper different ID
        Task task = new Task(); task.setId(2L); task.setTaskList(otherList); // different list

        when(taskListRepository.findById(1L)).thenReturn(Optional.of(list));
        when(taskRepository.findById(2L)).thenReturn(Optional.of(task));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.deleteTask(1L, 2L));
        assertEquals("Task does not belong to the specified list", ex.getMessage());
    }

    @Test
    @DisplayName("Move task should update task's list")
    void moveTaskTest() {
        TaskList fromList = new TaskList(); fromList.setId(1L);
        TaskList toList = new TaskList(); toList.setId(2L);
        Task task = new Task(); task.setId(3L); task.setTaskList(fromList);

        when(taskListRepository.findById(1L)).thenReturn(Optional.of(fromList));
        when(taskListRepository.findById(2L)).thenReturn(Optional.of(toList));
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        TaskResponse response = service.moveTask(1L, 3L, 2L);
        assertEquals(2L, task.getTaskList().getId());
        assertEquals(task.getId(), response.getId());
    }

    @Test
    @DisplayName("Delete list throws EntityNotFoundException if list not found")
    void deleteListNotFoundTest() {
        when(taskListRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.deleteList(1L));
        assertEquals("List not found with id 1", ex.getMessage());
    }

    @Test
    @DisplayName("Add task to non-existing list throws EntityNotFoundException")
    void addTaskToInvalidListTest() {
        when(taskListRepository.findById(999L)).thenReturn(Optional.empty());
        TaskRequest request = new TaskRequest("Task", "Desc");
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.addTaskToList(999L, request));
        assertEquals("List not found with id 999", ex.getMessage());
    }

    @Test
    @DisplayName("Move task throws exception if task not found")
    void moveTaskTaskNotFoundTest() {
        TaskList list = new TaskList(); list.setId(1L);
        when(taskListRepository.findById(1L)).thenReturn(Optional.of(list));
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.moveTask(1L, 999L, 1L));
        assertEquals("Task not found with id 999", ex.getMessage());
    }

    @Test
    @DisplayName("Move task throws exception if from list not found")
    void moveTaskFromListNotFoundTest() {
        when(taskListRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.moveTask(1L, 1L, 2L));
        assertEquals("List not found with id 1", ex.getMessage());
    }

    @Test
    @DisplayName("Move task throws exception if to list not found")
    void moveTaskToListNotFoundTest() {
        TaskList fromList = new TaskList(); fromList.setId(1L);
        Task task = new Task(); task.setId(2L); task.setTaskList(fromList);

        when(taskListRepository.findById(1L)).thenReturn(Optional.of(fromList));
        when(taskListRepository.findById(2L)).thenReturn(Optional.empty());
        when(taskRepository.findById(2L)).thenReturn(Optional.of(task));

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.moveTask(1L, 2L, 2L));
        assertEquals("List not found with id 2", ex.getMessage());
    }

    @Test
    @DisplayName("Update task throws exception if task not found")
    void updateTaskNotFoundTest() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());
        TaskRequest request = new TaskRequest("New", "Desc");

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.updateTask(999L, request));
        assertEquals("Task not found with id 999", ex.getMessage());
    }
}