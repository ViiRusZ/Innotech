package ru.innotech.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.innotech.entity.Task;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT t FROM Task t WHERE t.id = :id")
    Optional<Task> findTaskById(@Param("id") Long taskId);

    @Query(value = "SELECT t FROM Task t ")
    Page<Task> findAllTasks(Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM task WHERE id = :id")
    int deleteTaskById(@Param("id") Long taskId);

    @Query(nativeQuery = true, value = """
                UPDATE task
                SET title = COALESCE(:title, task.title),
                    description = COALESCE(:description, task.description),
                    user_id = COALESCE(:user_id, task.user_id)
                WHERE id = :id
                RETURNING *;
            """)
    Task updateTaskById(@Param("id") Long id,
                        @Param("title") String title,
                        @Param("description") String description,
                        @Param("user_id") Long userId);
}
