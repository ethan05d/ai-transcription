import { deleteVideo } from "@/services/videoService";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { useToast } from "./use-toast";

export const useDeleteFile = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const { toast } = useToast();

  const mutation = useMutation({
    mutationFn: (fileName: string) => deleteVideo(fileName),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["videos"] });
      navigate("/videos");
      toast({
        title: "Successfully deleted video!",
      });
    },
  });

  return mutation;
};
