package com.example.blog.services.impl;

import com.example.blog.domain.entities.Tag;
import com.example.blog.repositories.TagRepository;
import com.example.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }
}
