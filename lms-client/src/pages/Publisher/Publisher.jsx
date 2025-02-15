import React, { useEffect, useState } from 'react';
import { getAllPublishers } from '../../services/api/adminService';
import PublisherCard from '../../components/Card/Publisher/PublisherCard';
import Pagination from '../../components/Common/Pagination';
import './Publisher.css';

function Publisher() {
  const [publishers, setPublishers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const fetchPublishers = async (page = 1) => {
    setLoading(true);
    try {
      const { data } = await getAllPublishers(page);
      setPublishers(data.content);
      setTotalPages(data.totalPages);
    } catch (err) {
      setError('Failed to load publishers');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPublishers(currentPage);
  }, [currentPage]);

  return (
    <div className='publisher-container'>
      {/* {loading && <div>Loading publishers...</div>} */}
      {error && <div style={{ color: 'red' }}>{error}</div>}

      <div className='publisher-list'>
        {publishers.map((publisher) => (
          <PublisherCard
            key={publisher.publisherId}
            publisherId={publisher.publisherId}
            name={publisher.name}
            email={publisher.email}
            address={publisher.address}
          />
        ))}
      </div>

      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        setCurrentPage={setCurrentPage}
      />
    </div>
  );
}

export default Publisher;
