import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { toast } from "react-toastify";
import { getMyBorrowedBooks, getMyReservedBooks, deleteReservedBook } from '../../services/api/memberService';
import MemberBookCard from '../../components/Card/Book/MemberBookCard';
import './Interface.css';

function Interface() {
  const [books, setBooks] = useState([]);
  const [action, setAction] = useState(null);
  const [searchParms] = useSearchParams();
  const type = searchParms.get("type");

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (type === 'borrow') {
          const response = await getMyBorrowedBooks();
          setBooks(response.data);
        } else if (type === 'reserve') {
          const response = await getMyReservedBooks();
          setBooks(response.data);
        } else if (type === 'fav') {
          // const response = await getMyFavoriteBooks();
          // data = response.data.map((book) => ({ ...book, type: 'fav' }));
        }
      } catch (err) {
        console.log(err.error.message);
      }
    };

    fetchData();
  }, [type]);

  // Handle the member response for Reserved Books
  useEffect(() => {
    if (!action) return;
    const cancelBook = async () => {
      try {
        await deleteReservedBook(action);
        toast.success("Cancel Successfully");
        setBooks((prevBooks) => prevBooks.filter(book => book.reservationId !== action));
      } catch (err) {
        toast.error(err.error.message);
      }
    };
    cancelBook();
  }, [action]);

  return (
    <div className='interface-container'>
      <h1>{type === 'borrow' ? 'Borrowed Books' : type === 'reserve' ? 'Reserved Books' : 'Favorite Books'}</h1>
      <div className="mem-book-list">
        {books.length > 0 ? (
          books.map((book) => (
            <MemberBookCard setAction={setAction} key={book.bookCopy} book={book} />
          ))
        ) : (
          <p>No {type === 'borrow' ? 'borrowed' : type === 'reserve' ? 'reserved' : 'favorite'} books found.</p>
        )}
      </div>
    </div>
  );
}

export default Interface;
