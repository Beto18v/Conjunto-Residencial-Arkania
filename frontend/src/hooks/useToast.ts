/**
 * Hook para usar el sistema de notificaciones Toast
 */

import { useContext } from "react";
import {
  ToastContext,
  type ToastContextType,
} from "../contexts/ToastContext.tsx";

export const useToast = (): ToastContextType => {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error("useToast debe ser usado dentro de un ToastProvider");
  }
  return context;
};
