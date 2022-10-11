import {User} from "./user";

export interface AuthenticationState {
  user?: User;
  initialized: boolean;
}