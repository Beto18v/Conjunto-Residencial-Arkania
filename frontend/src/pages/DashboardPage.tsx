/**
 * Página de Dashboard principal
 */

import React, { useEffect, useState } from "react";
import {
  UsersIcon,
  ShieldCheckIcon,
  UserPlusIcon,
  ChartBarIcon,
  ClockIcon,
  ExclamationTriangleIcon,
} from "@heroicons/react/24/outline";
import {
  Card,
  CardHeader,
  CardTitle,
  CardContent,
  Spinner,
} from "../components";
import { useAuth } from "../hooks";
import { formatDate } from "../utils";

interface DashboardStats {
  totalUsers: number;
  totalRoles: number;
  totalAssignments: number;
  activeUsers: number;
}

const DashboardPage: React.FC = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState<DashboardStats>({
    totalUsers: 0,
    totalRoles: 0,
    totalAssignments: 0,
    activeUsers: 0,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadDashboardData = async () => {
      try {
        setLoading(true);
        setError(null);

        // Simular carga de estadísticas (reemplazar con llamadas reales a la API)
        await new Promise((resolve) => setTimeout(resolve, 1000));

        // TODO: Implementar llamadas reales a la API cuando esté lista
        setStats({
          totalUsers: 25,
          totalRoles: 8,
          totalAssignments: 45,
          activeUsers: 22,
        });
      } catch (err) {
        setError("Error al cargar las estadísticas del dashboard");
        console.error("Dashboard error:", err);
      } finally {
        setLoading(false);
      }
    };

    loadDashboardData();
  }, []);

  const statsCards = [
    {
      title: "Total Usuarios",
      value: stats.totalUsers,
      icon: UsersIcon,
      color: "text-blue-600",
      bgColor: "bg-blue-100",
      description: `${stats.activeUsers} activos`,
    },
    {
      title: "Roles Configurados",
      value: stats.totalRoles,
      icon: ShieldCheckIcon,
      color: "text-green-600",
      bgColor: "bg-green-100",
      description: "Roles del sistema",
    },
    {
      title: "Asignaciones",
      value: stats.totalAssignments,
      icon: UserPlusIcon,
      color: "text-purple-600",
      bgColor: "bg-purple-100",
      description: "Roles asignados",
    },
    {
      title: "Actividad",
      value: "98%",
      icon: ChartBarIcon,
      color: "text-orange-600",
      bgColor: "bg-orange-100",
      description: "Sistema operativo",
    },
  ];

  const recentActivities = [
    {
      id: 1,
      type: "user_created",
      description: "Nuevo usuario registrado: Juan Pérez",
      timestamp: new Date(Date.now() - 30 * 60 * 1000),
      icon: UsersIcon,
    },
    {
      id: 2,
      type: "role_assigned",
      description: 'Rol "Administrador" asignado a María García',
      timestamp: new Date(Date.now() - 2 * 60 * 60 * 1000),
      icon: UserPlusIcon,
    },
    {
      id: 3,
      type: "role_created",
      description: 'Nuevo rol creado: "Supervisor"',
      timestamp: new Date(Date.now() - 4 * 60 * 60 * 1000),
      icon: ShieldCheckIcon,
    },
  ];

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spinner size="lg" />
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center h-64">
        <Card className="p-6">
          <div className="text-center text-red-600">
            <ExclamationTriangleIcon className="h-12 w-12 mx-auto mb-4" />
            <p>{error}</p>
          </div>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-600">
          Bienvenido, {user?.firstName} {user?.lastName}
        </p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {statsCards.map((stat, index) => (
          <Card key={index} className="hover:shadow-lg transition-shadow">
            <CardContent className="p-6">
              <div className="flex items-center">
                <div className={`p-3 rounded-lg ${stat.bgColor}`}>
                  <stat.icon className={`h-6 w-6 ${stat.color}`} />
                </div>
                <div className="ml-4 flex-1">
                  <p className="text-sm font-medium text-gray-600">
                    {stat.title}
                  </p>
                  <p className="text-2xl font-bold text-gray-900">
                    {stat.value}
                  </p>
                  <p className="text-xs text-gray-500">{stat.description}</p>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Recent Activity */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center">
              <ClockIcon className="h-5 w-5 mr-2" />
              Actividad Reciente
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {recentActivities.map((activity) => (
                <div key={activity.id} className="flex items-start space-x-3">
                  <div className="flex-shrink-0">
                    <div className="p-2 bg-gray-100 rounded-lg">
                      <activity.icon className="h-4 w-4 text-gray-600" />
                    </div>
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm text-gray-900">
                      {activity.description}
                    </p>
                    <p className="text-xs text-gray-500">
                      {formatDate(activity.timestamp, true)}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Quick Actions */}
        <Card>
          <CardHeader>
            <CardTitle>Acciones Rápidas</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              <a
                href="/users"
                className="block p-3 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
              >
                <div className="flex items-center">
                  <UsersIcon className="h-5 w-5 text-blue-600 mr-3" />
                  <div>
                    <p className="font-medium text-gray-900">
                      Gestionar Usuarios
                    </p>
                    <p className="text-sm text-gray-500">
                      Crear, editar y administrar usuarios
                    </p>
                  </div>
                </div>
              </a>

              <a
                href="/roles"
                className="block p-3 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
              >
                <div className="flex items-center">
                  <ShieldCheckIcon className="h-5 w-5 text-green-600 mr-3" />
                  <div>
                    <p className="font-medium text-gray-900">
                      Administrar Roles
                    </p>
                    <p className="text-sm text-gray-500">
                      Configurar roles y permisos
                    </p>
                  </div>
                </div>
              </a>

              <a
                href="/user-roles"
                className="block p-3 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
              >
                <div className="flex items-center">
                  <UserPlusIcon className="h-5 w-5 text-purple-600 mr-3" />
                  <div>
                    <p className="font-medium text-gray-900">Asignar Roles</p>
                    <p className="text-sm text-gray-500">
                      Gestionar asignaciones de roles
                    </p>
                  </div>
                </div>
              </a>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default DashboardPage;
