package com.example.blog.security;

import com.example.blog.domain.entities.User;
import com.example.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Spring security uses the 'UserDetailsService' to look up users. We need to implement this interface to tell Spring Security how to 'look-up' users based on their username, this is because the username could be anything. Here we use the email as the username, it could very well be a phone number or another unique field inside our DB.
 */
@RequiredArgsConstructor
public class BlogUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new BlogUserDetails(user);
    }
}
