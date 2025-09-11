/**
 * Sistema de notificaciones Toast
 */

import React, { useState, useCallback } from "react";
import {
  CheckCircleIcon,
  ExclamationTriangleIcon,
  XCircleIcon,
  InformationCircleIcon,
  XMarkIcon,
} from "@heroicons/react/24/outline";
import { cn } from "../utils";
import { Button } from "./ui";
import { ToastContext, type Toast } from "../contexts/ToastContext.tsx";
import { useToast } from "../hooks/useToast";

interface ToastItemProps {
  toast: Toast;
  onRemove: (id: string) => void;
}

const ToastItem: React.FC<ToastItemProps> = ({ toast, onRemove }) => {
  const typeConfig = {
    success: {
      icon: CheckCircleIcon,
      bgColor: "bg-green-50",
      iconColor: "text-green-400",
      titleColor: "text-green-800",
      messageColor: "text-green-700",
      borderColor: "border-green-200",
    },
    error: {
      icon: XCircleIcon,
      bgColor: "bg-red-50",
      iconColor: "text-red-400",
      titleColor: "text-red-800",
      messageColor: "text-red-700",
      borderColor: "border-red-200",
    },
    warning: {
      icon: ExclamationTriangleIcon,
      bgColor: "bg-yellow-50",
      iconColor: "text-yellow-400",
      titleColor: "text-yellow-800",
      messageColor: "text-yellow-700",
      borderColor: "border-yellow-200",
    },
    info: {
      icon: InformationCircleIcon,
      bgColor: "bg-blue-50",
      iconColor: "text-blue-400",
      titleColor: "text-blue-800",
      messageColor: "text-blue-700",
      borderColor: "border-blue-200",
    },
  };

  const config = typeConfig[toast.type];
  const Icon = config.icon;

  // Auto-remove toast
  React.useEffect(() => {
    if (toast.autoClose !== false) {
      const timer = setTimeout(() => {
        onRemove(toast.id);
      }, toast.duration || 5000);

      return () => clearTimeout(timer);
    }
  }, [toast.id, toast.duration, toast.autoClose, onRemove]);

  return (
    <div
      className={cn(
        "max-w-sm w-full shadow-lg rounded-lg pointer-events-auto border",
        config.bgColor,
        config.borderColor
      )}
    >
      <div className="p-4">
        <div className="flex items-start">
          <div className="flex-shrink-0">
            <Icon className={cn("h-6 w-6", config.iconColor)} />
          </div>
          <div className="ml-3 w-0 flex-1 pt-0.5">
            <p className={cn("text-sm font-medium", config.titleColor)}>
              {toast.title}
            </p>
            {toast.message && (
              <p className={cn("mt-1 text-sm", config.messageColor)}>
                {toast.message}
              </p>
            )}
          </div>
          <div className="ml-4 flex-shrink-0 flex">
            <Button
              variant="outline"
              size="sm"
              onClick={() => onRemove(toast.id)}
              className="p-1 hover:bg-white/20 border-0"
            >
              <XMarkIcon className="h-5 w-5" />
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

const ToastContainer: React.FC = () => {
  const { toasts, removeToast } = useToast();
  return (
    <div className="fixed top-0 right-0 z-50 p-6 space-y-4 pointer-events-none">
      {toasts.map((toast) => (
        <ToastItem key={toast.id} toast={toast} onRemove={removeToast} />
      ))}
    </div>
  );
};

export const ToastProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [toasts, setToasts] = useState<Toast[]>([]);

  const addToast = useCallback((toast: Omit<Toast, "id">) => {
    const id = Math.random().toString(36).substr(2, 9);
    setToasts((prev) => [...prev, { ...toast, id }]);
  }, []);

  const removeToast = useCallback((id: string) => {
    setToasts((prev) => prev.filter((toast) => toast.id !== id));
  }, []);

  const success = useCallback(
    (title: string, message?: string, duration?: number) => {
      addToast({ type: "success", title, message, duration });
    },
    [addToast]
  );

  const error = useCallback(
    (title: string, message?: string, duration?: number) => {
      addToast({ type: "error", title, message, duration });
    },
    [addToast]
  );

  const warning = useCallback(
    (title: string, message?: string, duration?: number) => {
      addToast({ type: "warning", title, message, duration });
    },
    [addToast]
  );

  const info = useCallback(
    (title: string, message?: string, duration?: number) => {
      addToast({ type: "info", title, message, duration });
    },
    [addToast]
  );

  const value = {
    toasts,
    addToast,
    removeToast,
    success,
    error,
    warning,
    info,
  };

  return (
    <ToastContext.Provider value={value}>
      {children}
      <ToastContainer />
    </ToastContext.Provider>
  );
};

export default ToastProvider;
