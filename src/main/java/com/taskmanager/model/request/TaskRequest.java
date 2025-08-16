package com.taskmanager.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request to create or update a {@link com.taskmanager.entity.Task}.
 * Contains the task's name and description with validation rules.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

    /**
     * The name of the task.
     * Cannot be null or blank. Maximum length is 128 characters.
     */
    private String name;

    /**
     * The description of the task.
     * Cannot be null or blank. Maximum length is 128 characters.
     */
    private String description;

    private static final int NAME_MAX_LENGTH = 128;
    private static final int DESCRIPTION_MAX_LENGTH = 128;

    /**
     * Validates the {@link TaskRequest} fields.
     * Throws {@link IllegalArgumentException} if the name or description
     * is missing, blank, or exceeds the maximum allowed length.
     */
    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Task name is required");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("Task name must be at most " + NAME_MAX_LENGTH + " characters");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Task description is required");
        }
        if (description.length() > DESCRIPTION_MAX_LENGTH) {
            throw new IllegalArgumentException("Task description must be at most " + DESCRIPTION_MAX_LENGTH + " characters");
        }
    }
}
