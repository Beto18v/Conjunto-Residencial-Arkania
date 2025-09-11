/**
 * Cliente HTTP configurado para la API de Arkania
 */

import axios from "axios";
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from "axios";
import config from "../utils/config";
import { getErrorMessage } from "../utils";

// Crear instancia de axios
const apiClient: AxiosInstance = axios.create({
  baseURL: config.apiBaseUrl,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

// Interceptor para agregar token de autenticación
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("arkania_token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para manejar respuestas y errores
apiClient.interceptors.response.use(
  (response: AxiosResponse) => {
    return response;
  },
  (error) => {
    // Si el token expiró, limpiar localStorage y redirigir al login
    if (error.response?.status === 401) {
      localStorage.removeItem("arkania_token");
      localStorage.removeItem("arkania_user");
      window.location.href = "/login";
    }

    return Promise.reject(error);
  }
);

// Tipos para las respuestas de la API
export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  message?: string;
  errors?: Record<string, string[]>;
}

export interface PaginatedApiResponse<T> {
  success: boolean;
  data: T[];
  pagination: {
    page: number;
    limit: number;
    total: number;
    totalPages: number;
  };
  message?: string;
}

// Cliente HTTP con métodos tipados
export class HttpClient {
  /**
   * Petición GET
   */
  static async get<T>(
    url: string,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    try {
      const response = await apiClient.get<ApiResponse<T>>(url, config);
      return response.data;
    } catch (error) {
      throw new Error(getErrorMessage(error));
    }
  }

  /**
   * Petición POST
   */
  static async post<T, D = unknown>(
    url: string,
    data?: D,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    try {
      const response = await apiClient.post<ApiResponse<T>>(url, data, config);
      return response.data;
    } catch (error) {
      throw new Error(getErrorMessage(error));
    }
  }

  /**
   * Petición PUT
   */
  static async put<T, D = unknown>(
    url: string,
    data?: D,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    try {
      const response = await apiClient.put<ApiResponse<T>>(url, data, config);
      return response.data;
    } catch (error) {
      throw new Error(getErrorMessage(error));
    }
  }

  /**
   * Petición DELETE
   */
  static async delete<T>(
    url: string,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    try {
      const response = await apiClient.delete<ApiResponse<T>>(url, config);
      return response.data;
    } catch (error) {
      throw new Error(getErrorMessage(error));
    }
  }

  /**
   * Petición GET paginada
   */
  static async getPaginated<T>(
    url: string,
    params?: Record<string, unknown>,
    config?: AxiosRequestConfig
  ): Promise<PaginatedApiResponse<T>> {
    try {
      const response = await apiClient.get<PaginatedApiResponse<T>>(url, {
        ...config,
        params,
      });
      return response.data;
    } catch (error) {
      throw new Error(getErrorMessage(error));
    }
  }
}

export default HttpClient;
