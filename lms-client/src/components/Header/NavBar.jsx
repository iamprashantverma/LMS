import React, { useState, useContext } from "react";
import { AuthContext } from '../../Context/AuthContext'
import { NavLink, useNavigate } from "react-router-dom";
import { FaUserCircle, FaBars, FaTimes, FaUserPlus, FaSignInAlt, FaHome, FaTachometerAlt } from "react-icons/fa";
import "./NavBar.css";

function NavBar() {
  const navigate = useNavigate();
  const { user, logout, open, setOpen } = useContext(AuthContext);
  const { role = "unknown", userName } = user || {};
  const [userInput, setUserInput] = useState("");
  const [userSelect, setUserSelect] = useState("Book"); // Dropdown value

  const handleLogout = () => {
    logout();
    navigate("/",{replace:true});
  };

  const userSubmit = async(e)=>{
      e.preventDefault();
      
  }

  const adminSubmit = (e) => {
    e.preventDefault();
    if (userInput) {
      if (userSelect === "Book") {
        navigate(`/admin/bookdetails?isbn=${userInput}`); 
      } else if (userSelect === "Member") {
        navigate(`/admin/mem-details?id=${userInput}`); 
      }
    }
    setUserInput(""); 
  };

  return (

    <nav className="navbar-container">

      <div className="main-logo">
        {user && (!open ? (<FaBars onClick={() => setOpen(!open)} />) : (<FaTimes onClick={() => setOpen(!open)} />))}
        {user ? <h2>{role === "ADMIN" ? "ADMIN" : "MEMBER"}</h2> : <h2>Book Hive</h2>}
      </div>

      <div className="navbar-search-container">
        <form className="search-form" onSubmit={ user && user.role ==='ADMIN' ? adminSubmit:userSubmit}>
          { user && user.role === 'ADMIN' &&
           <select className="search-dropdown"
            value={userSelect}
            onChange={(e) => setUserSelect(e.target.value)} >

            <option value="Book">Book</option>
            <option value="Member">Member</option>

          </select>}

          <input
            name="searchInput"
            onChange={(e) => setUserInput(e.target.value)}
            type="text"
            placeholder={
              user !== null && user.role ==='ADMIN' ? (userSelect === "Book"
                ? "Enter ISBN Number"
                : "Enter User ID"):('Enter Book Title')
            }
            required
            value={userInput}
          />
          <button type="submit">Search</button>
        </form>
      </div>

      <ul className="navbar-item-list">
        {user && (
          <li>
            <NavLink to={user.role === 'ADMIN'?'/admin/profile':'/member/profile'}>
              <FaUserCircle size={20} /> {userName}
            </NavLink>
          </li>
        )}
        {!user && (
          <li>
            <NavLink to="/">
              <FaHome size={20} /> Home
            </NavLink>
          </li>
        )}
        {!user && (
          <li>
            <NavLink to="/signup">
              <FaUserPlus size={20} /> Signup
            </NavLink>
          </li>
        )}
        {!user && (
          <li>
            <NavLink to="/login">
              <FaSignInAlt size={20} /> Login
            </NavLink>
          </li>
        )}
        {user && (
          <li>
            <NavLink to={role === "ADMIN" ? "/admin" : "/member/dashboard"}>
              <FaTachometerAlt size={20} /> Dashboard
            </NavLink>
          </li>
        )}
        {user && (
          <li>
            <button className="logout-button" onClick={handleLogout}>
              Logout
            </button>
          </li>
        )}
      </ul>
    </nav>
  );
}

export default NavBar;
