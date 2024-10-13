package com.example.transcription.Services;

import com.example.transcription.Models.User;
import com.example.transcription.Models.Video;
import com.example.transcription.Repositories.VideoRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserService userService;

    public VideoService(VideoRepository videoRepository,
                        UserService userService) {
        this.videoRepository = videoRepository;
        this.userService = userService;
    }


    public List<Video> findVideosByPrincipal(OidcUser principal) {
        User user = userService.findOrCreateUserFromPrincipal(principal);
        Long userId = user.getId();

        return videoRepository.findByUserId(userId);

    }

    public Video findVideoByPrincipalAndVideoId(OidcUser principal, Long id) {
        User user = userService.findOrCreateUserFromPrincipal(principal);
        Long userId = user.getId();

        Optional<Video> video = videoRepository.findByUserIdAndId(userId, id);

        if (video.isPresent()) {
            return video.get();
        }

        throw new RuntimeException("Video not found.");

    }

    public Video createAndSaveVideo(String keyName,
                                   String bucketName,
                                   Long contentLength,
                                   String contentType,
                                   OidcUser principal) {
        Video video = Video.builder()
                .fileName(keyName)
                .s3Url("s3://" + bucketName + "/" + keyName)
                .uploadTimestamp(LocalDateTime.now())
                .contentLength(contentLength)
                .contentType(contentType)
                .user(userService.findOrCreateUserFromPrincipal(principal))
                .build();
        return videoRepository.save(video);
    }


}
