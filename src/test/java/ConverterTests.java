import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskList;
import com.taskmanager.model.response.TaskListResponse;
import com.taskmanager.model.response.TaskResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterTests {

    @Test
    @DisplayName("Convert Task entity to TaskResponse")
    void testTaskResponseFrom() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setDescription("Test Description");

        TaskResponse response = TaskResponse.from(task);

        assertEquals(task.getId(), response.getId());
        assertEquals(task.getName(), response.getName());
        assertEquals(task.getDescription(), response.getDescription());
    }

    @Test
    @DisplayName("Convert TaskList entity to TaskListResponse including tasks")
    void testTaskListResponseFrom() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("Task 1");
        task1.setDescription("Description 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setName("Task 2");
        task2.setDescription("Description 2");

        TaskList list = new TaskList();
        list.setId(100L);
        list.setName("My List");
        list.setTasks(List.of(task1, task2));

        TaskListResponse response = TaskListResponse.from(list);

        assertEquals(list.getId(), response.getId());
        assertEquals(list.getName(), response.getName());
        assertEquals(list.getTasks().size(), response.getTasks().size());

        // Verify each task is converted
        assertEquals(task1.getId(), response.getTasks().getFirst().getId());
        assertEquals(task1.getName(), response.getTasks().get(0).getName());
        assertEquals(task1.getDescription(), response.getTasks().get(0).getDescription());

        assertEquals(task2.getId(), response.getTasks().get(1).getId());
        assertEquals(task2.getName(), response.getTasks().get(1).getName());
        assertEquals(task2.getDescription(), response.getTasks().get(1).getDescription());
    }
}
