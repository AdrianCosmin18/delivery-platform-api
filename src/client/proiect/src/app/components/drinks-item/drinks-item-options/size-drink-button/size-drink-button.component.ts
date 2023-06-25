import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Product} from "../../../../interfaces/burger";

@Component({
  selector: 'app-size-drink-button',
  templateUrl: './size-drink-button.component.html',
  styleUrls: ['./size-drink-button.component.css']
})
export class SizeDrinkButtonComponent implements OnInit {
  @Input() type!: string;
  @Input() price!: number;
  @Output() emitPrice = new EventEmitter<number>();

  constructor() { }

  ngOnInit(): void {
  }

  returnPriceDrinkFromListOfDrinks(){
    this.emitPrice.emit(this.price);
  }

}
