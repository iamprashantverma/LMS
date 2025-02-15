import React, { useContext } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { AuthContext } from '../Context/AuthContext';

function PrivateRoute() {
  const { user } = useContext(AuthContext);
    if (!user) {
      return <Navigate to="/" replace />;
    }

  if (user.role !== 'ADMIN') {
    return <Navigate to="/unauthorized" replace />;
  }

  return <Outlet />;
}

export default PrivateRoute;
