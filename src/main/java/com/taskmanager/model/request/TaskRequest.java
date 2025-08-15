package com.taskmanager.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    private String name;
    private String description;

    private static final int NAME_MAX_LENGTH = 128;
    private static final int DESCRIPTION_MAX_LENGTH = 128;


    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Task name is required");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("Task name must be at most " + NAME_MAX_LENGTH + " characters");
        }
        if (description != null && description.length() > DESCRIPTION_MAX_LENGTH) {
            throw new IllegalArgumentException("Task description must be at most " + DESCRIPTION_MAX_LENGTH + " characters");
        }
    }
}
