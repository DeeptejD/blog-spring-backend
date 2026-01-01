package com.example.blog.mappers;

import com.example.blog.domain.PostStatus;
import com.example.blog.domain.dtos.CategoryDto;
import com.example.blog.domain.entities.Category;
import com.example.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    /**
     * Maps a Category entity to a CategoryDto. CategoryDto contains a field called postCount, which is the number of posts that have the said category. This field is not present in the Category entity. To convert it we need to write a function, which in this case is 'calculatePostCount'.
     * @param category Category entity that needs to be mapped to a DTO
     * @return CategoryDto
     */
    @Mapping(source = "posts", target = "postCount", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    /**
     * @param posts A list of posts for a particular Category
     * @return A count of all the posts that have their PostStatus set to PUBLISHED
     */
    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if (posts == null) {
            return 0;
        }
        return posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
