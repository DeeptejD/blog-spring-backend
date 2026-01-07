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
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>>  getAllPosts(
            @RequestParam(required = false)UUID categoryId,
            @RequestParam(required = false)UUID tagId
    ) {
        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getAllDrafts(@RequestAttribute UUID userId) {
        // we haven't implemented authorization here, we are going to user the attribute set inside the jwt filter to filer draft posts
        User loggedInUser = userService.findUserById(userId);
        List<Post> draftPosts = postService.getDraftPostsByAuthor(loggedInUser);
        List<PostDto> draftPostDtos = draftPosts.stream()
                .map(postMapper::toDto)
                .toList();
        return ResponseEntity.ok(draftPostDtos);
    }
}
