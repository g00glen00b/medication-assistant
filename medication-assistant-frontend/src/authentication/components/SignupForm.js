import {FormField} from '../../shared/components/FormField';
import {Button} from '../../shared/components/Button';
import './SignupForm.css';
import {isRequired, isSame, useForm} from '../../shared/hooks/useForm';


export const SignupForm = (onSubmit) => {
  const initialState = {firstName: '', lastName: '', email: '', password: '', repeatPassword: ''};
  const validations = [
    ({firstName}) => isRequired(firstName) || {firstName: 'First name is required'},
    ({lastName}) => isRequired(lastName) || {lastName: 'Last name is required'},
    ({email}) => isRequired(email) || {email: 'E-mail is required'},
    ({password}) => isRequired(password) || {password: 'Password is required'},
    ({password, repeatPassword}) => isSame(password, repeatPassword) || {repeatPassword: 'Passwords do not match'}
  ];
  const {values, errors, touched, isValid, changeHandler, submitHandler} = useForm(initialState, validations, onSubmit);

  return (
    <form
      className="signup-form"
      onSubmit={submitHandler}>
      <h2>Sign up</h2>
      <div className="form-fields">
        <FormField className="first-name">
          <label>First name</label>
          <input
            type="text"
            placeholder="ex. Harry"
            name="firstName"
            required
            value={values.firstName}
            onChange={changeHandler}/>
          {touched.firstName && errors.firstName && <p className="error">{errors.firstName}</p>}
        </FormField>
        <FormField className="last-name">
          <label>Last name</label>
          <input
            type="text"
            placeholder="ex. Potter"
            name="lastName"
            required
            value={values.lastName}
            onChange={changeHandler}/>
          {touched.lastName && errors.lastName && <p className="error">{errors.lastName}</p>}
        </FormField>
        <FormField className="email">
          <label>E-mail</label>
          <input
            type="email"
            placeholder="ex. harry.potter@example.org"
            name="email"
            required
            value={values.email}
            onChange={changeHandler}/>
          {touched.email && errors.email && <p className="error">{errors.email}</p>}
        </FormField>
        <FormField className="password">
          <label>Password</label>
          <input
            type="password"
            placeholder="ex. Pa$$w0rd"
            name="password"
            required
            value={values.password}
            onChange={changeHandler}/>
          {touched.password && errors.password && <p className="error">{errors.password}</p>}
        </FormField>
        <FormField className="repeat-password">
          <label>Repeat password</label>
          <input
            type="password"
            placeholder="ex. Pa$$w0rd"
            name="repeatPassword"
            required
            value={values.repeatPassword}
            onChange={changeHandler}/>
          {touched.repeatPassword && errors.repeatPassword && <p className="error">{errors.repeatPassword}</p>}
        </FormField>
      </div>
      <div className="actions">
        <Button
          disabled={!isValid}>
          Sign up
        </Button>
        <a href="#">
          I already have an account. Log me in.
        </a>
      </div>
    </form>
  );
};