/**
 * Hook personalizado para manejo de búsqueda con debounce
 */

import { useState, useEffect, useCallback, useRef } from "react";

interface UseSearchProps {
  onSearch: (term: string) => void;
  debounceMs?: number;
  minLength?: number;
}

interface UseSearchReturn {
  searchTerm: string;
  debouncedSearchTerm: string;
  setSearchTerm: (term: string) => void;
  clearSearch: () => void;
  isSearching: boolean;
}

export const useSearch = ({
  onSearch,
  debounceMs = 300,
  minLength = 2,
}: UseSearchProps): UseSearchReturn => {
  const [searchTerm, setSearchTerm] = useState("");
  const [debouncedSearchTerm, setDebouncedSearchTerm] = useState("");
  const [isSearching, setIsSearching] = useState(false);
  const timeoutRef = useRef<number | undefined>(undefined);

  // Efecto para ejecutar búsqueda cuando cambia el término
  useEffect(() => {
    if (timeoutRef.current) {
      clearTimeout(timeoutRef.current);
    }

    if (searchTerm.length >= minLength || searchTerm.length === 0) {
      setIsSearching(true);

      timeoutRef.current = window.setTimeout(() => {
        setDebouncedSearchTerm(searchTerm);
        setIsSearching(false);
        onSearch(searchTerm);
      }, debounceMs);
    }

    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, [searchTerm, onSearch, debounceMs, minLength]);

  const clearSearch = useCallback(() => {
    setSearchTerm("");
    setDebouncedSearchTerm("");
    setIsSearching(false);
    onSearch("");
  }, [onSearch]);

  return {
    searchTerm,
    debouncedSearchTerm,
    setSearchTerm,
    clearSearch,
    isSearching,
  };
};
