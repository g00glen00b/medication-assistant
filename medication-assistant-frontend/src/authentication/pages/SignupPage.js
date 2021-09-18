import {HeroContainer} from '../../shared/components/HeroContainer';
import {SignupForm} from '../components/SignupForm';
import {useState} from 'react';
import {useCreateUserApi} from '../hooks/apiHooks';
import {Alert} from 'evergreen-ui';

export const SignupPage = () => {
  const [signupInput, setSignupInput] = useState(null);
  const {response, error} = useCreateUserApi(signupInput);

  return (
    <HeroContainer>
      {error && <Alert intent="danger">
        {error.response.data.message}
      </Alert>}
      {response && <Alert intent="success">
        Sign up was succesful. You can now log in.
      </Alert>}
      <SignupForm onSubmit={setSignupInput}/>
    </HeroContainer>
  );
}