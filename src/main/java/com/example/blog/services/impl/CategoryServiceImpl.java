package com.example.blog.services.impl;

import com.example.blog.domain.entities.Category;
import com.example.blog.repositories.CategoryRepository;
import com.example.blog.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        // we also want the post-count here, so a simple findAll on the category repo wont work
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional // this annotation is required because we are making multiple DB calls, and we want them all to happen in the same transaction
    public Category createCategory(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("Category Already exists with name: " + category.getName());
        }
        // JPA 'save' returns the saved entity
        return categoryRepository.save(category);
    }
}
