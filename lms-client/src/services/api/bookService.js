import axios from "axios";
import { getAccessToken } from "./authService";

const BASE_URL = 'http://localhost:8080/admin';

const accessToken = getAccessToken();

const apiClient = axios.create({
    baseURL: BASE_URL,
    headers: { Authorization: `Bearer ${accessToken}` }
});

//  get all borrowd book of a Member
export const memberBorrowedBooks = async (memberId) => {
    try {
        const response = await apiClient.get(`/mem-borrow-rec/${memberId}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }

}


