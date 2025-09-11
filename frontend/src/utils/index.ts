/**
 * Utilidades generales de la aplicación
 */

import { type ClassValue, clsx } from "clsx";

/**
 * Combina clases CSS de forma segura
 */
export function cn(...inputs: ClassValue[]) {
  return clsx(inputs);
}

/**
 * Formatea una fecha a string legible
 */
export function formatDate(date: string | Date, includeTime = false): string {
  const dateObj = typeof date === "string" ? new Date(date) : date;

  if (isNaN(dateObj.getTime())) {
    return "Fecha inválida";
  }

  const options: Intl.DateTimeFormatOptions = {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  };

  if (includeTime) {
    options.hour = "2-digit";
    options.minute = "2-digit";
  }

  return dateObj.toLocaleDateString("es-ES", options);
}

/**
 * Capitaliza la primera letra de un string
 */
export function capitalize(str: string): string {
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

/**
 * Trunca un texto a una longitud específica
 */
export function truncateText(text: string, maxLength: number): string {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength) + "...";
}

/**
 * Valida si un email es válido
 */
export function isValidEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

/**
 * Valida si una contraseña cumple los requisitos mínimos
 */
export function isValidPassword(password: string): boolean {
  return password.length >= 6;
}

/**
 * Genera un ID único simple
 */
export function generateId(): string {
  return Math.random().toString(36).substr(2, 9);
}

/**
 * Debounce function para optimizar búsquedas
 */
export function debounce<T extends (...args: unknown[]) => unknown>(
  func: T,
  wait: number
): (...args: Parameters<T>) => void {
  let timeout: number;

  return (...args: Parameters<T>) => {
    clearTimeout(timeout);
    timeout = window.setTimeout(() => func(...args), wait);
  };
}

/**
 * Convierte un objeto a query string
 */
export function objectToQueryString(obj: Record<string, unknown>): string {
  const params = new URLSearchParams();

  Object.entries(obj).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== "") {
      params.append(key, String(value));
    }
  });

  return params.toString();
}

/**
 * Copia texto al portapapeles
 */
export async function copyToClipboard(text: string): Promise<boolean> {
  try {
    await navigator.clipboard.writeText(text);
    return true;
  } catch (error) {
    console.error("Error copying to clipboard:", error);
    return false;
  }
}

/**
 * Maneja errores de la API y extrae el mensaje apropiado
 */
export function getErrorMessage(error: unknown): string {
  if (typeof error === "string") return error;

  if (error && typeof error === "object") {
    const errorObj = error as {
      response?: { data?: { message?: string } };
      message?: string;
    };
    if (errorObj.response?.data?.message) return errorObj.response.data.message;
    if (errorObj.message) return errorObj.message;
  }

  return "Ha ocurrido un error inesperado";
}

/**
 * Valida si una string contiene solo letras y espacios
 */
export function isValidName(name: string): boolean {
  const nameRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
  return nameRegex.test(name.trim());
}

/**
 * Valida si un número de teléfono es válido (formato colombiano)
 */
export function isValidPhone(phone: string): boolean {
  const phoneRegex = /^(\+57)?[3][0-9]{9}$/;
  return phoneRegex.test(phone.replace(/\s/g, ""));
}

/**
 * Formatea un número de teléfono para mostrar
 */
export function formatPhone(phone: string): string {
  const cleaned = phone.replace(/\D/g, "");
  if (cleaned.length === 10) {
    return cleaned.replace(/(\d{3})(\d{3})(\d{4})/, "$1 $2 $3");
  }
  return phone;
}

/**
 * Ordena un array de objetos por una clave específica
 */
export function sortBy<T>(
  array: T[],
  key: keyof T,
  direction: "asc" | "desc" = "asc"
): T[] {
  return [...array].sort((a, b) => {
    const aVal = a[key];
    const bVal = b[key];

    if (aVal < bVal) return direction === "asc" ? -1 : 1;
    if (aVal > bVal) return direction === "asc" ? 1 : -1;
    return 0;
  });
}

/**
 * Filtra un array de objetos por un término de búsqueda
 */
export function filterBySearch<T>(
  array: T[],
  searchTerm: string,
  searchKeys: (keyof T)[]
): T[] {
  if (!searchTerm.trim()) return array;

  const lowerSearchTerm = searchTerm.toLowerCase();

  return array.filter((item) =>
    searchKeys.some((key) => {
      const value = item[key];
      return String(value).toLowerCase().includes(lowerSearchTerm);
    })
  );
}
