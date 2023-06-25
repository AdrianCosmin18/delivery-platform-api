import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {BehaviorSubject, catchError, map, Observable, throwError} from "rxjs";
import {Product} from "../interfaces/burger";
import {environment} from "../../environments/environment";
import {FoodType} from "../constants/constants";

@Injectable({
  providedIn: 'root'
})
export class BurgerService {
  private burgersUrl: string = environment.apiUrl + `restaurant/get-restaurant-products/BurgerShop?type=${FoodType.BURGER}`;
  private friesUrl: string = environment.apiUrl + `restaurant/get-restaurant-products/BurgerShop?type=${FoodType.FRIES}`;
  private extrasBurgerUrl: string = environment.apiUrl + `restaurant/get-restaurant-products/BurgerShop?type=${FoodType.EXTRAS_BURGER}`;
  private extrasFriesUrl: string = environment.apiUrl + `restaurant/get-restaurant-products/BurgerShop?type=${FoodType.EXTRAS_FRIES}`;
  private extrasSaucesUrl: string = environment.apiUrl + `restaurant/get-restaurant-products/BurgerShop?type=${FoodType.EXTRAS_SAUCE}`;
  private saucesUrl: string = environment.apiUrl + `restaurant/get-restaurant-products/BurgerShop?type=${FoodType.SAUCES}`;
  private drinksUrl: string = environment.apiUrl +  `restaurant/get-restaurant-products/BurgerShop?type=${FoodType.DRINK}`;
  private extrasDrinkUrl: string = environment.apiUrl + `restaurant/get-restaurant-products/BurgerShop?type=${FoodType.EXTRAS_DRINK}`

  public subjectExtraIngredientsDrinks = new BehaviorSubject<Product[]>([]);

  constructor(private http: HttpClient) {
    this.getExtrasDrinks().subscribe(data => {
      this.subjectExtraIngredientsDrinks.next(data);
    })
  }

  getBurgers(): Observable<Product[]>{
    console.log(this.burgersUrl);
    return this.http.get<Product[]>(this.burgersUrl);
  }

  getFries(): Observable<Product[]>{
    console.log(this.friesUrl);
    return this.http.get<Product[]>(this.friesUrl);
  }

  getExtrasBurgers(): Observable<Product[]>{
    return this.http.get<Product[]>(this.extrasBurgerUrl);
  }

  getExtrasFries(): Observable<Product[]>{
    return this.http.get<Product[]>(this.extrasFriesUrl);
  }

  getExtrasSauces(): Observable<Product[]>{
    return this.http.get<Product[]>(this.extrasSaucesUrl);
  }

  getDrinks(): Observable<Product[]>{
    return this.http.get<Product[]>(this.drinksUrl);
  }

  getExtrasDrinks(): Observable<Product[]>{
    return this.http.get<Product[]>(this.extrasDrinkUrl);
  }

  getSauces(): Observable<Product[]>{
    return this.http.get<Product[]>(this.saucesUrl);
  }

  getProductByName(name: string): Observable<Product>{
    const url = environment.apiUrl + `restaurant/get-product-by-restaurant-and-product-Name/BurgerShop?productName=${name}`;
    return this.http.get<Product>(url);
  }

  getProductsByIngredients(foodType: string, ingredientList: string): Observable<Product[]>{
    const url = `${environment.apiUrl}restaurant/get-products-by-ingredients?foodType=${foodType}&ingredientList=${ingredientList}` ;
    return this.http.post<Product[]>(url, null)
      .pipe(catchError(this.handleError));

  }

  getProductImageById(imageId: number){
    return `${environment.apiUrl}restaurant/get-image-product/${imageId}`;
  }


  handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      console.error('An error occurred:', error.error.message);
    } else {
      return throwError(error.error.message);
    }
    return throwError('Something bad happened; please try again later.');
  };

}
