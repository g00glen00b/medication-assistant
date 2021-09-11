import {HeroContainer} from '../../shared/components/HeroContainer';
import {useState} from 'react';
import {LoginForm} from '../components/LoginForm';
import {useLoginApi} from '../hooks/authenticationApiHooks';
import {Alert} from 'evergreen-ui';

export const LoginPage = () => {
  const [loginInput, setLoginInput] = useState({email: null, password: null});
  const {response, error} = useLoginApi(loginInput.email, loginInput.password);

  return (
    <HeroContainer>
      {error && <Alert intent="danger">
        {error.response.data.error}
      </Alert>}
      {response && <Alert type="success">
        Sign up was succesful. You can now log in.
      </Alert>}
      <LoginForm onSubmit={setLoginInput}/>
    </HeroContainer>
  );
}