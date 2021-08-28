import {FormField} from '../../shared/components/FormField';
import {Button} from '../../shared/components/Button';
import './LoginForm.css';
import {isRequired, useForm} from '../../shared/hooks/useForm';


export const LoginForm = ({onSubmit}) => {
  const initialState = {email: '', password: ''};
  const validations = [
    ({email}) => isRequired(email) || {email: 'E-mail is required'},
    ({password}) => isRequired(password) || {password: 'Password is required'}
  ];
  const {values, errors, touched, isValid, changeHandler, submitHandler} = useForm(initialState, validations, onSubmit);

  return (
    <form
      className="login-form"
      onSubmit={submitHandler}>
      <h2>Log in</h2>
      <div className="form-fields">
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
      </div>
      <div className="actions">
        <Button
          disabled={!isValid}>
          Log in
        </Button>
        <a href="#">
          I'm new, sign me up.
        </a>
      </div>
    </form>
  );
};