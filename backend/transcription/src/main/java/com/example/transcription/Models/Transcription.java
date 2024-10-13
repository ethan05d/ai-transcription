package com.example.transcription.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transcriptions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transcription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    @JsonIgnore
    private Video video;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Transcription status, ("In-progress", "Completed")
    @Column(name = "status")
    private String status;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "In-progress";  // Default status
        }
    }


}
