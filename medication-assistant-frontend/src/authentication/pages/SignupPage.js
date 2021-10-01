import {useState} from 'react';
import {Alert} from 'antd';
import {useCreateUserApi} from '../hooks/apiHooks';
import {HeroContainer} from '../../shared/components/HeroContainer';
import {SignupForm} from '../components/SignupForm';

export const SignupPage = () => {
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  const [signupInput, setSignupInput] = useState(null);
  useCreateUserApi(signupInput, setResponse, setError);

  return (
    <HeroContainer>
      {error && <Alert
        type="error"
        message={error.message}/>}
      {response && <Alert
        type="success"
        message="Sign up was succesful. You can now log in." />}
      <SignupForm onSubmit={setSignupInput}/>
    </HeroContainer>
  );
}