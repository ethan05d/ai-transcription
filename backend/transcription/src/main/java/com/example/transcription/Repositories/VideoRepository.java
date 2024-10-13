package com.example.transcription.Repositories;

import com.example.transcription.Models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByUserId(Long userId);
    Optional<Video> findByUserIdAndId(Long userId, Long videoId);
}
