package com.example.transcription.Controllers;

import com.example.transcription.Models.User;
import com.example.transcription.Services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    // Retrieve user-info endpoint (protected)
    @GetMapping("/user-info")
    public User getUserInfo(@AuthenticationPrincipal OidcUser principal) {
        return userService.findOrCreateUserFromPrincipal(principal);
    }
}
