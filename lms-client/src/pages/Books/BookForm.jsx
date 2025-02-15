import React, { useEffect, useState } from 'react';
import { getAllAuthors, getAllPublishers, addBook } from '../../services/api/adminService'; // Assume these functions are implemented
import './BookForm.css';

function BookForm() {
  const [authors, setAuthors] = useState([]);
  const [publishers, setPublishers] = useState([]);
  const [selectedAuthors, setSelectedAuthors] = useState([]);
  const [selectedPublisher, setSelectedPublisher] = useState('');
  const [bookFormat, setBookFormat] = useState('EBOOK');
  const [count, setCount] = useState(1); 
  const [pdfFile, setPdfFile] = useState(null);
  const [bookCover, setBookCover] = useState(null);
  const [genres, setGenres] = useState([]);

  // Fetch all authors and publishers
  useEffect(() => {
    async function fetchData() {
      try {
        let authorsData = [];
        let publishersData = [];
        let authorsPage = 0;
        let publishersPage = 0;
        
        // Fetch authors until all pages are fetched
        while (true) {
          const response = await getAllAuthors(authorsPage); 
          if (response.data.content.length === 0) break; 
          authorsData = [...authorsData, ...response.data.content];
          authorsPage += 1;
        }

        // Fetch publishers until all pages are fetched
        while (true) {
          const response = await getAllPublishers(publishersPage); 
          if (response.data.content.length === 0) break;
          publishersData = [...publishersData, ...response.data.content];
          publishersPage += 1;
        }

        // Set fetched data to state
        setAuthors(authorsData);
        setPublishers(publishersData);
      } catch (error) {
        console.error("Error fetching authors and publishers", error);
      }
    }

    fetchData();
  }, []);

  // Handle book format change (EBOOK or HARDCOVER)
  const handleFormatChange = (e) => {
    const selectedFormat = e.target.value;
    setBookFormat(selectedFormat);

    // If format is EBOOK, set count to 1 and hide count input
    if (selectedFormat === 'EBOOK') {
      setCount(1); 
    }
  };

  // Handle file upload for the PDF (for EBOOK format only)
  const handlePdfUpload = (e) => {
    const file = e.target.files[0];
    setPdfFile(file);
  };

  // Handle cover image upload
  const handleCoverUpload = (e) => {
    const file = e.target.files[0];
    setBookCover(file);
  };

  // Handle form submission (book creation)
  const handleSubmit = (e) => {
    e.preventDefault();

    const bookData = {
      isbn: e.target.isbn.value,
      title: e.target.title.value,
      subtitle: e.target.subtitle.value || null,
      edition: e.target.edition.value,
      language: e.target.language.value,
      publicationDate: e.target.publicationDate.value,
      pageCount: e.target.pageCount.value,
      format: bookFormat,
      price: parseFloat(e.target.price.value),
      count: count,
      publisherId: selectedPublisher,
      authors: selectedAuthors,
      genres: genres, 
      coverImage: bookCover, 
      pdfFile: bookFormat === 'EBOOK' ? pdfFile : null, 
    };

    const sendData = async() => {
      try {
        const formData = new FormData();
        
        // Add JSON data to FormData (book data)
        formData.append('bookData', new Blob([JSON.stringify(bookData)],{type:"application/json"}));

        // Add the files (book cover and PDF)
        if (bookCover) {
          formData.append('image', bookCover);
        }

        if (bookFormat === "EBOOK" && pdfFile) {
          formData.append('pdfFile', pdfFile);
        }

        console.log(bookData);
        // Send form data (book data and files) to the server
        const resp = await addBook(formData);
        console.log(resp);
      } catch (err) {
        console.error('Error submitting the form:', err);
      }
    };

    sendData();
  };

  return (
    <div className="book-form-container">
      <form onSubmit={handleSubmit} encType="multipart/form-data">
        <div className="form-group">
          <label>ISBN:</label>
          <input type="text" name="isbn" required />
        </div>

        <div className="form-group">
          <label>Title:</label>
          <input type="text" name="title" required />
        </div>

        <div className="form-group">
          <label>Subtitle:</label>
          <input type="text" name="subtitle" />
        </div>

        <div className="form-group">
          <label>Edition:</label>
          <input type="text" name="edition" required />
        </div>

        <div className="form-group">
          <label>Language:</label>
          <input type="text" name="language" required />
        </div>

        <div className="form-group">
          <label>Publication Date:</label>
          <input type="date" name="publicationDate" required />
        </div>

        <div className="form-group">
          <label>Page Count:</label>
          <input type="number" name="pageCount" required />
        </div>

        <div className="form-group">
          <label>Price:</label>
          <input type="number" name="price" step="0.01" required />
        </div>

        <div className="form-group">
          <label>Publisher:</label>
          <select
            value={selectedPublisher}
            onChange={(e) => setSelectedPublisher(e.target.value)}
            required
          >
            <option value="">Select Publisher</option>
            {publishers.map((publisher) => (
              <option key={publisher.publisherId} value={publisher.publisherId}>
                {publisher.name}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Authors:</label>
          <select
            multiple
            value={selectedAuthors}
            onChange={(e) => setSelectedAuthors([...e.target.selectedOptions].map(option => option.value))}
            required
          >
            {authors.map((author) => (
              <option key={author.authorId} value={author.authorId}>
                {author.name}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Genres:</label>
          <select
            multiple
            value={genres}
            onChange={(e) => setGenres([...e.target.selectedOptions].map(option => option.value))}
            required
          >
            <option value="FICTION">FICTION</option>
            <option value="NON_FICTION">NON_FICTION</option>
            <option value="SCIENCE_FICTION">SCIENCE_FICTION</option>
            <option value="FANTASY">FANTASY</option>
            <option value="HISTORY">HISTORY</option>
            <option value="BIOGRAPHY">BIOGRAPHY</option>
          </select>
        </div>

        <div className="form-group">
          <label>Format:</label>
          <select value={bookFormat} onChange={handleFormatChange}>
            <option value="EBOOK">EBOOK</option>
            <option value="HARDCOVER">HARDCOVER</option>
          </select>
        </div>

        {bookFormat === 'EBOOK' && (
          <>
            <div className="form-group">
              <label>Count (for EBOOK):</label>
              <input
                type="number"
                value={count}
                onChange={(e) => setCount(parseInt(e.target.value))}
                min="1"
                disabled // Disable count input for EBOOK
              />
            </div>
            <div className="form-group">
              <label>Upload PDF:</label>
              <input type="file" accept=".pdf" onChange={handlePdfUpload} required />
            </div>
          </>
        )}

        {bookFormat === 'HARDCOVER' && (
          <div className="form-group">
            <label>Count (for HARDCOVER):</label>
            <input
              type="number"
              value={count}
              onChange={(e) => setCount(parseInt(e.target.value))}
              min="1"
            />
          </div>
        )}

        <div className="form-group">
          <label>Book Cover:</label>
          <input type="file" accept="image/*" onChange={handleCoverUpload} required />
        </div>

        <button type="submit">Submit</button>
      </form>
    </div>
  );
}

export default BookForm;
