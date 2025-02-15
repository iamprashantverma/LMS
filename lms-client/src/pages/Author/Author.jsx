import React, { useEffect, useState } from 'react';
import { getAllAuthors } from '../../services/api/adminService';
import AuthorCard from '../../components/Card/Author/AuthorCard';
import Pagination from '../../components/Common/Pagination';
import './Author.css';

function Author() {
  const [authors, setAuthors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const fetchAuthors = async (page = 1) => {
    setLoading(true);
    try {
      const {data} = await getAllAuthors(page);
      setAuthors(data.content);
      setTotalPages(data.totalPages);

    } catch (err) {
      setError('Failed to load authors');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAuthors(currentPage);
  }, [currentPage]);

  return (
    <div className='author-container'>
      {error && <div style={{ color: 'red' }}>{error}</div>}

      <div className='author-list'>
        {authors.map((author) => (
          <AuthorCard
            key={author.authorId}
            name={author.name}
            dateOfBirth={author.dateOfBirth}
            nationality={author.nationality}
            email={author.email}
            id ={author.authorId}
          />
        ))}
      </div>

      <div className="author-pagination">
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          setCurrentPage={setCurrentPage}
        />
      </div>
    </div>
  );
}

export default Author;
