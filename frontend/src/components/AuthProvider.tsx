/**
 * Provider de autenticación para la aplicación Conjunto Residencial Arkania
 *
 * Este componente provee el contexto de autenticación a toda la aplicación.
 * Se encarga de:
 * - Gestionar el estado de autenticación (usuario, token, roles)
 * - Inicializar la autenticación al cargar la aplicación
 * - Verificar la validez del token periódicamente
 * - Proporcionar funciones de login y logout
 * - Manejar errores de autenticación y limpieza de estado
 */

import React, { useState, useEffect, useCallback } from "react";
import { AuthService } from "../services";
import { AuthContext } from "../contexts/AuthContext";
import type { User, Role, LoginCredentials, AuthContextType } from "../types";

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [user, setUser] = useState<User | null>(null);
  const [roles, setRoles] = useState<Role[]>([]);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // Función de logout
  const logout = useCallback(async () => {
    setIsLoading(true);

    try {
      await AuthService.logout();
    } catch (error) {
      console.error("Error during logout:", error);
    } finally {
      setToken(null);
      setUser(null);
      setRoles([]);
      setIsLoading(false);
    }
  }, []);

  // Función para inicializar el estado de autenticación
  const initializeAuth = useCallback(async () => {
    setIsLoading(true);

    try {
      const storedToken = AuthService.getStoredToken();
      const storedUser = AuthService.getStoredUser();

      if (storedToken && storedUser) {
        setToken(storedToken);
        setUser(storedUser);

        // Verificar si el token sigue siendo válido
        try {
          const verificationData = await AuthService.verifyToken();
          setUser(verificationData.user);
          setRoles(verificationData.roles);
        } catch {
          // Token inválido, limpiar estado
          logout();
        }
      }
    } catch (error) {
      console.error("Error initializing auth:", error);
      logout();
    } finally {
      setIsLoading(false);
    }
  }, [logout]);

  // Función de login
  const login = useCallback(async (credentials: LoginCredentials) => {
    setIsLoading(true);

    try {
      const response = await AuthService.login(credentials);

      setToken(response.token);
      setUser(response.user);
      setRoles(response.roles);
    } catch (error) {
      setIsLoading(false);
      throw error;
    }

    setIsLoading(false);
  }, []);

  // Verificar autenticación al montar el componente
  useEffect(() => {
    initializeAuth();
  }, [initializeAuth]);

  // Verificar expiración del token periódicamente
  useEffect(() => {
    if (!token) return;

    const checkTokenExpiration = () => {
      if (AuthService.isTokenExpiring()) {
        logout();
      }
    };

    const interval = setInterval(checkTokenExpiration, 60000); // Verificar cada minuto

    return () => clearInterval(interval);
  }, [token, logout]);

  const value: AuthContextType = {
    user,
    roles,
    token,
    isAuthenticated: !!user && !!token,
    isLoading,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
