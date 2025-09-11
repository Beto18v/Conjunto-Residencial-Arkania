/**
 * Página de Login
 */

import React from "react";
import { Navigate } from "react-router-dom";
import { EyeIcon, EyeSlashIcon } from "@heroicons/react/24/outline";
import { useAuth } from "../hooks";
import { Button, Input, Card, CardContent } from "../components";
import { useForm } from "../hooks";

const LoginPage: React.FC = () => {
  const { login, isAuthenticated, isLoading } = useAuth();
  const [showPassword, setShowPassword] = React.useState(false);

  const { values, errors, handleChange, handleSubmit, isSubmitting } = useForm<{
    username: string;
    password: string;
  }>({
    initialValues: {
      username: "",
      password: "",
    },
    validate: (values) => {
      const errors: Record<string, string> = {};

      if (!values.username.trim()) {
        errors.username = "El nombre de usuario es requerido";
      }

      if (!values.password) {
        errors.password = "La contraseña es requerida";
      } else if (values.password.length < 6) {
        errors.password = "La contraseña debe tener al menos 6 caracteres";
      }

      return errors;
    },
    onSubmit: async (credentials) => {
      await login(credentials);
    },
  });

  // Redirigir si ya está autenticado
  if (isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Verificando autenticación...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        {/* Header */}
        <div className="text-center">
          <div className="mx-auto h-16 w-16 bg-blue-600 rounded-full flex items-center justify-center">
            <span className="text-2xl font-bold text-white">A</span>
          </div>
          <h2 className="mt-6 text-3xl font-extrabold text-gray-900">
            Iniciar Sesión
          </h2>
          <p className="mt-2 text-sm text-gray-600">
            Accede a tu cuenta de Arkania
          </p>
        </div>

        {/* Form */}
        <Card>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-6">
              <Input
                label="Nombre de usuario"
                type="text"
                value={values.username}
                onChange={(e) => handleChange("username")(e.target.value)}
                error={errors.username}
                placeholder="Ingresa tu nombre de usuario"
                required
                disabled={isSubmitting}
                autoComplete="username"
              />

              <Input
                label="Contraseña"
                type={showPassword ? "text" : "password"}
                value={values.password}
                onChange={(e) => handleChange("password")(e.target.value)}
                error={errors.password}
                placeholder="Ingresa tu contraseña"
                required
                disabled={isSubmitting}
                autoComplete="current-password"
                rightIcon={
                  <button
                    type="button"
                    onClick={togglePasswordVisibility}
                    className="text-gray-400 hover:text-gray-600"
                  >
                    {showPassword ? (
                      <EyeSlashIcon className="h-5 w-5" />
                    ) : (
                      <EyeIcon className="h-5 w-5" />
                    )}
                  </button>
                }
              />

              <Button
                type="submit"
                variant="primary"
                size="lg"
                loading={isSubmitting}
                disabled={isSubmitting}
                className="w-full"
              >
                {isSubmitting ? "Iniciando sesión..." : "Iniciar Sesión"}
              </Button>
            </form>
          </CardContent>
        </Card>

        {/* Footer */}
        <div className="text-center">
          <p className="text-xs text-gray-500">
            © 2024 Arkania - Gestión Residencial. Todos los derechos reservados.
          </p>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
