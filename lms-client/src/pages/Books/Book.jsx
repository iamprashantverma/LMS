import React, { useEffect, useState, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { getAllBooks, getAllReservedBook, actionOnReservedBook } from '../../services/api/adminService';
import MemberBookCard from '../../components/Card/Book/MemberBookCard';
import Pagination from '../../components/Common/Pagination';
import BookCard from '../../components/Card/Book/BookCard';
import { useContext } from 'react';
import { AuthContext } from '../../Context/AuthContext';
import { getAllBooks as getMemberAllBooks } from '../../services/api/memberService';
import './Book.css';

function Book() {
  const {user} = useContext(AuthContext);
  const { id } = useParams(); 
  const [books, setBooks] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [error, setError] = useState(null);
  console.log(id);
  const fetchBooks = useCallback(async () => {
    try {
      let resp;
      if (id === 'res') {
        resp = await getAllReservedBook(currentPage);
      } else if(id ==='all'){
          resp = await getMemberAllBooks(currentPage);
      } else  {
        resp = await getAllBooks(currentPage);
      }
      setBooks(resp.data.content);
      setTotalPages(resp.data.totalPages);
    } catch (error) {
      console.error(error.message || error);
      setError('Failed to fetch books.');
    } finally {
    }
  }, [currentPage, id]);

  useEffect(() => {
    fetchBooks();
  }, [fetchBooks]);

  const bookAction = async (reservationId, actionStatus) => {
    try {
      await actionOnReservedBook({ id: reservationId, actionStatus: actionStatus });
      setBooks((prevBooks) => prevBooks.filter((book) => book.reservationId !== reservationId));
    } catch (error) {
      setError(error?.message || "Failed to update reservation");
    }
  };

  return (
    <div className='Book-container'>
      {/* ADMIN */}
      {user.role ==='ADMIN'&& <div className="book-content-container">
        {books && books.map((book) => (
          <BookCard bookAction={bookAction} key={book.Id} book={book} />
        ))}
      </div>}

        {/*  MEMBER */}
      {user.role ==='MEMBER' && <div className="book-content-container">
        {books && books.map((book) => (
          <MemberBookCard  key={book.bookId} book={book} />
        ))}
      </div>}
      
      {error && <div className="error-message">{error}</div>}

      {totalPages > 0 && (
        <div className='book-pagination-container'>
          <Pagination
            totalPages={totalPages}
            currentPage={currentPage}
            setCurrentPage={setCurrentPage}
          />
        </div>
      )}
    </div>
  );
}

export default Book;
