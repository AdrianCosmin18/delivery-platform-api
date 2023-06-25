import {Component, OnInit} from '@angular/core';
import {Product} from "../../../interfaces/burger";
import {BurgerService} from "../../../services/burger.service";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {ActionIngredient} from "../../../interfaces/action-ingredient";
import {ActionIngredientsEnum, Constant, Constants, ExtraRemoveIngredientMessage} from "../../../constants/constants";
import {OrderItem} from "../../../models/order-item";

import * as fromApp from '../../../store/app.reducer';
import * as itemAction from '../../../store/cart/product.action';
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-burger-item-options',
  templateUrl: './burger-item-options.component.html',
  styleUrls: ['./burger-item-options.component.css'],
  providers: [MessageService]
})
export class BurgerItemOptionsComponent implements OnInit {
  public burger: Product = {
    id: -1,
    name: "",
    price: 0,
    ingredients: "",
    imageId: -1,
    description: "",
    type: "",
    restaurantName:"",
    containsGluten: false,
    containsLactose: false,
    isVegetarian: false
  };
  public image: any;

  public extraList: Product[] = [];
  public lessList: Product[] = [];
  public ingredientsDB: Product[] = []; //lista tuturor ingredientelor din bd
  public burgerIngredients: Product[] = [];//lista de ingr a burgerului fara carine si chifla la care nu se umbla deobicei
  public burgerIngrCanEliminate: Product[] = [];//lista de ingr care pot fi eliminate care difera de cea de mai sus prin carnea care poate fi eliminata

  public burgerCounter = 1;
  public piMinusColor = 'grey';
  public piMinusCursor = 'not-allowed';

  public burgerPrice = this.burger.price;

  public extraIngredientMessage: ExtraRemoveIngredientMessage;
  public removeIngredientMessage: ExtraRemoveIngredientMessage;

  constructor(
    private burgerService: BurgerService,
    private dialogService: DialogService,
    private config: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private store:Store<fromApp.AppState>) {

    this.extraIngredientMessage = ExtraRemoveIngredientMessage.EXTRA;
    this.removeIngredientMessage = ExtraRemoveIngredientMessage.REMOVE;
  }

  ngOnInit(): void {
    this.getAllExtraIngredients();
  }

  getAllExtraIngredients(): void{
    this.burgerService.getExtrasBurgers().subscribe({
      next: data => {
        this.ingredientsDB = data;
        this.getBurger();
        this.createIngredientsLists();
        this.burgerPrice = this.burger.price;
      }
    });
  }

  getBurger(): void{
    this.burger = this.config.data.burger;
    this.image = this.getImageUrl(this.burger.imageId);
  }

  createIngredientsLists(): void{
    const currentBurgerIngredients = this.burger.ingredients.split(",");

    if(!this.checkIfIngredientsBurgerAreInGeneralListOfIngredients(currentBurgerIngredients)){
      alert("Something is wrong with ingredients list");
    }

    this.ingredientsDB.forEach(i => {

      //cream lista de ingrediente a burgerului care pot fi adaugate
      if(currentBurgerIngredients.includes(i.name) && (i.price > 0)){
        this.burgerIngredients.push(i);
      }
      //creem lista de ingr a burg care pot fi eliminate
      if(currentBurgerIngredients.includes(i.name) && (i.price > -1)){
        this.burgerIngrCanEliminate.push(i);
      }
    });
    console.log("lista ingrediente burger curent: ", this.burgerIngredients);
  }

  //functie care verifica daca fiecare ingredient din burger se afla in lista de ingrediente din bd pentru a evita erori
  checkIfIngredientsBurgerAreInGeneralListOfIngredients(burgerIngredients: string[]): boolean{
    const list = this.ingredientsDB.map(product => product.name);

    return burgerIngredients.every(element => {
      return list.includes(element);
    })
  }

  addOrRemoveIngredient(event: any): void{
    const action = event as ActionIngredient;

    switch (action.message) {

      case ExtraRemoveIngredientMessage.EXTRA:{

        switch (action.action){

          case ActionIngredientsEnum.ADD:{
            this.extraList.push(action.ingredient);
            this.burgerPrice += action.ingredient.price;
            this.burgerPrice = Number(this.burgerPrice.toFixed(2));
            break;
          }

          case ActionIngredientsEnum.REMOVE:{
            this.extraList.splice(this.extraList.indexOf(action.ingredient), 1);
            this.burgerPrice -= action.ingredient.price;
            this.burgerPrice = Number(this.burgerPrice.toFixed(2));
            console.log(this.extraList);
            break;
          }
        }

        console.log(this.extraList, " extraList");
        break;
      }

      case ExtraRemoveIngredientMessage.REMOVE:{

        switch (action.action){

          case ActionIngredientsEnum.ADD:{
            this.lessList.push(action.ingredient);
            break;
          }

          case ActionIngredientsEnum.REMOVE:{
            this.lessList.splice(this.extraList.indexOf(action.ingredient), 1);
            break;
          }
        }

        console.log(this.lessList, " lessList");
        break;
      }
    }
  }


  increaseBurgerCounter(){
    this.burgerCounter++;
    if(this.burgerCounter > 1){
      this.piMinusCursor = 'pointer';
      this.piMinusColor = '#b5393a';
    }
  }

  decreaseBurgerCounter() {
    if(this.burgerCounter === 1){
      return;
    } else if (this.burgerCounter === 2) {
      this.piMinusColor = 'grey';
      this.piMinusCursor = 'not-allowed';
    }
    this.burgerCounter--;
  }

  roundNumber(number: any){
    return Number(number);
  }

  addToCart(){

    console.log(localStorage.getItem("itemList"));
    let extraIngr = '';
    this.extraList.forEach(ingr => {
      extraIngr += ingr.name + ',';
    });
    extraIngr = extraIngr.slice(0, -1);
    console.log('extraingr: '+ extraIngr);

    let lessIngr = '';
    this.lessList.forEach(ingr => {
      lessIngr += ingr.name + ',';
    });
    lessIngr = lessIngr.slice(0, -1);
    console.log('lessIngr: ' + lessIngr);

    let extraIngrPrice = this.burgerPrice - this.burger.price;
    extraIngrPrice = Number(extraIngrPrice.toFixed(2));

    const orderItem: OrderItem = new OrderItem(this.burgerPrice, this.burgerCounter, this.burger.name, extraIngr, lessIngr, extraIngrPrice, Constant.BURGER_SHOP);
    this.store.dispatch(new itemAction.AddItems(orderItem));

    let itemsList = JSON.parse(localStorage.getItem(Constants.ITEM_LIST) || "[]");
    itemsList.push(orderItem);
    localStorage.setItem(Constants.ITEM_LIST, JSON.stringify(itemsList));
    this.close(this.burger.name, this.burgerCounter);
  }

  close(productName: string, productQuantity: number): void{
    this.ref.close({productName, productQuantity});
  }

  getImageUrl(imageId: number){
    return this.burgerService.getProductImageById(imageId);
  }
}
