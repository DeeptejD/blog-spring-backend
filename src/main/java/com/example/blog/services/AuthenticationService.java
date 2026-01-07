package com.example.blog.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    UserDetails authenticate(String email, String password); // recap, SS only understands UserDetails, hence in our authentication logic we are returning a UserDetails object.
    String generateToken(UserDetails userDetails); // this will just generate our JWT.

    /**
     * Parses the incoming JWT token and extracts the username from it, which is set as the subject in the claims
     * @param token JWT token
     * @return UserDetails object based on the username extracted from the JWT
     */
    UserDetails validateToken(String token); // turns a JWT into UserDetails if valid.
}
