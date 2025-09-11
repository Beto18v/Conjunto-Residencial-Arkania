/**
 * Componente principal de la aplicación Conjunto Residencial Arkania
 *
 * Este componente es el punto de entrada principal de la aplicación React.
 * Se encarga de:
 * - Configurar el enrutamiento con React Router
 * - Proporcionar los contextos de autenticación y notificaciones
 * - Definir las rutas públicas y protegidas
 * - Gestionar la navegación y protección de rutas basada en autenticación
 * - Renderizar el layout principal para rutas autenticadas
 */

import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import { useAuth } from "./hooks";
import { AuthProvider } from "./components/AuthProvider";
import { ToastProvider } from "./components";
import { MainLayout } from "./components/layout";
import {
  LoginPage,
  DashboardPage,
  UsersPage,
  RolesPage,
  UserRolesPage,
} from "./pages";

// Componente para rutas protegidas
const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Cargando...</p>
        </div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <MainLayout>{children}</MainLayout>;
};

// Componente de rutas principales
const AppRoutes: React.FC = () => {
  return (
    <Routes>
      {/* Ruta pública */}
      <Route path="/login" element={<LoginPage />} />

      {/* Rutas protegidas */}
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <DashboardPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/users"
        element={
          <ProtectedRoute>
            <UsersPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/roles"
        element={
          <ProtectedRoute>
            <RolesPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/user-roles"
        element={
          <ProtectedRoute>
            <UserRolesPage />
          </ProtectedRoute>
        }
      />

      {/* Ruta por defecto */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
};

// Componente principal de la aplicación
const App: React.FC = () => {
  return (
    <Router>
      <ToastProvider>
        <AuthProvider>
          <AppRoutes />
        </AuthProvider>
      </ToastProvider>
    </Router>
  );
};

export default App;
