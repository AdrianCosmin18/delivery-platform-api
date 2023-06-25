export enum FoodType{
  BURGER = "burger",
  FRIES = "fries",
  DRINK = "drinks",
  EXTRAS_BURGER = "extras-burger",
  EXTRAS_FRIES = "extras-fries",
  EXTRAS_DRINK = "extras-drink",
  SAUCES = "sauces",
  EXTRAS_SAUCE = "extras-sauce"
}

// daca e cu + sau vazut
export enum ActionIngredientsEnum{
  ADD = "Add ingredient",
  REMOVE = "Remove ingredient"
}

// ne referim pe ce tip de acordeon suntem
export enum ExtraRemoveIngredientMessage{
  EXTRA = "Extra",
  REMOVE = "Remove"
}

export enum Constants{
  ITEM_LIST = 'itemList',
  QUANTITY = 'quantity',
  USER_CREDENTIALS = "userCredentials"
}

export enum Roles{
  ROLE_USER = "ROLE_USER",
  ROLE_ADMIN = "ROLE_ADMIN",}

export enum FormType{
  ADD_FORM_ADDRESS = "Add new address",
  UPDATE_FORM_ADDRESS = "Update address"
}

export enum ErrorMessages{
  USER_ALREADY_OWN_ADDRESS_EXCEPTION = "User already has this address",
  USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION = "Already exists a user with this email",
  USER_ALREADY_EXISTS_PHONE_EXCEPTION = "This phone number belongs to someone else",
  USER_CARD_ALREADY_EXISTS_EXCEPTION = "Already added this card",
}

export class Constant{
  public static MASTERCARD: string = "Mastercard";
  public static VISA: string = "Visa";
  public static BURGER_SHOP: string = 'BurgerShop';
}

export class Intolerance{
  public name: string;

  public static LACTOSE: string = "Lactoză";
  public static GLUTEN: string = "Gluten";
  public static VEGETARIAN: string = "Vegetarian";


  constructor(name: string) {
    this.name = name;
  }
}


export class OrderStatus{

  public static PLACED_ORDER = "Placed order";
  public static CANCELED_ORDER = "Canceled order";
  public static PAYMENT_CONFIRMED = "Payment confirmed";
  public static ORDER_IN_PREPARATION = "Order in preparation";
  public static ORDER_IN_DELIVERY = "Order is being delivered";
  public static ORDER_DELIVERED = "Order delivered";

  public static COMENZI_PLASATE = "Comenzi plasate";
  public static COMENZI_ANULATE = "Comenzi anulate";
  public static PLATA_CONFIRMATA = "Plati confimrate";
  public static COMENZI_IN_PREPARARE = "Comenzi in preparare";
  public static COMENZI_IN_CURS_DE_LIVRARE = "Comenzi in curs de livrare";
  public static COMENZI_LIVRATE = "Comenzi livrate";
}

export class PublicRoutes{

  public static login = '/login';
  public static register = '/register';
  public static mainPage = '/mainPage';
  public static burgers = '/burgers';
  public static fries = '/fries';
  public static drinks = '/drinks';
  public static sauces = '/sauces';
  public static myPreferences = '/myPreferences';
  public static intolerance = '/intolerance';

  public static publicRoutes: Array<string> = Object.values(PublicRoutes);
}
