package com.example.transcription.Services;

import com.example.transcription.Models.Transcription;
import com.example.transcription.Models.Video;
import com.example.transcription.Models.gptWhisper.Request.TranscriptionRequest;
import com.example.transcription.Models.gptWhisper.Response.WhisperTranscriptionResponse;
import com.example.transcription.Repositories.TranscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class TranscriptionService {

    private final GptWhisperService gptWhisperService;
    private final TranscriptionRepository transcriptionRepository;

    public TranscriptionService(GptWhisperService gptWhisperService,
                                TranscriptionRepository transcriptionRepository) {
        this.gptWhisperService = gptWhisperService;
        this.transcriptionRepository = transcriptionRepository;
    }

    public WhisperTranscriptionResponse transcribeFile(MultipartFile file) {
        TranscriptionRequest transcriptionRequest = new TranscriptionRequest(file);

        return gptWhisperService.createTranscription(transcriptionRequest);
    }

    @Transactional
    public Transcription createAndProcessTranscription(Video video, MultipartFile file) {
        // Save initial transcription with status "In-progress"
        Transcription transcription = Transcription.builder()
                .video(video)
                .status("In-progress")
                .createdAt(LocalDateTime.now())
                .build();
        transcription = transcriptionRepository.save(transcription);

        // Transcribe the file
        WhisperTranscriptionResponse response = gptWhisperService.createTranscription(new TranscriptionRequest(file));
        transcription.setText(response.getText());

        // Update transcription status to "Completed"
        transcription.setStatus("Completed");
        return transcriptionRepository.save(transcription);  // Save updated transcription

    }


}
