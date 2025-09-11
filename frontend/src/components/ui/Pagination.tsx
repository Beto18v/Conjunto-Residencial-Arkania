/**
 * Componente Pagination reutilizable
 */

import React from "react";
import { cn } from "../../utils";
import {
  ChevronLeftIcon,
  ChevronRightIcon,
  ChevronDoubleLeftIcon,
  ChevronDoubleRightIcon,
} from "@heroicons/react/24/outline";
import Button from "./Button";

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  showFirstLast?: boolean;
  showPrevNext?: boolean;
  maxVisiblePages?: number;
  className?: string;
  size?: "sm" | "md" | "lg";
}

const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  totalPages,
  onPageChange,
  showFirstLast = true,
  showPrevNext = true,
  maxVisiblePages = 5,
  className = "",
  size = "md",
}) => {
  if (totalPages <= 1) return null;

  const getVisiblePages = () => {
    const pages: number[] = [];
    const halfVisible = Math.floor(maxVisiblePages / 2);

    let startPage = Math.max(1, currentPage - halfVisible);
    let endPage = Math.min(totalPages, currentPage + halfVisible);

    // Ajustar si no hay suficientes páginas en un lado
    if (endPage - startPage + 1 < maxVisiblePages) {
      if (startPage === 1) {
        endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);
      } else {
        startPage = Math.max(1, endPage - maxVisiblePages + 1);
      }
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }

    return pages;
  };

  const visiblePages = getVisiblePages();
  const hasPrevious = currentPage > 1;
  const hasNext = currentPage < totalPages;

  const buttonSizeClasses = {
    sm: "px-2 py-1 text-xs",
    md: "px-3 py-2 text-sm",
    lg: "px-4 py-2 text-base",
  };

  return (
    <nav
      className={cn("flex items-center justify-center space-x-1", className)}
      aria-label="Paginación"
    >
      {/* First page */}
      {showFirstLast && currentPage > 1 && (
        <Button
          variant="outline"
          size={size}
          onClick={() => onPageChange(1)}
          className={cn("flex items-center", buttonSizeClasses[size])}
          disabled={!hasPrevious}
        >
          <ChevronDoubleLeftIcon className="h-4 w-4" />
          <span className="sr-only">Primera página</span>
        </Button>
      )}

      {/* Previous page */}
      {showPrevNext && (
        <Button
          variant="outline"
          size={size}
          onClick={() => onPageChange(currentPage - 1)}
          className={cn("flex items-center", buttonSizeClasses[size])}
          disabled={!hasPrevious}
        >
          <ChevronLeftIcon className="h-4 w-4" />
          <span className="sr-only">Página anterior</span>
        </Button>
      )}

      {/* Page numbers */}
      {visiblePages.map((page) => (
        <Button
          key={page}
          variant={page === currentPage ? "primary" : "outline"}
          size={size}
          onClick={() => onPageChange(page)}
          className={cn(
            buttonSizeClasses[size],
            page === currentPage && "bg-blue-600 text-white border-blue-600"
          )}
        >
          {page}
        </Button>
      ))}

      {/* Next page */}
      {showPrevNext && (
        <Button
          variant="outline"
          size={size}
          onClick={() => onPageChange(currentPage + 1)}
          className={cn("flex items-center", buttonSizeClasses[size])}
          disabled={!hasNext}
        >
          <ChevronRightIcon className="h-4 w-4" />
          <span className="sr-only">Página siguiente</span>
        </Button>
      )}

      {/* Last page */}
      {showFirstLast && currentPage < totalPages && (
        <Button
          variant="outline"
          size={size}
          onClick={() => onPageChange(totalPages)}
          className={cn("flex items-center", buttonSizeClasses[size])}
          disabled={!hasNext}
        >
          <ChevronDoubleRightIcon className="h-4 w-4" />
          <span className="sr-only">Última página</span>
        </Button>
      )}
    </nav>
  );
};

export default Pagination;
