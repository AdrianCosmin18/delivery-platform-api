import { Component, OnInit } from '@angular/core';
import {Product} from "../../../interfaces/burger";
import {BurgerService} from "../../../services/burger.service";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {ActionIngredientsEnum, Constant, Constants, ExtraRemoveIngredientMessage} from "../../../constants/constants";
import {ActionIngredient} from "../../../interfaces/action-ingredient";
import {OrderItem} from "../../../models/order-item";
import * as itemAction from "../../../store/cart/product.action";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../store/app.reducer";

@Component({
  selector: 'app-drinks-item-options',
  templateUrl: './drinks-item-options.component.html',
  styleUrls: ['./drinks-item-options.component.css']
})
export class DrinksItemOptionsComponent implements OnInit {
  public drinkList: Product[] = [];
  public optionSelected: any;
  public optionDrink: number[] = [];
  public chosenSize: boolean = false;

  public extraList: Product[] = [];
  public ingredientsDB: Product[] = []; //lista tuturor ingredientelor din bd

  public extraIngredientMessage: ExtraRemoveIngredientMessage;

  public drinkCounter = 1;
  public piMinusColor = 'grey';
  public piMinusCursor = 'not-allowed';
  public drinkPrice = -1;

  public image: any;


  constructor(
    private burgerService: BurgerService,
    private dialogService: DialogService,
    private config: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private store:Store<fromApp.AppState>,
  ) {
    this.extraIngredientMessage = ExtraRemoveIngredientMessage.EXTRA;
  }

  ngOnInit(): void {
    this.getDrinks();
    this.initSizesDrinks();
    this.drinkPrice = this.drinkList[0].price;
    this.image = this.getImageUrl(this.drinkList[0].imageId);
  }

  getDrinkTypePrice(): void{
    if(this.chosenSize){
      this.drinkPrice = this.drinkList[this.optionSelected].price;
      this.extraList.forEach(ingr => {
        this.drinkPrice += ingr.price;
      })
    }else{
      this.chosenSize = true;
      this.drinkPrice = this.drinkList[this.optionSelected].price;
    }
  }

  getDrinks(): void{
    this.drinkList = this.config.data.drink;
    this.burgerService.subjectExtraIngredientsDrinks.subscribe({
      next: value => {
        this.ingredientsDB = value;
        console.log(this.ingredientsDB);
      }
    })
    // this.ingredientsDB = this.config.data.extraIngredients;
    console.log(this.drinkList);
    console.log(this.ingredientsDB);
  }

  //nr masurilor de bauturi pe care le are o bautura: cola fiind mica,medie,mare => 0,1,2
  initSizesDrinks(){
    for (let i = 0; i < this.drinkList.length; i++){
      this.optionDrink.push(i);
    }
  }

  increaseDrinkCounter(){
    this.drinkCounter++;
    if(this.drinkCounter > 1){
      this.piMinusCursor = 'pointer';
      this.piMinusColor = '#b5393a';
    }
  }

  decreaseDrinkCounter() {
    if(this.drinkCounter === 1){
      return;
    } else if (this.drinkCounter === 2) {
      this.piMinusColor = 'grey';
      this.piMinusCursor = 'not-allowed';
    }
    this.drinkCounter--;
  }

  addExtraIngredient(event: any): void{

    const action = event as ActionIngredient;

    switch (action.message){

      case ExtraRemoveIngredientMessage.EXTRA:{

        switch (action.action){

          case ActionIngredientsEnum.ADD:{
            this.extraList.push(action.ingredient);
            this.drinkPrice += action.ingredient.price;
            this.drinkPrice = Number(this.drinkPrice.toFixed(2));
            break;
          }

          case ActionIngredientsEnum.REMOVE:{
            this.extraList.splice(this.extraList.indexOf(action.ingredient), 1);
            this.drinkPrice -= action.ingredient.price;
            this.drinkPrice = Number(this.drinkPrice.toFixed(2));
            break;
          }
        }
      }
    }
    console.log(this.extraList);
  }

  roundNumber(number: any){
    return Number(number);
  }

  addToCart(): void{

    console.log(localStorage.getItem(Constants.ITEM_LIST));
    let extraIngr = '';
    this.extraList.forEach(ingr => {
      extraIngr += ingr.name + ',';
    });
    extraIngr = extraIngr.slice(0, -1);
    console.log('extraingr: '+ extraIngr);

    let extraIngrPrice = this.drinkPrice - this.drinkList[this.optionSelected].price;
    extraIngrPrice = Number(extraIngrPrice.toFixed(2));

    const orderItem: OrderItem = new OrderItem(
      this.drinkPrice,
      this.drinkCounter,
      this.drinkList[this.optionSelected].name,
      extraIngr,
      '',
      extraIngrPrice,
      Constant.BURGER_SHOP
    );
    this.store.dispatch(new itemAction.AddItems(orderItem));

    let itemsList = JSON.parse(localStorage.getItem(Constants.ITEM_LIST) || "[]");
    itemsList.push(orderItem);
    localStorage.setItem(Constants.ITEM_LIST, JSON.stringify(itemsList));
    this.close(this.drinkList[this.optionSelected].name, this.drinkCounter);

  }

  close(productName: string, productQuantity: number): void{
    this.ref.close({productName, productQuantity});
  }

  getImageUrl(imageId: number){
    return this.burgerService.getProductImageById(imageId);
  }
}
