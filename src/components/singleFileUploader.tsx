import { useState, useCallback, useEffect } from "react";
import { FileVideo, Upload, Check, Cloud } from "lucide-react";
import { useDropzone } from "react-dropzone";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { useAuthRedirect } from "@/hooks/useAuthRedirect";
import { BACKEND_API_URL } from "@/services/userService";
import { useUploadVideo } from "@/hooks/useUploadVideo";
import { useNavigate } from "react-router-dom";

const UPLOAD_STEPS = [
  { id: 1, label: "Upload to Cloud" },
  { id: 2, label: "Transcribing the Video" },
  { id: 3, label: "Uploading to User Profile" },
  { id: 4, label: "Redirecting..." },
];

export const SingleFileUploader = () => {
  const [file, setFile] = useState<File | null>(null);
  const [currentStep, setCurrentStep] = useState(0);
  const [isUploading, setIsUploading] = useState(false);
  const navigate = useNavigate();

  const { mutate: uploadVideo, isError, isSuccess } = useUploadVideo();
  const { userDetails } = useAuthRedirect();

  const onDrop = useCallback((acceptedFiles: File[]) => {
    if (acceptedFiles.length > 0) {
      setFile(acceptedFiles[0]);
    }
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: {
      "video/mp4": [".mp4"],
      "video/quicktime": [".mov"],
      "video/x-msvideo": [".avi"],
    },
    maxFiles: 1,
  });

  const handleLogin = () => {
    window.location.href = `${BACKEND_API_URL}/oauth2/authorization/google`;
  };

  const handleUpload = async () => {
    if (file) {
      setIsUploading(true);
      setCurrentStep(1);
      uploadVideo(file);
    }
  };

  // Effect to handle step progression based on isSuccess and isError
  useEffect(() => {
    if (isSuccess) {
      setCurrentStep(2); // Step 2: Transcribing
      // Simulate transcription delay
      setTimeout(() => {
        setCurrentStep(3); // Step 3: Uploading to User Profile
        setTimeout(() => {
          setCurrentStep(4); // Step 4: Redirecting
          setIsUploading(false);
          // Simulate redirection or any further actions
          navigate("/videos");
        }, 500);
      }, 500);
    } else if (isError) {
      setIsUploading(false);
      setCurrentStep(0); // Reset steps if there's an error
    }
  }, [isSuccess, isError]);

  return (
    <div className="w-full max-w-md mx-auto p-6">
      {userDetails ? (
        <h2 className="text-2xl font-semibold text-center mb-2">
          Transcribe your files here, {userDetails.name}!
        </h2>
      ) : (
        <h2 className="text-2xl font-semibold text-center mb-2">
          Transcribe your Files here
        </h2>
      )}
      <p className="text-sm text-gray-600 text-center mb-4">
        Supported files are .mov .mp4 .avi
      </p>
      {/* handle not showing drag and drop */}
      {currentStep === 0 && (
        <div
          {...getRootProps()}
          className={`border-2 border-dashed rounded-lg p-8 text-center cursor-pointer transition-colors ${
            isDragActive
              ? "border-primary bg-primary/10"
              : "border-gray-300 hover:border-primary"
          }`}
        >
          <input {...getInputProps()} />
          <Upload className="mx-auto h-12 w-12 text-gray-400" />
          <p className="mt-2 text-sm text-gray-600">
            Drag &amp; drop a video file here, or click to select one
          </p>
        </div>
      )}
      {file && (
        <div className="mt-4 space-y-4">
          <div className="p-4 bg-gray-100 rounded-lg flex items-center">
            <FileVideo className="h-6 w-6 text-primary mr-2" />
            <span className="text-sm text-gray-700 truncate">{file.name}</span>
          </div>
          {userDetails ? (
            !isUploading &&
            currentStep === 0 && (
              <Button onClick={handleUpload} className="w-full">
                <Cloud className="mr-2 h-4 w-4" /> Upload to Cloud
              </Button>
            )
          ) : (
            <Button onClick={handleLogin} className="w-full">
              <Cloud className="mr-2 h-4 w-4" /> Upload to Cloud
            </Button>
          )}
        </div>
      )}
      <Dialog open={isUploading} onOpenChange={setIsUploading}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Uploading File</DialogTitle>
          </DialogHeader>
          <div className="mt-4">
            {UPLOAD_STEPS.map((step) => (
              <div key={step.id} className="flex items-center mb-4">
                <div
                  className={`w-8 h-8 rounded-full flex items-center justify-center mr-3 transition-colors duration-300 ease-in-out ${
                    currentStep >= step.id ? "bg-green-500" : "bg-gray-200"
                  }`}
                >
                  {currentStep >= step.id ? (
                    <Check
                      className={`text-white transition-all duration-300 ease-in-out ${
                        currentStep >= step.id
                          ? "opacity-100 scale-100"
                          : "opacity-0 scale-0"
                      }`}
                    />
                  ) : (
                    // Custom spinner using Tailwind classes
                    <div className="animate-spin rounded-full h-5 w-5 border-t-2 border-b-2 border-primary"></div>
                  )}
                </div>
                <span
                  className={`text-sm transition-colors duration-300 ease-in-out ${
                    currentStep >= step.id
                      ? "text-green-500 font-medium"
                      : "text-gray-500"
                  }`}
                >
                  {step.label}
                </span>
              </div>
            ))}
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
};
