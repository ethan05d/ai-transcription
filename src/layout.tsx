import { Outlet } from "react-router-dom";
import { FloatingNavbar } from "./components/navbar";
import { useAuthRedirect } from "@/hooks/useAuthRedirect";

export const Layout = () => {
  const { userDetails } = useAuthRedirect();

  return (
    <>
      <FloatingNavbar userDetails={userDetails} />
      <Outlet />
    </>
  );
};
