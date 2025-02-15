import React, { useState, useEffect } from 'react';
import { findMember } from '../../services/api/adminService';
import { useContext } from 'react';
import { AuthContext } from '../../Context/AuthContext';
import "./Profile.css";

function Profile() {
  const { user } = useContext(AuthContext);
  const [member, setMember] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setError(null);
    const getData = async () => {
      try {
        const data = await findMember(user.userId);
        setMember(data);
      } catch (err) {
        setError(err.error.message);
      } finally {
        setLoading(false);
      }
    };
    getData();
  }, [user.userId]);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error loading profile: {error}</div>;
  }

  if (!member) {
    return <div>No member data available</div>;
  }

  return (
    <div className="profile-container">
      <h1>Admin Profile</h1>

      {/* Profile Image */}
      <div className="profile-image-container">
        <img
          src={member.image || "img_girl.jpg"}
          alt="Profile"
          className="profile-image"
        />
        <p><strong>Name:</strong> {member.name}</p>
      </div>
      
      <div className="profile-details">
        {/* <p><strong>Name:</strong> {member.name}</p> */}
        <p><strong>Email:</strong> {member.email}</p>
        <p><strong>Status:</strong> {member.isActive ? 'Active' : 'Inactive'}</p>
        <p><strong>Enrollment Date:</strong> {member.enrollmentDate}</p>
        <p><strong>Gender:</strong> {member.gender}</p>
        <p><strong>Address:</strong> {member.address}</p>
        <p><strong>Contact No:</strong> {member.contactNo}</p>
      </div>
    </div>
  );
}

export default Profile;
