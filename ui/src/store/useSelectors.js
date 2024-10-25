// src/redux/userSelectors.js
import { createSelector } from 'reselect';

// Selector to get user state
const selectUserState = (state) => state.user;

// Memoized selectors
export const selectUserId = createSelector(
  [selectUserState],
  (user) => user.userId
);

export const selectToken = createSelector(
  [selectUserState],
  (user) => user.token
);


export const selectRole = createSelector(
  [selectUserState],
  (user) => user.role
);

export const selectUsername = createSelector(
  [selectUserState],
  (user) => user.username
);
export const selectTokenExpiration = createSelector(
    [selectUserState],
    (user) => user.tokenExpiration
);

// You can add more selectors as needed
