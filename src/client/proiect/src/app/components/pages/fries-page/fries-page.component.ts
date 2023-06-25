import { Component, OnInit } from '@angular/core';
import {BurgerService} from "../../../services/burger.service";
import {Product} from "../../../interfaces/burger";
import {CustomerService} from "../../../services/customer.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-fries-page',
  templateUrl: './fries-page.component.html',
  styleUrls: ['./fries-page.component.css'],
  providers: [MessageService]
})
export class FriesPageComponent implements OnInit {
  public friesList: Product[] = [];
  private customerId!: number;

  constructor(
    private burgerService: BurgerService,
  ) { }

  ngOnInit(): void {
    this.burgerService.getFries().subscribe({
      next: (response) => {
        this.friesList = response;
      }
    })
  }

}
