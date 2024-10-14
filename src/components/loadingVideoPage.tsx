import { Skeleton } from "./ui/skeleton";

export const LoadingVideoPage = () => {
  return (
    <div className="container mx-auto p-4 md:p-6 lg:p-8">
      <h1 className="text-2xl font-bold mb-6">Video Transcription</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="w-full aspect-video bg-gray-200 rounded-lg overflow-hidden flex items-center justify-center">
          <div className="animate-spin rounded-full h-16 w-16 border-t-2 border-b-2 border-primary"></div>
        </div>
        <div className="space-y-4">
          <Skeleton className="h-6 w-3/4" />
          <Skeleton className="h-4 w-full" />
          <Skeleton className="h-4 w-5/6" />
          <Skeleton className="h-4 w-full" />
          <Skeleton className="h-4 w-4/5" />
        </div>
      </div>
    </div>
  );
};
