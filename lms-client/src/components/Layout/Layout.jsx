import React from 'react'
import SideBar from '../SideBar/SideBar'
import { Outlet } from 'react-router-dom'
import { useContext } from 'react'
import { AuthContext } from '../../Context/AuthContext'


function Layout() {

  const {open} = useContext(AuthContext);
  
  return (
    <div className='layout'>
        <div className={open ? "sidebar-open":"sidebar-close"}>
          <SideBar/>
        </div>
        <div className='content-container'>
          <Outlet/>
        </div>   
    </div>
  )
}

export default Layout
