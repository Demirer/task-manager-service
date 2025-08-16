package unit;

import com.taskmanager.model.request.TaskListRequest;
import com.taskmanager.model.request.TaskRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {

    @Test
    @DisplayName("TaskRequest with blank name should throw exception")
    void testBlankTaskName() {
        TaskRequest task = new TaskRequest();
        task.setName("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, task::validate);
        assertEquals("Task name is required", ex.getMessage());
    }

    @Test
    @DisplayName("TaskRequest with blank name should throw exception")
    void testNullTaskName() {
        TaskRequest task = new TaskRequest();
        task.setName(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, task::validate);
        assertEquals("Task name is required", ex.getMessage());
    }

    @Test
    @DisplayName("TaskRequest with name too long should throw exception")
    void testTaskNameTooLong() {
        TaskRequest task = new TaskRequest();
        task.setName("A".repeat(129));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, task::validate);
        assertEquals("Task name must be at most 128 characters", ex.getMessage());
    }

    @Test
    @DisplayName("TaskRequest with blank description should throw exception")
    void testBlankTaskDescription() {
        TaskRequest task = new TaskRequest();
        task.setName("Valid Name");
        task.setDescription("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, task::validate);
        assertEquals("Task description is required", ex.getMessage());
    }

    @Test
    @DisplayName("TaskRequest with null description should throw exception")
    void testNullTaskDescription() {
        TaskRequest task = new TaskRequest();
        task.setName("Valid Name");
        task.setDescription(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, task::validate);
        assertEquals("Task description is required", ex.getMessage());
    }

    @Test
    @DisplayName("TaskRequest with description too long should throw exception")
    void testTaskDescriptionTooLong() {
        TaskRequest task = new TaskRequest();
        task.setName("Valid Name");
        task.setDescription("x".repeat(129));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, task::validate);
        assertEquals("Task description must be at most 128 characters", ex.getMessage());
    }

    @Test
    @DisplayName("TaskListRequest with blank name should throw exception")
    void testBlankListName() {
        TaskListRequest list = new TaskListRequest();
        list.setName("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, list::validate);
        assertEquals("List name is required", ex.getMessage());
    }

    @Test
    @DisplayName("TaskListRequest with name too long should throw exception")
    void testListNameTooLong() {
        TaskListRequest list = new TaskListRequest();
        list.setName("x".repeat(129));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, list::validate);
        assertEquals("List name must be at most 128 characters", ex.getMessage());
    }
}
