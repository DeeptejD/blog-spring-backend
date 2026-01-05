package com.example.blog.controllers;

import com.example.blog.domain.dtos.CreateTagsRequest;
import com.example.blog.domain.dtos.TagResponse;
import com.example.blog.domain.entities.Tag;
import com.example.blog.mappers.TagMapper;
import com.example.blog.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<Tag> tags = tagService.getTags();
        List<TagResponse> tagResponses = tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(tagResponses);
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@RequestBody @Valid CreateTagsRequest createTagsRequest) {
        List<TagResponse> createdTags = tagService.createTags(createTagsRequest.getNames())
                .stream().map(tagMapper::toTagResponse)
                .toList();

        System.out.println(createdTags);

        return new ResponseEntity<>(
                createdTags,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTags(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
