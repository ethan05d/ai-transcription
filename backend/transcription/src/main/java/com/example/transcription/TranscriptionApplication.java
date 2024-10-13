package com.example.transcription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TranscriptionApplication {
	public static void main(String[] args) {
		SpringApplication.run(TranscriptionApplication.class, args);
	}
}

