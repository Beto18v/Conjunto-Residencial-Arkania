/**
 * Configuración principal de la aplicación
 */

export const config = {
  // API Configuration
  apiBaseUrl: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api",

  // App Information
  appName: "Arkania - Gestión Residencial",
  version: "1.0.0",

  // Authentication
  tokenKey: "arkania_token",
  tokenExpirationBuffer: 5 * 60 * 1000, // 5 minutos antes de que expire el token

  // Pagination
  defaultPageSize: 10,
  maxPageSize: 100,

  // UI Configuration
  defaultModalSize: "md" as const,
  toastDuration: 5000,

  // Validation
  passwordMinLength: 6,
  usernameMinLength: 3,

  // Date Format
  dateFormat: "dd/MM/yyyy",
  dateTimeFormat: "dd/MM/yyyy HH:mm",
} as const;

// Validate required environment variables
if (!config.apiBaseUrl) {
  console.warn(
    "VITE_API_BASE_URL no está configurada. Usando URL por defecto."
  );
}

export default config;
