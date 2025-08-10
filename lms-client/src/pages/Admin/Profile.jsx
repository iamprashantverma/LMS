import React, { useState, useEffect, useContext } from 'react';
import { findMember } from '../../services/api/adminService';
import { getMyDetails } from '../../services/api/memberService';
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
        let data;
        if (user.role === 'MEMBER') {
          data = await getMyDetails(user.userId);
          setMember(data.data);
        } else {
          data = await findMember(user.userId);
          setMember(data);
        }

      } catch (err) {
        setError(err?.error?.message || 'Something went wrong');
      } finally {
        setLoading(false);
      }
    };
    getData();
  }, [user.role, user.userId]);

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
      <h1>{user.role === 'MEMBER' ? 'Member Profile' : 'Admin Profile'}</h1>

      <div className="profile-image-container">
        <img
          src={member.image || "img_girl.jpg"}
          alt="Profile"
          className="profile-image"
        />
        <p><strong>Name:</strong> {member.name}</p>
      </div>
      
      <div className="profile-details">
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
