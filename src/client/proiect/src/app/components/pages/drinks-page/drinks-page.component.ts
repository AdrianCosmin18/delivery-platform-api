import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {BurgerService} from "../../../services/burger.service";
import {Product} from "../../../interfaces/burger";
import {CustomerService} from "../../../services/customer.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-drinks-page',
  templateUrl: './drinks-page.component.html',
  styleUrls: ['./drinks-page.component.css'],
  providers: [MessageService]
})
export class DrinksPageComponent implements OnInit {
  public allDrinks: Product[] = [];
  public drinkMap: Map<string, Product[]> = new Map<string, Product[]>();
  public drinkArray = [];
  public individualDrinkName: Set<string> = new Set<string>();
  public extraIngredientsDrink: Product[] = [];
  private customerId!: number;

  constructor(
    private burgerService: BurgerService,
    private customerService: CustomerService,
    private messageService: MessageService) { }

  ngOnInit(): void {

    this.burgerService.getDrinks().subscribe({
      next: (response) => {
        this.allDrinks = response;
        this.formDrinkMap();

        for (let [key, value] of this.drinkMap.entries()) {
          // @ts-ignore
          this.drinkArray.push({ key: key, value: value });
        }
      }
    });
    this.getExtraIngredientsDrinks();
  }

  //gandim stocarea bauturilor sub forma de : 'nume,proprietate' <=> 'cola,400ml' sau 'fuzetea,lamaie'
  formDrinkMap(){
    //obtinem numele bauturii unice care este inainte de prima vrigula din nume => adica din 'cola,400ml' => 'cola'
    this.individualDrinkName = new Set(this.allDrinks.map(drink => drink.name.split(",")[0]));

    //daca fiecare nume unic de bautura se gaseste in in numele, facem un map cu lista de bauturi care contine numele unic in nume
    //ex: cola: [{'Cola,mica', '350ml'}, {'Cola,mare', '600ml'}]

    this.individualDrinkName.forEach(value => {
      let d = this.allDrinks.filter(drink => drink.name.split(",")[0] === value);
      console.log(d);
      this.drinkMap.set(value, d);
    });
    // console.log(this.individualDrinkName);
    // console.log("driank map: " + this.drinkMap);
    // this.drinkMap.forEach((value, key) => {
    //   console.log(`${key}: ${value}\n`);
    // })
  }


  getExtraIngredientsDrinks(): void{
    this.burgerService.getExtrasDrinks().subscribe({
      next: data => {
        this.extraIngredientsDrink = data;
      }
    });
  }
}
