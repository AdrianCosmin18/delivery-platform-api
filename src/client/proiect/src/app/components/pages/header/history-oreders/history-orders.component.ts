import { Component, OnInit } from '@angular/core';
import {Observable, Subscription} from "rxjs";
import {CustomerService} from "../../../../services/customer.service";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../store/app.reducer";
import {Order} from "../../../../interfaces/order";

@Component({
  selector: 'app-history-oreders',
  templateUrl: './history-orders.component.html',
  styleUrls: ['./history-orders.component.css']
})
export class HistoryOrdersComponent implements OnInit {
  private auth$!: Observable<{ email: string, loggedIn: boolean }>;
  private storeSub: Subscription = new Subscription();
  public email: string = '';
  public orders: Order[] = [];

  constructor(
    private userService: CustomerService,
    private store: Store<fromApp.AppState>,
  ) { }

  ngOnInit(): void {
    this.getUserInfo();
    this.getOrders();
  }

  getUserInfo(){
    this.auth$ = this.store.select("auth");
    this.storeSub = this.auth$.subscribe(value => {
      this.email = value.email;
    })
  }

  getOrders(){
    this.userService.getHistoryOrders(this.email).subscribe({
      next: value => {
        this.orders = value;
        console.log(this.orders);
      }
    })
  }


}
