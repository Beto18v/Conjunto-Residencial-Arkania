/**
 * Componente Input reutilizable
 */

import React, { forwardRef } from "react";
import { cn } from "../../utils";

interface InputProps
  extends Omit<React.InputHTMLAttributes<HTMLInputElement>, "size"> {
  label?: string;
  error?: string;
  helperText?: string;
  size?: "sm" | "md" | "lg";
  variant?: "default" | "filled";
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  containerClassName?: string;
}

const Input = forwardRef<HTMLInputElement, InputProps>(
  (
    {
      label,
      error,
      helperText,
      size = "md",
      variant = "default",
      leftIcon,
      rightIcon,
      containerClassName = "",
      className = "",
      disabled,
      required,
      ...props
    },
    ref
  ) => {
    const baseInputClasses =
      "w-full border rounded-lg transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:bg-gray-50 disabled:text-gray-500 disabled:cursor-not-allowed";

    const variantClasses = {
      default: "border-gray-300 bg-white",
      filled: "border-gray-200 bg-gray-50",
    };

    const sizeClasses = {
      sm: "px-3 py-1.5 text-sm",
      md: "px-3 py-2 text-sm",
      lg: "px-4 py-3 text-base",
    };

    const errorClasses = error ? "border-red-500 focus:ring-red-500" : "";

    const inputClasses = cn(
      baseInputClasses,
      variantClasses[variant],
      sizeClasses[size],
      errorClasses,
      {
        "pl-10": leftIcon,
        "pr-10": rightIcon,
      },
      className
    );

    return (
      <div className={cn("w-full", containerClassName)}>
        {label && (
          <label className="block text-sm font-medium text-gray-700 mb-1">
            {label}
            {required && <span className="text-red-500 ml-1">*</span>}
          </label>
        )}

        <div className="relative">
          {leftIcon && (
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <div className="text-gray-400">{leftIcon}</div>
            </div>
          )}

          <input
            ref={ref}
            disabled={disabled}
            className={inputClasses}
            {...props}
          />

          {rightIcon && (
            <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
              <div className="text-gray-400">{rightIcon}</div>
            </div>
          )}
        </div>

        {error && <p className="mt-1 text-sm text-red-600">{error}</p>}

        {helperText && !error && (
          <p className="mt-1 text-sm text-gray-500">{helperText}</p>
        )}
      </div>
    );
  }
);

Input.displayName = "Input";

export default Input;
