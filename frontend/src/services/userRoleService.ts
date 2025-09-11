/**
 * Servicio para la gestión de asignación de roles a usuarios
 */

import HttpClient from "./httpClient";
import type {
  UserRole,
  CreateUserRoleDTO,
  PaginatedResponse,
  User,
  Role,
} from "../types";

export class UserRoleService {
  private static readonly BASE_URL = "/user-roles";

  /**
   * Obtiene todas las asignaciones de roles con paginación
   */
  static async getUserRoles(params?: {
    page?: number;
    limit?: number;
    userId?: string;
    roleId?: string;
    search?: string;
    sortBy?: string;
    sortDirection?: "asc" | "desc";
  }): Promise<PaginatedResponse<UserRole>> {
    const response = await HttpClient.getPaginated<UserRole>(
      this.BASE_URL,
      params
    );

    return {
      data: response.data,
      pagination: response.pagination,
    };
  }

  /**
   * Asigna un rol a un usuario
   */
  static async assignRole(
    assignmentData: CreateUserRoleDTO
  ): Promise<UserRole> {
    const response = await HttpClient.post<UserRole>(
      this.BASE_URL,
      assignmentData
    );

    if (!response.data) {
      throw new Error("Error al asignar el rol");
    }

    return response.data;
  }

  /**
   * Remueve un rol de un usuario
   */
  static async removeRole(id: string): Promise<void> {
    await HttpClient.delete(`${this.BASE_URL}/${id}`);
  }

  /**
   * Obtiene los roles asignados a un usuario específico
   */
  static async getUserAssignedRoles(userId: string): Promise<UserRole[]> {
    const response = await HttpClient.get<UserRole[]>(
      `${this.BASE_URL}/user/${userId}`
    );
    return response.data || [];
  }

  /**
   * Obtiene los usuarios que tienen un rol específico
   */
  static async getRoleAssignedUsers(roleId: string): Promise<UserRole[]> {
    const response = await HttpClient.get<UserRole[]>(
      `${this.BASE_URL}/role/${roleId}`
    );
    return response.data || [];
  }

  /**
   * Asigna múltiples roles a un usuario
   */
  static async assignMultipleRoles(
    userId: string,
    roleIds: string[]
  ): Promise<UserRole[]> {
    const response = await HttpClient.post<UserRole[]>(
      `${this.BASE_URL}/bulk-assign`,
      {
        userId,
        roleIds,
      }
    );

    return response.data || [];
  }

  /**
   * Remueve múltiples roles de un usuario
   */
  static async removeMultipleRoles(
    userId: string,
    roleIds: string[]
  ): Promise<void> {
    await HttpClient.post(`${this.BASE_URL}/bulk-remove`, {
      userId,
      roleIds,
    });
  }

  /**
   * Obtiene un resumen de usuarios con sus roles
   */
  static async getUsersWithRoles(params?: {
    page?: number;
    limit?: number;
    search?: string;
  }): Promise<PaginatedResponse<User & { roles: Role[] }>> {
    const response = await HttpClient.getPaginated<User & { roles: Role[] }>(
      `${this.BASE_URL}/users-with-roles`,
      params
    );

    return {
      data: response.data,
      pagination: response.pagination,
    };
  }

  /**
   * Verifica si un usuario tiene un rol específico
   */
  static async hasRole(userId: string, roleId: string): Promise<boolean> {
    try {
      const response = await HttpClient.get<{ hasRole: boolean }>(
        `${this.BASE_URL}/check/${userId}/${roleId}`
      );
      return response.data?.hasRole || false;
    } catch {
      return false;
    }
  }
}

export default UserRoleService;
