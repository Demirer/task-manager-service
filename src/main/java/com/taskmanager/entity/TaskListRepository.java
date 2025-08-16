package com.taskmanager.entity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * Repository interface for {@link TaskList} entities.
 * Extends {@link JpaRepository} to provide standard CRUD operations and
 * additional JPA features such as pagination and sorting.
 */
public interface TaskListRepository extends JpaRepository<TaskList, Long> {

    /**
     * Fetches all {@link TaskList} entities along with their associated {@link Task} entities
     * in a single query.
     * This method uses {@link EntityGraph} to eagerly load tasks and avoid the N+1 select problem.
     * Without this, JPA would lazily load the {@link Task} collection for each task list,
     * resulting in multiple additional queries and poor performance.
     *
     * @return a list of {@link TaskList} entities with their tasks loaded
     */
    @EntityGraph(attributePaths = "tasks")
    @Query("select tl from TaskList tl")
    List<TaskList> findAllWithTasks();
}
