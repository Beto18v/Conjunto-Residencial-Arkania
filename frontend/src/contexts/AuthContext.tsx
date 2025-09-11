/**
 * Contexto de autenticación para la aplicación Conjunto Residencial Arkania
 *
 * Este contexto proporciona el estado global de autenticación, incluyendo:
 * - Información del usuario autenticado
 * - Roles del usuario
 * - Token de acceso
 * - Estado de carga
 * - Funciones de login y logout
 *
 * Se utiliza junto con AuthProvider para gestionar la autenticación en toda la app.
 */

import React from "react";
import type { AuthContextType } from "../types";

export const AuthContext = React.createContext<AuthContextType | undefined>(
  undefined
);
