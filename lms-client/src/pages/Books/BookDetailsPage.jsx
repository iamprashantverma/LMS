import React, {  useEffect, useState } from 'react'
import Loading from '../../components/Common/Loading'
import { useSearchParams } from 'react-router-dom';
import { getBookDetails } from '../../services/api/adminService';
import BookDetails from '../../components/Card/Book/BookDetails';

function BookDetailsPage() {
    const [searchParams] = useSearchParams();
    const isbn = searchParams.get('isbn'); 
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [book, setBook ] = useState(null);
          
    useEffect(()=>{
        setError(null);
        setBook(null);
       const bookHandler  = async()=>{
        try {
            const {data} = await getBookDetails(isbn);
            setBook(data);
        } catch (error){
            console.log(error)
            setError(error.error.message);
        } finally {
            setLoading(false);
        }
       }
       bookHandler();
    },[isbn])

    if (loading )
        return <Loading/>
    
  return (
    <div className='book-detail-page'>
        { book && 
            <>
            <div className='book-detail'>
              <BookDetails book={book} />
            </div>
            <div className='borrowers-data'>
                
            </div> 
            </>
        }
        { error &&
            <>
            <p> {error}</p>
            </>
        }

    </div>
  )
}

export default BookDetailsPage
