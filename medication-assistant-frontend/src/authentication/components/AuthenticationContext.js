import {createContext, useContext, useEffect, useReducer} from 'react';
import {useCurrentUser} from '../hooks/authenticationApiHooks';

const initialState = {
  user: null
};

export const AuthenticationContext = createContext(initialState);

export const AuthenticationContextProvider = ({children}) => {
  const [state, dispatch] = useReducer((state, action) => {
    switch (action.type) {
      case 'LOGIN':
        return {...state, user: action.payload};
      default:
        return {...state};
    }
  }, initialState);

  return (
    <AuthenticationContext.Provider value={{state, dispatch}}>
      <UserInitializer>
        {children}
      </UserInitializer>
    </AuthenticationContext.Provider>
  );
};

export const UserInitializer = ({children}) => {
  const {dispatch} = useContext(AuthenticationContext);
  const {response} = useCurrentUser();
  useEffect(() => {
    if (response != null) dispatch({type: 'LOGIN', payload: response.data});
  }, [dispatch, response]);

  return <div>{children}</div>;
};