import React from 'react';
import { FaCircleNotch } from 'react-icons/fa'; 

import './Loading.css'; 

const Loading = ({ message = 'Loading...' }) => {
  return (
    <div className="loading-container">
      <FaCircleNotch className="spinner" size={40} /> 
    </div>
  );
};

export default Loading;
