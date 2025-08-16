package com.taskmanager.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request to create or update a task list.
 * Contains the task list name and provides validation to ensure that
 * the name is present and does not exceed the maximum allowed length.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskListRequest {

    /**
     * The name of the task list.
     */
    private String name;

    /**
     * Maximum allowed length for the task list name.
     */
    private static final int NAME_MAX_LENGTH = 128;

    /**
     * Validates the task list request.
     * Checks that the {@code name} field is not null, not blank,
     * and does not exceed {@link #NAME_MAX_LENGTH} characters.
     * If any of these conditions fail, an {@link IllegalArgumentException} is thrown.
     *
     * @throws IllegalArgumentException if the name is missing or too long
     */
    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("List name is required");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("List name must be at most " + NAME_MAX_LENGTH + " characters");
        }
    }
}
