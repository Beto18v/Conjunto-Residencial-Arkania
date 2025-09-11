/**
 * Servicio de autenticación para la aplicación Conjunto Residencial Arkania
 *
 * Esta clase proporciona métodos para gestionar la autenticación de usuarios,
 * incluyendo:
 * - Login y logout de usuarios
 * - Verificación de tokens
 * - Gestión del almacenamiento local de tokens y datos de usuario
 * - Comunicación con el backend para operaciones de autenticación
 * - Validación de expiración de tokens
 */

import HttpClient from "./httpClient";
import type { LoginCredentials, AuthResponse, User, Role } from "../types";
import config from "../utils/config";

export class AuthService {
  private static readonly BASE_URL = "/auth";

  /**
   * Inicia sesión con credenciales de usuario
   */
  static async login(credentials: LoginCredentials): Promise<AuthResponse> {
    const response = await HttpClient.post<AuthResponse>(
      `${this.BASE_URL}/login`,
      credentials
    );

    if (!response.data) {
      throw new Error("Credenciales inválidas");
    }

    // Guardar token en localStorage
    localStorage.setItem(config.tokenKey, response.data.token);
    localStorage.setItem("arkania_user", JSON.stringify(response.data.user));

    return response.data;
  }

  /**
   * Cierra la sesión del usuario
   */
  static async logout(): Promise<void> {
    try {
      await HttpClient.post(`${this.BASE_URL}/logout`);
    } catch (error) {
      // Ignorar errores del logout en el servidor
      console.warn("Error during logout:", error);
    } finally {
      // Siempre limpiar el localStorage
      localStorage.removeItem(config.tokenKey);
      localStorage.removeItem("arkania_user");
    }
  }

  /**
   * Verifica si el token actual es válido
   */
  static async verifyToken(): Promise<{ user: User; roles: Role[] }> {
    const response = await HttpClient.get<{ user: User; roles: Role[] }>(
      `${this.BASE_URL}/verify`
    );

    if (!response.data) {
      throw new Error("Token inválido");
    }

    return response.data;
  }

  /**
   * Refresca el token de autenticación
   */
  static async refreshToken(): Promise<AuthResponse> {
    const response = await HttpClient.post<AuthResponse>(
      `${this.BASE_URL}/refresh`
    );

    if (!response.data) {
      throw new Error("No se pudo refrescar el token");
    }

    // Actualizar token en localStorage
    localStorage.setItem(config.tokenKey, response.data.token);
    localStorage.setItem("arkania_user", JSON.stringify(response.data.user));

    return response.data;
  }

  /**
   * Cambia la contraseña del usuario actual
   */
  static async changePassword(
    currentPassword: string,
    newPassword: string
  ): Promise<void> {
    await HttpClient.post(`${this.BASE_URL}/change-password`, {
      currentPassword,
      newPassword,
    });
  }

  /**
   * Solicita recuperación de contraseña
   */
  static async forgotPassword(email: string): Promise<void> {
    await HttpClient.post(`${this.BASE_URL}/forgot-password`, { email });
  }

  /**
   * Restablece la contraseña con token de recuperación
   */
  static async resetPassword(
    token: string,
    newPassword: string
  ): Promise<void> {
    await HttpClient.post(`${this.BASE_URL}/reset-password`, {
      token,
      newPassword,
    });
  }

  /**
   * Obtiene el token almacenado en localStorage
   */
  static getStoredToken(): string | null {
    return localStorage.getItem(config.tokenKey);
  }

  /**
   * Obtiene el usuario almacenado en localStorage
   */
  static getStoredUser(): User | null {
    const userJson = localStorage.getItem("arkania_user");
    if (userJson) {
      try {
        return JSON.parse(userJson);
      } catch {
        return null;
      }
    }
    return null;
  }

  /**
   * Verifica si el usuario está autenticado
   */
  static isAuthenticated(): boolean {
    const token = this.getStoredToken();
    return !!token;
  }

  /**
   * Verifica si el token está próximo a expirar
   */
  static isTokenExpiring(): boolean {
    const token = this.getStoredToken();
    if (!token) return true;

    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      const expirationTime = payload.exp * 1000;
      const currentTime = Date.now();
      const timeUntilExpiration = expirationTime - currentTime;

      return timeUntilExpiration <= config.tokenExpirationBuffer;
    } catch {
      return true;
    }
  }
}

export default AuthService;
