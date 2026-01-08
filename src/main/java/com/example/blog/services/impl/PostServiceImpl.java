package com.example.blog.services.impl;

import com.example.blog.domain.CreatePostRequest;
import com.example.blog.domain.PostStatus;
import com.example.blog.domain.UpdatePostRequest;
import com.example.blog.domain.entities.Category;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.entities.Tag;
import com.example.blog.domain.entities.User;
import com.example.blog.repositories.PostRepository;
import com.example.blog.services.CategoryService;
import com.example.blog.services.PostService;
import com.example.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private static final int WPM = 200;
    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPublishedPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            Category category = categoryService.getById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED, category, tag);
        }

        if (categoryId != null) {
            Category category = categoryService.getById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        }

        if (tagId != null) {
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTags(PostStatus.PUBLISHED, tag);
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public Post getPostById(UUID id) {
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post with id " + id + " not found"));
    }

    @Override
    public List<Post> getAllDraftPostsByAuthor(User author) {
        return postRepository.findAllByStatusAndAuthor(PostStatus.DRAFT, author);
    }

    @Override
    @Transactional
    public Post createPost(User author, CreatePostRequest createPostRequest) {
        // for other entities we could directly call the 'save' method on the respective JPA repo, but here we can't since what we are getting is not the entity, but a representation of the request, and we need to populate it manually
        Post post = new Post();

        Category category = categoryService.getById(createPostRequest.getCategoryId());
        Set<Tag> tags = new HashSet<>(tagService.getTagsByIds(createPostRequest.getTagIds()));

        post.setTitle(createPostRequest.getTitle());
        post.setContent(createPostRequest.getContent());
        post.setAuthor(author);
        post.setCategory(category);
        post.setTags(tags);
        post.setReadingTime(calculateReadingTime(createPostRequest.getContent()));
        post.setStatus(createPostRequest.getStatus());

        // createdAt and updatedAt are populated automatically prePersist and preUpdate as defined in the Post entity

        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post updatePost(UUID postId, UpdatePostRequest updatePostRequest) {
        Post existingPost = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post does not exist with id " + postId));


        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        if (updatePostRequest.getCategoryId() != existingPost.getCategory().getId()) {
            Category existingCategory = categoryService.getById(updatePostRequest.getCategoryId());
            existingPost.setCategory(existingCategory);
        }

        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();

        if (!existingTagIds.equals(updatePostRequestTagIds)) {
            existingPost.setTags(updatePostRequestTagIds.stream().map(tagService::getTagById).collect(Collectors.toSet()));
        }

        return postRepository.save(existingPost);
    }


    private int calculateReadingTime(String content) {
        // at this point we haven't checked if content is empty or NULL since we already added Bean validations on the incoming request
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WPM);
    }
}
