/**
 * Página de gestión de asignación de roles a usuarios
 */

import React, { useState, useEffect, useCallback } from "react";
import {
  PlusIcon,
  MagnifyingGlassIcon,
  TrashIcon,
  EyeIcon,
  UserIcon,
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
  Select,
} from "../components";
import { useLoading, usePagination, useSearch } from "../hooks";
import { formatDate } from "../utils";
import type { UserRole, User, Role, TableColumn } from "../types";

const UserRolesPage: React.FC = () => {
  const [userRoles, setUserRoles] = useState<UserRole[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [roles, setRoles] = useState<Role[]>([]);
  const [selectedUserRole, setSelectedUserRole] = useState<UserRole | null>(
    null
  );
  const [isViewModalOpen, setIsViewModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [isAssignModalOpen, setIsAssignModalOpen] = useState(false);
  const [selectedUserId, setSelectedUserId] = useState<string>("");
  const [selectedRoleId, setSelectedRoleId] = useState<string>("");

  const { isLoading, error, executeAsync } = useLoading();
  const { pagination, goToPage, setTotal } = usePagination({
    initialPage: 1,
    initialLimit: 10,
  });

  // Función para cargar asignaciones de roles
  const loadUserRoles = useCallback(
    async (search: string = "") => {
      const result = await executeAsync(async () => {
        // Datos simulados hasta tener la API lista
        const mockUserRoles: UserRole[] = [
          {
            id: "1",
            userId: "1",
            roleId: "1",
            assignedAt: "2024-01-15T10:00:00Z",
            assignedBy: "admin",
            isActive: true,
            createdAt: "2024-01-15T10:00:00Z",
            updatedAt: "2024-01-15T10:00:00Z",
          },
          {
            id: "2",
            userId: "2",
            roleId: "3",
            assignedAt: "2024-02-01T14:30:00Z",
            assignedBy: "admin",
            isActive: true,
            createdAt: "2024-02-01T14:30:00Z",
            updatedAt: "2024-02-01T14:30:00Z",
          },
          {
            id: "3",
            userId: "3",
            roleId: "3",
            assignedAt: "2024-01-20T09:15:00Z",
            assignedBy: "admin",
            isActive: false,
            createdAt: "2024-01-20T09:15:00Z",
            updatedAt: "2024-02-10T16:45:00Z",
          },
        ];

        // Datos simulados de usuarios
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

        // Datos simulados de roles
        const mockRoles: Role[] = [
          {
            id: "1",
            name: "Administrador",
            description: "Acceso completo al sistema",
            permissions: ["*"],
            createdAt: "2024-01-01T00:00:00Z",
            updatedAt: "2024-01-01T00:00:00Z",
          },
          {
            id: "2",
            name: "Portero",
            description: "Gestión de correspondencia y visitantes",
            permissions: ["correspondencia.*", "visitantes.*"],
            createdAt: "2024-01-10T10:00:00Z",
            updatedAt: "2024-01-10T10:00:00Z",
          },
          {
            id: "3",
            name: "Residente",
            description: "Acceso básico para residentes",
            permissions: ["profile.*", "correspondencia.read"],
            createdAt: "2024-01-15T14:30:00Z",
            updatedAt: "2024-01-15T14:30:00Z",
          },
        ];

        setUsers(mockUsers);
        setRoles(mockRoles);

        // Filtrar por búsqueda si hay término
        let filteredUserRoles = mockUserRoles;
        if (search) {
          const searchLower = search.toLowerCase();
          filteredUserRoles = mockUserRoles.filter((userRole) => {
            const user = mockUsers.find((u) => u.id === userRole.userId);
            const role = mockRoles.find((r) => r.id === userRole.roleId);

            return (
              user?.firstName.toLowerCase().includes(searchLower) ||
              user?.lastName.toLowerCase().includes(searchLower) ||
              user?.email.toLowerCase().includes(searchLower) ||
              role?.name.toLowerCase().includes(searchLower)
            );
          });
        }

        setTotal(filteredUserRoles.length);
        return filteredUserRoles;
      });

      if (result) {
        setUserRoles(result);
      }
    },
    [executeAsync, setTotal]
  );

  // Función para buscar asignaciones
  function handleSearch(term: string) {
    loadUserRoles(term);
  }

  const { searchTerm, setSearchTerm } = useSearch({
    onSearch: handleSearch,
    debounceMs: 300,
  });

  useEffect(() => {
    loadUserRoles();
  }, [loadUserRoles]);

  // Funciones helper para obtener datos relacionados
  const getUserById = (userId: string) => users.find((u) => u.id === userId);
  const getRoleById = (roleId: string) => roles.find((r) => r.id === roleId);

  // Definir columnas de la tabla
  const columns: TableColumn<UserRole>[] = [
    {
      key: "userId",
      label: "Usuario",
      render: (userRole) => {
        const user = getUserById(userRole.userId);
        return (
          <div className="flex items-center">
            <UserIcon className="h-6 w-6 text-gray-400 mr-3" />
            <div>
              <div className="font-medium text-gray-900">
                {user
                  ? `${user.firstName} ${user.lastName}`
                  : "Usuario no encontrado"}
              </div>
              <div className="text-sm text-gray-500">
                {user ? user.email : "N/A"}
              </div>
            </div>
          </div>
        );
      },
    },
    {
      key: "roleId",
      label: "Rol",
      render: (userRole) => {
        const role = getRoleById(userRole.roleId);
        return (
          <div className="flex items-center">
            <ShieldCheckIcon className="h-6 w-6 text-blue-500 mr-3" />
            <div>
              <div className="font-medium text-gray-900">
                {role ? role.name : "Rol no encontrado"}
              </div>
              <div className="text-sm text-gray-500">
                {role ? role.description : "N/A"}
              </div>
            </div>
          </div>
        );
      },
    },
    {
      key: "assignedAt",
      label: "Fecha Asignación",
      render: (userRole) => (
        <div>
          <div className="text-sm text-gray-900">
            {formatDate(userRole.assignedAt)}
          </div>
          <div className="text-xs text-gray-500">
            Por: {userRole.assignedBy}
          </div>
        </div>
      ),
    },
    {
      key: "isActive",
      label: "Estado",
      render: (userRole) => (
        <span
          className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
            userRole.isActive
              ? "bg-green-100 text-green-800"
              : "bg-red-100 text-red-800"
          }`}
        >
          {userRole.isActive ? "Activo" : "Inactivo"}
        </span>
      ),
    },
    {
      key: "actions",
      label: "Acciones",
      render: (userRole) => (
        <div className="flex space-x-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => handleViewUserRole(userRole)}
            className="p-1"
          >
            <EyeIcon className="h-4 w-4" />
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={() => handleDeleteUserRole(userRole)}
            className="p-1 text-red-600 hover:bg-red-50"
          >
            <TrashIcon className="h-4 w-4" />
          </Button>
        </div>
      ),
    },
  ];

  // Handlers para las acciones
  const handleAssignRole = () => {
    setIsAssignModalOpen(true);
  };

  const handleViewUserRole = (userRole: UserRole) => {
    setSelectedUserRole(userRole);
    setIsViewModalOpen(true);
  };

  const handleDeleteUserRole = (userRole: UserRole) => {
    setSelectedUserRole(userRole);
    setIsDeleteModalOpen(true);
  };

  const handleCloseModals = () => {
    setIsViewModalOpen(false);
    setIsDeleteModalOpen(false);
    setIsAssignModalOpen(false);
    setSelectedUserRole(null);
    setSelectedUserId("");
    setSelectedRoleId("");
  };

  const confirmAssign = async () => {
    if (selectedUserId && selectedRoleId) {
      await executeAsync(async () => {
        // TODO: Implementar asignación real
        const newUserRole: UserRole = {
          id: Date.now().toString(),
          userId: selectedUserId,
          roleId: selectedRoleId,
          assignedAt: new Date().toISOString(),
          assignedBy: "current-user", // TODO: Obtener del contexto de auth
          isActive: true,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString(),
        };
        setUserRoles((prev) => [newUserRole, ...prev]);
      });
      handleCloseModals();
    }
  };

  const confirmDelete = async () => {
    if (selectedUserRole) {
      await executeAsync(async () => {
        // TODO: Implementar eliminación real
        console.log("Eliminar asignación:", selectedUserRole.id);
        // Simular eliminación
        setUserRoles((prev) =>
          prev.filter((ur) => ur.id !== selectedUserRole.id)
        );
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
            Asignación de Roles
          </h1>
          <p className="text-gray-600">
            Gestiona la asignación de roles a usuarios
          </p>
        </div>
        <Button
          variant="primary"
          onClick={handleAssignRole}
          icon={<PlusIcon className="h-5 w-5" />}
        >
          Asignar Rol
        </Button>
      </div>

      {/* Filtros y búsqueda */}
      <Card>
        <CardContent className="p-4">
          <div className="flex flex-col sm:flex-row gap-4">
            <div className="flex-1">
              <Input
                placeholder="Buscar asignaciones..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                leftIcon={<MagnifyingGlassIcon className="h-5 w-5" />}
              />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Tabla de asignaciones */}
      <Card>
        <CardHeader>
          <CardTitle>Asignaciones de Roles ({userRoles.length})</CardTitle>
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
              <Table<UserRole>
                data={userRoles}
                columns={columns}
                emptyMessage="No se encontraron asignaciones"
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

      {/* Modal de vista de asignación */}
      <Modal
        isOpen={isViewModalOpen}
        onClose={handleCloseModals}
        title="Detalles de la Asignación"
        size="md"
      >
        {selectedUserRole && (
          <div className="space-y-4">
            {(() => {
              const user = getUserById(selectedUserRole.userId);
              const role = getRoleById(selectedUserRole.roleId);

              return (
                <div className="grid grid-cols-1 gap-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">
                        Usuario
                      </label>
                      <p className="mt-1 text-sm text-gray-900">
                        {user ? `${user.firstName} ${user.lastName}` : "N/A"}
                      </p>
                      <p className="text-xs text-gray-500">
                        {user ? user.email : "N/A"}
                      </p>
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">
                        Rol
                      </label>
                      <p className="mt-1 text-sm text-gray-900">
                        {role ? role.name : "N/A"}
                      </p>
                      <p className="text-xs text-gray-500">
                        {role ? role.description : "N/A"}
                      </p>
                    </div>
                  </div>

                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">
                        Fecha de asignación
                      </label>
                      <p className="mt-1 text-sm text-gray-900">
                        {formatDate(selectedUserRole.assignedAt, true)}
                      </p>
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">
                        Asignado por
                      </label>
                      <p className="mt-1 text-sm text-gray-900">
                        {selectedUserRole.assignedBy}
                      </p>
                    </div>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">
                      Estado
                    </label>
                    <p className="mt-1">
                      <span
                        className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                          selectedUserRole.isActive
                            ? "bg-green-100 text-green-800"
                            : "bg-red-100 text-red-800"
                        }`}
                      >
                        {selectedUserRole.isActive ? "Activo" : "Inactivo"}
                      </span>
                    </p>
                  </div>
                </div>
              );
            })()}
          </div>
        )}
      </Modal>

      {/* Modal de asignación de rol */}
      <Modal
        isOpen={isAssignModalOpen}
        onClose={handleCloseModals}
        title="Asignar Rol a Usuario"
        size="md"
        footer={
          <div className="flex space-x-3">
            <Button variant="outline" onClick={handleCloseModals}>
              Cancelar
            </Button>
            <Button
              variant="primary"
              onClick={confirmAssign}
              disabled={!selectedUserId || !selectedRoleId}
            >
              Asignar
            </Button>
          </div>
        }
      >
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Seleccionar Usuario
            </label>
            <Select
              value={selectedUserId}
              onChange={(e) => setSelectedUserId(e.target.value)}
              placeholder="Seleccione un usuario"
              options={users
                .filter((user) => user.isActive)
                .map((user) => ({
                  value: user.id,
                  label: `${user.firstName} ${user.lastName} (${user.email})`,
                }))}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Seleccionar Rol
            </label>
            <Select
              value={selectedRoleId}
              onChange={(e) => setSelectedRoleId(e.target.value)}
              placeholder="Seleccione un rol"
              options={roles.map((role) => ({
                value: role.id,
                label: `${role.name} - ${role.description}`,
              }))}
            />
          </div>
        </div>
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
        {selectedUserRole && (
          <p>
            ¿Estás seguro de que deseas eliminar esta asignación de rol? Esta
            acción no se puede deshacer.
          </p>
        )}
      </Modal>
    </div>
  );
};

export default UserRolesPage;
