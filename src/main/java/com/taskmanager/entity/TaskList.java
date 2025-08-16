package com.taskmanager.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of tasks in the task management system.
 * Each {@link TaskList} can contain multiple {@link Task} entities.
 */
@Entity
@Table(name = "task_list")
@Data
@NoArgsConstructor
public class TaskList {

    /**
     * Unique identifier for the task list.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the task list. Cannot be null and has a maximum length of 128 characters.
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * The tasks that belong to this task list.
     * - One {@link TaskList} can have many {@link Task} entities.
     * - Cascade type is {@link CascadeType#ALL} and orphan removal is enabled.
     * - Uses LAZY loading by default.
     */
    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
}

