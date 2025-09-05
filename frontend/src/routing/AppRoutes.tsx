import { Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "../features/auth/view/LoginPage";
import SignupPage from "../features/auth/view/SignupPage";
import { PrivateRoute } from "./PrivateRoute";
import { isAuthenticated } from "../features/auth/repository/auth-repo";

export function AppRoutes() {
  return (
    <Routes>
      {/* root: auto-redirect based on auth */}
      <Route
        path="/"
        element={
          isAuthenticated() ? (
            <Navigate to="/dashboard" replace />
          ) : (
            <Navigate to="/login" replace />
          )
        }
      />

      {/* public routes */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />

      {/* protected */}
      <Route
        path="/dashboard"
        element={
          <PrivateRoute>
            <div>
              <h1>Dashboard</h1>
            </div>
          </PrivateRoute>
        }
      />
    </Routes>
  );
}
