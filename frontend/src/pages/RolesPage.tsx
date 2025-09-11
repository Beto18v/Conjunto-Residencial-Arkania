/**
 * Página de gestión de roles
 */

import React, { useState, useEffect, useCallback } from "react";
import {
  PlusIcon,
  MagnifyingGlassIcon,
  PencilIcon,
  TrashIcon,
  EyeIcon,
  ShieldCheckIcon,
} from "@heroicons/react/24/outline";
import {
  Card,
  CardHeader,
  CardTitle,
  CardContent,
  Button,
  Input,
  Table,
  Modal,
  Pagination,
  Spinner,
} from "../components";
import { useLoading, usePagination, useSearch } from "../hooks";
import { formatDate } from "../utils";
import type { Role, TableColumn } from "../types";

const RolesPage: React.FC = () => {
  const [roles, setRoles] = useState<Role[]>([]);
  const [selectedRole, setSelectedRole] = useState<Role | null>(null);
  const [isViewModalOpen, setIsViewModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

  const { isLoading, error, executeAsync } = useLoading();
  const { pagination, goToPage, setTotal } = usePagination({
    initialPage: 1,
    initialLimit: 10,
  });

  // Función para cargar roles
  const loadRoles = useCallback(
    async (search: string = "") => {
      const result = await executeAsync(async () => {
        // Datos simulados hasta tener la API lista
        const mockRoles: Role[] = [
          {
            id: "1",
            name: "Administrador",
            description: "Acceso completo al sistema",
            permissions: [
              "users.create",
              "users.read",
              "users.update",
              "users.delete",
              "roles.create",
              "roles.read",
              "roles.update",
              "roles.delete",
              "user_roles.create",
              "user_roles.read",
              "user_roles.update",
              "user_roles.delete",
            ],
            createdAt: "2024-01-01T00:00:00Z",
            updatedAt: "2024-01-01T00:00:00Z",
          },
          {
            id: "2",
            name: "Portero",
            description: "Gestión de correspondencia y visitantes",
            permissions: [
              "correspondencia.create",
              "correspondencia.read",
              "correspondencia.update",
              "visitantes.create",
              "visitantes.read",
            ],
            createdAt: "2024-01-10T10:00:00Z",
            updatedAt: "2024-01-10T10:00:00Z",
          },
          {
            id: "3",
            name: "Residente",
            description: "Acceso básico para residentes",
            permissions: [
              "profile.read",
              "profile.update",
              "correspondencia.read",
              "areas_comunes.read",
              "solicitudes.create",
              "solicitudes.read",
            ],
            createdAt: "2024-01-15T14:30:00Z",
            updatedAt: "2024-01-15T14:30:00Z",
          },
        ];

        // Filtrar por búsqueda si hay término
        let filteredRoles = mockRoles;
        if (search) {
          filteredRoles = mockRoles.filter(
            (role) =>
              role.name.toLowerCase().includes(search.toLowerCase()) ||
              role.description.toLowerCase().includes(search.toLowerCase())
          );
        }

        setTotal(filteredRoles.length);
        return filteredRoles;
      });

      if (result) {
        setRoles(result);
      }
    },
    [executeAsync, setTotal]
  );

  // Función para buscar roles
  function handleSearch(term: string) {
    loadRoles(term);
  }

  const { searchTerm, setSearchTerm } = useSearch({
    onSearch: handleSearch,
    debounceMs: 300,
  });

  useEffect(() => {
    loadRoles();
  }, [loadRoles]);

  // Definir columnas de la tabla
  const columns: TableColumn<Role>[] = [
    {
      key: "name",
      label: "Nombre",
      render: (role) => (
        <div className="flex items-center">
          <ShieldCheckIcon className="h-6 w-6 text-blue-500 mr-3" />
          <div>
            <div className="font-medium text-gray-900">{role.name}</div>
            <div className="text-sm text-gray-500">{role.description}</div>
          </div>
        </div>
      ),
    },
    {
      key: "permissions",
      label: "Permisos",
      render: (role) => (
        <div className="flex flex-wrap gap-1">
          {role.permissions.slice(0, 3).map((permission) => (
            <span
              key={permission}
              className="inline-flex px-2 py-1 text-xs font-medium rounded-full bg-blue-100 text-blue-800"
            >
              {permission}
            </span>
          ))}
          {role.permissions.length > 3 && (
            <span className="inline-flex px-2 py-1 text-xs font-medium rounded-full bg-gray-100 text-gray-800">
              +{role.permissions.length - 3} más
            </span>
          )}
        </div>
      ),
    },
    {
      key: "createdAt",
      label: "Fecha Creación",
      render: (role) => (
        <span className="text-sm text-gray-500">
          {formatDate(role.createdAt)}
        </span>
      ),
    },
    {
      key: "actions",
      label: "Acciones",
      render: (role) => (
        <div className="flex space-x-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => handleViewRole(role)}
            className="p-1"
          >
            <EyeIcon className="h-4 w-4" />
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={() => handleEditRole(role)}
            className="p-1"
          >
            <PencilIcon className="h-4 w-4" />
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={() => handleDeleteRole(role)}
            className="p-1 text-red-600 hover:bg-red-50"
          >
            <TrashIcon className="h-4 w-4" />
          </Button>
        </div>
      ),
    },
  ];

  // Handlers para las acciones
  const handleCreateRole = () => {
    // TODO: Implementar modal de creación
    console.log("Crear rol");
  };

  const handleViewRole = (role: Role) => {
    setSelectedRole(role);
    setIsViewModalOpen(true);
  };

  const handleEditRole = (role: Role) => {
    // TODO: Implementar modal de edición
    console.log("Editar rol:", role.id);
  };

  const handleDeleteRole = (role: Role) => {
    setSelectedRole(role);
    setIsDeleteModalOpen(true);
  };

  const handleCloseModals = () => {
    setIsViewModalOpen(false);
    setIsDeleteModalOpen(false);
    setSelectedRole(null);
  };

  const confirmDelete = async () => {
    if (selectedRole) {
      await executeAsync(async () => {
        // TODO: Implementar eliminación real
        console.log("Eliminar rol:", selectedRole.id);
        // Simular eliminación
        setRoles((prev) => prev.filter((r) => r.id !== selectedRole.id));
      });
      handleCloseModals();
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Gestión de Roles</h1>
          <p className="text-gray-600">
            Administra los roles y permisos del sistema
          </p>
        </div>
        <Button
          variant="primary"
          onClick={handleCreateRole}
          icon={<PlusIcon className="h-5 w-5" />}
        >
          Nuevo Rol
        </Button>
      </div>

      {/* Filtros y búsqueda */}
      <Card>
        <CardContent className="p-4">
          <div className="flex flex-col sm:flex-row gap-4">
            <div className="flex-1">
              <Input
                placeholder="Buscar roles..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                leftIcon={<MagnifyingGlassIcon className="h-5 w-5" />}
              />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Tabla de roles */}
      <Card>
        <CardHeader>
          <CardTitle>Roles ({roles.length})</CardTitle>
        </CardHeader>
        <CardContent className="p-0">
          {isLoading ? (
            <div className="flex justify-center py-8">
              <Spinner size="lg" />
            </div>
          ) : error ? (
            <div className="text-center py-8 text-red-600">
              <p>{error}</p>
            </div>
          ) : (
            <>
              <Table<Role>
                data={roles}
                columns={columns}
                emptyMessage="No se encontraron roles"
              />

              {/* Paginación */}
              <div className="px-6 py-4 border-t border-gray-200">
                <Pagination
                  currentPage={pagination.page}
                  totalPages={pagination.totalPages}
                  onPageChange={goToPage}
                />
              </div>
            </>
          )}
        </CardContent>
      </Card>

      {/* Modal de vista de rol */}
      <Modal
        isOpen={isViewModalOpen}
        onClose={handleCloseModals}
        title="Detalles del Rol"
        size="lg"
      >
        {selectedRole && (
          <div className="space-y-6">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Nombre del rol
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {selectedRole.name}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Fecha de creación
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {formatDate(selectedRole.createdAt, true)}
                </p>
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Descripción
              </label>
              <p className="text-sm text-gray-900">
                {selectedRole.description}
              </p>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-3">
                Permisos ({selectedRole.permissions.length})
              </label>
              <div className="grid grid-cols-2 gap-2">
                {selectedRole.permissions.map((permission) => (
                  <span
                    key={permission}
                    className="inline-flex px-3 py-2 text-sm font-medium rounded-md bg-blue-100 text-blue-800"
                  >
                    {permission}
                  </span>
                ))}
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4 pt-4 border-t border-gray-200">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Fecha de creación
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {formatDate(selectedRole.createdAt, true)}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Última actualización
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {formatDate(selectedRole.updatedAt, true)}
                </p>
              </div>
            </div>
          </div>
        )}
      </Modal>

      {/* Modal de confirmación de eliminación */}
      <Modal
        isOpen={isDeleteModalOpen}
        onClose={handleCloseModals}
        title="Confirmar Eliminación"
        size="sm"
        footer={
          <div className="flex space-x-3">
            <Button variant="outline" onClick={handleCloseModals}>
              Cancelar
            </Button>
            <Button variant="danger" onClick={confirmDelete}>
              Eliminar
            </Button>
          </div>
        }
      >
        {selectedRole && (
          <p>
            ¿Estás seguro de que deseas eliminar el rol{" "}
            <strong>{selectedRole.name}</strong>? Esta acción no se puede
            deshacer y afectará a todos los usuarios que tengan este rol
            asignado.
          </p>
        )}
      </Modal>

      {/* TODO: Modales de creación y edición */}
    </div>
  );
};

export default RolesPage;
