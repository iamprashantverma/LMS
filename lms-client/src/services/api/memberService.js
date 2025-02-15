import { getAccessToken } from "./authService";
import axios from 'axios';

const accessToken = getAccessToken();
const API_BASE_URL = 'http://localhost:8080/member';

const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        Authorization: `Bearer ${accessToken}`,
    },
});

export const getAllBooks = async (page) => {
    try {
        const response = await apiClient.get(`/book?page=${page}`, {
            headers: {
                Authorization: '', 
            },
        });
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

export const getMyReservedBooks = async () => {
    try {
        const response = await apiClient.get(`/reserve`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

export const deleteReservedBook = async (id) => {
    try {
        const response = await apiClient.delete(`/reserve/${id}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

export const getMyBorrowedBooks = async () => {
    try {
        const response = await apiClient.get(`/mem-borrow-rec`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

