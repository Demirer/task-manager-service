package unit;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskList;
import com.taskmanager.model.response.TaskListResponse;
import com.taskmanager.model.response.TaskResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterTest {

    @Test
    @DisplayName("Convert Task entity to TaskResponse")
    void testTaskResponseFrom() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setDescription("Test Description");

        TaskResponse taskResponse = TaskResponse.from(task);

        assertEquals(task.getId(), taskResponse.getId());
        assertEquals(task.getName(), taskResponse.getName());
        assertEquals(task.getDescription(), taskResponse.getDescription());
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

        TaskListResponse taskListResponse = TaskListResponse.from(list);

        assertEquals(list.getId(), taskListResponse.getId());
        assertEquals(list.getName(), taskListResponse.getName());
        assertEquals(list.getTasks().size(), taskListResponse.getTasks().size());

        // Verify each task is converted, system should convert and map every task via converter.
        assertEquals(task1.getId(), taskListResponse.getTasks().getFirst().getId());
        assertEquals(task1.getName(), taskListResponse.getTasks().get(0).getName());
        assertEquals(task1.getDescription(), taskListResponse.getTasks().get(0).getDescription());

        assertEquals(task2.getId(), taskListResponse.getTasks().get(1).getId());
        assertEquals(task2.getName(), taskListResponse.getTasks().get(1).getName());
        assertEquals(task2.getDescription(), taskListResponse.getTasks().get(1).getDescription());
    }
}
