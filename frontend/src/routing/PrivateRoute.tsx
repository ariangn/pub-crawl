import { Navigate } from 'react-router-dom';
import { isAuthenticated } from '../features/auth/repository/auth-repo';

interface PrivateRouteProps {
  children: React.ReactNode;
}

export function PrivateRoute({ children }: PrivateRouteProps) {
  // Simple check - if not authenticated, redirect to login
  if (!isAuthenticated()) {
    return <Navigate to="/login" replace />;
  }

  // If authenticated, show the protected content
  return <>{children}</>;
}