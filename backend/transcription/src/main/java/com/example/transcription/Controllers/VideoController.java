package com.example.transcription.Controllers;

import com.example.transcription.Models.Video;
import com.example.transcription.Services.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    // Get the list of videos a user has
    @GetMapping("/videos")
    public ResponseEntity<List<Video>> listVideosFromPrincipal(@AuthenticationPrincipal OidcUser principal) {
        return ResponseEntity.ok(videoService.findVideosByPrincipal(principal));
    }

    // Get video from video ID
    @GetMapping("/video/{id}")
    public ResponseEntity<Video> getVideo(@AuthenticationPrincipal OidcUser principal,
                                          @PathVariable("id") Long id) {
        Video video = videoService.findVideoByPrincipalAndVideoId(principal, id);
        return ResponseEntity.ok(video);
    }



}
