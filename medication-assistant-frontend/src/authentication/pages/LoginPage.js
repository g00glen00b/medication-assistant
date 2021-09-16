import {HeroContainer} from '../../shared/components/HeroContainer';
import {useContext, useState} from 'react';
import {LoginForm} from '../components/LoginForm';
import {useLoginApi} from '../hooks/authenticationApiHooks';
import {Alert} from 'evergreen-ui';
import {Redirect} from 'react-router-dom';
import {AuthenticationContext} from '../components/AuthenticationContext';

export const LoginPage = () => {
  const [loginInput, setLoginInput] = useState({email: null, password: null});

  const {response, error} = useLoginApi(loginInput.email, loginInput.password);
  const {dispatch, state: {user}} = useContext(AuthenticationContext);

  if (response != null) dispatch({type: 'LOGIN', payload: response.data});

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