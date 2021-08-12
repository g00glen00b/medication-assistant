import './SignupPage.css';
import {HeroContainer} from "../../shared/HeroContainer";
import {FormField} from '../../shared/FormField';
import {Button} from '../../shared/Button';

export const SignupPage = () => (
  <HeroContainer>
    <form>
      <h2>Sign up</h2>
      <FormField className="first-name">
        <label>First name</label>
        <input
          type="text"
          placeholder="ex. Harry" />
      </FormField>
      <FormField className="last-name">
        <label>Last name</label>
        <input
          type="text"
          placeholder="ex. Potter" />
      </FormField>
      <FormField className="email">
        <label>E-mail</label>
        <input
          type="email"
          placeholder="ex. harry.potter@example.org" />
      </FormField>
      <FormField className="password">
        <label>Password</label>
        <input
          type="password"
          placeholder="ex. Pa$$w0rd" />
      </FormField>
      <FormField className="repeat-password">
        <label>Repeat password</label>
        <input
          type="password"
          placeholder="ex. Pa$$w0rd" />
      </FormField>
      <div className="actions">
        <Button>
          Sign up
        </Button>
        <a href="#">
          I already have an account. Log me in.
        </a>
      </div>
    </form>
  </HeroContainer>
);