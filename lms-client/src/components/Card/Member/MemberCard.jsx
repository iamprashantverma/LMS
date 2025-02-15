import React from 'react';
import './MemberCard.css';
import { Link } from 'react-router-dom';

function MemberCard({ member,userAction }) {
  console.log(member)
  if (!member) {
    return <div>No member data available.</div>;
  }

  return (
    <div className="member-card-container">
      <div className="member-content">
        <div className="member-img">
          {member.name?.[0] || '-'}
        </div>
        <div className="member-info">
          {member.name && <p>Name: {member.name}</p>}
          {member.userId && <Link to={`/admin/mem-details?id=${member.userId}`} >User ID: {member.userId}</Link>}
          {member.registrationId && <Link to={`/admin/mem-details?id=${member.registrationId}`} >Registration ID: {member.registrationId}</Link>}
          {member.gender && <p>Gender: {member.gender}</p>}
          {member.isActive != null && <p className={member.isActive ? 'active': 'inactive'}> {member.isActive?'Active': 'Deactive' }</p>}
        </div>
      </div>
      {member.isActive == null && (
        <div className="action-container">
          <button onClick={()=>userAction(member.registrationId,'approve')}>Approve</button>
          <button onClick={()=>userAction(member.registrationId,'reject')}>Reject</button>
        </div>
      )}
    </div>
  );
}

export default MemberCard;
