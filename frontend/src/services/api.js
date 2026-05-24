import axios from 'axios';

const API_BASE_URL = 'http://localhost:8095/app';

const api = axios.create({
  baseURL: API_BASE_URL,
});

// Users
export const getUsers = () => api.get('/');
export const getUser = (username) => api.get(`/${username}`);
export const createUser = (userData) => api.post('/', userData);
export const updateUser = (id, userData) => api.put(`/${id}`, userData);

// Friends
export const getFriends = (userId) => api.get(`/${userId}/friend`);
export const addFriend = (userId, friendId) => api.post(`/friend`, { userId, friendId });
export const removeFriend = (userId, friendId) => api.delete(`/friend`, { data: { userId, friendId } });

// Transactions (Ledger)
export const createTransaction = (giverId, receiverId, amount) => 
  api.post(`/${giverId}/ledger/${receiverId}?amount=${amount}`);

export const updateTransactionStatus = (ledgerId, paid) => 
  api.patch(`/ledger/${ledgerId}/status?paid=${paid}`);

export const deleteTransaction = (ledgerId) => 
  api.delete(`/ledger/${ledgerId}`);

export const getTransactionsByReceiver = (giverId, receiverId) => 
  api.get(`/ledger/receiver/${receiverId}?giverId=${giverId}`);

export const getTransactionsByGiver = (giverId) => 
  api.get(`/${giverId}/ledger/giver/`);

export const getTransactionsByGiverAndStatus = (giverId, status) => 
  api.get(`/${giverId}/ledger/giver/status?status=${status}`);

export default api;
