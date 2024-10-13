package com.example.transcription.Controllers;

import com.example.transcription.FileType;
import com.example.transcription.Models.gptWhisper.Response.WhisperTranscriptionResponse;

import com.example.transcription.Services.AwsService;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/s3")
public class AwsController {

    private final AwsService awsService;

    @Autowired
    public AwsController(AwsService awsService) {
        this.awsService = awsService;
    }

    @GetMapping("/{bucketName}")
    public ResponseEntity<?> listFiles(@PathVariable("bucketName") String bucketName) {
        val body = awsService.listFiles(bucketName);
        return ResponseEntity.ok(body);
    }

    // Endpoint to upload a file to a bucket
    @PostMapping("/{bucketName}/upload")
    @SneakyThrows(IOException.class)
    public ResponseEntity<?> uploadFile(
            @PathVariable("bucketName") String bucketName,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal OidcUser principal
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String contentType = file.getContentType();
        long fileSize = file.getSize();
        InputStream inputStream = file.getInputStream();

        awsService.uploadFile(bucketName, fileName, fileSize, contentType, inputStream, file, principal);

        return ResponseEntity.ok().body("File successfully uploaded and transcribed.");
    }

    // Endpoint to download a file from a bucket
    @SneakyThrows
    @GetMapping("/{bucketName}/download/{fileName}")
    public ResponseEntity<?> downloadFile(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("fileName") String fileName
    ) {
        val body = awsService.downloadFile(bucketName, fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(FileType.fromFilename(fileName))
                .body(body.toByteArray());
    }

    // Endpoint to delete a file from a bucket
    @DeleteMapping("/{bucketName}/delete/{fileName}")
    public ResponseEntity<?> deleteFile(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("fileName") String fileName
    ) {
        awsService.deleteFile(bucketName, fileName);
        return ResponseEntity.ok().build();
    }


}
