import React from 'react';
import './AuthorCard.css'; 

function AuthorCard({ id, name, dateOfBirth, nationality, email }) {
  const avatarLetter = name ? name.charAt(0).toUpperCase() : '';

  return (
    <div className="author-card">
      <div className="author-avatar">
        {avatarLetter}
      </div>
      <div className="author-details">
        <h3>{name}</h3>
        <p><strong>ID:</strong> {id}</p>
        <p><strong>Date of Birth:</strong> {dateOfBirth}</p>
        <p><strong>Nationality:</strong> {nationality}</p>
        <p><strong>Email:</strong> {email}</p>
      </div>
    </div>
  );
}

export default AuthorCard;
