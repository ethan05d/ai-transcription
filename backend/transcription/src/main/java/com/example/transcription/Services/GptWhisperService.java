package com.example.transcription.Services;

import com.example.transcription.Clients.GptWhisperClient;
import com.example.transcription.Configs.GptWhisperClientConfig;
import com.example.transcription.Models.gptWhisper.Request.*;
import com.example.transcription.Models.gptWhisper.Response.ChatGPTResponse;
import com.example.transcription.Models.gptWhisper.Response.WhisperTranscriptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GptWhisperService {
    private final GptWhisperClient gptWhisperClient;
    private final GptWhisperClientConfig gptWhisperClientConfig;

    private final static String ROLE_USER = "user";

    public ChatGPTResponse chat(ChatRequest chatRequest){
        Message message = Message.builder()
                .role(ROLE_USER)
                .content(chatRequest.getQuestion())
                .build();
        ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder()
                .model(gptWhisperClientConfig.getModel())
                .messages(Collections.singletonList(message))
                .build();
        
        return gptWhisperClient.chat(chatGPTRequest);
    }

    public WhisperTranscriptionResponse createTranscription(TranscriptionRequest transcriptionRequest){
        WhisperTranscriptionRequest whisperTranscriptionRequest = WhisperTranscriptionRequest.builder()
                .model(gptWhisperClientConfig.getAudioModel())
                .file(transcriptionRequest.getFile())
                .build();
        return gptWhisperClient.createTranscription(whisperTranscriptionRequest);
    }

}
