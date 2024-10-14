export interface userDetailsInterface {
  id: number;
  name: string;
  email: string;
  profilePictureUrl: string;
}

export const BACKEND_API_URL = import.meta.env.VITE_BACKEND_API_URL as String;

export const getUserInfo = async (): Promise<userDetailsInterface> => {
  try {
    const response = await fetch(`${BACKEND_API_URL}/api/v1/user-info`, {
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching user, ", error);
    throw error; // Ensure that error propagates to the query's error state
  }
};
