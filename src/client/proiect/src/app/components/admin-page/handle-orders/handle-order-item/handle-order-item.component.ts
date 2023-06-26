import { Component, OnInit } from '@angular/core';
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {OrderService} from "../../../../services/order.service";
import {OrderItem} from "../../../../models/order-item";
import {Constant, Constants, OrderStatus, Roles} from "../../../../constants/constants";
import {MessageService} from "primeng/api";
import {NotificationService} from "../../../../services/notification.service";
import {SelectCourierComponent} from "./select-courier/select-courier.component";
import {Observable, Subscription} from "rxjs";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../store/app.reducer";
import {LoadingScreenService} from "../../../../services/loading-screen.service";

@Component({
  selector: 'app-handle-order-item',
  templateUrl: './handle-order-item.component.html',
  styleUrls: ['./handle-order-item.component.css']
})
export class HandleOrderItemComponent implements OnInit {
  private orderId!: number;
  public message: string = "";
  public currentStateOrder: string = "";
  public orderItems: OrderItem[] = [];

  public role: string = '';
  private auth$!: Observable<{role: string}>;
  private storeSub: Subscription = new Subscription();


  constructor(
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
    public dialogService: DialogService,
    private orderService: OrderService,
    private store: Store<fromApp.AppState>,
    private loadingScreenService: LoadingScreenService
  ) { }

  ngOnInit(): void {

    this.getRoleUser();

    this.orderId = this.config.data.orderId;
    this.message = this.config.data.message;
    this.currentStateOrder = this.config.data.currentStateOrder;
    this.getOrderItemsOfOrder();
  }

  getRoleUser(){
    this.auth$ = this.store.select("auth");
    this.storeSub = this.auth$.subscribe(value => {
      this.role = value.role;
    });
  }

  getOrderItemsOfOrder(){
    this.orderService.getOrderItemsByOrderId(this.orderId).subscribe({
      next: value => {
        this.orderItems = value;
        this.getTotalQuantity();
        console.log(this.orderItems);
      },
      error: err => {
        alert(err.error.message);
      }
    })
  }

  getTotalQuantity(): number{
    let nr = 0;
    this.orderItems.forEach(oi => {
      nr += oi.quantity;
    });
    return nr;
  }

  changeState() {
    if(this.currentStateOrder === OrderStatus.COMENZI_PLASATE){
      this.changeStateToConfirmedPayment();
    }else if(this.currentStateOrder === OrderStatus.PLATA_CONFIRMATA){
      this.changeStateToPreparation();
    }else if(this.currentStateOrder === OrderStatus.COMENZI_IN_PREPARARE){
      this.selectCourier();
    }
  }

  changeStateToConfirmedPayment(){
    this.loadingScreenService.setLoading(true);
    this.orderService.putOrderInPaymentConfirmationState(this.orderId).subscribe({
      next: () => {
        this.loadingScreenService.setLoading(false);
        this.close(`Starea comenzii #${this.orderId} modificata cu success`);
      }
    })
  }

  changeStateToPreparation(){
    this.loadingScreenService.setLoading(true);
    this.orderService.putOrderInPreparationState(this.orderId).subscribe({
      next: () => {
        this.loadingScreenService.setLoading(false);
        this.close(`Starea comenzii #${this.orderId} modificata cu success`);
      }
    })
  }

  selectCourier(){
    const ref = this.dialogService.open(SelectCourierComponent, {
      header: `Curieri`,
      width: '40%',
    });

    ref.onClose.subscribe(courierId => {
      if(courierId){
        this.loadingScreenService.setLoading(true);
        this.orderService.putOrderInDeliveryState(this.orderId, courierId).subscribe({
          next: () => {
            this.loadingScreenService.setLoading(false);
            this.close(`Comanda #${this.orderId} a fost asignată curierului`);
          }
        });

      }
    })
  }

  close(msg: string){
    this.ref.close(msg);
  }
}
