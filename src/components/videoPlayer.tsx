import { useEffect, useState } from "react";
import { streamVideo } from "@/services/videoService"; // Your streamVideo function

export const VideoPlayer = ({ fileName }: { fileName: string }) => {
  const [videoSrc, setVideoSrc] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let isMounted = true;
    let objectUrl: string | null = null;

    const fetchVideo = async () => {
      try {
        const videoBlob = await streamVideo(fileName);

        // Create a URL for the video blob
        objectUrl = URL.createObjectURL(videoBlob);
        if (isMounted) {
          setVideoSrc(objectUrl);
        }
      } catch (err) {
        console.error("Error streaming video:", err);
        if (isMounted) {
          setError("Failed to load video");
        }
      }
    };

    fetchVideo();

    // Cleanup function
    return () => {
      isMounted = false;
      if (objectUrl) {
        URL.revokeObjectURL(objectUrl);
      }
    };
  }, [fileName]);

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div>
      {videoSrc ? (
        <video controls width="900" src={videoSrc} className="border" />
      ) : (
        <div className="w-full aspect-video bg-gray-200 rounded-lg overflow-hidden flex items-center justify-center">
          <div className="animate-spin rounded-full h-16 w-16 border-t-2 border-b-2 border-primary"></div>
        </div>
      )}
    </div>
  );
};
