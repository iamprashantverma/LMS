import React from 'react'
import './HomeBookCard.css' ;

function HomeBookCard({book}) {
  return (
    <div className='home-book-card'>
        <div className="home-book-card-header">
            <img
                src={book.image}
                alt='book pic'
            />
        </div>
        <div className="home-book-card-footer">
            <p>{book.bookTitle}</p>
            <p> <strong>Format</strong> {book.format}</p>
            <p id={(book.isAvailable)?'true':book.format==='EBOOK'?'pdf':'false'}>{(book.isAvailable)?'Available':book.format==='EBOOK'?'PDF':'Not Available'}</p>
        </div>
      
    </div>
  )
}

export default HomeBookCard
