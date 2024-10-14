import { uploadVideo } from "@/services/videoService";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useToast } from "./use-toast";

export const useUploadVideo = () => {
  const queryClient = useQueryClient();
  const { toast } = useToast();

  const mutation = useMutation({
    mutationFn: (file: File) => uploadVideo(file),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["videos"] });
      toast({
        title: "Successfully uploaded video to user profile!",
      });
    },
  });

  return { ...mutation };
};
