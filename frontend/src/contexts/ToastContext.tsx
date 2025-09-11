/**
 * Contexto para el sistema de notificaciones Toast
 */

import { createContext } from "react";

export type NotificationType = "success" | "error" | "warning" | "info";

export interface Toast {
  id: string;
  type: NotificationType;
  title: string;
  message?: string;
  duration?: number;
  autoClose?: boolean;
}

interface ToastContextType {
  toasts: Toast[];
  addToast: (toast: Omit<Toast, "id">) => void;
  removeToast: (id: string) => void;
  success: (title: string, message?: string, duration?: number) => void;
  error: (title: string, message?: string, duration?: number) => void;
  warning: (title: string, message?: string, duration?: number) => void;
  info: (title: string, message?: string, duration?: number) => void;
}

export type { ToastContextType };

export const ToastContext = createContext<ToastContextType | null>(null);
