import {isRequired, isSame, useForm} from '../../shared/hooks/useForm';
// import {Button, Heading, Link, Pane, TextInputField} from 'evergreen-ui';
import {Link as RouterLink} from 'react-router-dom';


export const SignupForm = ({onSubmit}) => {
  const initialState = {firstName: '', lastName: '', email: '', password: '', repeatPassword: ''};
  const validations = [
    ({firstName}) => isRequired(firstName) || {firstName: 'First name is required'},
    ({lastName}) => isRequired(lastName) || {lastName: 'Last name is required'},
    ({email}) => isRequired(email) || {email: 'E-mail is required'},
    ({password}) => isRequired(password) || {password: 'Password is required'},
    ({password, repeatPassword}) => isSame(password, repeatPassword) || {repeatPassword: 'Passwords do not match'}
  ];
  const onSubmitWrapper = ({repeatPassword, ...rest}) => onSubmit(rest);
  const {errors, touched, isValid, changeHandler, submitHandler} = useForm(initialState, validations, onSubmitWrapper);
  return (
    <Pane
      is="form"
      width="100%"
      onSubmit={submitHandler}>
      <Heading
        size={800}
        marginBottom="1em">
        Sign up
      </Heading>
      <Pane
        display="grid"
        gridTemplateColumns="repeat(2, 1fr)"
        gridTemplateRows="repeat(3, auto)">
        <TextInputField
          required
          gridRow={1}
          gridColumn={1}
          paddingRight="1em"
          label="First name"
          name="firstName"
          description="For example: Harry"
          isInvalid={errors.firstName != null && touched.firstName}
          validationMessage={touched.firstName && errors.firstName}
          onChange={changeHandler}/>
        <TextInputField
          required
          gridRow={1}
          gridColumn={2}
          paddingLeft="1em"
          label="Last name"
          name="lastName"
          description="For example: Potter"
          isInvalid={errors.lastName != null && touched.lastName}
          validationMessage={touched.lastName && errors.lastName}
          onChange={changeHandler}/>
        <TextInputField
          required
          gridRow={2}
          gridColumnStart={1}
          gridColumnEnd={3}
          type="email"
          label="E-mail"
          name="email"
          description="For example: harry.potter@example.org"
          isInvalid={errors.email != null && touched.email}
          validationMessage={touched.email && errors.email}
          onChange={changeHandler}/>
        <TextInputField
          required
          gridRow={3}
          gridColumn={1}
          paddingRight="1em"
          type="password"
          label="Password"
          name="password"
          description="For example: Pa$$w0rd"
          isInvalid={errors.password != null && touched.password}
          validationMessage={touched.password && errors.password}
          onChange={changeHandler}/>
        <TextInputField
          required
          gridRow={3}
          gridColumn={2}
          paddingLeft="1em"
          className="repeat-password"
          type="password"
          label="Repeat password"
          name="repeatPassword"
          description="For example: Pa$$w0rd"
          isInvalid={errors.repeatPassword != null && touched.repeatPassword}
          validationMessage={touched.repeatPassword && errors.repeatPassword}
          onChange={changeHandler}/>
      </Pane>
      <Pane
        display="flex"
        flexDirection="column"
        marginTop="1em">
        <Button
          disabled={!isValid}
          appearance="primary"
          size="large">
          Sign up
        </Button>
        <Link
          href="#"
          marginTop="1em"
          alignSelf="center"
          is={RouterLink}
          to="/login">
          I already have an account. Log me in.
        </Link>
      </Pane>
    </Pane>
  );
};