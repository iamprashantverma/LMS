import React, { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { toast } from "react-toastify";
import {
  findMember,
  memberReservedBooks,
  deactivateMember,
  activateMember,
  memberBorrowedBooks,
  actionOnReservedBook,
  findUser,
  rejectMember,
  approveUser,
} from "../../services/api/adminService";
import Loading from "../../components/Common/Loading";
import BookCard from "../../components/Card/Book/BookCard";
import MemberDetails from "../../components/Card/Member/MemberDetails";
import "./MemberDetailsPage.css";

function MemberDetailsPage() {
  const [searchParams] = useSearchParams();
  const id = searchParams.get("id");

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [member, setMember] = useState(null);
  const [action, setAction] = useState(null);
  const [bookList, setBookList] = useState(null);
  const [refresh, setRefresh] = useState(false);

  // Fetch member or user data based on ID
  useEffect(() => {
    if (!id) {
      setError("Please enter a valid User ID");
      setLoading(false);
      return;
    }
    const fetchData = async () => {
      setError(null);
      setLoading(true);
      try {
        let resp;
        if (id.startsWith("USER-")) {
          resp = await findMember(id);
        } else {
          resp = await findUser(id);
        }
        setMember(resp);
 
      } catch (err) {
        toast.error(err?.error?.message || "Failed to fetch details");
        
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id, refresh]);

  // Handle actions such as activating, deactivating, fetching books, etc.
  useEffect(() => {
    const handleAdminAction = async () => {
      if (!action) return;

      try {
        if (action === "deactive") {
          await deactivateMember(id);
          setRefresh((prev) => !prev);
        } else if (action === "active") {
          await activateMember(id);
          setRefresh((prev) => !prev);
        } else if (action === "borrowed") {
          const { data } = await memberBorrowedBooks(id);
          setBookList({ type: "borrowed", books: data });
        } else if (action === "reserved") {
          const { data } = await memberReservedBooks(id);
          setBookList({ type: "reserved", books: data });
        }
      } catch (err) {
        toast.error(err?.error?.message || "Failed to fetch details");
      } finally {
        setAction(null);
      }
    };

    handleAdminAction();
  }, [action, id]);

  // Handle book actions (approve/reject reservation)
  const bookAction = async (reservationId, actionStatus) => {
    try {
      await actionOnReservedBook({ id: reservationId, actionStatus });
      setBookList((prevBookList) => {
        if (Array.isArray(prevBookList?.books)) {
          return {
            ...prevBookList,
            books: prevBookList.books.filter(
              (book) => book.reservationId !== reservationId
            ),
          };
        }
        return prevBookList;

      });
    } catch (error) {
      toast.error(error?.error?.message || "Failed to fetch details");
    }
  };

  // Handle user actions (approve/reject membership requests)
  const userAction = async (id, memAction) => {
    try {
      let resp;
      if (memAction === "approve") {
        resp = await approveUser(id);
      } else if (memAction === "reject") {
        resp = await rejectMember(id);
      }
      setMember(resp.data);
      toast.success("Successfully Approved");
    } catch (err) {
      setError(err?.error?.message || "Failed to process request");
    }
  };

  return (
    <div className="member-detail-page-main-container">
      {/* Member Details Section */}
     {member && <div className="member-info-container">
        <MemberDetails
          userAction={userAction}
          setAction={setAction}
          member={member}
        />
      </div>}
  
      {/* Borrowed Books Section */}
    <div className="main-catlog-container">
      <div className="borrowed-books-container">
        {bookList?.type === "borrowed" && (
          <div className="member-borrow-book">
            <h3>Borrowed Books</h3>
            <ul className="mem-bor-cont">
              {bookList.books.map((book) => (
                <BookCard
                  bookAction={bookAction}
                  book={book}
                  key={book.isbn}
                />
              ))}
            </ul>
          </div>
        )}
      </div>
  
      {/* Reserved Books Section */}
      <div className="reserved-books-container">
        {bookList?.type === "reserved" && (
          <div className="member-reserved-book">
            <h3>Reserved Books</h3>
            <ul className="mem-res-cont">
              {bookList.books.map((book) => (
                <BookCard
                  bookAction={bookAction}
                  book={book}
                  key={book.reservationId}
                />
              ))}
            </ul>
          </div>
        )}
       
      </div>
    </div>
  
      {/* Loading and Error Messages */}
      {/* {loading && <Loading />} */}
      {/* {error && <p className="error-message">{error}</p>} */}
    </div>
  );
  
}

export default MemberDetailsPage;
