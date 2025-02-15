import React, { useEffect, useState } from 'react'
import { useContext } from 'react';
import { AuthContext } from '../../Context/AuthContext';
import { useNavigate } from 'react-router-dom';
import './Home.css';
import HomeBookCard from '../../components/Card/Book/HomeBookCard';
import { getAllBooks } from '../../services/api/memberService';
import { Link } from 'react-router-dom';

function Home() {
  const navigate = useNavigate();
  const { user } = useContext(AuthContext);
  const [book, setBook] = useState([]);
  const [todayQuote, setTodayQuote] = useState("there is more treasure in books than in all the piratae's loot on Treasure island");

  useEffect(()=>{
    if(user) {
      if(user.role === "ADMIN")
        navigate("/admin")
      if(user.role === 'MEMBER')
          navigate("/member");
    }
    //  fetching all the data's
    const fetchData = async()=>{
      try {
        const resp = await getAllBooks(0);
        console.log(resp.data.content)
        setBook(resp.data.content);
      } catch(err) {
        console.log(err);
      } finally{

      }
    }
    fetchData();
  },[]) ;

  return (
    <div className='home-page-contaner'>
      
       <div className="home-page-header">
          <div className="home-header-left">
            <p>Today Quote's</p>
            <p>{todayQuote}</p>
          </div>
          <div className="home-header-right">
                <p>this is the right part fo the header of the home page</p>
          </div>
        </div>

        <div className="home-page-middle">
          <div className="home-middle-upper">
            <h3>Good Morning</h3>
            <p>Recommended for You </p>
            <Link to="/member/view-books/all">Show All</Link>
          </div>
          <div className="home-middle-lower">
            {book.slice(0, 4).map((bk) => (
              <HomeBookCard book={bk} key={bk.bookId} />
            ))}
          </div>
        </div>

        <div className="home-page-footer">
          <div className="home-footer-upper">
            <p>New Arrivals </p>
            <Link>Show All</Link>
          </div>
          <div className="home-footer-lower">
            {book.slice(0, 4).map((bk) => (
              <HomeBookCard book={bk} key={bk.bookId} />
            ))}
          </div>
        </div>  
    </div>
  )
}

export default Home
