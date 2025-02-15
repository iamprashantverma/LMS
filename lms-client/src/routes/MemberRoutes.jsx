import React from 'react'
import { Routes,Route } from 'react-router-dom'
import Layout from '../components/Layout/Layout'
import Home from '../pages/User/Home'
import DashBoard from '../pages/User/DashBoard';
import ProtectedRoutes from './ProtectedRoutes';
import Interface from '../pages/User/Interface';
import Book from '../pages/Books/Book';


function MemberRoutes() {
  return (
    <Routes>
        <Route element= {<ProtectedRoutes/>} >
          <Route path='/'  element={<Layout/>} >
          <Route path='dashboard' element={<DashBoard/>} />
          <Route index element={<Home/>} />  
          <Route path='interface' element={<Interface/>} />
          <Route path='view-books/:id'element={<Book/>} />
        </Route>
      </Route>
    </Routes>
  )
}

export default MemberRoutes
