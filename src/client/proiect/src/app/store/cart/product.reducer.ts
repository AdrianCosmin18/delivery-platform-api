import {OrderItem} from "../../models/order-item";
import * as Actions from './product.action';

export interface State{
  itemList: OrderItem[];
}

const initialState: State = {
  itemList: []
}

export function itemsReducer(
  state: State = initialState,
  action: Actions.ItemListAction
){
  switch (action.type){

    case Actions.ADD_ITEM:
      return{
        ...state,
        itemList: [...state.itemList, action.item],
      }

    case Actions.GET_ITEMS:
      return{
        ...state,
        itemList: action.items,
      }

    case Actions.REMOVE_ITEM:

      let itemsListRemove = state.itemList;
      let hasRemovedItem = false;
      itemsListRemove = itemsListRemove.filter((item: OrderItem) => {
        if(equals(action.item, item) && !hasRemovedItem){ //cat timp gasesc produse care sunt egale cu cel dat de sters si daca nu a fost sters niciunul
          hasRemovedItem = true;
          return false;
        }else{
          return true;
        }
      });

      return {
        ...state,
        itemList: [...itemsListRemove]
      }

    case Actions.DECREASE_QUANTITY:

      let changedDecrease = false;
      let itemsListDecrease = state.itemList;
      itemsListDecrease = itemsListDecrease.map((item: OrderItem) => {
        if (equals(item, action.item) && !changedDecrease) {
          changedDecrease = true;
          return {...item, quantity: item.quantity - 1};
        } else {
          return item;
        }
      });

      return{
        ...state,
        itemList: [...itemsListDecrease]
      }

    case Actions.INCREASE_QUANTITY:

      let changedIncrease = false;
      let itemsListIncrease = state.itemList;
      itemsListIncrease = itemsListIncrease.map((item: OrderItem) => {
        if(equals(item, action.item) && !changedIncrease){
          changedIncrease = true;
          return { ...item, quantity: item.quantity + 1 };
        } else{
          return item;
        }
      });

      return{
        ...state,
        itemList: [...itemsListIncrease]
      }

    case Actions.EMPTY_LIST:
      return{
        ...state,
        itemList: []
      }

    default:
      return state;
  }
}

export function equals(i1: OrderItem, i2: OrderItem): boolean{
  return i1.price === i2.price &&
    i1.quantity === i2.quantity &&
    i1.productName === i2.productName &&
    i1.extraIngredients === i2.extraIngredients &&
    i1.lessIngredients === i2.lessIngredients;

}
