/**
 * Hook personalizado para manejo de paginación
 */

import { useState, useCallback, useMemo } from "react";
import type { PaginationState } from "../types";

interface UsePaginationProps {
  initialPage?: number;
  initialLimit?: number;
  total?: number;
}

interface UsePaginationReturn {
  pagination: PaginationState;
  goToPage: (page: number) => void;
  goToNextPage: () => void;
  goToPreviousPage: () => void;
  goToFirstPage: () => void;
  goToLastPage: () => void;
  setLimit: (limit: number) => void;
  setTotal: (total: number) => void;
  hasNextPage: boolean;
  hasPreviousPage: boolean;
  totalPages: number;
}

export const usePagination = ({
  initialPage = 1,
  initialLimit = 10,
  total = 0,
}: UsePaginationProps = {}): UsePaginationReturn => {
  const [page, setPage] = useState(initialPage);
  const [limit, setLimitState] = useState(initialLimit);
  const [totalItems, setTotalItems] = useState(total);

  const totalPages = useMemo(() => {
    return Math.ceil(totalItems / limit);
  }, [totalItems, limit]);

  const hasNextPage = useMemo(() => {
    return page < totalPages;
  }, [page, totalPages]);

  const hasPreviousPage = useMemo(() => {
    return page > 1;
  }, [page]);

  const goToPage = useCallback(
    (newPage: number) => {
      if (newPage >= 1 && newPage <= totalPages) {
        setPage(newPage);
      }
    },
    [totalPages]
  );

  const goToNextPage = useCallback(() => {
    if (hasNextPage) {
      setPage((prev) => prev + 1);
    }
  }, [hasNextPage]);

  const goToPreviousPage = useCallback(() => {
    if (hasPreviousPage) {
      setPage((prev) => prev - 1);
    }
  }, [hasPreviousPage]);

  const goToFirstPage = useCallback(() => {
    setPage(1);
  }, []);

  const goToLastPage = useCallback(() => {
    setPage(totalPages);
  }, [totalPages]);

  const setLimit = useCallback((newLimit: number) => {
    setLimitState(newLimit);
    setPage(1); // Resetear a la primera página al cambiar el límite
  }, []);

  const setTotal = useCallback(
    (newTotal: number) => {
      setTotalItems(newTotal);
      // Si la página actual excede las páginas totales, ir a la última página
      const newTotalPages = Math.ceil(newTotal / limit);
      if (page > newTotalPages && newTotalPages > 0) {
        setPage(newTotalPages);
      }
    },
    [limit, page]
  );

  const pagination: PaginationState = {
    page,
    limit,
    total: totalItems,
    totalPages,
  };

  return {
    pagination,
    goToPage,
    goToNextPage,
    goToPreviousPage,
    goToFirstPage,
    goToLastPage,
    setLimit,
    setTotal,
    hasNextPage,
    hasPreviousPage,
    totalPages,
  };
};
