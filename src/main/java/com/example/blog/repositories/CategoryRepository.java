package com.example.blog.repositories;

import com.example.blog.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPostCount();

    // SpringBoot feels like witchcraft, it understands what needs to be done in this method because our entity contains a property called name, and we have used the work exists in the method name
    boolean existsByNameIgnoreCase(String name);
}
