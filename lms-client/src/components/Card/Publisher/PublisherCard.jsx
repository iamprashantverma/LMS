import React from 'react';
import './PublisherCard.css'; 

function PublisherCard({ publisherId, name, email, address }) {
  const avatarLetter = name ? name.charAt(0).toUpperCase() : '';

  return (
    <div className="publisher-card">
      <div className="publisher-avatar">
        {avatarLetter}
      </div>
      <div className="publisher-details">
        <h3>{name}</h3>
        <p><strong>Publisher ID:</strong> {publisherId}</p>
        <p><strong>Email:</strong> {email}</p>
        <p><strong>Address:</strong> {address}</p>
      </div>
    </div>
  );
}

export default PublisherCard;
