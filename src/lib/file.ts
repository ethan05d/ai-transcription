// Split the text into sentences, then group them into paragraphs
export const splitTextIntoParagraphs = (
  text: string,
  sentencesPerParagraph: number
): string[] => {
  // Split text into sentences using regex (handles ., ?, !, and quotes)
  const sentences = text.match(/[^.!?]+[.!?]*["']?\s*/g) || [];
  const paragraphs: string[] = [];

  let currentParagraph = "";

  // Group sentences into paragraphs
  sentences.forEach((sentence, index) => {
    currentParagraph += sentence;

    // After 2-3 sentences, push the current paragraph and reset
    if ((index + 1) % sentencesPerParagraph === 0) {
      paragraphs.push(currentParagraph.trim());
      currentParagraph = "";
    }
  });

  // Push any remaining sentences into the final paragraph
  if (currentParagraph.trim()) {
    paragraphs.push(currentParagraph.trim());
  }

  return paragraphs;
};

export function formatFileName(fileName: string) {
  const firstUnderscoreIndex = fileName.indexOf("_");

  let fullName = fileName.substring(firstUnderscoreIndex + 1);

  fullName = fullName.replace(/\.[^/.]+$/, "");
  return fullName;
}

export function formatFileSize(bytes: number): string {
  if (bytes === 0) return "0 Bytes";
  const sizes = ["Bytes", "KB", "MB", "GB", "TB"];
  const i = Math.floor(Math.log(bytes) / Math.log(1024));
  return parseFloat((bytes / Math.pow(1024, i)).toFixed(2)) + " " + sizes[i];
}

export function formatTimestamp(uploadTimestamp: string): string {
  const date = new Date(uploadTimestamp);

  const formattedDate = new Intl.DateTimeFormat("en-US", {
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
    hour12: true,
  }).format(date);

  return formattedDate;
}
