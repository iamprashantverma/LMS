import React, { useState, useEffect } from 'react';
import DashBoardCard from './DashBoardCard';
import './DashBoard.css';

import {
  getAllBooks,
  membershipRequests,
  getAllMembers, 
  getAllReservedBook,
  getAllBorrowedBook,
  getAllDiscardBook
} from '../../services/api/adminService';
import { FaBook, FaUsers, FaBookmark, FaClipboardList, FaTrashAlt } from 'react-icons/fa';
import { Pie } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { toast } from 'react-toastify';

// Register Chart.js components
ChartJS.register(ArcElement, Tooltip, Legend);

function DashBoard() {
  // State variables to hold the data
  const [books, setBooks] = useState(0);
  const [memberRequest, setMemberRequest] = useState(0);
  const [members, setMembers] = useState(0);
  const [reservedBooks, setReservedBooks] = useState(0);
  const [borrowedBooks, setBorrowedBooks] = useState(0);
  const [discarded, setDiscarded] = useState(0);
  const [availableBooks, setAvailableBooks] = useState(0);  

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetching the total number of books
        const bookResponse = await getAllBooks(0);
        setBooks(bookResponse.data.totalElements);

        // Fetching the number of membership requests
        const memberRequestResponse = await membershipRequests(0);
        setMemberRequest(memberRequestResponse.data.totalElements); // Assuming 'pendingRequests'

        // Fetching the total number of members
        const membersResponse = await getAllMembers(0);
        setMembers(membersResponse.data.totalElements); 

        // Fetching the number of discarded books
        const discardBookResponse = await getAllDiscardBook(0);
        setDiscarded(discardBookResponse.data.totalElements); 

        // Fetching the number of reserved books
        const reservedBooksResponse = await getAllReservedBook();
        setReservedBooks(reservedBooksResponse.data.totalElements); 

        const startDate = '2024-01-01'; 
        const endDate = new Date();
        endDate.setDate(endDate.getDate() + 1); 
        const formattedEndDate = endDate.toISOString().split('T')[0];  // Format to 'YYYY-MM-DD'
       
        // Fetch the borrowed books data
        const borrowedBooksResponse = await getAllBorrowedBook({
          startDate,
          endDate: formattedEndDate,
          page: 0, 
        });
        setBorrowedBooks(borrowedBooksResponse.data.totalElements); 

        // Calculate the available books
        const available = bookResponse.data.totalElements - (borrowedBooksResponse.data.totalElements + reservedBooksResponse.data.totalElements + discardBookResponse.data.totalElements);
        setAvailableBooks(available);

      } catch (error) {
        console.error("Error fetching data: ", error);
      }
    };

    fetchData();
  }, []); 

  // Data for the Pie chart, including Available Books
  const pieData = {
    labels: ['Available', 'Borrowed', 'Reserved', 'Discarded'],
    datasets: [
      {
        data: [availableBooks, borrowedBooks, reservedBooks, discarded],
        backgroundColor: ['#9b59b6', '#3498db', '#2ecc71', '#e74c3c'],
        hoverBackgroundColor: ['#8e44ad', '#2980b9', '#27ae60', '#c0392b'], 
      },
    ],
  };


  const pieOptions = {
    responsive: true,
    plugins: {
      tooltip: {
        callbacks: {
          label: function (tooltipItem) {
            let label = tooltipItem.label || '';
            let value = tooltipItem.raw;
            const percentage = ((value / books) * 100).toFixed(2) + '%';
            return label + ': ' + value + ' (' + percentage + ')';
          },
        },
      },
      legend: {
        position: 'top',
      },
    },
  };


  return (
    <div className='dashboard-container'>
      <DashBoardCard 
        title="Total Books"
        count={books}
        icon={<FaBook />}
        link="books/all"
      />
      <DashBoardCard 
        title="Membership Pending"
        count={memberRequest}
        icon={<FaClipboardList />}
        link="mem-req?from=user"
      />
      <DashBoardCard 
        title="Total Members"
        count={members}
        icon={<FaUsers />}
        link="members?from=member"
      />
      <DashBoardCard 
        title="Reserved Books"  
        count={reservedBooks}
        icon={<FaBookmark />}
        link="books/res"
      />

      <DashBoardCard 
          title="Today's Borrows"
          count={borrowedBooks}
          icon={<FaBookmark />}
          link="borrow-reports"
      />
      <DashBoardCard 
          title="Discarded Books"
          count={discarded}
          icon={<FaTrashAlt />}
          link="discard-books"
      />
      <div className="graph-container">
        <h3>Book Distribution</h3>
        <Pie data={pieData} options={pieOptions} />
      </div>
    </div>
  );
}

export default DashBoard;
