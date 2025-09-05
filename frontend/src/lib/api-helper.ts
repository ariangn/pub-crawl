const API_BASE_URL = import.meta.env.VITE_API_URL;

// Token management
export const getToken = (): string | null => {
  return localStorage.getItem('accessToken');
};

export const setToken = (token: string): void => {
  localStorage.setItem('accessToken', token);
};

export const removeToken = (): void => {
  localStorage.removeItem('accessToken');
};

export const apiCall = async (endpoint: string, options: RequestInit = {}): Promise<Response> => {
  const token = getToken();
  const headers: Record<string, string> = {
    ...(options.headers as Record<string, string>),
  };

  if (!(options.body instanceof FormData)) {
    headers['Content-Type'] = 'application/json';
  }

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });


  if (response.status === 401) {
    try {
      // attempt to refresh token
      const refreshResponse = await fetch(`${API_BASE_URL}/auth/refresh`, {
        method: 'POST',
        credentials: 'include',
      });

      if (refreshResponse.ok) {
        const refreshData = await refreshResponse.json();
        setToken(refreshData.token);
        
        headers.Authorization = `Bearer ${refreshData.token}`;
        const retryResponse = await fetch(`${API_BASE_URL}${endpoint}`, {
          ...options,
          headers,
        });
        
        if (!retryResponse.ok) {
          // if retry still fails, redirect to login
          removeToken();
          window.location.href = '/login';
          throw new Error('Session expired. Please login again.');
        }
        
        return retryResponse;
      } else {
        // refresh failed, redirect to login
        removeToken();
        window.location.href = '/login';
        throw new Error('Session expired. Please login again.');
      }
    } catch {
      removeToken();
      window.location.href = '/login';
      throw new Error('Session expired. Please login again.');
    }
  }

  if (!response.ok) {
    const errorText = await response.text();
    let errorMessage = 'An error occurred';
    
    try {
      const errorData = JSON.parse(errorText);
      errorMessage = errorData.message || errorData.error || errorMessage;
    } catch {
      errorMessage = errorText || `HTTP ${response.status}`;
    }
    
    throw new Error(errorMessage);
  }

  return response;
};
