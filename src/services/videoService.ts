const BACKEND_API_URL = import.meta.env.VITE_BACKEND_API_URL as String;

export const getVideos = async () => {
  try {
    const response = await fetch(`${BACKEND_API_URL}/videos`);

    if (!response.ok) {
      throw new Error("Failed to fetch videos");
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching videos, ", error);
    throw error; // Ensure that error propagates to the query's error state
  }
};
