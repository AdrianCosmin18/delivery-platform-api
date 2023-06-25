import {Action} from "@ngrx/store";
import {OrderItem} from "../../models/order-item";


export const GET_ITEMS = 'GET ITEMS';
export const ADD_ITEM = 'ADD ITEM';
export const REMOVE_ITEM = "REMOVE ITEM";
export const DECREASE_QUANTITY = "DECREASE QUANTITY";
export const INCREASE_QUANTITY = "INCREASE QUANTITY";
export const EMPTY_LIST = "EMPTY LIST";

export class GetItems implements Action{

  readonly type = GET_ITEMS;
  constructor(public items: OrderItem[]) {
  }
}

export class AddItems implements Action{

  readonly type = ADD_ITEM;
  constructor(public item: OrderItem) {
  }
}

export class RemoveItem implements Action{
  readonly type = REMOVE_ITEM;

  constructor(public item: OrderItem) {
  }
}

export class DecreaseQuantity implements Action{
  readonly type = DECREASE_QUANTITY;
  constructor(public item: OrderItem) {
  }
}

export class IncreaseQuantity implements Action{
  readonly type = INCREASE_QUANTITY;
  constructor(public item: OrderItem) {
  }
}

export class EmptyList implements Action{
  readonly type = EMPTY_LIST
}


export type ItemListAction = GetItems | AddItems | RemoveItem | DecreaseQuantity | IncreaseQuantity | EmptyList;

