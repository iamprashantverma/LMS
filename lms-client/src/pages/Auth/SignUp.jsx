import React, { useState } from 'react';
import { signup } from '../../services/api/authService';
import { useNavigate } from 'react-router-dom';

function SignUp() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    address: '',
    contactNo: '',
    gender: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await signup(formData);
      console.log('Signup successful:', response.data);
      navigate("/login");
      setFormData({
        name: '',
        email: '',
        address: '',
        contactNo: '',
        gender: '',
      });
    } catch (error) {
      console.error('Signup failed:', error);
      alert('Something went wrong. Please try again.');
    }
  };

  const containerStyle = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    minHeight: '100vh',
    backgroundColor: '#f0f2f5',
    padding: '1rem',
  };

  const formStyle = {
    backgroundColor: '#fff',
    padding: '2rem',
    borderRadius: '8px',
    boxShadow: '0 0 10px rgba(0,0,0,0.1)',
    width: '100%',
    maxWidth: '400px',
    boxSizing: 'border-box',
  };

  const inputStyle = {
    width: '100%',
    padding: '0.6rem',
    marginBottom: '1rem',
    borderRadius: '4px',
    border: '1px solid #ccc',
    fontSize: '1rem',
  };

  const labelStyle = {
    marginBottom: '0.3rem',
    fontWeight: '500',
    display: 'block',
  };

  const buttonStyle = {
    width: '100%',
    padding: '0.7rem',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    fontSize: '1rem',
    cursor: 'pointer',
  };

  return (
    <div style={containerStyle}>
      <form style={formStyle} onSubmit={handleSubmit}>
        <h2 style={{ textAlign: 'center', marginBottom: '1.5rem' }}>Sign Up</h2>

        <label style={labelStyle}>Name</label>
        <input
          style={inputStyle}
          type="text"
          name="name"
          value={formData.name}
          onChange={handleChange}
          required
        />

        <label style={labelStyle}>Email</label>
        <input
          style={inputStyle}
          type="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          required
        />

        <label style={labelStyle}>Address</label>
        <input
          style={inputStyle}
          type="text"
          name="address"
          value={formData.address}
          onChange={handleChange}
          required
        />

        <label style={labelStyle}>Contact No</label>
        <input
          style={inputStyle}
          type="tel"
          name="contactNo"
          value={formData.contactNo}
          onChange={handleChange}
          required
        />

        <label style={labelStyle}>Gender</label>
        <select
          style={inputStyle}
          name="gender"
          value={formData.gender}
          onChange={handleChange}
          required
        >
          <option value="">Select</option>
          <option value="male">Male</option>
          <option value="female">Female</option>
          <option value="other">Other</option>
        </select>

        <button type="submit" style={buttonStyle}>
          Register
        </button>
      </form>
    </div>
  );
}

export default SignUp;
