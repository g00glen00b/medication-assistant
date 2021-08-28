import {HeroContainer} from '../../shared/components/HeroContainer';
import {useState} from 'react';
import {Message} from '../../shared/components/Message';
import {LoginForm} from '../components/LoginForm';
import {useLoginApi} from '../hooks/authenticationApiHooks';

export const LoginPage = () => {
  const [loginInput, setLoginInput] = useState({email: null, password: null});
  const {response, error} = useLoginApi(loginInput.email, loginInput.password);

  return (
    <HeroContainer>
      {error && <Message type="error">
        {error.response.data.error}
      </Message>}
      {response && <Message type="success">
        Sign up was succesful. You can now log in.
      </Message>}
      <LoginForm onSubmit={setLoginInput}/>
    </HeroContainer>
  );
}