package com.example.transcription.Controllers;

import com.example.transcription.DTO.VideoDetailDTO;
import com.example.transcription.FileType;
import com.example.transcription.Models.Video;
import com.example.transcription.Services.AwsServiceImpl;
import com.example.transcription.Services.VideoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VideoController {

    private final VideoService videoService;
    private final AwsServiceImpl awsService;

    public VideoController(VideoService videoService, AwsServiceImpl awsService) {
        this.videoService = videoService;
        this.awsService = awsService;
    }

    // Get the list of videos a user has
    @GetMapping("/videos")
    public ResponseEntity<List<Video>> listVideosFromPrincipal(@AuthenticationPrincipal OidcUser principal) {
        return ResponseEntity.ok(videoService.findVideosByPrincipal(principal));
    }

    // Get video from video ID
    @GetMapping("/video/{id}")
    public ResponseEntity<VideoDetailDTO> getVideo(@AuthenticationPrincipal OidcUser principal,
                                          @PathVariable("id") Long id) {
        Video video = videoService.findVideoByPrincipalAndVideoId(principal, id);
        VideoDetailDTO videoDTO = new VideoDetailDTO(
                video.getId(),
                video.getFileName(),
                video.getContentLength(),
                video.getContentType(),
                video.getTranscription()
        );
        return ResponseEntity.ok(videoDTO);
    }

    // Endpoint to download a file from a bucket as INPUTSTREAM
    @GetMapping("/video/stream/{bucketName}/{fileName}")
    public void streamVideo(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("fileName") String fileName,
            HttpServletResponse response
    ) throws IOException {
        InputStream inputStream = awsService.downloadFileAsStream(bucketName, fileName);

        response.setContentType("video/mp4");

        // Write the InputStream to the response output stream
        byte[] buffer = new byte[4096];  // Buffer to improve streaming performance
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            response.getOutputStream().write(buffer, 0, bytesRead);
        }

        inputStream.close();  // Close the input stream after streaming
    }

}
