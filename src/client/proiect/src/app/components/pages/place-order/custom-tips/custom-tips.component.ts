import { Component, OnInit } from '@angular/core';
import {DynamicDialogRef} from "primeng/dynamicdialog";

@Component({
  selector: 'app-custom-tips',
  templateUrl: './custom-tips.component.html',
  styleUrls: ['./custom-tips.component.css']
})
export class CustomTipsComponent implements OnInit {
  public value: number = 1;

  constructor(public ref: DynamicDialogRef) { }

  ngOnInit(): void {
  }

  saveTip() {
    this.ref.close(this.value);
  }
}
