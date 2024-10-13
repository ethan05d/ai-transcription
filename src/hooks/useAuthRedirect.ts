import { useQuery } from "@tanstack/react-query";
import { getUserInfo, userDetailsInterface } from "@/services/userService";

export const useAuthRedirect = () => {
  const { data: userDetails, isLoading } = useQuery<userDetailsInterface>({
    queryFn: getUserInfo,
    queryKey: ["userDetails"],
  });

  return { userDetails, isLoading };
};
