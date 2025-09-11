/**
 * Componente Header principal de la aplicación
 */

import React from "react";
import {
  Bars3Icon,
  BellIcon,
  UserCircleIcon,
  PowerIcon,
} from "@heroicons/react/24/outline";
import { useAuth } from "../../hooks";
import { Button } from "../ui";
import { formatDate } from "../../utils";

interface HeaderProps {
  onToggleSidebar: () => void;
  isSidebarOpen: boolean;
}

const Header: React.FC<HeaderProps> = ({ onToggleSidebar, isSidebarOpen }) => {
  const { user, logout } = useAuth();

  const handleLogout = async () => {
    try {
      await logout();
    } catch (error) {
      console.error("Error al cerrar sesión:", error);
    }
  };

  return (
    <header className="bg-white shadow-sm border-b border-gray-200 sticky top-0 z-30">
      <div className="flex items-center justify-between h-16 px-4 sm:px-6 lg:px-8">
        {/* Left side */}
        <div className="flex items-center space-x-4">
          <Button
            variant="outline"
            size="sm"
            onClick={onToggleSidebar}
            className="p-2 md:hidden"
            aria-label={isSidebarOpen ? "Cerrar menú" : "Abrir menú"}
          >
            <Bars3Icon className="h-5 w-5" />
          </Button>

          <div className="hidden md:block">
            <h1 className="text-xl font-semibold text-gray-900">
              Arkania - Gestión Residencial
            </h1>
          </div>
        </div>

        {/* Center - Breadcrumb or current page info */}
        <div className="hidden lg:flex items-center space-x-2 text-sm text-gray-500">
          <span>Hoy es {formatDate(new Date())}</span>
        </div>

        {/* Right side */}
        <div className="flex items-center space-x-4">
          {/* Notifications */}
          <Button
            variant="outline"
            size="sm"
            className="p-2 relative"
            aria-label="Notificaciones"
          >
            <BellIcon className="h-5 w-5" />
            {/* Badge for notifications */}
            <span className="absolute -top-1 -right-1 h-3 w-3 bg-red-500 rounded-full"></span>
          </Button>

          {/* User menu */}
          <div className="flex items-center space-x-3">
            <div className="hidden md:block text-right">
              <p className="text-sm font-medium text-gray-900">
                {user?.firstName} {user?.lastName}
              </p>
              <p className="text-xs text-gray-500">{user?.email}</p>
            </div>

            <div className="flex items-center space-x-2">
              <Button
                variant="outline"
                size="sm"
                className="p-2"
                aria-label="Perfil de usuario"
              >
                <UserCircleIcon className="h-5 w-5" />
              </Button>

              <Button
                variant="outline"
                size="sm"
                onClick={handleLogout}
                className="p-2 text-red-600 hover:bg-red-50 hover:text-red-700"
                aria-label="Cerrar sesión"
              >
                <PowerIcon className="h-5 w-5" />
              </Button>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
