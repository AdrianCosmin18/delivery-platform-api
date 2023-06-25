import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {BehaviorSubject, catchError, Observable, throwError} from "rxjs";
import {City} from "../interfaces/city";
import {normalizeExtraEntryPoints} from "@angular-devkit/build-angular/src/webpack/utils/helpers";

@Injectable({
  providedIn: 'root'
})
export class CityService {
  private path = environment.apiUrl + "city";
  public subjectCities = new BehaviorSubject<City[]>([]);


  constructor(private http: HttpClient) {
    this.getCities().subscribe(value => {
      this.subjectCities.next(value);
    })
  }

  getCities(): Observable<City[]>{
    return this.http.get<City[]>(this.path)
      .pipe(catchError(this.handleError));
  }



  handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      console.error('An error occurred:', error.error.message);
    } else {
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    return throwError('Something bad happened; please try again later.');
  };
}
