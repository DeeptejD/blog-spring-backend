package com.example.blog.services;

import com.example.blog.domain.entities.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getTags();
}
