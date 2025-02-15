import axios from 'axios';
// import dotenv from 'dotenv';
// dotenv.config();
// const hui = process.env.BASE_URL;
// console.log(hui);
const accessToken = localStorage.getItem('accessToken');
const API_BASE_URL = 'http://localhost:8080/admin';


const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    Authorization: `Bearer ${accessToken}`,
    'Content-Type': 'application/json', 
  },
});

// Add new author
export const addAuthor = async (authorData) => {
  try {
    const response = await apiClient.post('/author', authorData); 
    return response.data; 
  } catch (error) {
    throw error.response.data; 
  }
};
// Add new Book
export const addBook = async (authorData) => {
    try {
      const response = await apiClient.post(`/book`, authorData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }); 
      return response.data; 
    } catch (error) {
      throw error.response?.data || error.message; 
    }
  };
  
//  get All AUthor
export const getAllAuthors= async (page) => {
    try {
        const response = await apiClient.get(`/author?page=${page}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }

}

// Add new author
export const addPublisher = async (authorData) => {
    try {
      const response = await apiClient.post('/publisher', authorData); 
      return response.data; 
    } catch (error) {
      throw error.response.data; 
    }
  };

//  get All Publisehr
export const getAllPublishers = async (page) => {
    try {
        const response = await apiClient.get(`/publisher?page=${page}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }

}


//  fetch all the members 
export const getAllMembers = async (page) => {
    try {
        const { data } = await apiClient.get(`/member?page=${page}`);
        return data;

    } catch (error) {

        throw error.response.data;
    }
};
//  get All MemberShip Requests
export const membershipRequests = async (page) => {
    try {
        const { data } = await apiClient.get(`/mem-reg?page=${page}`);
        return data;
    } catch (error) {
        throw error.response.data;
    }
};
// reject the member request
export const rejectMember = async (memberId) => {
    try {
        const response = await apiClient.delete(`reject-reg-req/${memberId}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};
//  find Requested Membership user by their Id
export const findUser = async (id) => {
    try {
        const { data } = await apiClient.get(`/mem-reg/${id}`);
        return data.data;
    } catch (error) {
        throw error.response.data;
    }
};

//  find Member by their Id
export const findMember = async (id) => {
    try {
        const { data } = await apiClient.get(`/member/${id}`);
        return data.data;
    } catch (error) {
        throw error.response.data;
    }
};

//  approve the member requeste
export const approveUser = async (regId) => {
    try {
        const response = await apiClient.post(`approve-reg-req/${regId}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}

//  get current borrowers of a single book
export const getAllBorrowers = async (isbn,page) => {
    try {
        const response = await apiClient.get(`/borrower/${isbn}?${page}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }

}

//  deactivate a member
export const deactivateMember = async (id) => {
    try {
        const response = await apiClient.put(`/deactivate/${id}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}
//  activate member
export const activateMember = async (id) => {
    try {
        const response = await apiClient.put(`/activate/${id}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }

}

//  get all Reserved  book of a Member
export const memberReservedBooks = async (memberId) => {
    try {
        const response = await apiClient.get(`/reserve/${memberId}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}

// get all borrowed books of a member
export const memberBorrowedBooks = async (memberId) => {
    try {
        const response = await apiClient.get(`/mem-borrow-rec/${memberId}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}

//  get all reserved books
export const getAllReservedBook = async () => {
    try {
        const response = await apiClient.get(`/reserve`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}

// get All books with brief details only isbn 
export const getAllBooks = async (page) => {
    try {
        const response = await apiClient.get(`/book?page=${page}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}

//   all borrowed books in library
export const getAllBorrowedBook = async ({ startDate, endDate, page }) => {
    try {
        const response = await apiClient.get(`/borrow-btw?startDate=${startDate}&endDate=${endDate}&page=${page}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}

//  get alll the records of the library regarding book
export const getAllRecords = async ({ startDate, endDate, page }) => {
    try {
        const response = await apiClient.get(`/record-btw?startDate=${startDate}&endDate=${endDate}&page=${page}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}


//  get Dues
export const getDueRecords = async (page) => {
    try {
        const response = await apiClient.get(`/due-record?page=${page}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}

//  get All Lost or Damaged Books
export const getAllDiscardBook = async (page) => {
    try {
        const response = await apiClient.get(`/discard-book?page=${page}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}


//  return Book with condition GOOD,DAMAGE,LOST
export const returnBook = async ({ id, condition }) => {
    try {
        const response = await apiClient.put(`/return-book/${id}/${condition}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}

//  get single book details
export const getBookDetails = async (isbn) => {
    try {
        const response = await apiClient.get(`/book/${isbn}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}

// reject or Approve Reserved book Request
export const actionOnReservedBook = async ({ id, actionStatus }) => {
    console.log(id, actionStatus)
    try {
        const response = await apiClient.delete(`/approveBook/${id}/${actionStatus}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}

// refresh the access token
export const refreshAccessToken = async (refreshToken) => {
    try {
        const response = await apiClient.get(`/accessToken`, {
            headers: {
                Authorization: `Bearer ${refreshToken}`,
            },
        });
        return response.data;
    } catch (error) {
        throw error.response?.data || error;
    }
};



