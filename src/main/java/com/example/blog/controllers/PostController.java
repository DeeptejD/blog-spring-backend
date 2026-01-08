package com.example.blog.controllers;

import com.example.blog.domain.CreatePostRequest;
import com.example.blog.domain.UpdatePostRequest;
import com.example.blog.domain.dtos.CreatePostRequestDto;
import com.example.blog.domain.dtos.PostDto;
import com.example.blog.domain.dtos.UpdatePostRequestDto;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.entities.User;
import com.example.blog.mappers.PostMapper;
import com.example.blog.services.PostService;
import com.example.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

    private final PostMapper postMapper;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPublishedPosts(
            @RequestParam(required = false)UUID categoryId,
            @RequestParam(required = false)UUID tagId
    ) {
        List<Post> publishedPosts = postService.getAllPublishedPosts(categoryId, tagId);
        List<PostDto> publishedPostsDtos = publishedPosts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(publishedPostsDtos);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getAllDraftPosts(
            @RequestAttribute UUID userId
    ) {
        // we haven't implemented authorization here, we are going to user the attribute set inside the jwt filter to filer draft posts
        User loggedInUser = userService.findUserById(userId);
        List<Post> draftPostsByAuthor = postService.getAllDraftPostsByAuthor(loggedInUser);
        List<PostDto> draftPostsByAuthorDtos = draftPostsByAuthor.stream()
                .map(postMapper::toDto)
                .toList();
        return ResponseEntity.ok(draftPostsByAuthorDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable UUID id) {
        Post postById = postService.getPostById(id);
        return ResponseEntity.ok(postMapper.toDto(postById));
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestAttribute UUID userId,
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto
    ) {
        User loggedInUser = userService.findUserById(userId);

        // normally at this point we would convert the incoming DTO into an entity and send it to our service layer, but since here the request to create a post does not directly match the structure of the entity we will create another Class and map the request DTO to that, which will then be sent over to the service layer.
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);

        Post createdPost = postService.createPost(loggedInUser, createPostRequest);

        return new ResponseEntity<>(
                postMapper.toDto(createdPost),
                HttpStatus.CREATED
        );
    }

    // NOTE: this is a FULL UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequestDto  updatePostRequestDto
            ) {
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post updatedPost = postService.updatePost(id, updatePostRequest);
        return ResponseEntity.ok(postMapper.toDto(updatedPost));
    }
}
