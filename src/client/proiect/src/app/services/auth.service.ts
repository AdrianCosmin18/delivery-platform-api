import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {AuthenticationResponse} from "../models/authentication-response";
import {catchError, Observable, throwError} from "rxjs";
import {User} from "../interfaces/user";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private url = environment.apiUrl + 'user';
  private token: string | null | undefined;
  private email: string | null | undefined;
  private firstName: string | null | undefined;
  private role: string | null | undefined;
  private loggedIn: boolean | null | undefined;

  private auth!: AuthenticationResponse;

  constructor(private http: HttpClient) { }

  register(user: User): Observable<HttpResponse<AuthenticationResponse>>{
    const path = `${this.url}/register`;

    return this.http.post<AuthenticationResponse>(path, user, {observe: 'response'})
      .pipe(catchError(this.handleError));
  }

  login(user: User): Observable<HttpResponse<AuthenticationResponse>>{
    const path = `${this.url}/login`;
    return this.http.post<AuthenticationResponse>(path, user, {observe: 'response'})
      .pipe(catchError(this.handleError));
  }

  saveEmail(email: string){
    localStorage.setItem("email", email);
  }

  getEmail(){
    this.email = localStorage.getItem("email");
    return this.email;
  }

  saveRole(role: string): void{
    localStorage.setItem("role", role);
  }

  getRole() {
    this.role = localStorage.getItem("role");
    return this.role;
  }

  saveToken(token: string){
    localStorage.setItem("jwtToken", token);
  }

  loadToken(): void{
    this.token = localStorage.getItem("jwtToken");
  }

  getToken(){
    this.token = localStorage.getItem("jwtToken");
    return this.token;
  }

  saveFirstName(firstName: string){
    localStorage.setItem("firstName", firstName);
  }

  getFirstName(){
    this.firstName = localStorage.getItem("firstName");
    return this.firstName;
  }

  saveLoggedIn(loggedIn: boolean){
    localStorage.setItem("loggedIn", String(loggedIn));
  }

  getLoggedIn(){
    if (localStorage.getItem("loggedIn") === "true"){
      this.loggedIn = true;
    }
    else {
      this.loggedIn = false;
    }
    return this.loggedIn
  }

  logOut(): void{
    localStorage.removeItem("jwtToken");
    localStorage.removeItem("email");
    localStorage.removeItem("firstName");
    localStorage.removeItem("role");
  }


  private handleError(error: HttpErrorResponse): Observable<never>{
    console.log(error);
    let errorMessage:string;

    errorMessage = error.error.message;
    return throwError(errorMessage);
  }
}
