package com.example.transcription.Services;

import com.example.transcription.Models.User;
import com.example.transcription.Repositories.UserRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Abstraction method that uses principal to findUserByOAuthId
    public User findOrCreateUserFromPrincipal(OidcUser principal) {
        String oauthId = principal.getSubject();
        String email = principal.getEmail();
        String name = principal.getFullName();
        String provider = principal.getIssuer().toString();
        String profilePictureUrl = principal.getPicture();

        return findOrCreateUser(oauthId, email, name, provider, profilePictureUrl);
    }

    // Check if the user exists by oauthId; if not, create a new user
    public User findOrCreateUser(String oauthId, String email, String name, String provider, String profilePictureUrl) {
        // Find the user by oauthId
        Optional<User> existingUser = userRepository.findByOauthId(oauthId);

        // If user exists, return it
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        // Otherwise, create and save a new user
        User newUser = new User();
        newUser.setOauthId(oauthId);
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setProvider(provider);
        newUser.setProfilePictureUrl(profilePictureUrl);
        return userRepository.save(newUser);
    }
}
