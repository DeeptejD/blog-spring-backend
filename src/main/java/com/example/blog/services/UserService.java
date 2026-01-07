package com.example.blog.services;

import com.example.blog.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User findUserById(UUID id);
}
