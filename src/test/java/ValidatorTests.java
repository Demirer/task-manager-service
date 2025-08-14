import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.exception.GlobalExceptionHandler;
import com.taskmanager.model.request.TaskListRequest;
import com.taskmanager.model.request.TaskRequest;
import com.taskmanager.rest.TaskManagerController;
import com.taskmanager.service.TaskManagerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ValidatorTests {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc buildMockMvc(TaskManagerController controller) {
        return MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private TaskManagerController createController() {
        TaskManagerService serviceMock = mock(TaskManagerService.class);
        return new TaskManagerController(serviceMock);
    }

    private void performPostRequestAndAssertResults(MockMvc mockMvc, String url, Object body, String expectedMessage) throws Exception {
        var resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        if (expectedMessage == null) {
            resultActions.andExpect(status().isOk());
        } else {
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertInstanceOf(IllegalArgumentException.class, result.getResolvedException()))
                    .andExpect(result -> assertEquals(expectedMessage,
                            requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @Test
    @DisplayName("Request with blank task name should return bad request")
    void testBlankTaskRequest() throws Exception {
        TaskManagerController controller = createController();
        MockMvc mockMvc = buildMockMvc(controller);

        TaskRequest invalidTask = new TaskRequest();
        invalidTask.setName("");

        performPostRequestAndAssertResults(mockMvc, "/api/v1/task-manager/lists/1/tasks",
                invalidTask, "Task name is required");
    }

    @Test
    @DisplayName("Request with too long task name should return bad request")
    void testTaskNameTooLong() throws Exception {
        TaskManagerController controller = createController();
        MockMvc mockMvc = buildMockMvc(controller);

        TaskRequest invalidTask = new TaskRequest();
        invalidTask.setName("A".repeat(129));

        performPostRequestAndAssertResults(mockMvc, "/api/v1/task-manager/lists/1/tasks",
                invalidTask, "Task name must be at most 128 characters");
    }

    @Test
    @DisplayName("Task with description longer than MAX_LENGTH should return bad request")
    void testTaskDescriptionTooLong() throws Exception {
        TaskManagerController controller = createController();
        MockMvc mockMvc = buildMockMvc(controller);

        TaskRequest invalidTask = new TaskRequest();
        invalidTask.setName("Valid Name");
        invalidTask.setDescription("x".repeat(129));

        performPostRequestAndAssertResults(mockMvc, "/api/v1/task-manager/lists/1/tasks",
                invalidTask, "Task description must be at most 128 characters");
    }

    @Test
    @DisplayName("Request with blank list name should return bad request")
    void testBlankListName() throws Exception {
        TaskManagerController controller = createController();
        MockMvc mockMvc = buildMockMvc(controller);

        TaskListRequest invalidList = new TaskListRequest();
        invalidList.setName("");

        performPostRequestAndAssertResults(mockMvc, "/api/v1/task-manager/lists",
                invalidList, "List name is required");
    }

    @Test
    @DisplayName("List name longer than MAX_LENGTH should return bad request")
    void testListNameTooLong() throws Exception {
        TaskManagerController controller = createController();
        MockMvc mockMvc = buildMockMvc(controller);

        TaskListRequest invalidList = new TaskListRequest();
        invalidList.setName("x".repeat(129));

        performPostRequestAndAssertResults(mockMvc, "/api/v1/task-manager/lists",
                invalidList, "List name must be at most 128 characters");
    }
}
