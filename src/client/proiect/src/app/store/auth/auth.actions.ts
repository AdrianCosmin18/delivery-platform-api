import {Action} from "@ngrx/store";

export const LOGIN_START = '[Auth] Login Start';
export const REGISTER_START = '[Auth] Register Start';
export const AUTHENTICATE_SUCCESS = '[Auth] Login SUCCESS';
export const AUTHENTICATE_FAIL = '[Auth] Login Fail';
export const LOGOUT = '[Auth] Logout';
export const REGISTER = '[Auth] Register';
export const LOAD_DATA = 'Load data';

export class LoginStart implements Action{
  readonly type = LOGIN_START;

  constructor(public payload: { email: string; password: string }) {}
}

export class RegisterStart implements Action{
  readonly type = REGISTER_START;

  constructor(
    public payload: {
      email: string;
      password: string;
      firstName: string;
      lastName: string;
      phone: string;
    }) {}

}

export class AuthenticationSuccess implements Action{
  readonly type = AUTHENTICATE_SUCCESS;

  constructor(
    public payload: {
      email: string,
      firstName: string,
      token: string,
      role: string
    }
  ) {
  }
}

export class AuthenticateFail implements Action {
  readonly type = AUTHENTICATE_FAIL;

  constructor(public payload: string) {}
}

export class Logout implements Action {
  readonly type = LOGOUT;
}

export class Register implements Action{
  readonly type = REGISTER;

  constructor(
    public payload: {
      email: string,
      firstName: string,
      token: string,
      role: string
    }
  ) {
  }
}

export class LoadData implements Action{
  readonly type = LOAD_DATA;

  constructor(
    public payload:{
      email: string;
      firstName: string;
      loggedIn: boolean;
      role: string;
      token: string;
    }
  ) {
  }
}


export type AuthActions = LoginStart | AuthenticationSuccess | AuthenticateFail | Logout | Register | LoadData | RegisterStart;

