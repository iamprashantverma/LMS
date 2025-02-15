import React, { useState, useEffect } from 'react';
import './Pagination.css';

function Pagination({ totalPages, currentPage, setCurrentPage }) {
  const [jumpTo, setJumpTo] = useState('');

  useEffect(() => {
    setJumpTo('');
  }, [currentPage]);

  const handlePageChange = (newPage) => {
    if (newPage >= 1 && newPage <= totalPages) {
      setCurrentPage(newPage - 1);
    }
  };

  const handleJumpTo = () => {
    const pageNumber = parseInt(jumpTo, 10);
    if (pageNumber >= 1 && pageNumber <= totalPages) {
      handlePageChange(pageNumber);
    } else {
      alert('Invalid page number');
    }
    setJumpTo('');
  };

  if (totalPages === 0) return null;

  return (
    <div className="unique-pagination-container">
      <button
        onClick={() => handlePageChange(currentPage)}
        disabled={currentPage === 0}
        className="unique-pagination-button"
      >
        Prev
      </button>
      <span className="unique-pagination-info">
        Page {currentPage + 1} of {totalPages}
      </span>
      <button
        onClick={() => handlePageChange(currentPage + 2)}
        disabled={currentPage + 1 === totalPages}
        className="unique-pagination-button"
      >
        Next
      </button>
      <input
        name="jumpTo"
        type="number"
        placeholder="Go to page"
        onChange={(e) => setJumpTo(e.target.value)}
        value={jumpTo}
        min={0}
        className="unique-pagination-input"
      />
      <button
        onClick={handleJumpTo}
        disabled={jumpTo === ''}
        className="unique-pagination-button"
      >
        Go
      </button>
    </div>
  );
}

export default Pagination;
