package com.example.blog.services.impl;

import com.example.blog.domain.PostStatus;
import com.example.blog.domain.entities.Category;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.entities.Tag;
import com.example.blog.domain.entities.User;
import com.example.blog.repositories.PostRepository;
import com.example.blog.services.CategoryService;
import com.example.blog.services.PostService;
import com.example.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            Category category = categoryService.findCategoryById(categoryId);
            Tag tag = tagService.findTagById(tagId);

            return postRepository.findAllByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED, category, tag);
        } else if (categoryId != null) {
            Category category = categoryService.findCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        } else if (tagId != null) {
            Tag tag = tagService.findTagById(tagId);
            return postRepository.findAllByStatusAndTags(PostStatus.PUBLISHED, tag);
        } else {
            return postRepository.findAllByStatus(PostStatus.PUBLISHED);
        }
    }

    @Override
    public List<Post> getDraftPostsByAuthor(User author) {
        return postRepository.findAllByStatusAndAuthor(PostStatus.DRAFT, author);
    }
}
