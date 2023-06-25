import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Product} from "../../interfaces/burger";
import {OrderItem} from "../../models/order-item";
import {BurgerService} from "../../services/burger.service";
import {Constant, Constants} from "../../constants/constants";
import {Store} from "@ngrx/store";
import * as fromApp from "../../store/app.reducer";
import {Observable, Subscription} from "rxjs";
import * as Action from '../../store/cart/product.action';
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'app-cart-item',
  templateUrl: './cart-item.component.html',
  styleUrls: ['./cart-item.component.css']
})
export class CartItemComponent implements OnInit {
  @Input() item: OrderItem = new OrderItem(-1,-1,'','','', 0, Constant.BURGER_SHOP);
  @Output() productEvent = new EventEmitter<string>();

  public image: any;
  public extraIngr: string[] = [];
  public extraPrice: number = 0;
  public product: Product = {
    id: -1,
    name: '',
    price: -1,
    type: '',
    description: '',
    ingredients: '',
    imageId: -1,
    restaurantName: '',
    containsGluten: false,
    containsLactose: false,
    isVegetarian: false
  };

  constructor(
    private burgerService: BurgerService,
    private store:Store<fromApp.AppState>,
  ) { }

  ngOnInit(): void {
    this.getProductByName();
    this.getExtraIngr();
  }


  getExtraIngr(){
    const list = this.item.extraIngredients.split(",");
    list.forEach(ingrName => {
      this.extraIngr.push(ingrName);
    });
    console.log("getExtraIngr " + this.product.name + " :" + this.extraIngr);
  }

  getProductByName(){
    this.burgerService.getProductByName(this.item.productName).subscribe({
      next: data => {
        this.product = data;
        this.extraPrice = Number((this.item.price - this.product.price).toFixed(2));
        this.image = this.getImageUrl(this.product.imageId);
      }
    });
  }

  removeFromCart(){
    let itemsList = JSON.parse(localStorage.getItem(Constants.ITEM_LIST) || "[]");
    // const initialSize = itemsList.length;
    let hasRemovedItem = false;
    itemsList = itemsList.filter((item: OrderItem) => {
      if(this.equals(this.item, item) && !hasRemovedItem){
        hasRemovedItem = true;
        return false;
      }else{
        return true;
      }
    });
    localStorage.setItem(Constants.ITEM_LIST, JSON.stringify(itemsList));
    // if(initialSize > itemsList.length){
    //   this.productEvent.emit('Produs șters');
    // }

    this.store.dispatch(new Action.RemoveItem(this.item));
    this.productEvent.emit('Produs șters'); //folosit pentru place-order
  }

  decreaseQuantity(){
    let changed = false;
    if(this.item.quantity === 1){
      this.removeFromCart();
    }else{

      let itemsList = JSON.parse(localStorage.getItem(Constants.ITEM_LIST) || "[]");
      itemsList = itemsList.map((item: OrderItem) => {
        if(this.equals(item, this.item) && !changed){
          changed = true;
          return { ...item, quantity: item.quantity - 1 };
        } else{
          return item;
        }
      });
      localStorage.setItem(Constants.ITEM_LIST, JSON.stringify(itemsList));
      // if(changed){
      //   this.productEvent.emit("Cantitate produs modificată");
      // }

      this.store.dispatch(new Action.DecreaseQuantity(this.item));
      this.productEvent.emit("Cantitate produs modificată"); //folosit pentru place-order

    }
  }

  increaseQuantity(){
    let changed = false;
    let itemsList = JSON.parse(localStorage.getItem(Constants.ITEM_LIST) || "[]");
    itemsList = itemsList.map((item: OrderItem) => {
      if(this.equals(item, this.item) && !changed){
        changed = true;
        return { ...item, quantity: item.quantity + 1 };
      } else{
        return item;
      }
    });
    localStorage.setItem(Constants.ITEM_LIST, JSON.stringify(itemsList));
    // if(changed){
    //   this.productEvent.emit("Cantitate produs modificată");
    // }

    this.store.dispatch(new Action.IncreaseQuantity(this.item));
    this.productEvent.emit("Cantitate produs modificată"); //folosit pentru place-order
  }

  orderItemTotalPrice(): number{
    return Number((this.item.price * this.item.quantity).toFixed(2));
  }

  equals(i1: OrderItem, i2: OrderItem): boolean{
    return i1.price === i2.price &&
      i1.quantity === i2.quantity &&
      i1.productName === i2.productName &&
      i1.extraIngredients === i2.extraIngredients &&
      i1.lessIngredients === i2.lessIngredients;

  }

  getImageUrl(imageId: number){
    return this.burgerService.getProductImageById(imageId);
  }
}
