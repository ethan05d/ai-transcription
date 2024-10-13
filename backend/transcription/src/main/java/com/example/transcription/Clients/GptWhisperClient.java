package com.example.transcription.Clients;

import com.example.transcription.Configs.GptWhisperClientConfig;
import com.example.transcription.Models.gptWhisper.Request.ChatGPTRequest;
import com.example.transcription.Models.gptWhisper.Request.WhisperTranscriptionRequest;
import com.example.transcription.Models.gptWhisper.Response.ChatGPTResponse;
import com.example.transcription.Models.gptWhisper.Response.WhisperTranscriptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "openai-service",
        url = "${openai-service.urls.base-url}",
        configuration = GptWhisperClientConfig.class
)
public interface GptWhisperClient {
    @PostMapping(value = "${openai-service.urls.chat-url}", headers = {"Content-Type=application/json"})
    ChatGPTResponse chat(@RequestBody ChatGPTRequest chatRequest);

    @PostMapping(value = "${openai-service.urls.create-transcription-url}", headers = {"Content-Type=multipart/form-data"})
    WhisperTranscriptionResponse createTranscription(@ModelAttribute WhisperTranscriptionRequest whisperTranscriptionRequest);
}

