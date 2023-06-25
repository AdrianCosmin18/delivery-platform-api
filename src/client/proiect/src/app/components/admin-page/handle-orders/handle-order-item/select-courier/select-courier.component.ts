import { Component, OnInit } from '@angular/core';
import {CourierService} from "../../../../../services/courier.service";
import {Courier} from "../../../../../interfaces/courier";
import {DynamicDialogRef} from "primeng/dynamicdialog";

@Component({
  selector: 'app-select-courier',
  templateUrl: './select-courier.component.html',
  styleUrls: ['./select-courier.component.css']
})
export class SelectCourierComponent implements OnInit {
  public couriers: Courier[] = [];

  constructor(
    private courierService: CourierService,
    public ref: DynamicDialogRef,
  ) { }

  ngOnInit(): void {
    this.getCouriers();
  }

  getCouriers(){
    this.courierService.getCouriers().subscribe({
      next: value => {
        this.couriers = value;
        console.log(value);
      }
    })
  }

  addOrderToCourier(courierId: number) {
    this.ref.close(courierId);
  }
}
