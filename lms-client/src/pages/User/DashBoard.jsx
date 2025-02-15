import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getMyBorrowedBooks, getMyReservedBooks } from '../../services/api/memberService';
import './dashboardStyles.css';

function UserDashboard() {
  const [error, setError] = useState(false);
  const [reservedBooks, setReservedBooks] = useState([]);
  const [borrowedBooks, setBorrowedBooks] = useState([]);
  const [recentLogins, setRecentLogins] = useState(["Jan 27, 2025 - 10:30 AM", "Jan 25, 2025 - 08:15 PM"]);
  const [dueReminders, setDueReminders] = useState([
    "Return 'The Great Gatsby' soon!",
    "Late Fee Alert for 'Atomic Habits'",
  ]);
  const [notifications, setNotifications] = useState([
    "New book added: 'The Alchemist'!",
    "Library closed on Feb 10",
  ]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const resp = await getMyReservedBooks();
        setReservedBooks(resp.data);
        console.log(resp);
        const bResp = await getMyBorrowedBooks();
        setBorrowedBooks(bResp.data);
      } catch (err) {
        setError(err.error.message);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="user-dashboard-container">
      <h2 className="dashboard-title">User Dashboard</h2>

      <div className="dashboard-section">
        <h3>📚 Borrowed Books</h3>
        <ul>
          {borrowedBooks.map((book, index) => (
            <Link key={book.bookCopy}>
            <span>{book.bookTitle}</span>
            <span style={{ margin: "0 23%" }}>-</span>
            <span>Borrow Date: {book.borrowDate}</span>
          </Link> 
          ))}
        </ul>
      </div>

      <div className="dashboard-section">
        <h3>📖 Reserved Books</h3>
        <ul>
          {reservedBooks.map((book, index) => (
            <Link key={book.bookCopy}>
            <span>{book.bookTitle}</span>
            <span style={{ margin: "0 20%" }}>-</span>
            <span>Reservation Date: {book.reservationDate}</span>
          </Link>          
          ))}
        </ul>
      </div>

      <div className="dashboard-section">
        <h3>🔑 Recent Logins</h3>
        <ul>
          {recentLogins.map((login, index) => (
            <li key={index}>{login}</li>
          ))}
        </ul>
      </div>

      <div className="dashboard-section">
        <h3>⏳ Due Reminders</h3>
        <ul>
          {dueReminders.map((reminder, index) => (
            <li key={index}>{reminder}</li>
          ))}
        </ul>
      </div>

      <div className="dashboard-section">
        <h3>🔔 Notifications</h3>
        <ul>
          {notifications.map((note, index) => (
            <li key={index}>{note}</li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default UserDashboard;
