import React, { useEffect, useState } from "react";
import RecordCard from "../../components/Card/Record/RecordCard";
import Pagination from "../../components/Common/Pagination";
import Loading from "../../components/Common/Loading";
import { getAllRecords } from "../../services/api/adminService"; 
import "./RecordPage.css"; 

function RecordPage() {
  // State variables
  const currentDate = new Date();
  const oneDayBefore = new Date(currentDate);
  oneDayBefore.setDate(currentDate.getDate() - 1);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [data, setData] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [startDate, setStartDate] = useState(oneDayBefore.toISOString().split("T")[0]);
  const [endDate, setEndDate] = useState(currentDate.toISOString().split("T")[0]);

  // Fetch data function
  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await getAllRecords({
        startDate: startDate,
        endDate: endDate,
        page: currentPage,
      });
      console.log(startDate, endDate, response);
      setData(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (err) {
      setError(err.error.message || "Failed to fetch data.");
    } finally {
      setLoading(false);
    }
  };

  
  useEffect(() => {
    fetchData();
  }, [currentPage]);

  // Handle Search Button Click
  const handleSearch = () => {
    setCurrentPage(0);
    setData([]);
    fetchData();
  };

  return (
    <div className="record-container">

      {/* Date Filter */}
      <div className="record-date-container">
        <label className="filter-label">Select Start and End Date</label>
        <div className="record-input-container">
          <input
            name="startDate"
            type="date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
          />
          <input
            name="endDate"
            type="date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
          />
          <button onClick={handleSearch} className="search-button">Search</button>
        </div>
      </div>
      {/* Data List */}
      {!error && data.length > 0 && (
        <div className="record-data-grid">
          {data.map((record) => (
            <RecordCard key={record.recordId} record={record} />
          ))}
        </div>
      )}

      <div className="all-record-footer"> 
          {/* No Data Message */}
          {!loading && !error && data.length === 0 && (
            <div className="no-data-message">No records found for the selected dates.</div>
          )}

          {/* Error Message */}
          {error && <div className="error-message">{error}</div>}

          {/* Pagination */}
          {!loading && !error && totalPages > 0 && (
            <Pagination
              currentPage={currentPage}
              setCurrentPage={setCurrentPage}
              totalPages={totalPages}
            />
          )}
      </div>
    </div>
  );
}

export default RecordPage;
