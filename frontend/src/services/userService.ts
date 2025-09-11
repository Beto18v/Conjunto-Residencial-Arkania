/**
 * Servicio para la gestión de usuarios
 */

import HttpClient from "./httpClient";
import type {
  User,
  CreateUserDTO,
  UpdateUserDTO,
  PaginatedResponse,
} from "../types";

export class UserService {
  private static readonly BASE_URL = "/users";

  /**
   * Obtiene todos los usuarios con paginación
   */
  static async getUsers(params?: {
    page?: number;
    limit?: number;
    search?: string;
    sortBy?: string;
    sortDirection?: "asc" | "desc";
  }): Promise<PaginatedResponse<User>> {
    const response = await HttpClient.getPaginated<User>(this.BASE_URL, params);

    return {
      data: response.data,
      pagination: response.pagination,
    };
  }

  /**
   * Obtiene un usuario por ID
   */
  static async getUserById(id: string): Promise<User> {
    const response = await HttpClient.get<User>(`${this.BASE_URL}/${id}`);

    if (!response.data) {
      throw new Error("Usuario no encontrado");
    }

    return response.data;
  }

  /**
   * Crea un nuevo usuario
   */
  static async createUser(userData: CreateUserDTO): Promise<User> {
    const response = await HttpClient.post<User>(this.BASE_URL, userData);

    if (!response.data) {
      throw new Error("Error al crear el usuario");
    }

    return response.data;
  }

  /**
   * Actualiza un usuario existente
   */
  static async updateUser(id: string, userData: UpdateUserDTO): Promise<User> {
    const response = await HttpClient.put<User>(
      `${this.BASE_URL}/${id}`,
      userData
    );

    if (!response.data) {
      throw new Error("Error al actualizar el usuario");
    }

    return response.data;
  }

  /**
   * Elimina un usuario
   */
  static async deleteUser(id: string): Promise<void> {
    await HttpClient.delete(`${this.BASE_URL}/${id}`);
  }

  /**
   * Activa o desactiva un usuario
   */
  static async toggleUserStatus(id: string, isActive: boolean): Promise<User> {
    const response = await HttpClient.put<User>(
      `${this.BASE_URL}/${id}/status`,
      {
        isActive,
      }
    );

    if (!response.data) {
      throw new Error("Error al cambiar el estado del usuario");
    }

    return response.data;
  }

  /**
   * Obtiene los roles de un usuario
   */
  static async getUserRoles(userId: string) {
    const response = await HttpClient.get(`${this.BASE_URL}/${userId}/roles`);
    return response.data || [];
  }

  /**
   * Busca usuarios por término
   */
  static async searchUsers(searchTerm: string): Promise<User[]> {
    const response = await HttpClient.get<User[]>(`${this.BASE_URL}/search`, {
      params: { q: searchTerm },
    });

    return response.data || [];
  }
}

export default UserService;
