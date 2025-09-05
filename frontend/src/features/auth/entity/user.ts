export interface User {
  id: string;
  username: string;
  email: string;
  pfpUrl?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  username: string;
  email: string;
  password: string;
  pfpUrl?: string;
}

export interface AuthResponse {
  token: string;
}
