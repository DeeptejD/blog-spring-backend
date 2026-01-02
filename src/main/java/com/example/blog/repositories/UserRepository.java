package com.example.blog.repositories;

import com.example.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email); // we created this method because we need to implement the 'UserDetailsService' for Spring Security and there we will need to implement a SS method to look up users based on their username. The username we chose here is the 'Email' and hence we create this new method.
}
