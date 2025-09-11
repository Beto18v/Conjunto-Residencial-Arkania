/**
 * Componente Modal reutilizable
 */

import React, { useEffect } from "react";
import { cn } from "../../utils";
import { XMarkIcon } from "@heroicons/react/24/outline";
import Button from "./Button";

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  children: React.ReactNode;
  size?: "sm" | "md" | "lg" | "xl";
  showCloseButton?: boolean;
  closeOnOverlayClick?: boolean;
  preventClose?: boolean;
  footer?: React.ReactNode;
  className?: string;
}

const Modal: React.FC<ModalProps> = ({
  isOpen,
  onClose,
  title,
  children,
  size = "md",
  showCloseButton = true,
  closeOnOverlayClick = true,
  preventClose = false,
  footer,
  className = "",
}) => {
  // Cerrar modal con tecla Escape
  useEffect(() => {
    const handleEscape = (event: KeyboardEvent) => {
      if (event.key === "Escape" && !preventClose) {
        onClose();
      }
    };

    if (isOpen) {
      document.addEventListener("keydown", handleEscape);
      document.body.style.overflow = "hidden";
    }

    return () => {
      document.removeEventListener("keydown", handleEscape);
      document.body.style.overflow = "unset";
    };
  }, [isOpen, onClose, preventClose]);

  const sizeClasses = {
    sm: "max-w-sm",
    md: "max-w-lg",
    lg: "max-w-2xl",
    xl: "max-w-4xl",
  };

  const handleOverlayClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget && closeOnOverlayClick && !preventClose) {
      onClose();
    }
  };

  const handleClose = () => {
    if (!preventClose) {
      onClose();
    }
  };

  if (!isOpen) return null;

  return (
    <div
      className="fixed inset-0 z-50 overflow-y-auto"
      aria-labelledby="modal-title"
      role="dialog"
      aria-modal="true"
    >
      {/* Overlay */}
      <div
        className="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0"
        onClick={handleOverlayClick}
      >
        <div
          className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"
          aria-hidden="true"
        />

        {/* Modal positioning */}
        <span
          className="hidden sm:inline-block sm:align-middle sm:h-screen"
          aria-hidden="true"
        >
          &#8203;
        </span>

        {/* Modal content */}
        <div
          className={cn(
            "relative inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle w-full",
            sizeClasses[size],
            className
          )}
        >
          {/* Header */}
          <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <div className="flex items-center justify-between mb-4">
              <h3
                className="text-lg font-medium leading-6 text-gray-900"
                id="modal-title"
              >
                {title}
              </h3>
              {showCloseButton && (
                <Button
                  variant="outline"
                  size="sm"
                  onClick={handleClose}
                  className="p-2 hover:bg-gray-100"
                  disabled={preventClose}
                >
                  <XMarkIcon className="h-5 w-5" />
                </Button>
              )}
            </div>

            {/* Content */}
            <div className="mt-2">{children}</div>
          </div>

          {/* Footer */}
          {footer && (
            <div className="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              {footer}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Modal;
