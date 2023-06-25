import {Component, Input, OnInit} from '@angular/core';
import {Product} from "../../../interfaces/burger";
import {BurgerService} from "../../../services/burger.service";
import {CustomerService} from "../../../services/customer.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-burger',
  templateUrl: './burger.component.html',
  styleUrls: ['./burger.component.css'],
  providers: [MessageService]
})
export class BurgerComponent implements OnInit {
  public burgers: Product[] = [];

  constructor(private burgerService: BurgerService) { }

  ngOnInit(): void {

    this.burgerService.getBurgers().subscribe({
      next: (response) => {
        this.burgers = response;
        console.log(this.burgers);
      }
    });
  }

}
