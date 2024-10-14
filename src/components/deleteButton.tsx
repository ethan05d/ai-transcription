import { Trash } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogClose,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "./ui/button";

interface DeleteButtonProps {
  handleDelete: () => void;
}

export const DeleteButton = ({ handleDelete }: DeleteButtonProps) => {
  return (
    <Dialog>
      <DialogTrigger asChild>
        <button className="shadow-md rounded-full mt-1">
          <Trash className="text-red-500 h-6 w-6 m-1" />
        </button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>Delete Video</DialogTitle>
          <DialogDescription>
            This will delete the video and transcription permanently.
          </DialogDescription>
        </DialogHeader>
        <DialogFooter className="sm:justify-end">
          <DialogClose asChild>
            <Button type="button" onClick={handleDelete} variant="destructive">
              Delete
            </Button>
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
