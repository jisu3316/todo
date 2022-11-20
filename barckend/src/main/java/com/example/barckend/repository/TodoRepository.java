package com.example.barckend.repository;

import com.example.barckend.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, String> {
    List<Todo> findByUserId(String userId);

    @Query("SELECT t FROM Todo t WHERE t.userId = ?1")
    Todo findByUserIdQuery(String userId);
}
