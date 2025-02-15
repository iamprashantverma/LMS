import React from "react";
import "./RecordCard.css"; 
import { Link } from "react-router-dom";

const RecordCard = ({ record }) => {

  if (!record) {
    return <div>No record data available.</div>;
  }

  // Determine if the condition is "LOST"
  const conditionStyle = record?.condition === "LOST" ? { color: "red", fontWeight: "bold" } : {};

  return (
    <div className="record-card">
      <h2 className="record-title">{record?.bookTitle || "Unknown Title"}</h2>
      { record?.recordId && <p>
        <strong>Record ID:</strong> {record?.recordId }
      </p>}
      <p>
        <strong>Member ID:</strong> {record?.userId || record.borrowerId}
      </p>
      <p>
        <strong>Member Name:</strong> {record?.memberName ||record.borrowerName}
      </p>
      <p>
        <strong>Book Copy:</strong> {record?.bookCopy || "N/A"}
      </p>
      <p>
        <Link to={`/admin/bookdetails?isbn=${record.isbn}`}>
          <strong>ISBN:</strong> {record?.isbn || record.bookIsbn}
        </Link>
      </p>
      
      <p>
        <strong>Borrow Date:</strong> {record?.borrowDate || "N/A"}
      </p>
      <p>
        <strong>Return Date:</strong> {record?.returnDate || "Not Returned"}
      </p>
      {<p>
          Fine: {record?.fine || '0'}
      </p>}
      { record?.status && <p>
        <strong>Status:</strong> {record?.status }
      </p>}
      {record?.issuedBy && <p>
        <strong>Issued By:</strong> {record?.issuedBy }
      </p>}
      {record?.returnedBy && <p>
        <strong>Returned By:</strong> {record?.returnedBy }
      </p>}
      {/* Special condition styling */}
      {record?.condition && <p style={conditionStyle}>
        <strong>Condition:</strong> {record?.condition}
      </p>}
      {record?.format && <p>
        <strong>Format:</strong> {record?.format}
      </p>}
    </div>
  );
};

export default RecordCard;
