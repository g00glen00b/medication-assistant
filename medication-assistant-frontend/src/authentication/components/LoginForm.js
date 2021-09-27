import {Link} from 'react-router-dom';
import {Button, Form, Input, Typography} from 'antd';

export const LoginForm = ({onSubmit}) => {
  const [form] = Form.useForm();
  return (
    <Form
      form={form}
      layout="vertical"
      requiredMark="optional"
      initialValues={{email: '', password: ''}}
      onFinish={onSubmit}
      style={{
        width: '100%'
      }}
      autoComplete="off">
      <Typography.Title
        style={{
          marginBottom: '1em'
        }}>
        Log in
      </Typography.Title>
      <Form.Item
        name="email"
        label="E-mail"
        extra="For example: harry.potter@example.org"
        rules={[{
          required: true,
          message: 'Please provide your e-mail address'
        }, {
          type: 'email',
          message: 'Please enter a valid e-mail address'
        }]}>
        <Input/>
      </Form.Item>
      <Form.Item
        label="Password"
        name="password"
        extra="For example: Pa$$w0rd"
        rules={[{
          required: true,
          message: 'Please provide your password'
        }]}>
        <Input.Password />
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
          htmlType="submit">
          Log in
        </Button>
        <Link
          to="/signup"
          style={{
            marginTop: '1em',
            alignSelf: 'center'
          }}>
          I'm new, sign me up.
        </Link>
      </div>
    </Form>
  );
};