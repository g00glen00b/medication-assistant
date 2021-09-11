import './SignupForm.css';
import {isRequired, isSame, useForm} from '../../shared/hooks/useForm';
import {Button, Link, TextInputField} from 'evergreen-ui';
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
    <form
      className="signup-form"
      onSubmit={submitHandler}>
      <h2>Sign up</h2>
      <div className="form-fields">
        <div className="first-name">
          <TextInputField
            required
            label="First name"
            name="firstName"
            description="For example: Harry"
            isInvalid={errors.firstName}
            validationMessage={touched.firstName && errors.firstName}
            onChange={changeHandler}/>
        </div>
        <div className="last-name">
          <TextInputField
            required
            label="Last name"
            name="lastName"
            description="For example: Potter"
            isInvalid={errors.lastName}
            validationMessage={touched.lastName && errors.lastName}
            onChange={changeHandler}/>
        </div>
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
        <div className="repeat-password">
          <TextInputField
            required
            className="repeat-password"
            type="password"
            label="Repeat password"
            name="repeatPassword"
            description="For example: Pa$$w0rd"
            isInvalid={errors.repeatPassword}
            validationMessage={touched.repeatPassword && errors.repeatPassword}
            onChange={changeHandler}/>
        </div>
      </div>
      <div className="actions">
        <Button
          disabled={!isValid}
          appearance="primary"
          size="large">
          Sign up
        </Button>
        <Link
          href="#"
          is={RouterLink}
          to="/login">
          I already have an account. Log me in.
        </Link>
      </div>
    </form>
  );
};