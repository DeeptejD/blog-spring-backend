package com.example.blog.services.impl;

import com.example.blog.domain.entities.Category;
import com.example.blog.repositories.CategoryRepository;
import com.example.blog.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public void deleteCategory(UUID id) {
        // note: we only want to delete categories that DON'T have a post associated with them
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            if(!category.get().getPosts().isEmpty()) {
                throw new IllegalStateException("Category has posts associated with it");
            }
            categoryRepository.deleteById(id);
        }
    }

    @Override
    public Category getById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category with id " + id + " not found")
        );
    }
}
