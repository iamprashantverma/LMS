import React, { useEffect, useState } from "react";
import { getAllDiscardBook } from "../../services/api/adminService"; // Assuming the API to fetch discarded books is set up
import RecordCard from "../../components/Card/Record/RecordCard"; // Reusing RecordCard component
import Pagination from "../../components/Common/Pagination"; // Pagination component
import './DiscardBooks.css' ;

function DiscardedBooks() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [data, setData] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // Fetch discarded books data
  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await getAllDiscardBook(currentPage); // API call to get discarded books
      setData(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (err) {
      setError("Failed to fetch discarded books.");
    } finally {
      setLoading(false);
    }
  };

  // Trigger data fetching when the page changes
  useEffect(() => {
    fetchData();
  }, [currentPage]);

  return (
    <div className="discarded-books-container">
      <h1>Discarded Books</h1>


      {data.length > 0 && (
        <div className="discarded-books-grid">
          {data.map((record) => (
            <RecordCard key={record.recordId} record={record} />
          ))}
        </div>
      )}

      {/* Pagination */}
      {!error && totalPages > 0 && (
        <Pagination
          currentPage={currentPage}
          setCurrentPage={setCurrentPage}
          totalPages={totalPages}
        />
      )}
    </div>
  );
}

export default DiscardedBooks;
