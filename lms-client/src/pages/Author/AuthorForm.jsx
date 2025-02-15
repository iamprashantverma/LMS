import React, { useState } from 'react';
import { addAuthor } from '../../services/api/adminService';
import './AuthorForm.css';

function AuthorForm() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [authorData, setAuthorData] = useState({
    name: '',
    dateOfBirth: '',
    nationality: '',
    email: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setAuthorData((prevData) => ({
      ...prevData,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await addAuthor(authorData);
      setAuthorData({
        name: '',
        dateOfBirth: '',
        nationality: '',
        email: ''
      });
    } catch (err) {
      console.log(err);
      const errorMessage = err?.error?.message || "An error occurred while adding the author.";
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="author-form-container">
      {error && <div className="author-form-error">{error}</div>}
      <h2 className="author-form-heading">Author Form</h2>
      <form className="author-form" onSubmit={handleSubmit}>
        <div className="author-form-field">
          <label className="author-form-label" htmlFor="name">Name:</label>
          <input
            className="author-form-input"
            type="text"
            id="name"
            name="name"
            value={authorData.name}
            onChange={handleChange}
            required
          />
        </div>

        <div className="author-form-field">
          <label className="author-form-label" htmlFor="dateOfBirth">Date of Birth:</label>
          <input
            className="author-form-input"
            type="date"
            id="dateOfBirth"
            name="dateOfBirth"
            value={authorData.dateOfBirth}
            onChange={handleChange}
            required
          />
        </div>

        <div className="author-form-field">
          <label className="author-form-label" htmlFor="nationality">Nationality:</label>
          <input
            className="author-form-input"
            type="text"
            id="nationality"
            name="nationality"
            value={authorData.nationality}
            onChange={handleChange}
            required
          />
        </div>

        <div className="author-form-field">
          <label className="author-form-label" htmlFor="email">Email:</label>
          <input
            className="author-form-input"
            type="email"
            id="email"
            name="email"
            value={authorData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="author-form-action">
          <button className="author-form-submit" disabled={loading} type="submit">
            Submit
          </button>
        </div>
      </form>
    </div>
  );
}

export default AuthorForm;
