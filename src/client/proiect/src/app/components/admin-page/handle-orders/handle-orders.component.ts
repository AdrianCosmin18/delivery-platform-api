import { Component, OnInit } from '@angular/core';
import {OrderStatus} from "../../../constants/constants";
import {OrderService} from "../../../services/order.service";
import {Order} from "../../../interfaces/order";
import {CityService} from "../../../services/city.service";
import {City} from "../../../interfaces/city";

@Component({
  selector: 'app-handle-orders',
  templateUrl: './handle-orders.component.html',
  styleUrls: ['./handle-orders.component.css']
})
export class HandleOrdersComponent implements OnInit {
  public placedOrder: string = OrderStatus.COMENZI_PLASATE;
  public cancelOrder: string = OrderStatus.COMENZI_ANULATE;
  public paymentConfirmed: string = OrderStatus.PLATA_CONFIRMATA;
  public orderInPreparation: string = OrderStatus.COMENZI_IN_PREPARARE;
  public orderInDelivery: string = OrderStatus.COMENZI_IN_CURS_DE_LIVRARE;
  public orderDelivered: string = OrderStatus.COMENZI_LIVRATE;

  public orderInPlacedOrderState: Order[] = [];
  public ordersInPaymentConfirmedState: Order[] = [];
  public ordersInPreparationState: Order[] = [];
  public ordersInDeliveryState: Order[] = [];
  public finalizedOrders: Order[] = [];
  public canceledOrders: Order[] = [];


  public cities: City[] = [];
  public citySelected!: City;

  public date!: Date;



  constructor(
    private orderService: OrderService,
    private cityService: CityService,
  ) { }

  ngOnInit(): void {

    this.getOrdersInPlacedOrderState();
    this.getOrdersInPaymentConfState();
    this.getOrdersInPreparationState();
    this.getOrdersInDeliveryState();
    this.getFinalizedOrders();
    this.getCanceledOrders();


    this.getCities();
  }


  getOrdersInPlacedOrderState(){
    this.orderService.getOrdersInPlacedOrderState().subscribe({
      next: value => {
        this.orderInPlacedOrderState = value;
        console.log(value);
      },
      error: err => {
        alert('smth went wrong to get order: ' + err.error.message)
      }
    })
  }

  getOrdersInPaymentConfState(){
    this.orderService.getOrdersInPaymentConfirmedState().subscribe({
      next: value => {
        this.ordersInPaymentConfirmedState = value;
        console.log(value);
      },
      error: err => {
        alert('smth went wrong to get order: ' + err.error.message)
      }
    })
  }

  getOrdersInPreparationState(){
    this.orderService.getOrdersInPreparationState().subscribe({
      next: value => {
        this.ordersInPreparationState = value;
        console.log(value);
      },
      error: err => {
        alert('smth went wrong to get order: ' + err.error.message)
      }
    })
  }

  getOrdersInDeliveryState(){
    this.orderService.getOrdersInDeliveryState().subscribe({
      next: value => {
        this.ordersInDeliveryState = value;
        console.log(value);
      },
      error: err => {
        alert('smth went wrong to get order: ' + err.error.message)
      }
    })
  }

  getFinalizedOrders(){
    this.orderService.getFinalizedOrders().subscribe({
      next: value => {
        this.finalizedOrders = value;
        console.log(value);
      },
      error: err => {
        alert('smth went wrong to get order: ' + err.error.message)
      }
    })
  }

  getCanceledOrders(){
    this.orderService.getCanceledOrders().subscribe({
      next: value => {
        this.canceledOrders = value;
        console.log(value);
      },
      error: err => {
        alert('smth went wrong to get order: ' + err.error.message)
      }
    })
  }

  getCities(){
    this.cityService.getCities().subscribe({
      next:value => {
        this.cities = value;
        console.log(this.cities);
      },
      error:err => {
        alert("Something went wrong");
      }
    });
  }

  reloadOrderStates() {
    this.ngOnInit();
  }

  filterOrders() {

    if(this.citySelected){

      this.orderService.getOrdersInPlacedOrderStateInACity(this.citySelected.name).subscribe({
        next: value => {
          this.orderInPlacedOrderState = value;
          console.log(value);
        },
        error: err => {
          alert('smth went wrong to get order: ' + err.error.message)
        }
      });

      this.orderService.getOrdersInPaymentConfirmedStateInACity(this.citySelected.name).subscribe({
        next: value => {
          this.ordersInPaymentConfirmedState = value;
          console.log(value);
        },
        error: err => {
          alert('smth went wrong to get order: ' + err.error.message)
        }
      });

      this.orderService.getOrdersInPreparationStateInACity(this.citySelected.name).subscribe({
        next: value => {
          this.ordersInPreparationState = value;
          console.log(value);
        },
        error: err => {
          alert('smth went wrong to get order: ' + err.error.message)
        }
      });

      this.orderService.getOrdersInDeliveryStateInACity(this.citySelected.name).subscribe({
        next: value => {
          this.ordersInDeliveryState = value;
          console.log(value);
        },
        error: err => {
          alert('smth went wrong to get order: ' + err.error.message)
        }
      });

      this.orderService.getFinalizedOrdersInACity(this.citySelected.name).subscribe({
        next: value => {
          this.finalizedOrders = value;
          console.log(value);
        },
        error: err => {
          alert('smth went wrong to get order: ' + err.error.message)
        }
      });

      this.orderService.getCanceledOrdersInACity(this.citySelected.name).subscribe({
        next: value => {
          this.canceledOrders = value;
          console.log(value);
        },
        error: err => {
          alert('smth went wrong to get order: ' + err.error.message)
        }
      })

    }else{
      this.ngOnInit();
    }
  }


}
