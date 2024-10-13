import { userDetailsInterface } from "@/services/userService";
import { Home, Video, LogIn } from "lucide-react";
import { Link, useLocation } from "react-router-dom";
import { ProfileMenu } from "./profileMenu";

interface FloatingNavBarProps {
  userDetails?: userDetailsInterface;
}

export const FloatingNavbar = ({ userDetails }: FloatingNavBarProps) => {
  const location = useLocation();

  const handleAuth = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };

  const handleLogout = () => {
    window.location.href = "http://localhost:8080/logout";
  };

  return (
    <div className="sticky top-0 z-50 flex justify-center w-full py-2 bg-transparent">
      <nav className="inline-flex items-right justify-center h-12 px-6 bg-white border shadow-md rounded-xl">
        <div className="flex items-center space-x-4">
          <Link
            to="/"
            className={`flex items-center justify-center w-10 h-10 rounded-full ${
              location.pathname === "/" ? "text-red-500" : "text-gray-600"
            } hover:text-red-500 hover:bg-gray-200 transition-all duration-200`}
          >
            <Home className="h-6 w-6" />
            <span className="sr-only">Home</span>
          </Link>
          <Link
            to="/video"
            className={`flex items-center justify-center w-10 h-10 rounded-full ${
              location.pathname === "/video" ? "text-blue-500" : "text-gray-600"
            } hover:text-blue-500 hover:bg-gray-200 transition-all duration-200`}
          >
            <Video className="h-6 w-6" />
            <span className="sr-only">Video</span>
          </Link>
          {userDetails ? (
            <ProfileMenu
              userDetails={userDetails}
              handleLogout={handleLogout}
            />
          ) : (
            <button
              onClick={handleAuth}
              className="flex items-center justify-center w-10 h-10 rounded-full text-gray-600 hover:text-green-500 hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-300 transition-all duration-200"
            >
              <LogIn className="h-6 w-6" />
              <span className="sr-only">Login</span>
            </button>
          )}
        </div>
      </nav>
    </div>
  );
};
