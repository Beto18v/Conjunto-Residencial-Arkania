/**
 * Definición de tipos para el sistema de gestión residencial
 */

// Tipos base
export interface BaseEntity extends Record<string, unknown> {
  id: string;
  createdAt: string;
  updatedAt: string;
}

// Usuario
export interface User extends BaseEntity {
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  isActive: boolean;
}

export interface CreateUserDTO {
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  password: string;
}

export interface UpdateUserDTO {
  email?: string;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  isActive?: boolean;
}

// Rol
export interface Role extends BaseEntity {
  name: string;
  description: string;
  permissions: string[];
}

export interface CreateRoleDTO {
  name: string;
  description: string;
  permissions: string[];
}

export interface UpdateRoleDTO {
  name?: string;
  description?: string;
  permissions?: string[];
}

// Usuario-Rol
export interface UserRole extends BaseEntity {
  userId: string;
  roleId: string;
  user?: User; // Opcional para cuando se carga por separado
  role?: Role; // Opcional para cuando se carga por separado
  assignedAt: string;
  assignedBy: string;
  isActive: boolean;
}

export interface CreateUserRoleDTO {
  userId: string;
  roleId: string;
}

// Autenticación
export interface LoginCredentials {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  user: User;
  roles: Role[];
  expiresAt: string;
}

export interface AuthContextType {
  user: User | null;
  roles: Role[];
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (credentials: LoginCredentials) => Promise<void>;
  logout: () => void;
}

// Estados de la aplicación
export interface LoadingState {
  isLoading: boolean;
  error: string | null;
}

export interface PaginationState {
  page: number;
  limit: number;
  total: number;
  totalPages: number;
}

export interface PaginatedResponse<T> {
  data: T[];
  pagination: PaginationState;
}

// Formularios
export interface FormFieldProps {
  name: string;
  label: string;
  type?: "text" | "email" | "password" | "tel" | "textarea" | "select";
  placeholder?: string;
  required?: boolean;
  disabled?: boolean;
  options?: Array<{ value: string; label: string }>;
  validation?: {
    required?: string;
    pattern?: { value: RegExp; message: string };
    minLength?: { value: number; message: string };
    maxLength?: { value: number; message: string };
  };
}

// Componentes UI
export interface ButtonProps {
  children: React.ReactNode;
  variant?: "primary" | "secondary" | "danger" | "outline";
  size?: "sm" | "md" | "lg";
  disabled?: boolean;
  loading?: boolean;
  type?: "button" | "submit" | "reset";
  onClick?: () => void;
  className?: string;
}

export interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  children: React.ReactNode;
  size?: "sm" | "md" | "lg" | "xl";
}

export interface TableColumn<T> {
  key: keyof T | string;
  label: string;
  sortable?: boolean;
  render?: (item: T) => React.ReactNode;
  className?: string;
}

export interface TableProps<T> {
  data: T[];
  columns: TableColumn<T>[];
  loading?: boolean;
  emptyMessage?: string;
  onSort?: (key: string, direction: "asc" | "desc") => void;
  sortKey?: string;
  sortDirection?: "asc" | "desc";
}

// Notificaciones
export type NotificationType = "success" | "error" | "warning" | "info";

export interface NotificationProps {
  type: NotificationType;
  title: string;
  message?: string;
  duration?: number;
}

// Rutas
export interface RouteConfig {
  path: string;
  component: React.ComponentType;
  title: string;
  requiresAuth: boolean;
  requiredPermissions?: string[];
}

// API
export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  message?: string;
  errors?: Record<string, string[]>;
}

export interface ApiError {
  message: string;
  status: number;
  errors?: Record<string, string[]>;
}

// Configuración
export interface AppConfig {
  apiBaseUrl: string;
  appName: string;
  version: string;
}

// Permisos disponibles en el sistema
export const Permission = {
  // Usuarios
  USER_CREATE: "user:create",
  USER_READ: "user:read",
  USER_UPDATE: "user:update",
  USER_DELETE: "user:delete",

  // Roles
  ROLE_CREATE: "role:create",
  ROLE_READ: "role:read",
  ROLE_UPDATE: "role:update",
  ROLE_DELETE: "role:delete",

  // Usuario-Roles
  USER_ROLE_CREATE: "user_role:create",
  USER_ROLE_READ: "user_role:read",
  USER_ROLE_DELETE: "user_role:delete",

  // Administración
  ADMIN_ACCESS: "admin:access",
} as const;

export type PermissionType = (typeof Permission)[keyof typeof Permission];

export const PERMISSIONS_LIST = Object.values(Permission);
