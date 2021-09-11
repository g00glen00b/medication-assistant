import {isRequired, useForm} from '../../shared/hooks/useForm';
import {Button, Heading, Link, Pane, TextInputField} from 'evergreen-ui';
import {Link as RouterLink} from 'react-router-dom';

export const LoginForm = ({onSubmit}) => {
  const initialState = {email: '', password: ''};
  const validations = [
    ({email}) => isRequired(email) || {email: 'E-mail is required'},
    ({password}) => isRequired(password) || {password: 'Password is required'}
  ];
  const {errors, touched, isValid, changeHandler, submitHandler} = useForm(initialState, validations, onSubmit);

  return (
    <Pane
      is="form"
      width="100%"
      onSubmit={submitHandler}>
      <Heading
        size={800}
        marginBottom="1em">
        Log in
      </Heading>
      <div className="form-fields">
        <div className="email">
          <TextInputField
            required
            type="email"
            label="E-mail"
            name="email"
            description="For example: harry.potter@example.org"
            isInvalid={errors.email}
            validationMessage={touched.email && errors.email}
            onChange={changeHandler}/>
        </div>
        <div className="password">
          <TextInputField
            required
            type="password"
            label="Password"
            name="password"
            description="For example: Pa$$w0rd"
            isInvalid={errors.password}
            validationMessage={touched.password && errors.password}
            onChange={changeHandler}/>
        </div>
      </div>
      <Pane
        display="flex"
        flexDirection="column"
        marginTop="1em">
        <Button
          appearance="primary"
          size="large"
          disabled={!isValid}>
          Log in
        </Button>
        <Link
          is={RouterLink}
          to="/signup"
          marginTop="1em"
          alignSelf="center">
          I'm new, sign me up.
        </Link>
      </Pane>
    </Pane>
  );
};