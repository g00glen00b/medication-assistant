import {createContext, useReducer} from 'react';
import {useCurrentUser} from '../hooks/apiHooks';

const initialState = {
  user: null,
  loadingUser: true
};

export const AuthenticationContext = createContext(initialState);

export const AuthenticationContextProvider = ({children}) => {
  const [state, dispatch] = useReducer((state, action) => {
    switch (action.type) {
      case 'LOGIN':
        return {...state, user: action.payload, loadingUser: false};
      case 'NO_LOGIN':
        return {...state, loadingUser: false};
      default:
        return {...state};
    }
  }, initialState);
  const {response, error} = useCurrentUser(state.loadingUser);
  if (response != null && state.loadingUser) dispatch({type: 'LOGIN', payload: response});
  if (error != null && state.loadingUser) dispatch({type: 'NO_LOGIN'});

  return (
    <AuthenticationContext.Provider value={{state, dispatch}}>
      {!state.loadingUser && children}
    </AuthenticationContext.Provider>
  );
};