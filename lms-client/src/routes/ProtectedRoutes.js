import React from 'react'
import { useContext } from 'react';
import { AuthContext } from '../Context/AuthContext';
import { Navigate, Outlet} from 'react-router-dom';
function ProtectedRoutes() {

    const { user } = useContext(AuthContext);

    if (!user) {
        return <Navigate to='/' replace />
    }

    if(user.role !== 'MEMBER')
        return <Navigate to="/unauthorized" replace />;
    
    return <Outlet/>
}

export default ProtectedRoutes
