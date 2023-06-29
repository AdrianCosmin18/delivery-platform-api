import * as AuthActions from "./auth.actions";
import {Injectable} from "@angular/core";
import {AuthService} from "../../services/auth.service";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, map, of, switchMap, tap} from "rxjs";
import {User} from "../../interfaces/user";
import * as AuthAction from "./auth.actions";
import {AuthorityModel} from "../../models/authority-model";
import {ErrorMessages, Roles} from "../../constants/constants";
import {NotificationService} from "../../services/notification.service";
import {LoadingScreenService} from "../../services/loading-screen.service";


@Injectable(
)
export class AuthEffects{

  constructor(
    private action$: Actions,
    private authService: AuthService,
    private notificationService : NotificationService,
    private loadingScreenService: LoadingScreenService
  ) {
  }



  authLogin$ = createEffect(() => {
    return  this.action$.pipe(
      ofType(AuthActions.LOGIN_START),
      switchMap((authData: AuthActions.LoginStart) => {
        this.loadingScreenService.setLoading(true);
        let user: User = {
          email: authData.payload.email,
          password: authData.payload.password,
          phone: '',
          firstName: '',
          lastName: ''
        };
        return this.authService.login(user)
          .pipe(
            map(response => {
              let arrAuth: Array<AuthorityModel> = response.body?.authorities as Array<AuthorityModel>;
              let role = '';
              if(arrAuth?.some(elem => elem.authority === Roles.ROLE_USER)){
                role = Roles.ROLE_USER;
              }else if(arrAuth?.some(elem => elem.authority === Roles.ROLE_ADMIN)){
                role = Roles.ROLE_ADMIN;
              }

              this.loadingScreenService.setLoading(false);
              this.notificationService.onSuccess('loginSuccess','Te-ai logat cu success');
              return new AuthAction.AuthenticationSuccess({
                email: response.body!.email,
                firstName: response.body!.firstName,
                token: response.body!.token,
                role: role,
              })
            }),
            catchError(err => {
              this.loadingScreenService.setLoading(false);
              this.notificationService.onError('loginFailed','Mail sau parolă incorectă');
              return handleError(err);
            })
          )
      })
    );
  });

  authRegister$ = createEffect(() => {
    return this.action$.pipe(
      ofType(AuthActions.REGISTER_START),
      switchMap((authData: AuthActions.RegisterStart) => {
        this.loadingScreenService.setLoading(true);
        let user: User = {
          email: authData.payload.email,
          password: authData.payload.password,
          phone: authData.payload.phone,
          firstName: authData.payload.firstName,
          lastName: authData.payload.lastName
        };
        return this.authService.register(user)
          .pipe(
            map(value => {
              let arrAuth: Array<AuthorityModel> = value.body?.authorities as Array<AuthorityModel>;
              let role = '';
              if(arrAuth?.some(elem => elem.authority === Roles.ROLE_USER)){
                role = Roles.ROLE_USER;
              }else if(arrAuth?.some(elem => elem.authority === Roles.ROLE_ADMIN)){
                role = Roles.ROLE_ADMIN;
              }

              this.loadingScreenService.setLoading(false);
              this.notificationService.onSuccess('loginSuccess','Ți-ai creat cont cu success');
              return new AuthAction.AuthenticationSuccess({
                email: value.body!.email,
                firstName: value.body!.firstName,
                token: value.body!.token,
                role: role,
              })
            }),
            catchError(err => {
              this.loadingScreenService.setLoading(false);
              if(err === ErrorMessages.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION) {
                this.notificationService.onError('registerFailed', 'Există deja un cont cu acest mail');
              } else if(err === ErrorMessages.USER_ALREADY_EXISTS_PHONE_EXCEPTION){
                this.notificationService.onError('registerFailed', 'Există deja un cont cu acest număr de telefon');
              }
                return handleError(err);
            })
          )
      })
    )
  })
}

const handleError = (errorRes: any) => {
  console.log(errorRes);
  let errorMessage = 'Mail sau parolă incorectă';
  // this.notificationService.onError('loginFailed','Email sau parola incorecta', '');
  // if (!errorRes.error || !errorRes.error.error) {
  //   return of(new AuthAction.AuthenticateFail(errorMessage));
  // }
  // switch (errorRes.error.error.message) {
  //   case 'EMAIL_EXISTS':
  //     errorMessage = 'This email exists already';
  //     break;
  //   case 'EMAIL_NOT_FOUND':
  //     errorMessage = 'This email does not exist.';
  //     break;
  //   case 'INVALID_PASSWORD':
  //     errorMessage = 'This password is not correct.';
  //     break;
  // }
  return of(new AuthAction.AuthenticateFail(errorMessage));
};
