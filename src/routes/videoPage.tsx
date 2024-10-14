import { useParams } from "react-router-dom";
import { ScrollArea } from "@/components/ui/scroll-area";
import { getVideoById, videoDetailsInterface } from "@/services/videoService";
import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { VideoPlayer } from "@/components/videoPlayer";
import { DeleteButton } from "@/components/deleteButton";
import { formatFileName } from "@/lib/file";
import { splitTextIntoParagraphs } from "@/lib/file";
import { LoadingVideoPage } from "@/components/loadingVideoPage";
import { useDeleteFile } from "@/hooks/useDeleteVideo";
import { useAuthRedirect } from "@/hooks/useAuthRedirect";

export const VideoPage = () => {
  const navigate = useNavigate();
  const { videoId } = useParams();
  const deleteFileMutation = useDeleteFile();

  const { userDetails } = useAuthRedirect();

  if (!videoId) {
    return <>No videoId!</>;
  }

  const {
    data: video,
    isLoading,
    error,
  } = useQuery<videoDetailsInterface>({
    queryFn: () => getVideoById(videoId),
    queryKey: ["videos", videoId],
  });

  const handleDelete = async (fileName: string) => {
    if (userDetails) {
      deleteFileMutation.mutate(fileName);
    } else {
      console.log("Not logged in!");
    }
  };

  if (error) {
    navigate("/videos");
    return null;
  }

  if (isLoading) {
    return <LoadingVideoPage />;
  }

  return (
    <div className="container mx-auto p-4 md:p-6 lg:p-8">
      <div className="flex justify-between mb-6">
        <h1 className="text-2xl font-bold">
          {formatFileName(video?.fileName || "")}
        </h1>
        <DeleteButton
          handleDelete={() => {
            handleDelete(video?.fileName || "");
          }}
        />
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="w-full aspect-video bg-gray-200 rounded-lg overflow-hidden">
          <VideoPlayer fileName={video?.fileName || ""} />
        </div>
        <ScrollArea className="h-[calc(100vh-12rem)] border rounded-lg p-4">
          <h2 className="flex justify-center text-xl font-semibold mb-4">
            Transcription
          </h2>
          <div className="space-y-4">
            {splitTextIntoParagraphs(video?.transcription?.text || "", 4).map(
              (paragraph, index) => (
                <p key={index}>{paragraph}</p>
              )
            )}
          </div>
        </ScrollArea>
      </div>
    </div>
  );
};
