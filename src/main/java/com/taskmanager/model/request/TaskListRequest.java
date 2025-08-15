package com.taskmanager.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskListRequest {
    private String name;

    private static final int NAME_MAX_LENGTH = 128;

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("List name is required");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("List name must be at most " + NAME_MAX_LENGTH + " characters");
        }
    }
}
