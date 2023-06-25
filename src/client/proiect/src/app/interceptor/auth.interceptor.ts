import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";
import {AuthService} from "../services/auth.service";
import {Store} from "@ngrx/store";
import * as fromApp from "../store/app.reducer";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private auth$!: Observable<{ token: string; }>;
  private token: string = '';

  constructor(
    private service: AuthService,
    private store: Store<fromApp.AppState>) {}

  // se plaseaza intre request si server si intercepteaza astfel
  intercept(request: HttpRequest<any>, handler: HttpHandler): Observable<HttpEvent<any>> {
    if(request.url.includes(`${environment.apiUrl}user/register`)){
      // nu punem niciun token pentru ca nu este un url la care e nevoie de autoritate
      return handler.handle(request);
    }

    if(request.url.includes(`${environment.apiUrl}user/login`)){
      // nu punem niciun token pentru ca nu este un url la care e nevoie de autoritate
      return handler.handle(request);
    }

    // altfel luam tokenul
    this.getCurrentToken();
    const httpRequest = request.clone({ // clonam requestul si punem token-ul
      setHeaders:{Authorization: `Bearer ${this.token}`}
    });
    return handler.handle(httpRequest);// il trimitem mai departe catre server
  }


  getCurrentToken(): void{
    this.auth$ = this.store.select("auth");
    this.auth$.subscribe(value => {
      this.token =  value.token;
    })
  }
}
