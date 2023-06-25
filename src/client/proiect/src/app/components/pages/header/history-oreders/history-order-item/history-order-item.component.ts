import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Order} from "../../../../../interfaces/order";
import {Router} from "@angular/router";
import {OrderStatus} from "../../../../../constants/constants";
import {DialogService} from "primeng/dynamicdialog";
import {
  HandleOrderItemComponent
} from "../../../../admin-page/handle-orders/handle-order-item/handle-order-item.component";
import {NotificationService} from "../../../../../services/notification.service";


@Component({
  selector: '.history-order-item',
  templateUrl: './history-order-item.component.html',
  styleUrls: ['./history-order-item.component.css']
})
export class HistoryOrderItemComponent implements OnInit {
  @Input() order!: Order;
  @Input() stateOrderMessageAdmin!: string;
  @Input() currentStateOrderAdmin!: string;
  @Output() emitUpdateOrderStatusAdmin = new EventEmitter<boolean>();
  public status = '';
  public borderColor = '';

  constructor(private router: Router, public dialogService: DialogService, private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.initStatus();
  }

  initStatus(){

    if(this.order.status === OrderStatus.PLACED_ORDER){
      this.status = "Comandă plasată";
    }else if(this.order.status === OrderStatus.PAYMENT_CONFIRMED){
      this.status = "Plată confirmată";
    }else if(this.order.status === OrderStatus.ORDER_IN_PREPARATION){
      this.status = "Comandă în preparare";
    }else if(this.order.status === OrderStatus.ORDER_IN_DELIVERY){
      this.status = "Comandă în curs de livrare";
    }else if(this.order.status === OrderStatus.ORDER_DELIVERED){
      this.status = "Comandă livrată";

    }else if(this.order.status === OrderStatus.CANCELED_ORDER){
      this.status = "Comandă anulată";
      this.borderColor = "red";
    }

    //vreau sa fie verde pe comenzile inca nelivrate doar pentru useri
    if(this.hasNotRoute("/adminPage/handleOrders") && this.status !== 'Comandă livrată' && this.status !== 'Comandă anulată'){
      this.borderColor = 'green';
    }
  }

  checkOrder() {

    if(this.hasNotRoute('/historyOrders')){

      const ref = this.dialogService.open(HandleOrderItemComponent, {
        header: `Comanda #${this.order.id}`,
        width: '40%',
        data: {
          orderId: this.order.id,
          message: this.stateOrderMessageAdmin,
          currentStateOrder: this.currentStateOrderAdmin
        }
      });

      ref.onClose.subscribe(msg => {
        if(msg){
          this.notificationService.onSuccess('modifyStatusOrder', msg);
          this.emitUpdateOrderStatusAdmin.emit(true);
        }
      });

    }else{
      this.router.navigate([`historyOrders/${this.order.id}`]);
    }
  }

  hasNotRoute(route: string){
    return this.router.url !== route;
  }
}
