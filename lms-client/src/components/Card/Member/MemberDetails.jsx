import React from "react";
import { HiOutlineBan } from "react-icons/hi";
import { FaBookReader, FaCheckCircle, FaBookmark, FaTimesCircle } from "react-icons/fa";
import "./MemberDetails.css";

function MemberDetails({ member, setAction , userAction }) {
  return (
    <div className="member-details-container">
      {/* Member Header Section */}
      <div className="member-details-header">
        <div className="member-details-img">
          {member.name[0]}
        </div>
        <div className="member-detail-page-info">
          <p>
            <strong>Name:</strong> {member.name}
          </p>
          <p>
            <strong>ID:</strong> {member.userId || member.registrationId}
          </p>
          <p>
            <strong>Status:</strong> {member.isActive ? "Active" : "Inactive"}
          </p>
          <p>
            <strong>Gender:</strong> {member.gender}
          </p>
        </div>
      </div>

      {/* Member Details Section */}
      <div className="member-details-middle">
          <strong>Enrollment Date:</strong> <p> {member.enrollmentDate}</p>

          <strong>Approved By:</strong> <p>{member.approvedBy}</p>
        
          <strong>Email:</strong> <p> {member.email}
        </p>
      
          <strong>Contact No:</strong>  <p> {member.contactNo}
        </p>
      
          <strong>Address:</strong>  <p> {member.address}
        </p>
      </div>

      {/* Member Footer Section */}
      <div className="member-details-footer">
        {member.registrationId && (
          <div className="member-button-group">
            <button onClick={()=>{userAction(member.registrationId,'approve')}} >
              <FaCheckCircle size={20} /> Approve
            </button>
            <button onClick={()=>{userAction(member.registrationId,'reject')}} >
              <FaTimesCircle size={20} /> Reject
            </button>
          </div>
        )}
        {member.userId && (
          <div className="member-button-group">
            <button onClick={() => setAction("borrowed")}>
              <FaBookReader size={20} /> Borrowed Books
            </button>
            <button onClick={() => setAction("reserved")}>
              <FaBookmark size={20} /> Reserved Books
            </button>
            <button onClick={() => setAction(member.isActive ? "deactive" : "active")}>
              <HiOutlineBan size={20} /> {member.isActive ? "Deactivate" : "Activate"}
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default MemberDetails;
