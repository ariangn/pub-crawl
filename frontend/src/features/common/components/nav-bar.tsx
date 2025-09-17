import { useNavigate } from "react-router-dom";
import { isAuthenticated, logout, getCurrentUser } from "@/features/auth/repository/auth-repo";
import { Button } from "@/components/ui/button";
import { useEffect, useState } from "react";

interface User {
  id: string;
  username: string;
  email: string;
  pfpUrl?: string;
}

export function NavBar() {
  const navigate = useNavigate();
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const loadUser = async () => {
      if (isAuthenticated()) {
        try {
          const userData = await getCurrentUser();
          setUser(userData);
        } catch (error) {
          console.error("Failed to load user:", error);
        }
      }
      setIsLoading(false);
    };

    loadUser();
  }, []);

  const handleLogoClick = () => {
    if (isAuthenticated()) {
      navigate("/dashboard");
    } else {
      navigate("/login");
    }
  };

  const handleLogout = () => {
    logout();
  };

  return (
    <nav className="flex items-center justify-between px-6 py-4 border-b border-border bg-background">
      {/* logo */}
      <button
        onClick={handleLogoClick}
        className="text-2xl font-bold hover:opacity-80 transition-opacity"
        style={{ fontFamily: 'bitaria, sans-serif' }}
      >
        pubcrawl
      </button>

      {!isLoading && isAuthenticated() && user && (
        <div className="flex items-center gap-4">
          {/* User profile picture and username */}
          <div className="flex items-center gap-2">
            {user.pfpUrl ? (
              <img
                src={user.pfpUrl}
                alt={user.username}
                className="w-8 h-8 rounded-full object-cover"
              />
            ) : (
              <div className="w-8 h-8 rounded-full bg-primary flex items-center justify-center text-primary-foreground text-sm font-medium">
                {user.username.charAt(0).toUpperCase()}
              </div>
            )}
            <span className="text-sm font-medium text-foreground">
              {user.username}
            </span>
          </div>

          {/* logout button */}
          <Button
            variant="outline"
            size="sm"
            onClick={handleLogout}
            className="text-sm"
          >
            Logout
          </Button>
        </div>
      )}
    </nav>
  );
}
