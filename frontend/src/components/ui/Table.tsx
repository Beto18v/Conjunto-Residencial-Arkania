/**
 * Componente Table reutilizable para mostrar datos tabulares
 */

import React from "react";
import { cn } from "../../utils";
import { ChevronUpIcon, ChevronDownIcon } from "@heroicons/react/24/outline";
import Button from "./Button";

export interface TableColumn<T> {
  key: keyof T | string;
  label: string;
  sortable?: boolean;
  render?: (item: T, index: number) => React.ReactNode;
  className?: string;
  headerClassName?: string;
}

interface TableProps<T = Record<string, unknown>> {
  data: T[];
  columns: TableColumn<T>[];
  loading?: boolean;
  emptyMessage?: string;
  onSort?: (key: string, direction: "asc" | "desc") => void;
  sortKey?: string;
  sortDirection?: "asc" | "desc";
  className?: string;
  striped?: boolean;
  hover?: boolean;
}

const Table = <T extends Record<string, unknown>>({
  data,
  columns,
  loading = false,
  emptyMessage = "No hay datos para mostrar",
  onSort,
  sortKey,
  sortDirection,
  className = "",
  striped = true,
  hover = true,
}: TableProps<T>) => {
  const handleSort = (columnKey: string) => {
    if (!onSort) return;

    const newDirection =
      sortKey === columnKey && sortDirection === "asc" ? "desc" : "asc";

    onSort(columnKey, newDirection);
  };

  const getSortIcon = (columnKey: string) => {
    if (sortKey !== columnKey) return null;

    return sortDirection === "asc" ? (
      <ChevronUpIcon className="h-4 w-4" />
    ) : (
      <ChevronDownIcon className="h-4 w-4" />
    );
  };

  const getValue = (item: T, key: string): unknown => {
    return key.split(".").reduce((obj: unknown, k: string) => {
      if (obj && typeof obj === "object" && k in obj) {
        return (obj as Record<string, unknown>)[k];
      }
      return undefined;
    }, item);
  };

  if (loading) {
    return (
      <div className="w-full">
        <div className="animate-pulse">
          {/* Header skeleton */}
          <div className="bg-gray-50 rounded-t-lg">
            <div className="grid grid-cols-12 gap-4 p-4">
              {columns.map((_, index) => (
                <div key={index} className="col-span-2">
                  <div className="h-4 bg-gray-200 rounded"></div>
                </div>
              ))}
            </div>
          </div>

          {/* Rows skeleton */}
          {Array.from({ length: 5 }).map((_, rowIndex) => (
            <div key={rowIndex} className="border-b border-gray-200">
              <div className="grid grid-cols-12 gap-4 p-4">
                {columns.map((_, colIndex) => (
                  <div key={colIndex} className="col-span-2">
                    <div className="h-4 bg-gray-200 rounded"></div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  }

  return (
    <div
      className={cn(
        "w-full overflow-hidden rounded-lg border border-gray-200",
        className
      )}
    >
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              {columns.map((column) => (
                <th
                  key={String(column.key)}
                  className={cn(
                    "px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider",
                    column.headerClassName
                  )}
                >
                  {column.sortable && onSort ? (
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => handleSort(String(column.key))}
                      className="flex items-center space-x-1 p-0 border-0 bg-transparent hover:bg-gray-100 text-gray-500 hover:text-gray-700"
                    >
                      <span>{column.label}</span>
                      {getSortIcon(String(column.key))}
                    </Button>
                  ) : (
                    column.label
                  )}
                </th>
              ))}
            </tr>
          </thead>

          <tbody className="bg-white divide-y divide-gray-200">
            {data.length === 0 ? (
              <tr>
                <td
                  colSpan={columns.length}
                  className="px-6 py-12 text-center text-gray-500"
                >
                  {emptyMessage}
                </td>
              </tr>
            ) : (
              data.map((item, index) => (
                <tr
                  key={index}
                  className={cn({
                    "bg-gray-50": striped && index % 2 === 1,
                    "hover:bg-gray-100": hover,
                  })}
                >
                  {columns.map((column) => (
                    <td
                      key={String(column.key)}
                      className={cn(
                        "px-6 py-4 whitespace-nowrap text-sm text-gray-900",
                        column.className
                      )}
                    >
                      {column.render
                        ? column.render(item, index)
                        : String(getValue(item, String(column.key)) || "-")}
                    </td>
                  ))}
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Table;
