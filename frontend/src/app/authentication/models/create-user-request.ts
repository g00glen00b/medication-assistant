export interface CreateUserRequest {
  email: string;
  name?: string;
  password: string;
  timezone: string;
}