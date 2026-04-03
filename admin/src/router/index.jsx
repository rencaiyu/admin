import React from 'react';
import { Navigate, createBrowserRouter } from 'react-router-dom';
import MainLayout from '../layouts/MainLayout';
import LoginPage from '../pages/auth/LoginPage';
import UserPage from '../pages/user/UserPage';
import MenuPage from '../pages/menu/MenuPage';
import RolePage from '../pages/role/RolePage';
import { authStore } from '../store/authStore';

const AuthGuard = ({ children }) => {
  if (!authStore.getToken()) return <Navigate to="/login" replace />;
  return children;
};

export const router = createBrowserRouter([
  { path: '/login', element: <LoginPage /> },
  {
    path: '/',
    element: (
      <AuthGuard>
        <MainLayout />
      </AuthGuard>
    ),
    children: [
      { index: true, element: <Navigate to="/users" replace /> },
      { path: 'users', element: <UserPage /> },
      { path: 'menus', element: <MenuPage /> },
      { path: 'roles', element: <RolePage /> },
    ],
  },
]);
