import axios from "axios";
import { ChatRoomType } from "./types";

const API_BASE_URL = "https://chat-backend.mangjoo.com/api/v1"; // Backend Url

export const register = async ({ email, password, name }) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/register`, {
            email,
            password,
            name
        });
        return response.data;
    } catch (error) {
        console.error("ERROR request api [register]: ", error);
        return null;
    }
};

export const login = async ({ email, password }) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/login`, {
        email,
        password
    });   
    return response.data;
  } catch (error) {
    console.error("ERROR request api [login]: ", error);
    return null;
  }
};

export const chatrooms = async () => {
    try {
        const response = await axios.get<ChatRoomType[]>(`${API_BASE_URL}/chat/waiting-rooms`);
        return response.data;
    } catch (error) {
        console.error("ERROR request api [fetchChatromms]: ", error);
        return null;
    }
};