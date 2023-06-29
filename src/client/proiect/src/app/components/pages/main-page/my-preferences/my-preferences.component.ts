import { Component, OnInit } from '@angular/core';
import {MegaMenuItem} from "primeng/api";
import {BurgerService} from "../../../../services/burger.service";
import {Product} from "../../../../interfaces/burger";
import {Constant, FoodType} from "../../../../constants/constants";

@Component({
  selector: 'app-my-preferences',
  templateUrl: './my-preferences.component.html',
  styleUrls: ['./my-preferences.component.css']
})
export class MyPreferencesComponent implements OnInit {
  public ingredientsBurgers: Product[] = [];
  public selectedBurgerIngredient: any[] = [];
  public burgers: Product[] = [];

  public ingredientsFries: Product[] = [];
  public selectedFriesIngredient: any[] = [];
  public fries: Product[] = [];


  public ingredientsSauces: Product[] = [];
  public selectedSauceIngredient: any[] = [];
  public sauces: Product[] = [];

  public activeTab = 0;



  constructor(
    private burgerService: BurgerService
  ) { }

  ngOnInit(): void {
    this.getIngredients();
  }

  getIngredients(){
    this.burgerService.getExtrasBurgers().subscribe({
      next: value => {
        this.ingredientsBurgers = value;
      },
      error: err => {
        alert("Something went wrong to get burger ingredients");
      }
    });

    this.burgerService.getExtrasFries().subscribe({
      next: value => {
        this.ingredientsFries = value;
      },
      error: err => {
        alert("Something went wrong to get fries ingredients");
      }
    });

    this.burgerService.getExtrasSauces().subscribe({
      next: value => {
        this.ingredientsSauces = value;
      },
      error: err => {
        alert("Something went wrong to get fries ingredients");
      }
    });
  }


  filterBurgers() {
    let ingredientList = '';
    this.selectedBurgerIngredient.forEach(ingr => {
      ingredientList += ingr + ",";
    });
    ingredientList = ingredientList.slice(0,-1);
    console.log(ingredientList);

    this.burgerService.getProductsByIngredients(FoodType.BURGER, ingredientList).subscribe({
      next: value => {
        this.burgers = value;
        console.log(this.burgers);
      },
      error: err => {
        alert("Smth went wrong to get burgers");
      }
    })
  }

  filterFries(){
    let ingredientList = '';
    this.selectedFriesIngredient.forEach(ingr => {
      ingredientList += ingr + ",";
    });
    ingredientList = ingredientList.slice(0,-1);
    console.log(ingredientList);

    this.burgerService.getProductsByIngredients(FoodType.FRIES, ingredientList).subscribe({
      next: value => {
        this.fries = value;
        console.log(this.fries);
      },
      error: err => {
        alert("Smth went wrong to get fries");
      }
    })
  }

  filterSauces(){
    let ingredientList = '';
    this.selectedSauceIngredient.forEach(ingr => {
      ingredientList += ingr + ",";
    });
    ingredientList = ingredientList.slice(0,-1);
    console.log(ingredientList);

    this.burgerService.getProductsByIngredients(FoodType.SAUCES, ingredientList).subscribe({
      next: value => {
        this.sauces = value;
        console.log(this.sauces);
      },
      error: err => {
        alert("Smth went wrong to get sauces");
      }
    })
  }

  onTabChange(event: any) {
    this.activeTab = event.index;
    console.log(this.activeTab);
  }
}
