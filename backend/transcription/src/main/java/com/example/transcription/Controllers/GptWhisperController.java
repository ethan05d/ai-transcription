package com.example.transcription.Controllers;

import com.example.transcription.Models.gptWhisper.Request.ChatRequest;
import com.example.transcription.Models.gptWhisper.Request.TranscriptionRequest;
import com.example.transcription.Models.gptWhisper.Response.ChatGPTResponse;
import com.example.transcription.Models.gptWhisper.Response.WhisperTranscriptionResponse;
import com.example.transcription.Services.GptWhisperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class GptWhisperController {
    private final GptWhisperService gptWhisperService;

    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChatGPTResponse chat(@RequestBody ChatRequest chatRequest){
        return gptWhisperService.chat(chatRequest);
    }

//    @PostMapping(value = "/transcription", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public WhisperTranscriptionResponse createTranscription(@ModelAttribute TranscriptionRequest transcriptionRequest){
//        return gptWhisperService.createTranscription(transcriptionRequest);
//    }
}
