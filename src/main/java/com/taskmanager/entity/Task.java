package com.taskmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a task in the task management system.
 * Each task has a name, description, and belongs to a {@link TaskList}.
 */
@Entity
@Table(
        name = "task",
        indexes = {
                @Index(name = "idx_task_list_id", columnList = "task_list_id")
        }
)
@Data
@NoArgsConstructor
public class Task {

    /**
     * Unique identifier for the task.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the task. Cannot be null and has a maximum length of 128 characters.
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * Description of the task. Cannot be null and has a maximum length of 128 characters.
     */
    @Column(nullable = false, length = 128)
    private String description;

    /**
     * The {@link TaskList} to which this task belongs.
     * This association is mandatory and uses lazy loading for performance.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_list_id", nullable = false)
    private TaskList taskList;
}
