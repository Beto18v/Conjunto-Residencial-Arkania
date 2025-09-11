/**
 * Componente Sidebar de navegación principal
 */

import React from "react";
import { NavLink, useLocation } from "react-router-dom";
import {
  HomeIcon,
  UsersIcon,
  ShieldCheckIcon,
  UserPlusIcon,
  XMarkIcon,
} from "@heroicons/react/24/outline";
import { cn } from "../../utils";
import { Button } from "../ui";

interface SidebarProps {
  isOpen: boolean;
  onClose: () => void;
}

interface NavigationItem {
  name: string;
  href: string;
  icon: React.ComponentType<{ className?: string }>;
  description: string;
}

const navigation: NavigationItem[] = [
  {
    name: "Dashboard",
    href: "/",
    icon: HomeIcon,
    description: "Panel principal de control",
  },
  {
    name: "Usuarios",
    href: "/users",
    icon: UsersIcon,
    description: "Gestión de usuarios del sistema",
  },
  {
    name: "Roles",
    href: "/roles",
    icon: ShieldCheckIcon,
    description: "Administración de roles y permisos",
  },
  {
    name: "Asignación de Roles",
    href: "/user-roles",
    icon: UserPlusIcon,
    description: "Asignar roles a usuarios",
  },
];

const Sidebar: React.FC<SidebarProps> = ({ isOpen, onClose }) => {
  const location = useLocation();

  const isActiveRoute = (href: string) => {
    if (href === "/") {
      return location.pathname === "/";
    }
    return location.pathname.startsWith(href);
  };

  return (
    <>
      {/* Mobile overlay */}
      {isOpen && (
        <div
          className="fixed inset-0 bg-gray-600 bg-opacity-75 z-40 md:hidden"
          onClick={onClose}
        />
      )}

      {/* Sidebar */}
      <div
        className={cn(
          "fixed inset-y-0 left-0 z-50 w-64 bg-white shadow-lg transform transition-transform duration-300 ease-in-out md:translate-x-0 md:static md:inset-0",
          isOpen ? "translate-x-0" : "-translate-x-full"
        )}
      >
        {/* Sidebar header */}
        <div className="flex items-center justify-between h-16 px-6 border-b border-gray-200">
          <div className="flex items-center space-x-2">
            <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-sm">A</span>
            </div>
            <span className="text-lg font-semibold text-gray-900">Arkania</span>
          </div>

          <Button
            variant="outline"
            size="sm"
            onClick={onClose}
            className="p-2 md:hidden"
            aria-label="Cerrar menú"
          >
            <XMarkIcon className="h-5 w-5" />
          </Button>
        </div>

        {/* Navigation */}
        <nav className="mt-8 px-4">
          <div className="space-y-2">
            {navigation.map((item) => {
              const isActive = isActiveRoute(item.href);

              return (
                <NavLink
                  key={item.name}
                  to={item.href}
                  onClick={() => {
                    // Cerrar sidebar en mobile después de navegar
                    if (window.innerWidth < 768) {
                      onClose();
                    }
                  }}
                  className={({ isActive: linkIsActive }) =>
                    cn(
                      "group flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200",
                      linkIsActive || isActive
                        ? "bg-blue-50 text-blue-700 border-r-2 border-blue-600"
                        : "text-gray-700 hover:bg-gray-100 hover:text-gray-900"
                    )
                  }
                >
                  <item.icon
                    className={cn(
                      "mr-3 h-5 w-5 flex-shrink-0",
                      isActive
                        ? "text-blue-600"
                        : "text-gray-400 group-hover:text-gray-600"
                    )}
                    aria-hidden="true"
                  />
                  <div>
                    <div className="font-medium">{item.name}</div>
                    <div className="text-xs text-gray-500 group-hover:text-gray-600">
                      {item.description}
                    </div>
                  </div>
                </NavLink>
              );
            })}
          </div>
        </nav>

        {/* Footer */}
        <div className="absolute bottom-0 left-0 right-0 p-4 border-t border-gray-200">
          <div className="text-xs text-gray-500 text-center">
            <p>© 2024 Arkania</p>
            <p>Versión 1.0.0</p>
          </div>
        </div>
      </div>
    </>
  );
};

export default Sidebar;
