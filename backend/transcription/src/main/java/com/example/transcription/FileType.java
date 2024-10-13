package com.example.transcription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
public enum FileType {

    MP4("mp4", MediaType.valueOf("video/mp4")),
    AVI("avi", MediaType.valueOf("video/x-msvideo")),
    MKV("mkv", MediaType.valueOf("video/x-matroska")),
    MOV("mov", MediaType.valueOf("video/quicktime")),
    FLV("flv", MediaType.valueOf("video/x-flv"));

    private final String extension;
    private final MediaType mediaType;

    public static MediaType fromFilename(String filename) {
        // Get the file extension from the filename
        String fileExtension = getFileExtension(filename);

        // Match the file extension with the corresponding FileType
        for (FileType fileType : FileType.values()) {
            if (fileType.getExtension().equalsIgnoreCase(fileExtension)) {
                return fileType.getMediaType();
            }
        }

        // Throw exception if file type is not supported
        throw new IllegalArgumentException("Unsupported file type: " + fileExtension);
    }

    // Helper method to extract file extension from the filename
    private static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        throw new IllegalArgumentException("Invalid filename: " + filename);
    }
}
