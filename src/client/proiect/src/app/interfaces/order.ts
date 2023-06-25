export interface Order{
  id: number;
  courierId: number;
  amount: number;
  status: string;
  deliverTime?: string;
  placedOrderTime: string;
  paymentConfirmed: string;
  orderInPreparation: string;
  orderInDelivery: string;
  canceledOrder: string;
  productsAmount: number;
  deliveryTax: number;
  tipsTax: number;
  addressToString: string;
  city: string
  cardNumber: string;
  username: string;
  commentsSection: string
}
