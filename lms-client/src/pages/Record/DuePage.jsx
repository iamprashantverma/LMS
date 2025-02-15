import React, { useEffect, useState } from 'react';
import RecordCard from '../../components/Card/Record/RecordCard'; 
import Pagination from '../../components/Common/Pagination';
import Loading from '../../components/Common/Loading';
import { getDueRecords} from '../../services/api/adminService'; 
import './DuePage.css';

function DuePage() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [data, setData] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // Fetch due books data from the server
  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await getDueRecords(currentPage);
      setData(response.data.content);  
      setTotalPages(response.data.totalPages);  
    } catch (err) {
      setError('Error fetching due books.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [currentPage]);

  return (
    <div className="due-page-container">
      <h1>Due Books</h1>

      {/* Loading Spinner */}
      {/* {loading && <Loading />} */}

      {/* Error Message */}
      {error && <div className="error-message">{error}</div>}

      {/* Due Data */}
      {!loading && !error && data.length > 0 && (
        <div className="due-data">
          {data.map((record) => (
            <RecordCard key={record.recordId} record={record} />
          ))}
        </div>
      )}

      {/* No Data Message */}
      {!loading && !error && data.length === 0 && (
        <div className="no-data-message">No due records found.</div>
      )}

      {/* Pagination */}
      {!loading && !error && totalPages > 0 && (
        <Pagination
          currentPage={currentPage}
          setCurrentPage={setCurrentPage}
          totalPages={totalPages}
        />
      )}
    </div>
  );
}

export default DuePage;
