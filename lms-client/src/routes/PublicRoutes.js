import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Home from '../pages/User/Home'
import Login from '../pages/Auth/Login';
import SignUp from '../pages/Auth/SignUp';
import AdminRoutes from './AdminRoutes';
import MemberRoutes from './MemberRoutes';

function PublicRoutes() {
  return (
    <Routes>
      {/* Public routes */}
      <Route path="/" element={<Home/>}/>
      <Route path='/member/*' element={<MemberRoutes/>} />
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<SignUp />} />
      {/* Delegate /admin routes to AdminRoutes */}
      <Route path="/admin/*" element={<AdminRoutes />} />
    </Routes>
  );
}

export default PublicRoutes;
