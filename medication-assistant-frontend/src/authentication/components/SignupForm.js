import {Button, Form, Input, Typography} from 'antd';
import {Link} from 'react-router-dom';

function samePasswordRule({getFieldValue}) {
  return {
    validator(_, value) {
      if (!value || getFieldValue('password') === value) {
        return Promise.resolve();
      } else {
        return Promise.reject(new Error('The passwords do not match'));
      }
    }
  }
}

export const SignupForm = ({onSubmit}) => {
  const [form] = Form.useForm();
  return (
    <Form
      form={form}
      layout="vertical"
      requiredMark="optional"
      initialValues={{
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        repeatPassword: ''
      }}
      onFinish={({repeatPassword, ...rest}) => onSubmit(rest)}
      style={{
        width: '100%'
      }}
      autoComplete="off">
      <Typography.Title
        style={{
          marginBottom: '1em'
        }}>
        Sign up
      </Typography.Title>

      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(2, 1fr)',
          gridTemplateRows: 'repeat(3, auto)'
        }}>
        <Form.Item
          name="firstName"
          label="First name"
          extra="For example: Harry"
          rules={[{
            required: true,
            message: 'Please provide your first name'
          }]}
          style={{
            gridRow: 1,
            gridColumn: 1,
            marginRight: '1em'
          }}>
          <Input/>
        </Form.Item>
        <Form.Item
          name="lastName"
          label="Last name"
          extra="For example: Potter"
          rules={[{
            required: true,
            message: 'Please provide your last name'
          }]}
          style={{
            gridRow: 1,
            gridColumn: 2,
            marginLeft: '1em'
          }}>
          <Input/>
        </Form.Item>
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
          }]}
          style={{
            gridRow: 2,
            gridColumnStart: 1,
            gridColumnEnd: 3
          }}>
          <Input/>
        </Form.Item>
        <Form.Item
          label="Password"
          name="password"
          extra="For example: Pa$$w0rd"
          rules={[{
            required: true,
            message: 'Please provide your password'
          }]}
          style={{
            gridRow: 3,
            gridColumn: 1,
            marginRight: '1em'
          }}>
          <Input.Password />
        </Form.Item>
        <Form.Item
          label="Repeat password"
          name="repeatPassword"
          extra="For example: Pa$$w0rd"
          rules={[{
            required: true,
            message: 'Please provide your password'
          },
          samePasswordRule]}
          style={{
            gridRow: 3,
            gridColumn: 2,
            marginLeft: '1em'
          }}>
          <Input.Password />
        </Form.Item>
      </div>
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
          Sign up
        </Button>
        <Link
          to="/login"
          style={{
            marginTop: '1em',
            alignSelf: 'center'
          }}>
          I already have an account. Log me in.
        </Link>
      </div>
    </Form>
  );
};