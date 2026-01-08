package com.example.blog.services;

import com.example.blog.domain.CreatePostRequest;
import com.example.blog.domain.UpdatePostRequest;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPublishedPosts(UUID categoryId, UUID tagId);

    Post getPostById(UUID id);

    List<Post> getAllDraftPostsByAuthor(User author);

    Post createPost(User author, CreatePostRequest createPostRequest);

    Post updatePost(UUID postId, UpdatePostRequest updatePostRequest);

    void deletePost(UUID postId);
}
