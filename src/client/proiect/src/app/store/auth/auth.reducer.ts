import * as Actions from './auth.actions';
import {AuthActions} from "./auth.actions";

export interface State{
  email: string,
  firstName: string,
  token: string,
  role: string,
  authError: string;
  loggedIn:boolean;
}

const initialState: State = {
  email: '',
  firstName: '',
  token: '',
  role: '',
  authError: '',
  loggedIn: false
};

export function authReducer(
  state = initialState,
  action: Actions.AuthActions
){

  switch (action.type){
    case Actions.AUTHENTICATE_SUCCESS:

      return {
        ...state,
        email: action.payload.email,
        firstName: action.payload.firstName,
        token: action.payload.token,
        role: action.payload.role,
        authError: '',
        loggedIn: true
      }

    case Actions.REGISTER:

      return{
        ...state,
        email: action.payload.email,
        firstName: action.payload.firstName,
        token: action.payload.token,
        role: action.payload.role,
        authError: '',
        loggedIn: true
      }

    case Actions.AUTHENTICATE_FAIL:
      return {
        ...state,
        email: '',
        firstName: '',
        token: '',
        role: '',
        authError: "",
        loggedIn: false
      }

    case Actions.LOGOUT:

      return {
        ...state,
        email: '',
        firstName: '',
        token: '',
        role: '',
        authError:"",
        loggedIn: false
      }

    case Actions.LOAD_DATA:
      return{
        ...state,
        email: action.payload.email,
        firstName: action.payload.firstName,
        token: action.payload.token,
        role: action.payload.role,
        authError: '',
        loggedIn: true
      }

    default:
      return state;
  }
}

