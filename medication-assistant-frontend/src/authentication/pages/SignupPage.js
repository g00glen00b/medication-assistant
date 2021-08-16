import './SignupPage.css';
import {HeroContainer} from '../../shared/components/HeroContainer';
import {SignupForm} from '../components/SignupForm';
import {useState} from 'react';
import {useCreateUserApi} from '../hooks/authenticationApiHooks';
import {Message} from '../../shared/components/Message';

export const SignupPage = () => {
  const [signupInput, setSignupInput] = useState(null);
  const {response, error} = useCreateUserApi(signupInput);

  return (
    <HeroContainer>
      {error && <Message type="error">
        {error.response.data.message}
      </Message>}
      {response && <Message type="success">
        Sign up was succesful. You can now log in.
      </Message>}
      <SignupForm onSubmit={setSignupInput}/>
    </HeroContainer>
  );
}