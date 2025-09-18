import type { LoginRequest, SignupRequest, AuthResponse } from '../entity/user';
import { apiCall, authApiCall, getToken, setToken, removeToken } from '@/lib/api-helper';

export { getToken, setToken, removeToken };

export const isAuthenticated = (): boolean => {
  return getToken() !== null;
};

export const clearAuthState = (): void => {
  removeToken();
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('user');
};

export const login = async (credentials: LoginRequest): Promise<AuthResponse> => {
  // clear any existing tokens before login
  clearAuthState();
  
  const response = await authApiCall('/auth/login', {
    method: 'POST',
    body: JSON.stringify(credentials),
  });
  
  return response.json();
};

export const signup = async (userData: SignupRequest): Promise<AuthResponse> => {
  const response = await authApiCall('/users', {
    method: 'POST',
    body: JSON.stringify(userData),
  });
  
  return response.json();
};

export const getCurrentUser = async () => {
  const response = await apiCall('/auth/me');
  return response.json();
};

export const logout = () => {
  clearAuthState();
  // redirect to login page
  window.location.href = '/login';
};