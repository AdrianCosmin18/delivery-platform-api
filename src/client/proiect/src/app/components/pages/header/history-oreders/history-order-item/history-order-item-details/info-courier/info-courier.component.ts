import { Component, OnInit } from '@angular/core';
import {Courier} from "../../../../../../../interfaces/courier";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";

@Component({
  selector: 'app-info-courier',
  templateUrl: './info-courier.component.html',
  styleUrls: ['./info-courier.component.css']
})
export class InfoCourierComponent implements OnInit {
  public courier!: Courier;

  constructor(public config: DynamicDialogConfig) { }

  ngOnInit(): void {
    this.courier = this.config.data.courier;
  }

}
