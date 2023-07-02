import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {OrderService} from "../../../../../../services/order.service";
import {Order} from "../../../../../../interfaces/order";
import {OrderItem} from "../../../../../../models/order-item";
import {CustomerService} from "../../../../../../services/customer.service";
import {Observable, Subscription} from "rxjs";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../../../store/app.reducer";
import {ConfirmationService, MessageService} from "primeng/api";
import {OrderStatus} from "../../../../../../constants/constants";
import {CourierService} from "../../../../../../services/courier.service";
import {Courier} from "../../../../../../interfaces/courier";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {InfoCourierComponent} from "./info-courier/info-courier.component";
import {LoadingScreenService} from "../../../../../../services/loading-screen.service";

@Component({
  selector: 'app-history-order-item-details',
  templateUrl: './history-order-item-details.component.html',
  styleUrls: ['./history-order-item-details.component.css']
})
export class HistoryOrderItemDetailsComponent implements OnInit, OnDestroy {
  public orderId: number = -1;
  public order: Order = {
    id: -1,
    courierId: -1,
    amount: 0,
    status: '',
    deliverTime: '',
    placedOrderTime: '',
    paymentConfirmed: '',
    orderInPreparation: '',
    orderInDelivery: '',
    canceledOrder: '',
    productsAmount: 0,
    deliveryTax: 0,
    tipsTax: 0,
    addressToString: '',
    city: '',
    cardNumber: '',
    username: '',
    commentsSection: ''
  };
  public orderItems: OrderItem[] = [];

  private auth$!: Observable<{email: string}>;
  private email!: string;
  private storeSub: Subscription = new Subscription();
  public loading = false;

  public courier!: Courier;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private userService: CustomerService,
    private courierService: CourierService,
    private store: Store<fromApp.AppState>,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
    public dialogService: DialogService,
    private loadingScreenService: LoadingScreenService

  ) { }

  ngOnInit(): void {
    this.getIdFromRoute();

    this.auth$ = this.store.select("auth");
    this.storeSub = this.auth$.subscribe(value => {
      this.email = value.email;
    });
  }

  getIdFromRoute(){
    this.route.params.subscribe({
      next: url => {
        let id = url['id'];
        this.orderId = parseInt(id);
        this.getOrder();
      }
    })
  }

  getOrder(){
    this.orderService.getOrderById(this.orderId).subscribe({
      next: value => {
        this.order = value;
        this.getOrderItemsOfOrder();
        this.getCourierById();
        console.log(this.order);
      },
      error: err => {
        alert(err.error.message);
      }
    })
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

  confirmReceivedOrder() {
    this.loadingScreenService.setLoading(true);
    this.userService.confirmReceivedOrder(this.email, this.orderId).subscribe({
      next: value => {
        this.loadingScreenService.setLoading(false);
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          this.ngOnInit();
        }, 2000);
      }
    })
  }

  cancelOrder(){
    this.confirmationService.confirm({
      message: 'Sunteți sigur că doriți să anulați comanda ?',
      header: 'Confirmation',
      acceptLabel: 'Da',
      rejectLabel: 'Nu',
      icon: 'pi pi-exclamation-triangle',
      key:'cancelOrder',
      accept: () => {
        this.loadingScreenService.setLoading(true);
        this.userService.cancelOrder(this.email, this.orderId).subscribe({
          next: () => {
            this.loadingScreenService.setLoading(false);
            this.loading = true;
            setTimeout(() => {
              this.loading = false;
              this.ngOnInit();
              this.messageService.add({severity:'error', summary:'Comandă anulată'});
            }, 2000);
          }
        })

      }
    });


  }

  getCourierById(){
    if(this.order.courierId){
      this.courierService.getCourierById(this.order.courierId).subscribe({
        next: value => {
          this.courier = value;
          console.log(value);
        }
      })
    }
  }

  ngOnDestroy() {
    this.storeSub.unsubscribe();
  }

  detailsCourier() {

    const ref = this.dialogService.open(InfoCourierComponent, {
      width: '420px',
      header: 'Informații curier',
      data: {
        courier: this.courier
      }
    })
  }
}
