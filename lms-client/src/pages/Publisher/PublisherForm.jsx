import React, { useState } from 'react';
import { addPublisher } from '../../services/api/adminService'; 
import './PublisherForm.css';

function PublisherForm() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [publisherData, setPublisherData] = useState({
    email: '',
    name: '',
    address: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPublisherData((prevData) => ({
      ...prevData,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await addPublisher(publisherData);  
      setPublisherData({
        email: '',
        name: '',
        address: ''
      });
    } catch (err) {
      console.log(err);
      const errorMessage = err?.error?.message || "An error occurred while adding the publisher.";
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="publisher-form-container">
      {error && <div className="publisher-form-error">{error}</div>}
      <h2 className="publisher-form-heading">Publisher Form</h2>
      <form className="publisher-form" onSubmit={handleSubmit}>
        <div className="publisher-form-field">
          <label className="publisher-form-label" htmlFor="name">Publisher Name:</label>
          <input
            className="publisher-form-input"
            type="text"
            id="name"
            name="name"
            value={publisherData.name}
            onChange={handleChange}
            required
          />
        </div>

        <div className="publisher-form-field">
          <label className="publisher-form-label" htmlFor="email">Email:</label>
          <input
            className="publisher-form-input"
            type="email"
            id="email"
            name="email"
            value={publisherData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="publisher-form-field">
          <label className="publisher-form-label" htmlFor="address">Address:</label>
          <input
            className="publisher-form-input"
            type="text"
            id="address"
            name="address"
            value={publisherData.address}
            onChange={handleChange}
            required
          />
        </div>

        <div className="publisher-form-action">
          <button className="publisher-form-submit" disabled={loading} type="submit">
            Submit
          </button>
        </div>
      </form>
    </div>
  );
}

export default PublisherForm;
