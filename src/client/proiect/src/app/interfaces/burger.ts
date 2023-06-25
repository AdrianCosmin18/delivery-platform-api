export interface Product{
  id: number;
  name: string;
  price: number;
  type: string;
  description: string;
  ingredients: string;
  imageId: number;
  restaurantName: string;
  containsLactose: boolean;
  containsGluten: boolean;
  isVegetarian: boolean;
}
