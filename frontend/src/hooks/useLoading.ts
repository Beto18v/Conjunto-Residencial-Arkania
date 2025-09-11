/**
 * Hook personalizado para manejo de estado de carga y errores
 */

import { useState, useCallback } from "react";

interface UseLoadingState {
  isLoading: boolean;
  error: string | null;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
  clearError: () => void;
  executeAsync: <T>(asyncFunction: () => Promise<T>) => Promise<T | null>;
}

export const useLoading = (): UseLoadingState => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const setLoading = useCallback((loading: boolean) => {
    setIsLoading(loading);
    if (loading) {
      setError(null); // Limpiar error al comenzar nueva operación
    }
  }, []);

  const clearError = useCallback(() => {
    setError(null);
  }, []);

  const executeAsync = useCallback(
    async <T>(asyncFunction: () => Promise<T>): Promise<T | null> => {
      setIsLoading(true);
      setError(null);

      try {
        const result = await asyncFunction();
        return result;
      } catch (err) {
        const errorMessage =
          err instanceof Error ? err.message : "Error desconocido";
        setError(errorMessage);
        return null;
      } finally {
        setIsLoading(false);
      }
    },
    []
  );

  return {
    isLoading,
    error,
    setLoading,
    setError,
    clearError,
    executeAsync,
  };
};
