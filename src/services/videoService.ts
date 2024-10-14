export interface videoDetailsInterface {
  id: number;
  fileName: string;
  contentLength: number;
  contentType: string;
  uploadTimestamp: string;
  transcription?: Transcription;
}

type Transcription = {
  transcriptionId: string;
  text: string;
  createdAt: number[];
  status: string;
};

const BACKEND_API_URL = import.meta.env.VITE_BACKEND_API_URL as String;
const S3_BUCKET_NAME = import.meta.env.VITE_S3_BUCKET_NAME as String;

export const getVideos = async (): Promise<videoDetailsInterface[]> => {
  try {
    const response = await fetch(`${BACKEND_API_URL}/api/v1/videos`, {
      method: "GET",
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Failed to fetch videos");
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching videos, ", error);
    throw error; // Ensure that error propagates to the query's error state
  }
};

export const getVideoById = async (
  videoId: string
): Promise<videoDetailsInterface> => {
  try {
    const response = await fetch(`${BACKEND_API_URL}/api/v1/video/${videoId}`, {
      method: "GET",
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Failed to fetch videos");
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching videos, ", error);
    throw error; // Ensure that error propagates to the query's error state
  }
};

export const streamVideo = async (fileName: string) => {
  try {
    const response = await fetch(
      `${BACKEND_API_URL}/api/v1/video/stream/${S3_BUCKET_NAME}/${fileName}`,
      {
        method: "GET",
        credentials: "include", // Ensure that session cookies are included if needed
      }
    );

    if (!response.ok) {
      throw new Error("Failed to stream video");
    }

    // Return the response as a Blob (for video streaming)
    return await response.blob();
  } catch (error) {
    console.error("Error streaming video: ", error);
    throw error;
  }
};

export const deleteVideo = async (fileName: string) => {
  try {
    const response = await fetch(
      `${BACKEND_API_URL}/api/v1/s3/${S3_BUCKET_NAME}/delete/${fileName}`,
      {
        method: "DELETE", // Specify the DELETE method
        credentials: "include",
      }
    );

    if (!response.ok) {
      throw new Error("Failed to delete video");
    }
  } catch (error) {
    console.error("Error deleting video: ", error);
    throw error;
  }
};

export const uploadVideo = async (file: File) => {
  try {
    const body = new FormData();
    body.append("file", file);
    const response = await fetch(
      `${BACKEND_API_URL}/api/v1/s3/${S3_BUCKET_NAME}/upload`,
      {
        method: "POST",
        body,
        credentials: "include",
      }
    );

    if (!response.ok) {
      throw new Error("Failed to upload video");
    }
  } catch (error) {
    console.error("Error uploading video: ", error);
    throw error;
  }
};
