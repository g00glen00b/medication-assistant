import {HeroContainer} from '../../shared/components/HeroContainer';
import {useContext, useEffect, useState} from 'react';
import {LoginForm} from '../components/LoginForm';
import {useLoginApi} from '../hooks/apiHooks';
import {Redirect} from 'react-router-dom';
import {AuthenticationContext} from '../components/AuthenticationContext';
import {Alert} from 'antd';

export const LoginPage = () => {
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  const [loginInput, setLoginInput] = useState({email: null, password: null});
  const {dispatch, state: {user}} = useContext(AuthenticationContext);

  useLoginApi(loginInput.email, loginInput.password, setResponse, setError);
  useEffect(() => {
    if (response != null) dispatch({type: 'LOGIN', payload: response});
  }, [response, dispatch]);

  return (
    <HeroContainer>
      {error && <Alert
        type="error"
        message={error.error} />}
      {user != null && <Redirect to={`/app`} />}
      <LoginForm onSubmit={setLoginInput}/>
    </HeroContainer>
  );
}