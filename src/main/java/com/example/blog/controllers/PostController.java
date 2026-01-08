package com.example.blog.controllers;

import com.example.blog.domain.dtos.PostDto;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.entities.User;
import com.example.blog.mappers.PostMapper;
import com.example.blog.services.PostService;
import com.example.blog.services.UserService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<PostDto>> getAllDraftPosts(@RequestAttribute UUID userId) {
        // we haven't implemented authorization here, we are going to user the attribute set inside the jwt filter to filer draft posts
        User loggedInUser = userService.findUserById(userId);
        List<Post> draftPostsByAuthor = postService.getAllDraftPostsByAuthor(loggedInUser);
        List<PostDto> draftPostsByAuthorDtos = draftPostsByAuthor.stream()
                .map(postMapper::toDto)
                .toList();
        return ResponseEntity.ok(draftPostsByAuthorDtos);
    }
}
