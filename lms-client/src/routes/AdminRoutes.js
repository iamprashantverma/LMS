import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Layout from '../components/Layout/Layout';
import Book from '../pages/Books/Book';
import PrivateRoute from './PrivateRoute'; 
import BookDetailsPage from '../pages/Books/BookDetailsPage';
import Member from '../pages/Members/Member';
import MemberDetailsPage from '../pages/Members/MemberDetailsPage';
import DashBoard from '../pages/Admin/DashBoard';
import AuthorForm from '../pages/Author/AuthorForm';
import Author from '../pages/Author/Author';
import PublisherForm from '../pages/Publisher/PublisherForm';
import Publisher from '../pages/Publisher/Publisher';
import BookForm from '../pages/Books/BookForm';
import Profile from '../pages/Admin/Profile';
import Borrow from '../pages/Record/Borrow';
import DuePage from '../pages/Record/DuePage';
import RecordPage from '../pages/Record/RecordPage';
import DiscardedBooks from '../pages/Record/DiscardBooks';
import ReturnBookForm from '../pages/Record/ReturnBookForm';

function AdminRoutes() {

  return (
    <Routes>
      <Route element={<PrivateRoute />}>
        <Route path="/" element={<Layout />}>
          
          <Route path='profile' element={<Profile />} />
          <Route index element={<DashBoard />} />
          
          <Route path="books/:id" element={<Book />} />
          <Route path='add-book' element={<BookForm />} />
          <Route path="bookdetails" element={<BookDetailsPage />} />
          <Route path="return" element={<ReturnBookForm/>} />

          {/* Member Related Routes */}
          <Route path='members' element={<Member />} />
          <Route path='mem-details' element={<MemberDetailsPage />} /> {/* Dynamic Member Details */}
          <Route path='mem-req' element={<Member />} />

          {/* Author Related Routes */}
          <Route path='add-author' element={<AuthorForm />} />
          <Route path='authors' element={<Author />} />

          {/* Publisher Related Routes */}
          <Route path='add-publisher' element={<PublisherForm />} />
          <Route path='publishers' element={<Publisher />} />

          {/* Catch-all Route for incorrect paths */}
          <Route path='borrow-reports' element= {<Borrow/>} />
          <Route path='due-reports' element= {<DuePage/>} />
          <Route path='all-records' element= {<RecordPage/>} />
          <Route path='discard-books' element= {<DiscardedBooks/>} />
          <Route path="*" element={<p>Wrong route under /admin</p>} />
        </Route>
      </Route>
    </Routes>
  );
}

export default AdminRoutes;
