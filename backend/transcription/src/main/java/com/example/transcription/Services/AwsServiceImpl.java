package com.example.transcription.Services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.transcription.Models.Transcription;
import com.example.transcription.Models.Video;
import com.example.transcription.Repositories.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AwsServiceImpl implements AwsService {

    private final AmazonS3 s3Client;
    private final TranscriptionService transcriptionService;
    private final VideoService videoService;

    @Autowired
    public AwsServiceImpl(AmazonS3 s3Client,
                          TranscriptionService transcriptionService,
                          VideoService videoService) {
        this.s3Client = s3Client;
        this.transcriptionService = transcriptionService;
        this.videoService = videoService;
    }

    @Override
    @Transactional
    public void uploadFile(
            final String bucketName,
            final String keyName,
            final Long contentLength,
            final String contentType,
            final InputStream value,
            final MultipartFile file,
            OidcUser principal
    ) throws AmazonClientException {
        if (principal == null) {
            log.error("Unauthenticated users cannot upload files.");
            throw new SecurityException("You must be logged in to upload files.");
        }

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentLength);
            metadata.setContentType(contentType);

            s3Client.putObject(bucketName, keyName, value, metadata);
            log.info("File uploaded to bucket({}): {}", bucketName, keyName);

            // Save to Video Table DB
            Video video = videoService.createAndSaveVideo(keyName,
                    bucketName,
                    metadata.getContentLength(),
                    metadata.getContentType(),
                    principal);
            log.info("Video record created with ID: {}", video.getId());

            // Save Transcription to DB
            Transcription transcription = transcriptionService.createAndProcessTranscription(video, file);
            log.info("Transcription record created for video ID: {}", transcription.getVideo().getId());

        } catch (AmazonClientException e) {
            log.error("Error uploading file to S3: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public ByteArrayOutputStream downloadFile(
            final String bucketName,
            final String keyName
    ) throws IOException, AmazonClientException {
        try (S3Object s3Object = s3Client.getObject(bucketName, keyName);
             InputStream inputStream = s3Object.getObjectContent();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            log.info("File downloaded from bucket({}): {}", bucketName, keyName);
            return outputStream;
        }
    }

    public InputStream downloadFileAsStream(final String bucketName, final String keyName) throws AmazonClientException {
        S3Object s3Object = s3Client.getObject(bucketName, keyName);
        return s3Object.getObjectContent();  // Return the InputStream directly for streaming
    }

    // Method to list files in an S3 bucket
    @Override
    public List<String> listFiles(final String bucketName) throws AmazonClientException {
        List<String> keys = new ArrayList<>();
        ObjectListing objectListing = s3Client.listObjects(bucketName);

        while (true) {
            List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            if (objectSummaries.isEmpty()) {
                break;
            }

            objectSummaries.stream()
                    .filter(item -> !item.getKey().endsWith("/"))
                    .map(S3ObjectSummary::getKey)
                    .forEach(keys::add);

            objectListing = s3Client.listNextBatchOfObjects(objectListing);
        }

        log.info("Files found in bucket({}): {}", bucketName, keys);
        return keys;
    }

    // Method to delete a file from an S3 bucket
    @Override
    public void deleteFile(
            final String bucketName,
            final String keyName
    ) throws AmazonClientException {
        try {
            s3Client.deleteObject(bucketName, keyName);
            log.info("File deleted from bucket({}): {}", bucketName, keyName);
        } catch (AmazonClientException e) {
            log.error("Error deleting file from S3: {}", e.getMessage());
            throw e;
        }
    }

}
