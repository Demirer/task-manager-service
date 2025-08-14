package com.taskmanager.model.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequest {
    private String name;
    private String description;
}
