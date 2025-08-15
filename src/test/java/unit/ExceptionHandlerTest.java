package unit;

import com.taskmanager.exception.GlobalExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Handle IllegalArgumentException returns BAD_REQUEST")
    void testHandleInvalidRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid request data");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleInvalidRequest(ex);

        assertEquals(400, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.get("status"));
        assertEquals("Bad Request", body.get("error"));
        assertEquals("Invalid request data", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    @DisplayName("Handle EntityNotFoundException returns NOT_FOUND")
    void testHandleNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.get("status"));
        assertEquals("Not Found", body.get("error"));
        assertEquals("Entity not found", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    @DisplayName("Handle generic Exception returns INTERNAL_SERVER_ERROR")
    void testHandleGenericException() {
        Exception ex = new Exception("Something went wrong");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(ex);

        assertEquals(500, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("Something went wrong", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }
}
