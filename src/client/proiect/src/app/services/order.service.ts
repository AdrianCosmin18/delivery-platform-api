import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Order} from "../interfaces/order";
import {OrderItem} from "../models/order-item";
import {Product} from "../interfaces/burger";

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private path = environment.apiUrl + "order";


  constructor(private http: HttpClient) { }

  getOrderById(id: number): Observable<Order>{
    let url = `${this.path}/${id}`;
    return this.http.get<Order>(url)
      .pipe(catchError(this.handleError));
  }

  getOrderItemsByOrderId(orderId: number): Observable<OrderItem[]>{
    let url = `${this.path}/get-orderItems-by-orderId/${orderId}`;
    return this.http.get<OrderItem[]>(url)
      .pipe(catchError(this.handleError));
  }

//admin functions

  getOrdersInPlacedOrderState(): Observable<Order[]>{
    let url = `${this.path}/get-orders-in-placed-order-state`;
    return this.http.get<Order[]>(url)
      .pipe(catchError(this.handleError));
  }

  getOrdersInPlacedOrderStateInACity(cityName: string): Observable<Order[]>{
    let url = `${this.path}/get-orders-in-placed-order-state/${cityName}`;
    return this.http.get<Order[]>(url)
      .pipe(catchError(this.handleError));
  }

  putOrderInPaymentConfirmationState(orderId: number): Observable<void>{
    let url = `${this.path}/put-order-in-payment-confirmation-state/${orderId}`;
    return this.http.put<void>(url, null)
      .pipe(catchError(this.handleError));
  }

  getOrdersInPaymentConfirmedState(): Observable<Order[]>{
    let url = `${this.path}/get-orders-in-payment-confirmation-state`;
    return this.http.get<Order[]>(url)
      .pipe(catchError(this.handleError));
  }

  getOrdersInPaymentConfirmedStateInACity(cityName: string): Observable<Order[]>{
    let url = `${this.path}/get-orders-in-payment-confirmation-state/${cityName}`;
    return this.http.get<Order[]>(url)
      .pipe(catchError(this.handleError));
  }

  putOrderInPreparationState(orderId: number): Observable<void>{
    let url = `${this.path}/put-order-in-preparation-state/${orderId}`;
    return this.http.put<void>(url, null)
      .pipe(catchError(this.handleError));
  }

  getOrdersInPreparationState(): Observable<Order[]>{
    let url = `${this.path}/get-orders-in-preparation-state`;
    return this.http.get<Order[]>(url)
      .pipe(catchError(this.handleError));
  }

  getOrdersInPreparationStateInACity(cityName: string): Observable<Order[]>{
    let url = `${this.path}/get-orders-in-preparation-state/${cityName}`;
    return this.http.get<Order[]>(url)
      .pipe(catchError(this.handleError));
  }

  putOrderInDeliveryState(orderId: number, courierId: number): Observable<void>{
    let url = `${this.path}/put-order-in-delivery-state/${orderId}/${courierId}`;
    return this.http.put<void>(url, null)
      .pipe(catchError(this.handleError));
  }

  getOrdersInDeliveryState(): Observable<Order[]>{
    let url = `${this.path}/get-orders-in-delivery-state`;
    return this.http.get<Order[]>(url)
      .pipe(catchError(this.handleError));
  }

  getOrdersInDeliveryStateInACity(cityName: string): Observable<Order[]>{
    let url = `${this.path}/get-orders-in-delivery-state/${cityName}`;
    return this.http.get<Order[]>(url)
      .pipe(catchError(this.handleError));
  }

  getFinalizedOrders(): Observable<Order[]>{
    let url = `${this.path}/get-finalized-orders`;
    return this.http.get<Order[]>(url)
      .pipe(catchError(this.handleError));
  }

  getCanceledOrders(): Observable<Order[]>{
    let url = `${this.path}/get-canceled-orders`;
    return this.http.get<Order[]>(url)
      .pipe(catchError(this.handleError));
  }

  getFinalizedOrdersInACity(cityName: string): Observable<Order[]>{
    let url = `${this.path}/get-finalized-orders/${cityName}`;
    return this.http.get<Order[]>(url)
      .pipe(catchError(this.handleError));
  }

  getCanceledOrdersInACity(cityName: string): Observable<Order[]>{
    let url = `${this.path}/get-canceled-orders/${cityName}`;
    return this.http.get<Order[]>(url)
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



