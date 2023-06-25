import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: '.status-order',
  templateUrl: './status-order.component.html',
  styleUrls: ['./status-order.component.css']
})
export class StatusOrderComponent implements OnInit {
  @Input() icon: string = '';
  @Input() colorIcon: string = '';
  @Input() message: string = '';
  @Input() date: string = '';
  @Input() active: boolean = true;

  public colorText = '';

  constructor() { }

  ngOnInit(): void {
    if(!this.active){
      this.colorText = "red";
    }
  }

}
