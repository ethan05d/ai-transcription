package com.example.transcription.DTO;

import com.example.transcription.Models.Transcription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetailDTO {
    private Long id;
    private String fileName;
    private Long contentLength;
    private String contentType;
    private Transcription transcription;  // Include transcription here
}
