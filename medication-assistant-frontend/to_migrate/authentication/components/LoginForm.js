import {isRequired, useForm} from '../../shared/hooks/useForm';
import {Link as RouterLink} from 'react-router-dom';
import {Button, Form, Input, Typography} from 'antd';

export const LoginForm = ({onSubmit}) => {
  const initialState = {email: '', password: ''};
  const validations = [
    ({email}) => isRequired(email) || {email: 'E-mail is required'},
    ({password}) => isRequired(password) || {password: 'Password is required'}
  ];
  const {errors, touched, isValid, changeHandler, submitHandler} = useForm(initialState, validations, onSubmit);
  const [form] = Form.useForm();
  return (
    <Form
      form={form}
      layout="vertical"
      initialValues={{email: '', password: ''}}
      onValuesChange={arg => console.log(arg)}
      style={{
        width: '100%'
      }}
      onSubmit={submitHandler}>
      <Typography.Title
        style={{
          marginBottom: '1em'
        }}>
        Log in
      </Typography.Title>
      <Form.Item
        label="E-mail">
        <Input
          required
          name="email"
          placeholder="For example: harry.potter@example.org"
          isInvalid={errors.email != null && touched.email}
          validationMessage={touched.email && errors.email}
          onChange={changeHandler}/>
      </Form.Item>
      <Form.Item
        label="Password">
        <Input.Password
          required
          name="password"
          placeholder="For example: Pa$$w0rd"
          isInvalid={errors.password != null && touched.password}
          validationMessage={touched.password && errors.password}
          onChange={changeHandler}/>
      </Form.Item>
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          marginTop: '1em'
        }}>
        <Button
          type="primary"
          size="large"
          disabled={!isValid}>
          Log in
        </Button>
        <Typography.Link
          is={RouterLink}
          to="/signup"
          marginTop="1em"
          alignSelf="center">
          I'm new, sign me up.
        </Typography.Link>
      </div>
    </Form>
  );
};