import React, { useState } from 'react';
import { returnBook } from '../../services/api/adminService'; 
const ReturnBookForm = () => {
  const [bookCopyId, setBookCopyId] = useState('');
  const [condition, setCondition] = useState('');
  const [loading, setLoading] = useState(false);
  const [responseMessage, setResponseMessage] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setResponseMessage(null);

    if (!bookCopyId || !condition) {
      setResponseMessage('Please fill in all fields.');
      setLoading(false);
      return;
    }

    try {

      const response = await returnBook({ id: bookCopyId, condition });
      console.log(response);
      setResponseMessage(`Fine Amount :${response.data.fine}`);
    } catch (error) {
      setResponseMessage(`Error: ${error.error.message || 'Something went wrong.'}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="shadow-lg">
      <form onSubmit={handleSubmit} className="space-y-4">
        <h2 className="text-xl font-semibold">Return Book</h2>

        <div className="">
          <label htmlFor="bookCopyId" className="font-medium">Book Copy ID</label>
          <input
            id="bookCopyId"
            type="text"
            value={bookCopyId}
            onChange={(e) => setBookCopyId(e.target.value)}
            placeholder="Enter Book Copy ID"
            required
            className="border p-2 rounded"
          />
        </div>

        <div className="flex flex-col space-y-2">
          <label htmlFor="condition" className="font-medium">Condition</label>
          <select
            id="condition"
            value={condition}
            onChange={(e) => setCondition(e.target.value)}
            required
            className="border p-2 rounded"
          >
            <option value="">Select Condition</option>
            <option value="GOOD">Good</option>
            <option value="DAMAGED">Damaged</option>
            <option value="LOST">Lost</option>
          </select>
        </div>

        <div className="flex items-center space-x-4">
          <button type="submit" disabled={loading} className="w-full p-2 bg-blue-500 text-white rounded">
            {loading ? 'Processing...' : 'Return Book'}
          </button>
        </div>
      </form>

      {responseMessage && (
        <div className="mt-4 p-2 text-center rounded-lg bg-gray-100">
          <p>{responseMessage}</p>
        </div>
      )}
    </div>
  );
};

export default ReturnBookForm;
