/**
 * Componente Spinner/Loader reutilizable
 */

import React from "react";
import { cn } from "../../utils";

interface SpinnerProps {
  size?: "xs" | "sm" | "md" | "lg" | "xl";
  color?: "primary" | "secondary" | "white";
  className?: string;
}

const Spinner: React.FC<SpinnerProps> = ({
  size = "md",
  color = "primary",
  className = "",
}) => {
  const sizeClasses = {
    xs: "w-3 h-3",
    sm: "w-4 h-4",
    md: "w-6 h-6",
    lg: "w-8 h-8",
    xl: "w-12 h-12",
  };

  const colorClasses = {
    primary: "text-blue-600",
    secondary: "text-gray-600",
    white: "text-white",
  };

  return (
    <div className={cn("flex items-center justify-center", className)}>
      <svg
        className={cn("animate-spin", sizeClasses[size], colorClasses[color])}
        fill="none"
        viewBox="0 0 24 24"
      >
        <circle
          className="opacity-25"
          cx="12"
          cy="12"
          r="10"
          stroke="currentColor"
          strokeWidth="4"
        />
        <path
          className="opacity-75"
          fill="currentColor"
          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
        />
      </svg>
    </div>
  );
};

interface LoadingOverlayProps {
  isLoading: boolean;
  message?: string;
  children: React.ReactNode;
  className?: string;
}

export const LoadingOverlay: React.FC<LoadingOverlayProps> = ({
  isLoading,
  message = "Cargando...",
  children,
  className = "",
}) => {
  return (
    <div className={cn("relative", className)}>
      {children}
      {isLoading && (
        <div className="absolute inset-0 bg-white bg-opacity-75 flex items-center justify-center z-10">
          <div className="text-center">
            <Spinner size="lg" />
            {message && <p className="mt-2 text-sm text-gray-600">{message}</p>}
          </div>
        </div>
      )}
    </div>
  );
};

export default Spinner;
