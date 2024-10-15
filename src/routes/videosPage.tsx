import { useAuthRedirect } from "@/hooks/useAuthRedirect";
import { formatFileName, formatFileSize, formatTimestamp } from "@/lib/file";
import { getVideos, videoDetailsInterface } from "@/services/videoService";
import { useQuery } from "@tanstack/react-query";
import { Link } from "react-router-dom";

export const Videos = () => {
  const { userDetails } = useAuthRedirect();
  const { data: videos } = useQuery<videoDetailsInterface[]>({
    queryFn: getVideos,
    queryKey: ["videoDetails"],
  });

  if (!userDetails) {
    return (
      <h1 className="flex font-semibold justify-center mt-5">
        Please login to see your videos!
      </h1>
    );
  }

  if (videos === undefined || videos.length == 0) {
    return (
      <h1 className="flex font-semibold justify-center mt-5">
        No videos found!
      </h1>
    );
  }
  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="flex justify-center text-2xl font-bold mb-6">
        Transcribed Videos
      </h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {videos.map((video) => (
          <div key={video.id} className="flex flex-col">
            <Link to={`/video/${video.id}`} className="group">
              <div className="relative aspect-video mb-2 overflow-hidden rounded-lg">
                <img
                  src="https://placehold.co/600x400.png"
                  alt={`Thumbnail for ${video.fileName}`}
                  className="transition-transform group-hover:scale-105"
                />
              </div>
              <h2 className="truncate text-lg font-semibold line-clamp-2 mb-1 group-hover:text-blue-600">
                {formatFileName(video.fileName)}
              </h2>
            </Link>
            <div className="text-sm text-gray-600">
              <p>{formatTimestamp(video.uploadTimestamp)}</p>
              <p>{`${video.contentType} • ${formatFileSize(
                video.contentLength
              )}`}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
