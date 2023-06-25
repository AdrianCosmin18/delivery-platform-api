import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Order} from "../interfaces/order";
import {Courier} from "../interfaces/courier";

@Injectable({
  providedIn: 'root'
})
export class CourierService {
  private path = environment.apiUrl + "courier";

  constructor(private http: HttpClient) { }


  getCouriers(): Observable<Courier[]>{
    let url = `${this.path}`;
    return this.http.get<Courier[]>(url)
      .pipe(catchError(this.handleError));
  }

  getCourierById(id: number): Observable<Courier>{
    let url = `${this.path}/${id}`;
    return this.http.get<Courier>(url)
      .pipe(catchError(this.handleError));
  }


  handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      console.error('An error occurred:', error.error.message);
    } else {
      // console.error(
      //   `Backend returned code ${error.status}, ` +
      //   `body was: ${error.error}`);
      return throwError(error.error.message);
    }
    return throwError('Something bad happened; please try again later.');
  };
}

