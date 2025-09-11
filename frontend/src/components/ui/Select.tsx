/**
 * Componente Select reutilizable
 */

import React, { forwardRef } from "react";
import { cn } from "../../utils";
import { ChevronDownIcon } from "@heroicons/react/24/outline";

interface SelectOption {
  value: string;
  label: string;
  disabled?: boolean;
}

interface SelectProps
  extends Omit<React.SelectHTMLAttributes<HTMLSelectElement>, "size"> {
  label?: string;
  error?: string;
  helperText?: string;
  size?: "sm" | "md" | "lg";
  options: SelectOption[];
  placeholder?: string;
  containerClassName?: string;
}

const Select = forwardRef<HTMLSelectElement, SelectProps>(
  (
    {
      label,
      error,
      helperText,
      size = "md",
      options,
      placeholder,
      containerClassName = "",
      className = "",
      disabled,
      required,
      ...props
    },
    ref
  ) => {
    const baseSelectClasses =
      "w-full border rounded-lg transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:bg-gray-50 disabled:text-gray-500 disabled:cursor-not-allowed appearance-none bg-white pr-10";

    const sizeClasses = {
      sm: "px-3 py-1.5 text-sm",
      md: "px-3 py-2 text-sm",
      lg: "px-4 py-3 text-base",
    };

    const errorClasses = error
      ? "border-red-500 focus:ring-red-500"
      : "border-gray-300";

    const selectClasses = cn(
      baseSelectClasses,
      sizeClasses[size],
      errorClasses,
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
          <select
            ref={ref}
            disabled={disabled}
            className={selectClasses}
            {...props}
          >
            {placeholder && (
              <option value="" disabled>
                {placeholder}
              </option>
            )}
            {options.map((option) => (
              <option
                key={option.value}
                value={option.value}
                disabled={option.disabled}
              >
                {option.label}
              </option>
            ))}
          </select>

          <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
            <ChevronDownIcon className="h-4 w-4 text-gray-400" />
          </div>
        </div>

        {error && <p className="mt-1 text-sm text-red-600">{error}</p>}

        {helperText && !error && (
          <p className="mt-1 text-sm text-gray-500">{helperText}</p>
        )}
      </div>
    );
  }
);

Select.displayName = "Select";

export default Select;
