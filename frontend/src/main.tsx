/**
 * Punto de entrada principal de la aplicación Conjunto Residencial Arkania
 *
 * Este archivo inicializa la aplicación React montándola en el DOM.
 * Se encarga de:
 * - Importar y aplicar los estilos globales
 * - Crear el root de React con createRoot
 * - Renderizar el componente App dentro de StrictMode para desarrollo
 * - Gestionar el ciclo de vida inicial de la aplicación
 */

import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
