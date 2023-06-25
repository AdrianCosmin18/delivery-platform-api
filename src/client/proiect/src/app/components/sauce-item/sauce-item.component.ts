import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Product} from "../../interfaces/burger";
import {DialogService} from "primeng/dynamicdialog";
import {MessageService} from "primeng/api";
import {OrderItem} from "../../models/order-item";
import {Constant, Constants} from "../../constants/constants";
import {BurgerService} from "../../services/burger.service";
import * as itemAction from "../../store/cart/product.action";
import {Store} from "@ngrx/store";
import * as fromApp from "../../store/app.reducer";

@Component({
  selector: '.sauce-item',
  templateUrl: './sauce-item.component.html',
  styleUrls: ['./sauce-item.component.css'],
  providers: [MessageService]
})
export class SauceItemComponent implements OnInit {
  @Input() sauce!: Product;
  @Output() addedToCart = new EventEmitter<string>();
  // public count: number = 0;

  public isFavorite = false;
  public favoriteColor = 'p-button-secondary p-button-outlined';
  public favoriteTooltipMessage = 'Adauga la favorite';

  constructor(
    private dialogService: DialogService,
    private messageService: MessageService,
    private burgerService: BurgerService,
    private store:Store<fromApp.AppState>,
  ) { }

  ngOnInit(): void {}


  addToFavorites() {
    this.isFavorite = !this.isFavorite;
    this.favoriteColor = this.isFavorite ? 'p-button-danger' : 'p-button-secondary p-button-outlined';
    if (this.isFavorite) {
      // Adaugă produsul la lista de favorite
      this.favoriteTooltipMessage = 'Sterge de la favorite';
    } else {
      // Elimină produsul din lista de favorite
      this.favoriteTooltipMessage = 'Adauga la favorite';
    }
  }

  addCart(){

    const orderItem: OrderItem = new OrderItem(this.sauce.price, 1, this.sauce.name, '', '', 0, Constant.BURGER_SHOP);
    let itemsList = JSON.parse(localStorage.getItem(Constants.ITEM_LIST) || "[]");
    itemsList.push(orderItem);
    localStorage.setItem(Constants.ITEM_LIST, JSON.stringify(itemsList));
    this.store.dispatch(new itemAction.AddItems(orderItem));
    this.messageService.add({severity: 'success', summary: `${this.sauce.name} adăugat in coș`});
  }



  getImageUrl(imageId: number){
    return this.burgerService.getProductImageById(imageId);
  }
}
