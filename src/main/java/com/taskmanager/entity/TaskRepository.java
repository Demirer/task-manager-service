package com.taskmanager.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link Task} entities.
 * Extends {@link JpaRepository} to provide standard CRUD operations, pagination, and sorting
 * capabilities for {@link Task} objects.
 * This repository allows managing tasks independently of their {@link TaskList} associations.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
}
