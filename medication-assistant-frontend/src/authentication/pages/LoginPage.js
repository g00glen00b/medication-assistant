import {HeroContainer} from '../../shared/components/HeroContainer';
import {useContext, useEffect, useState} from 'react';
import {LoginForm} from '../components/LoginForm';
import {useLoginApi} from '../hooks/apiHooks';
import {Alert} from 'evergreen-ui';
import {Redirect} from 'react-router-dom';
import {AuthenticationContext} from '../components/AuthenticationContext';

export const LoginPage = () => {
  const [loginInput, setLoginInput] = useState({email: null, password: null});

  const {response, error} = useLoginApi(loginInput.email, loginInput.password);
  const {dispatch, state: {user}} = useContext(AuthenticationContext);

  useEffect(() => {
    if (response != null) dispatch({type: 'LOGIN', payload: response.data});
  }, [response, dispatch]);

  return (
    <HeroContainer>
      {error && <Alert intent="danger">
        {error.response.data.error}
      </Alert>}
      {user != null && <Redirect to={`/app`} />}
      <LoginForm onSubmit={setLoginInput}/>
    </HeroContainer>
  );
}