import { Component, OnInit } from '@angular/core';
import {Product} from "../../../interfaces/burger";
import {BurgerService} from "../../../services/burger.service";
import {CustomerService} from "../../../services/customer.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-sauce-page',
  templateUrl: './sauce-page.component.html',
  styleUrls: ['./sauce-page.component.css'],
  providers: [MessageService]
})
export class SaucePageComponent implements OnInit {
  public sauces: Product[] = [];


  constructor(
    private burgerService: BurgerService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.getSauces();
  }

  getSauces(): void{
    this.burgerService.getSauces().subscribe({
      next: value => {
        this.sauces = value;
        console.log(this.sauces);
      }
    })
  }

  displayMessage(productName: string) {
    this.messageService.add({severity:'success', summary:`${productName} adăugat in coș`});
  }
}
