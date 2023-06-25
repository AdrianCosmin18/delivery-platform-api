import {OrderItem} from "../models/order-item";

export interface OrderRequest{
  emailUser: string;
  productsInCart: OrderItem[];
  cardId: number;
  addressId: number;
  productsAmount: number;
  deliveryTax: number;
  tipsTax: number;
  totalAmount: number;
  commentsSection: string;
}
