package com.taskmanager.entity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {

    // Use when returning lists with tasks to avoid N+1
    @EntityGraph(attributePaths = "tasks")
    @Query("select tl from TaskList tl")
    List<TaskList> findAllWithTasks();
}
