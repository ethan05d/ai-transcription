package com.example.transcription.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Maps to SERIAL (auto-incrementing ID)
    private Long id;

    @Column(name = "oauth_id", unique = true, nullable = false)
    private String oauthId;  // OAuth ID from the provider (e.g., Google, GitHub)

    @Column(name = "email", unique = true, nullable = false)
    private String email;  // User email address

    @Column(name = "name")
    private String name;  // User full name

    @Column(name = "provider")
    private String provider;  // OAuth provider (e.g., Google, GitHub)

    @Column(name = "profile_picture_url", length = 512)
    private String profilePictureUrl;  // Profile picture URL (if available)

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;  // Last update time

    // One user can have many videos
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Video> videos;


}
