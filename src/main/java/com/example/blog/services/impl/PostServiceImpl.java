package com.example.blog.services.impl;

import com.example.blog.domain.CreatePostRequest;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    public List<Post> getAllDraftPostsByAuthor(User author) {
        return postRepository.findAllByStatusAndAuthor(PostStatus.DRAFT, author);
    }

    @Override
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
        post.setStatus(createPostRequest.getPostStatus());

        // createdAt and updatedAt are populated automatically prePersist and preUpdate as defined in the Post entity

        return postRepository.save(post);
    }


    private int calculateReadingTime(String content) {
        // at this point we haven't checked if content is empty or NULL since we already added Bean validations on the incoming request
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WPM);
    }
}
