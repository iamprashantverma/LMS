import "./BookCard.css";
import { Link } from "react-router-dom";
import { FaCheckCircle, FaTimesCircle } from "react-icons/fa";

function BookCard({ book, setAction, bookAction }) {
  const authorNames =
    book?.authors &&
    book.authors.map((author) => {
      const nameMatch = author.match(/name=([^,]+)/);
      return nameMatch ? nameMatch[1] : "Unknown";
    });

  return (
    <div className="book-card-container">
      <div className="book-Header">
        <div className="book-card-image">
          <img src={book.image} alt="book" width="130px" height="130px" />
        </div>
        <div className="book-card-header-content">
          <p style={{ fontSize: "16px" }}>{book.title || book.bookTitle}</p>

          {book?.bookCopy && <p>Book ID: {book.bookCopy}</p>}
          {book?.reservationId && <p>{book.reservationId}</p>}

          {/* Inline details: Condition, ISBN, Format */}
          <div style={{ display: "flex", gap: "12px", flexWrap: "wrap" }}>
            {/* Condition */}
            {book?.condition && (
              <span>
                <span style={{ color: "#555", fontWeight: "bold" }}>
                  Condition:
                </span>{" "}
                <span
                  style={{
                    color:
                      book.condition === "New"
                        ? "green"
                        : book.condition === "Good"
                        ? "blue"
                        : book.condition === "Fair"
                        ? "orange"
                        : "purple",
                    fontWeight: "bold",
                  }}
                >
                  {book.condition}
                </span>
              </span>
            )}

            {/* ISBN */}
          {book?.bookIsbn && (
            <span style={{ fontSize: "14px" }}>
              <span style={{ color: "#555", fontWeight: "bold" }}>ISBN:</span>{" "}
              <Link
                to={`/admin/bookdetails?isbn=${book.bookIsbn}`}
                style={{ color: "#0077cc", fontWeight: "bold", fontSize: "14px" }}
              >
                {book.bookIsbn}
              </Link>
            </span>
          )}


            {/* Format */}
            {book?.format && (
              <span>
                <span style={{ color: "#555", fontWeight: "bold" }}>
                  Format:
                </span>{" "}
                <span
                  style={{
                    color:
                      book.format.toLowerCase() === "hardcover"
                        ? "#8B4513"
                        : book.format.toLowerCase() === "paperback"
                        ? "#DAA520"
                        : "#555",
                    fontWeight: "bold",
                  }}
                >
                  {book.format}
                </span>
              </span>
            )}
          </div>

          {book?.language && <p>Language: {book.language}</p>}
        </div>
      </div>

      <div className="book-footer">
        <div className="book-card-footer-content">
          {book?.memberName && <p>Member: {book.memberName}</p>}
          {book?.reservationDate && (
            <p>Reservation Date: {book.reservationDate}</p>
          )}
          {book?.publicationDate && (
            <p>Publication Date: {book.publicationDate}</p>
          )}
          {book?.authors &&
            authorNames.map((auth, index) => <p key={index}>{auth}</p>)}
        </div>

        {/* Action Buttons */}
        {book?.reservationDate && (
          <div className="book-action-buttons">
            <button
              className="approve-button"
              onClick={() => {
                bookAction(book.reservationId, "APPROVE");
              }}
              disabled={book.status === "APPROVED"}
            >
              <FaCheckCircle size={20} /> Approve
            </button>
            <button
              className="reject-button"
              onClick={() => {
                bookAction(book.reservationId, "REJECT");
              }}
              disabled={book.status === "REJECTED"}
            >
              <FaTimesCircle size={20} /> Reject
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default BookCard;
