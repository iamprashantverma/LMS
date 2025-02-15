import React from 'react';
import { Link } from 'react-router-dom';
import './DashBoardCard.css';

function DashBoardCard({ title, count, icon, link }) {
  
  return (
    <Link to={link} className="dashboard-card">
      <div className="card-icon">{icon}</div>
      <div className="card-content">
        <h3>{title}</h3>
        <p>{count}</p>
      </div>
    </Link>
  );
}

export default DashBoardCard;
