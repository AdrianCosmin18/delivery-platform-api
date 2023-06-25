import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Product} from "../../../../interfaces/burger";
import {ActionIngredient} from "../../../../interfaces/action-ingredient";
import {ActionIngredientsEnum, ExtraRemoveIngredientMessage} from "../../../../constants/constants";

@Component({
  selector: '.app-ingredient',
  templateUrl: './ingredient.component.html',
  styleUrls: ['./ingredient.component.css']
})
export class IngredientComponent implements OnInit {
  @Input() ingredient: Product = {
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
  @Input() message!: ExtraRemoveIngredientMessage;
  @Output() action = new EventEmitter<(ActionIngredient)>();
  public iconStyle!: string;
  public colorIcon!: string;
  public isAdded: boolean = false;


  constructor() { }

  ngOnInit(): void {
    this.iconStyle = 'pi-plus-circle';
    this.colorIcon = '#ffc244';
  }

  add(): void{
    this.isAdded = !this.isAdded;
    this.iconStyle = this.isAdded ? 'pi-check': 'pi-plus-circle';
    this.colorIcon = this.isAdded ? 'green' : '#ffc244';

    // in cazul in care adaug un ingredient extra si apoi ma rasgandesc
    // trimit un mesaj daca ingredientul respectiv va fi adaugat sau nu
    if(this.isAdded){
      console.log(this.ingredient.name, " added");
      this.action.emit(new ActionIngredient(this.ingredient, ActionIngredientsEnum.ADD, this.message));
    }else{
      console.log(this.ingredient.name, " removed");
      this.action.emit(new ActionIngredient(this.ingredient, ActionIngredientsEnum.REMOVE, this.message));
    }
  }

}
