import {Product} from "./burger";
import {ActionIngredientsEnum, ExtraRemoveIngredientMessage} from "../constants/constants";

export class ActionIngredient{
  public ingredient: Product;
  public action: ActionIngredientsEnum;
  public message: ExtraRemoveIngredientMessage;


  constructor(ingredient: Product, action: ActionIngredientsEnum, message: ExtraRemoveIngredientMessage) {
    this.ingredient = ingredient;
    this.action = action;
    this.message = message;
  }
}
