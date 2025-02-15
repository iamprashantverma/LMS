import React, { useState, createContext, useEffect } from 'react';
import { login as loginService } from '../services/api/authService';
import {jwtDecode} from 'jwt-decode';
import Loading from '../components/Common/Loading';
import { refreshAccessToken } from '../services/api/adminService';

const AuthContext = createContext();

function AuthProvider({ children }) {
  const [open, setOpen] = useState(true);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [accessToken, setAccessToken] = useState('');
  const [refreshInterval, setRefreshInterval] = useState(null);

  // Decode the access token and set the user
  const decodeAndSetUser = (token) => {
    try {
      const decoded = jwtDecode(token);
      const currentTime = Math.floor(Date.now() / 1000);
      //  if token expired the logout
      if (decoded.exp && decoded.exp < currentTime) {
        logout();
        return;
      }

      //  seting up user Context
      setUser({
        email: decoded.email,
        role: decoded.role,
        userName: decoded.userName,
        userId: decoded.userId,
        exp: decoded.exp,
        sub: decoded.sub,
      });

      // Schedule a refresh 1 minute before token expiration
      const timeUntilRefresh = (decoded.exp - currentTime - 60) * 1000; 
      if (timeUntilRefresh > 0) {
        clearInterval(refreshInterval);
        const interval = setTimeout(() => {
          handleTokenRefresh();
        }, timeUntilRefresh);
        setRefreshInterval(interval);
      }
    } catch (error) {
      console.error("Error decoding token:", error);
      setUser(null);
    }
  };

  // Refresh the access token using the refresh token
  const handleTokenRefresh = async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      logout();
      return;
    }
    try {
      const { data } = await refreshAccessToken(refreshToken);
      const newAccessToken = data.accessToken;
      localStorage.setItem('accessToken', newAccessToken);
      setAccessToken(newAccessToken);
      decodeAndSetUser(newAccessToken);
    } catch (error) {
      console.error("Error refreshing access token:", error);
      logout(); // Log out if refresh fails
    }

  };

  // Initialize authentication on app load
  useEffect(() => {
    const initializeAuth = () => {
      const storedAccessToken = localStorage.getItem('accessToken');
      if (storedAccessToken) {
        decodeAndSetUser(storedAccessToken);
      }
      setLoading(false);
    };
    initializeAuth();

    return () => clearInterval(refreshInterval); 
  }, []);

  // Login function
  const login = async (credentials) => {
    try {
      const { data } = await loginService(credentials);
      const { accessToken, refreshToken } = data;

      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);

      setAccessToken(accessToken);
      decodeAndSetUser(accessToken);
      setOpen(true);

      return data;
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    }
  };

  // Logout function
  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    setUser(null);
    setOpen(false);
    clearInterval(refreshInterval);
  };

  // Context value
  const value = {
    user,
    login,
    logout,
    open,
    accessToken,
    setOpen,
  };

  if (loading) {
    return <Loading />;
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export default AuthProvider;
export { AuthContext };
