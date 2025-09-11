/**
 * Servicio para la gestión de roles
 */

import HttpClient from "./httpClient";
import type {
  Role,
  CreateRoleDTO,
  UpdateRoleDTO,
  PaginatedResponse,
} from "../types";

export class RoleService {
  private static readonly BASE_URL = "/roles";

  /**
   * Obtiene todos los roles con paginación
   */
  static async getRoles(params?: {
    page?: number;
    limit?: number;
    search?: string;
    sortBy?: string;
    sortDirection?: "asc" | "desc";
  }): Promise<PaginatedResponse<Role>> {
    const response = await HttpClient.getPaginated<Role>(this.BASE_URL, params);

    return {
      data: response.data,
      pagination: response.pagination,
    };
  }

  /**
   * Obtiene todos los roles sin paginación (para selects)
   */
  static async getAllRoles(): Promise<Role[]> {
    const response = await HttpClient.get<Role[]>(`${this.BASE_URL}/all`);
    return response.data || [];
  }

  /**
   * Obtiene un rol por ID
   */
  static async getRoleById(id: string): Promise<Role> {
    const response = await HttpClient.get<Role>(`${this.BASE_URL}/${id}`);

    if (!response.data) {
      throw new Error("Rol no encontrado");
    }

    return response.data;
  }

  /**
   * Crea un nuevo rol
   */
  static async createRole(roleData: CreateRoleDTO): Promise<Role> {
    const response = await HttpClient.post<Role>(this.BASE_URL, roleData);

    if (!response.data) {
      throw new Error("Error al crear el rol");
    }

    return response.data;
  }

  /**
   * Actualiza un rol existente
   */
  static async updateRole(id: string, roleData: UpdateRoleDTO): Promise<Role> {
    const response = await HttpClient.put<Role>(
      `${this.BASE_URL}/${id}`,
      roleData
    );

    if (!response.data) {
      throw new Error("Error al actualizar el rol");
    }

    return response.data;
  }

  /**
   * Elimina un rol
   */
  static async deleteRole(id: string): Promise<void> {
    await HttpClient.delete(`${this.BASE_URL}/${id}`);
  }

  /**
   * Busca roles por término
   */
  static async searchRoles(searchTerm: string): Promise<Role[]> {
    const response = await HttpClient.get<Role[]>(`${this.BASE_URL}/search`, {
      params: { q: searchTerm },
    });

    return response.data || [];
  }

  /**
   * Obtiene los permisos disponibles en el sistema
   */
  static async getAvailablePermissions(): Promise<string[]> {
    const response = await HttpClient.get<string[]>(
      `${this.BASE_URL}/permissions`
    );
    return response.data || [];
  }
}

export default RoleService;
