package com.example.transcription.Models.gptWhisper.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
public class Message implements Serializable {
    private String role;
    private String content;
}
