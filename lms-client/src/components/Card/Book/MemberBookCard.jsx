import React from 'react';
import { Link } from 'react-router-dom';
import { FaTimesCircle } from 'react-icons/fa';
import './MemberBookCard.css';
import { reserveBook } from '../../../services/api/memberService';
import { toast } from 'react-toastify';

function MemberBookCard({ book, setAction }) {
    console.log(book);
  const handleReserve = async () => {
    try {
      await reserveBook(book.bookId      );
      toast.success("Book reserved successfully!");
    } catch (err) {
      toast.error(err?.error?.message || "Reservation failed");
    }
  };

  return (
    <div className='mem-book-card-container'>
      <div className="mem-book-card-header">
        <div className="mem-book-card-image">
          <img
            src={book.image}
            alt="book-image"
            width="120px"
            height="120px"
          />
        </div>
        <div className="mem-book-card-header-content">
          <p>{book.bookTitle}</p>
          {book?.bookCopy && <p>Book ID: {book.bookCopy}</p>}
          {book?.reservationId && <p>{book.reservationId}</p>}
          {book?.recordId && <p>{book.recordId}</p>}
          {(book?.bookIsbn || book.bookIsbn) && (
            <Link>ISBN: {book.isbn || book.bookIsbn}</Link>
          )}
        </div>
      </div>

      <div className="mem-book-card-footer">
        <div className="mem-book-detail-footer">
          {book?.borrowDate && (
            <div className="mem-card-items">
              <p>Borrow On:</p>
              <p>{book.borrowDate}</p>
            </div>
          )}
          {book?.reservationDate && (
            <div className="mem-card-items">
              <p>Reservation Date:</p>
              <p>{book.reservationDate}</p>
            </div>
          )}
          {book?.format && (
            <div className="mem-card-items">
              <p>Format:</p>
              <p>{book.format}</p>
            </div>
          )}
          <div className="mem-card-items">
            <p>Available:</p>
            <p className={book.format === 'EBOOK' ? 'available' : book.isAvailable ? 'available' : 'notavailable'}>
              {book.format === 'EBOOK' ? 'Yes' : book.isAvailable ? 'Yes' : 'No'}
            </p>
          </div>
          {book?.issuedBy && (
            <div className="mem-card-items">
              <p>Issued By:</p>
              <p>{book.issuedBy}</p>
            </div>
          )}
        </div>

        <div className="mem-book-card-button-action">
          {book?.reservationId && (
            <button onClick={() => setAction(book.reservationId)}>
              <FaTimesCircle size={20} /> Cancel
            </button>
          )}

          {!book?.reservationId && book.format === "HARDCOVER" && (
            book.isAvailable ? (
              <button className="reserve-btn" onClick={handleReserve}>Reserve</button>
            ) : (
              <p className="notavailable">Currently Unavailable</p>
            )
          )}
        </div>
      </div>
    </div>
  );
}

export default MemberBookCard;
