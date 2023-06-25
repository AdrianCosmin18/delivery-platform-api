import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OrderItemService {
  private path = environment.apiUrl + "orderItem";

  constructor(private http: HttpClient) { }

  getStatisticsProducts(): Observable<{ [key: string]: number }>{
    let url = `${this.path}/get-statistics`;
    return this.http.get<{ [key: string]: number }>(url)
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

