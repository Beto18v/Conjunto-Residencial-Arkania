/**
 * Página de gestión de usuarios
 */

import React, { useState, useEffect, useCallback } from "react";
import {
  PlusIcon,
  MagnifyingGlassIcon,
  PencilIcon,
  TrashIcon,
  EyeIcon,
  UserCircleIcon,
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
import type { User, TableColumn } from "../types";

const UsersPage: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [isViewModalOpen, setIsViewModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

  const { isLoading, error, executeAsync } = useLoading();
  const { pagination, goToPage, setTotal } = usePagination({
    initialPage: 1,
    initialLimit: 10,
  });

  // Función para cargar usuarios con useCallback para evitar dependencias faltantes
  const loadUsers = useCallback(
    async (search: string = "") => {
      const result = await executeAsync(async () => {
        // Datos simulados hasta tener la API lista
        const mockUsers: User[] = [
          {
            id: "1",
            username: "admin",
            email: "admin@arkania.com",
            firstName: "Administrador",
            lastName: "Sistema",
            phoneNumber: "+57 300 123 4567",
            isActive: true,
            createdAt: "2024-01-15T10:00:00Z",
            updatedAt: "2024-01-15T10:00:00Z",
          },
          {
            id: "2",
            username: "jperez",
            email: "juan.perez@arkania.com",
            firstName: "Juan",
            lastName: "Pérez",
            phoneNumber: "+57 300 987 6543",
            isActive: true,
            createdAt: "2024-02-01T14:30:00Z",
            updatedAt: "2024-02-01T14:30:00Z",
          },
          {
            id: "3",
            username: "mgarcia",
            email: "maria.garcia@arkania.com",
            firstName: "María",
            lastName: "García",
            phoneNumber: "+57 300 555 1234",
            isActive: false,
            createdAt: "2024-01-20T09:15:00Z",
            updatedAt: "2024-02-10T16:45:00Z",
          },
        ];

        // Filtrar por búsqueda si hay término
        let filteredUsers = mockUsers;
        if (search) {
          filteredUsers = mockUsers.filter(
            (user) =>
              user.firstName.toLowerCase().includes(search.toLowerCase()) ||
              user.lastName.toLowerCase().includes(search.toLowerCase()) ||
              user.email.toLowerCase().includes(search.toLowerCase()) ||
              user.username.toLowerCase().includes(search.toLowerCase())
          );
        }

        setTotal(filteredUsers.length);
        return filteredUsers;
      });

      if (result) {
        setUsers(result);
      }
    },
    [executeAsync, setTotal]
  );

  // Función para buscar usuarios
  function handleSearch(term: string) {
    loadUsers(term);
  }

  const { searchTerm, setSearchTerm } = useSearch({
    onSearch: handleSearch,
    debounceMs: 300,
  });

  useEffect(() => {
    loadUsers();
  }, [loadUsers]);

  // Definir columnas de la tabla
  const columns: TableColumn<User>[] = [
    {
      key: "firstName",
      label: "Nombre Completo",
      render: (user) => (
        <div className="flex items-center">
          <UserCircleIcon className="h-8 w-8 text-gray-400 mr-3" />
          <div>
            <div className="font-medium text-gray-900">
              {user.firstName} {user.lastName}
            </div>
            <div className="text-sm text-gray-500">@{user.username}</div>
          </div>
        </div>
      ),
    },
    {
      key: "email",
      label: "Email",
      render: (user) => (
        <span className="text-sm text-gray-900">{user.email}</span>
      ),
    },
    {
      key: "phoneNumber",
      label: "Teléfono",
      render: (user) => (
        <span className="text-sm text-gray-900">{user.phoneNumber || "-"}</span>
      ),
    },
    {
      key: "isActive",
      label: "Estado",
      render: (user) => (
        <span
          className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
            user.isActive
              ? "bg-green-100 text-green-800"
              : "bg-red-100 text-red-800"
          }`}
        >
          {user.isActive ? "Activo" : "Inactivo"}
        </span>
      ),
    },
    {
      key: "createdAt",
      label: "Fecha Creación",
      render: (user) => (
        <span className="text-sm text-gray-500">
          {formatDate(user.createdAt)}
        </span>
      ),
    },
    {
      key: "actions",
      label: "Acciones",
      render: (user) => (
        <div className="flex space-x-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => handleViewUser(user)}
            className="p-1"
          >
            <EyeIcon className="h-4 w-4" />
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={() => handleEditUser(user)}
            className="p-1"
          >
            <PencilIcon className="h-4 w-4" />
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={() => handleDeleteUser(user)}
            className="p-1 text-red-600 hover:bg-red-50"
          >
            <TrashIcon className="h-4 w-4" />
          </Button>
        </div>
      ),
    },
  ];

  // Handlers para las acciones
  const handleCreateUser = () => {
    // TODO: Implementar modal de creación
    console.log("Crear usuario");
  };

  const handleViewUser = (user: User) => {
    setSelectedUser(user);
    setIsViewModalOpen(true);
  };

  const handleEditUser = (user: User) => {
    // TODO: Implementar modal de edición
    console.log("Editar usuario:", user.id);
  };

  const handleDeleteUser = (user: User) => {
    setSelectedUser(user);
    setIsDeleteModalOpen(true);
  };

  const handleCloseModals = () => {
    setIsViewModalOpen(false);
    setIsDeleteModalOpen(false);
    setSelectedUser(null);
  };

  const confirmDelete = async () => {
    if (selectedUser) {
      await executeAsync(async () => {
        // TODO: Implementar eliminación real
        console.log("Eliminar usuario:", selectedUser.id);
        // Simular eliminación
        setUsers((prev) => prev.filter((u) => u.id !== selectedUser.id));
      });
      handleCloseModals();
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Gestión de Usuarios
          </h1>
          <p className="text-gray-600">
            Administra los usuarios del sistema residencial
          </p>
        </div>
        <Button
          variant="primary"
          onClick={handleCreateUser}
          icon={<PlusIcon className="h-5 w-5" />}
        >
          Nuevo Usuario
        </Button>
      </div>

      {/* Filtros y búsqueda */}
      <Card>
        <CardContent className="p-4">
          <div className="flex flex-col sm:flex-row gap-4">
            <div className="flex-1">
              <Input
                placeholder="Buscar usuarios..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                leftIcon={<MagnifyingGlassIcon className="h-5 w-5" />}
              />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Tabla de usuarios */}
      <Card>
        <CardHeader>
          <CardTitle>Usuarios ({users.length})</CardTitle>
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
              <Table<User>
                data={users}
                columns={columns}
                emptyMessage="No se encontraron usuarios"
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

      {/* Modal de vista de usuario */}
      <Modal
        isOpen={isViewModalOpen}
        onClose={handleCloseModals}
        title="Detalles del Usuario"
        size="md"
      >
        {selectedUser && (
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Nombre de usuario
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {selectedUser.username}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Email
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {selectedUser.email}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Nombre
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {selectedUser.firstName}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Apellido
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {selectedUser.lastName}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Teléfono
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {selectedUser.phoneNumber || "No especificado"}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Estado
                </label>
                <p className="mt-1">
                  <span
                    className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                      selectedUser.isActive
                        ? "bg-green-100 text-green-800"
                        : "bg-red-100 text-red-800"
                    }`}
                  >
                    {selectedUser.isActive ? "Activo" : "Inactivo"}
                  </span>
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Fecha de creación
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {formatDate(selectedUser.createdAt, true)}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Última actualización
                </label>
                <p className="mt-1 text-sm text-gray-900">
                  {formatDate(selectedUser.updatedAt, true)}
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
        {selectedUser && (
          <p>
            ¿Estás seguro de que deseas eliminar el usuario{" "}
            <strong>
              {selectedUser.firstName} {selectedUser.lastName}
            </strong>
            ? Esta acción no se puede deshacer.
          </p>
        )}
      </Modal>

      {/* TODO: Modales de creación y edición */}
    </div>
  );
};

export default UsersPage;
