import type { LoginRequest, SignupRequest, AuthResponse } from '../entity/user';
import { apiCall, getToken, setToken, removeToken } from '@/lib/api-helper';

export { getToken, setToken, removeToken };

export const isAuthenticated = (): boolean => {
  return getToken() !== null;
};

export const login = async (credentials: LoginRequest): Promise<AuthResponse> => {
  const response = await apiCall('/auth/login', {
    method: 'POST',
    body: JSON.stringify(credentials),
  });
  
  return response.json();
};

export const signup = async (userData: SignupRequest): Promise<AuthResponse> => {
  const response = await apiCall('/users/signup', {
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
  removeToken();
  window.location.href = '/login';
};