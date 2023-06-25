import { Component, OnInit } from '@angular/core';
import {Product} from "../../../interfaces/burger";
import {BurgerService} from "../../../services/burger.service";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {ActionIngredientsEnum, Constant, Constants, ExtraRemoveIngredientMessage} from "../../../constants/constants";
import {ActionIngredient} from "../../../interfaces/action-ingredient";

import * as fromApp from '../../../store/app.reducer';
import * as itemAction from '../../../store/cart/product.action';
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {OrderItem} from "../../../models/order-item";

@Component({
  selector: 'app-fries-item-options',
  templateUrl: './fries-item-options.component.html',
  styleUrls: ['./fries-item-options.component.css']
})
export class FriesItemOptionsComponent implements OnInit {
  public fries: Product = {
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
  public ingredientsDB: Product[] = [];//lista tuturor ingredientelor din bd pt acesti cartofi
  public friesIngredients: Product[] = [];//lista de ingr a burgerului fara carine si chifla la care nu se umbla deobicei
  public friesIngrCanEliminate: Product[] = [];//lista de ingr care pot fi eliminate care difera de cea de mai sus prin carnea care poate fi eliminata

  public friesCounter = 1;
  public piMinusColor = 'grey';
  public piMinusCursor = 'not-allowed';

  public friesPrice = this.fries.price;

  public extraIngredientMessage: ExtraRemoveIngredientMessage;
  public removeIngredientMessage: ExtraRemoveIngredientMessage;


  constructor(
    private burgerService: BurgerService,
    private dialogService: DialogService,
    public config: DynamicDialogConfig,
    private ref: DynamicDialogRef,
    private store:Store<fromApp.AppState>
  ) {
    this.extraIngredientMessage = ExtraRemoveIngredientMessage.EXTRA;
    this.removeIngredientMessage = ExtraRemoveIngredientMessage.REMOVE;
  }

  ngOnInit(): void {
    this.getAllExtraIngredients();
  }

  getFries(): void{
    this.fries = this.config.data.fries;
    this.image = this.getImageUrl(this.fries.imageId);
  }

  getAllExtraIngredients(): void{
    this.burgerService.getExtrasFries().subscribe({
      next: data => {
        this.ingredientsDB = data;
        this.getFries();
        this.createIngredientsLists();
        this.friesPrice = this.fries.price;
      }
    });
  }

  createIngredientsLists(): void{
    const currentFriesIngredients = this.fries.ingredients.split(",");

    if(!this.checkIfIngredientsFriesAreInGeneralListOfIngredients(currentFriesIngredients)){
      alert("Something is wrong with ingredients list");
    }

    this.ingredientsDB.forEach(i => {

      //cream lista de ingrediente a cartofilor care pot fi adaugate
      if(currentFriesIngredients.includes(i.name)){
        this.friesIngredients.push(i);
      }
      //creem lista de ingr a cartof care pot fi eliminate
      if(currentFriesIngredients.includes(i.name)){
        this.friesIngrCanEliminate.push(i);
      }
    });
    console.log("lista ingrediente cartofi curent: ", this.friesIngredients);
  }

  //functie care verifica daca fiecare ingredient din cartofi se afla in lista de ingrediente din bd pentru
  // a evita erori
  checkIfIngredientsFriesAreInGeneralListOfIngredients(friesIngredients: string[]): boolean{
    const list = this.ingredientsDB.map(product => product.name);

    return friesIngredients.every(element => {
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
            this.friesPrice += action.ingredient.price;
            this.friesPrice = Number(this.friesPrice.toFixed(2));
            break;
          }

          case ActionIngredientsEnum.REMOVE:{
            this.extraList.splice(this.extraList.indexOf(action.ingredient), 1);
            this.friesPrice -= action.ingredient.price;
            this.friesPrice = Number(this.friesPrice.toFixed(2));
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

  increaseFriesCounter(){
    this.friesCounter++;
    if(this.friesCounter > 1){
      this.piMinusCursor = 'pointer';
      this.piMinusColor = '#b5393a';
    }
  }

  decreaseFriesCounter() {
    if(this.friesCounter === 1){
      return;
    } else if (this.friesCounter === 2) {
      this.piMinusColor = 'grey';
      this.piMinusCursor = 'not-allowed';
    }
    this.friesCounter--;
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

    let extraIngrPrice = this.friesPrice - this.fries.price;
    extraIngrPrice = Number(extraIngrPrice.toFixed(2));

    const orderItem: OrderItem = new OrderItem(this.friesPrice, this.friesCounter, this.fries.name, extraIngr, lessIngr, extraIngrPrice, Constant.BURGER_SHOP);
    this.store.dispatch(new itemAction.AddItems(orderItem));

    let itemsList = JSON.parse(localStorage.getItem(Constants.ITEM_LIST) || "[]");
    itemsList.push(orderItem);
    localStorage.setItem(Constants.ITEM_LIST, JSON.stringify(itemsList));
    this.close(this.fries.name, this.friesCounter);
  }

  close(productName: string, productQuantity: number): void{
    this.ref.close({productName, productQuantity});
  }

  getImageUrl(imageId: number){
    return this.burgerService.getProductImageById(imageId);
  }
}
