import {createContext, useEffect, useReducer, useState} from 'react';
import {useCurrentUser} from '../hooks/apiHooks';

const initialState = {
  user: null
};

function stateReducer(state, action) {
  switch (action.type) {
    case 'LOGIN':
      return {...state, user: action.payload};
    default:
      return {...state};
  }
}

export const AuthenticationContext = createContext(initialState);

export const AuthenticationContextProvider = ({children}) => {
  const [response, setResponse] = useState(null);
  const [state, dispatch] = useReducer(stateReducer, initialState);
  useCurrentUser(setResponse);
  useEffect(() => {
    if (response != null) dispatch({type: 'LOGIN', payload: response});
  }, [dispatch, response]);

  return (
    <AuthenticationContext.Provider value={{state, dispatch}}>
      {children}
    </AuthenticationContext.Provider>
  );
};