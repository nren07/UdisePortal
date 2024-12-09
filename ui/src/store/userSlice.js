import { createSlice } from '@reduxjs/toolkit';

// Helper: Load state from sessionStorage
const loadFromSessionStorage = () => {
  try {
    const serializedState = sessionStorage.getItem('userState');
    return serializedState ? JSON.parse(serializedState) : {};
  } catch (e) {
    console.error('Could not load state:', e);
    return {};
  }
};

// Initial state from sessionStorage (or defaults)
const initialState = {
  userId: loadFromSessionStorage()?.userId || null,
  role: loadFromSessionStorage()?.role || null,
  token: loadFromSessionStorage()?.token || null,
  username: loadFromSessionStorage()?.username || null,  // Added username
  tokenExpiration: loadFromSessionStorage()?.tokenExpiration || null, // Add token expiration
};

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setUserId(state, action) {
      state.userId = action.payload;
      sessionStorage.setItem('userState', JSON.stringify(state));
    },
    setRole(state, action) {
      state.role = action.payload;
      sessionStorage.setItem('userState', JSON.stringify(state));
    },
    setToken(state, action) {
      state.token = action.payload;
      state.tokenExpiration = Date.now() + 50 * 60 * 1000; // Set expiration to 50 minute from now
      sessionStorage.setItem('userState', JSON.stringify(state));
    },
    setUsername(state, action) {  // Added action to set username
      state.username = action.payload;
      sessionStorage.setItem('userState', JSON.stringify(state));
    },
    clearUser(state) {
      sessionStorage.removeItem('userState');
      state=initialState;
    },
  },
});

export const {
  setUserId,
  setRole,
  setToken,
  setUsername,  // Export the setUsername action
  clearUser,
} = userSlice.actions;

export default userSlice.reducer;
