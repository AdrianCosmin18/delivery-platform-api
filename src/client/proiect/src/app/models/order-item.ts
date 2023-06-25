export class OrderItem{

  public price: number;
  public quantity: number;
  public extraIngredients: string;
  public lessIngredients: string;
  public extraIngredientsPrice: number;
  public productName: string;
  public restaurantName: string;


  constructor(
    price: number,
    quantity: number,
    productName: string,
    extraIngredients: string,
    lessIngredients: string,
    extraIngredientsPrice: number,
    restaurantName: string
  ) {
    this.price = price;
    this.quantity = quantity;
    this.productName = productName;
    this.extraIngredients = extraIngredients;
    this.lessIngredients = lessIngredients;
    this.extraIngredientsPrice = extraIngredientsPrice;
    this.restaurantName = restaurantName;
  }
}
