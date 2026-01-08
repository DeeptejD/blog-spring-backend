package com.example.blog.domain.dtos;

import com.example.blog.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * This DTO represents the structure of the incoming JSON that hits the create post endpoint
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostRequestDto {
    @NotBlank(message = "Title cant be blank") // @NotBlank checks if the length AFTER trim is != 0, unlike @NotEmpty which does not perform the trim
    @Size(min = 3, max = 200, message = "Title must be between {min} and {max} characters.")
    private String title;

    @NotBlank(message = "Content cant be blank")
    @Size(min = 10, max = 50_000, message = "Content must be between {min} and {max} characters.")
    private String content;

    @NotNull(message = "Category ID cant be NULL")
    private UUID categoryId;

    @Builder.Default
    @Size(max = 10, message = "Max {max} tags allowed")
    private Set<UUID> tagIds = new HashSet<>(); // initializing this to prevent NPE in case no tags were applied

    @NotNull(message = "Post Status is required")
    private PostStatus postStatus;
}
