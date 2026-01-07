package com.example.blog.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// we are creating this DTO since we don't want to expose ALL the information about a user, since out User is also an Author
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {
    private UUID id;
    private String name;
}
