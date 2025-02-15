import React, { useState, useEffect } from "react";
import { getAllBorrowers } from "../../../services/api/adminService"; // Assuming this is the correct path
import Pagination from "../../Common/Pagination";
import MemberCard from '../../Card/Member/MemberCard';
import "./BookDetails.css";

function BookDetails({ book }) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [borrowers, setBorrowers] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalLength, setTotalLength] = useState(0);
  const [refresh, setRefresh] = useState(false);

  const authorNames = book.authors.map((author) => {
    const match = author.match(/name=([^,]+)/);
    return match ? match[1] : "Unknown";
  });

  useEffect(() => {
    const getBorrowers = async () => {
      if (!book.isbn) return;

      setLoading(true);
      setError(null);
      try {
        const resp = await getAllBorrowers(book.isbn, currentPage);
        console.log(resp); 
        setBorrowers(resp.data.content); 
        setTotalLength(resp.data.totalPages); 
      } catch (error) {
        console.error(error);
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    if (refresh) {
      getBorrowers();
      setRefresh(false); 
    }
  }, [currentPage, refresh, book.isbn]);


  const handleLoadBorrowers = () => {
    setCurrentPage(0); 
    setRefresh(true); 

  };

  return (
    <div>
      <div className="book-details-container">
        <div className="book-header">
          <div className="book-image">
            <img
              src={book.image || "default-book-image.jpg"}
              alt={book.title || "Book Cover"}
            />
          </div>

          <div className="book-title-section">
            {book.title && <h1 className="book-title">{book.title}</h1>}
            {book.language && <p className="book-language">Language: {book.language}</p>}
            {book.format && <p className="book-format">Format: {book.format}</p>}
          </div>
        </div>

        <div className="book-details">
          <div className="book-left-details">
            <p><strong>Author(s):</strong> {authorNames.join(", ")}</p>
            {book.publisher && <p><strong>Publisher:</strong> {book.publisher.name}</p>}
            {book.dueDate && <p><strong>Due Date:</strong> {book.dueDate}</p>}
            {book.isAvailable !== undefined && (
              <p><strong>Available:</strong> {book.isAvailable ? "Yes" : "No"}</p>
            )}
            {book.status && <p><strong>Status:</strong> {book.status}</p>}
            {book.publicationDate && <p><strong>Publication Date:</strong> {book.publicationDate}</p>}
          </div>

          <div className="book-right-details">
            {book.isbn && <p><strong>ISBN:</strong> {book.isbn}</p>}
            {book.condition && <p><strong>Condition:</strong> {book.condition}</p>}
            {book.count && <p><strong>Total Copies:</strong> {book.count}</p>}
            {book.pageCount && <p><strong>Page Count:</strong> {book.pageCount}</p>}
            {book.price && <p><strong>Price:</strong> ${book.price}</p>}
            <button onClick={handleLoadBorrowers}>Load Borrowers</button>
          </div>
        </div>
      </div>

      <div className="borrowers-container">
        {loading ? (
          <p>Loading...</p>
        ) : error ? (
          <p className="error">{error}</p>
        ) : (
          borrowers.map((borrower) => (
            <MemberCard member={borrower} />
          ))
        )}
        <Pagination
          totalPages={totalLength}
          currentPage={currentPage}
          setCurrentPage={setCurrentPage} 
        />
      </div>
    </div>
  );
}

export default BookDetails;
