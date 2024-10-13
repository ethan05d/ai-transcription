package com.example.transcription.Services;


import com.amazonaws.AmazonClientException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface AwsService {

    // Method to upload a file to an S3 bucket
    void uploadFile(
            final String bucketName,
            final String keyName,
            final Long contentLength,
            final String contentType,
            final InputStream value,
            final MultipartFile file,
            OidcUser principal
            ) throws AmazonClientException;

    // Method to download a file from an S3 bucket
    ByteArrayOutputStream downloadFile(
            final String bucketName,
            final String keyName
    ) throws IOException, AmazonClientException;

    // Method to download a file to stream, not in memory / better performance
    InputStream downloadFileAsStream(
            final String bucketName,
            final String keyName
    ) throws AmazonClientException;

    // Method to list files in an S3 bucket
    List<String> listFiles(final String bucketName) throws AmazonClientException;

    // Method to delete a file from an S3 bucket
    void deleteFile(
            final String bucketName,
            final String keyName
    ) throws AmazonClientException;
}
