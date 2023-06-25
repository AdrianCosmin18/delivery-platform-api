import { Component, OnInit } from '@angular/core';
import {Product} from "../../../../interfaces/burger";
import {BurgerService} from "../../../../services/burger.service";
import {Intolerance} from "../../../../constants/constants";
import {MegaMenuItem} from "primeng/api";

@Component({
  selector: 'app-intolerance-food',
  templateUrl: './intolerance-food.component.html',
  styleUrls: ['./intolerance-food.component.css']
})
export class IntoleranceFoodComponent implements OnInit {

  public items: MegaMenuItem[] = [];

  public burgers: Product[] = [];
  public filteredBurgers: Product[] = [];

  public fries: Product[] = [];
  public filteredFries: Product[] = [];

  public sauces: Product[] = [];
  public filteredSauces: Product[] = [];

  public intolerances: Intolerance[] = [
    {name: Intolerance.LACTOSE},
    {name: Intolerance.GLUTEN},
    {name: Intolerance.VEGETARIAN}
  ];
  public selectedIntolerance!: string;


  constructor(
    private burgerService: BurgerService
  ) { }

  ngOnInit(): void {
    this.getProducts();
    this.initItems();
  }

  initItems(){
    this.items = [
      {
        label: Intolerance.LACTOSE,
        items: [
          [
            {
              label:'Burger',
              items: [{label: 'produse', command: () => this.filterBurgers(Intolerance.LACTOSE)}],
            },
            {
              label: 'Cartofi',
              items: [{label: 'produse', command: () => this.filterFries(Intolerance.LACTOSE)}],
            },
            {
              label: 'Sosuri',
              items: [{label: 'produse', command: () => this.filterSauces(Intolerance.LACTOSE)}],
            }
          ]
        ]
      },
      {
        label: Intolerance.GLUTEN,
        items: [
          [
            {
              label:'Burger',
              items: [{label: 'produse', command: () => this.filterBurgers(Intolerance.GLUTEN)}],
            },
            {
              label: 'Cartofi',
              items: [{label: 'produse', command: () => this.filterFries(Intolerance.GLUTEN)}],
            },
            {
              label: 'Sosuri',
              items: [{label: 'produse', command: () => this.filterSauces(Intolerance.GLUTEN)}],
            }
          ]
        ]
      },
      {
        label: Intolerance.VEGETARIAN,
        items: [
          [
            {
              label:'Burger',
              items: [{label: 'produse', command: () => this.filterBurgers(Intolerance.VEGETARIAN)}],
            },
            {
              label: 'Cartofi',
              items: [{label: 'produse', command: () => this.filterFries(Intolerance.VEGETARIAN)}],
            },
            {
              label: 'Sosuri',
              items: [{label: 'produse', command: () => this.filterSauces(Intolerance.VEGETARIAN)}],
            }
          ]
        ]
      }
    ]
  }

  getProducts(): void{
    this.burgerService.getBurgers().subscribe({
      next: value => {
        this.burgers = value;
      },
      error: err => {
        alert("Something went wrong to get burgers");
      }
    });

    this.burgerService.getFries().subscribe({
      next: value => {
        this.fries = value;
      },
      error: err => {
        alert("Something went wrong to get fries");
      }
    });

    this.burgerService.getSauces().subscribe({
      next: value => {
        this.sauces = value;
      },
      error: err => {
        alert("Something went wrong to get sauces");
      }
    });
  }

  filterBurgers(type: string){
    if (type === Intolerance.LACTOSE) {
      this.filteredBurgers = this.burgers.filter(burger => !burger.containsLactose);
    }else if(type === Intolerance.GLUTEN){
      this.filteredBurgers = this.burgers.filter(burger => !burger.containsGluten);
    }else if(type === Intolerance.VEGETARIAN){
      this.filteredBurgers = this.burgers.filter(burger => burger.isVegetarian);
    }else{
      this.filteredBurgers = [];
    }

    this.filteredSauces = [];
    this.filteredFries = [];
  }

  filterFries(type: string){
    if (type === Intolerance.LACTOSE) {
      this.filteredFries = this.fries.filter(fries => !fries.containsLactose);
    }else if(type === Intolerance.GLUTEN){
      this.filteredFries = this.fries.filter(fries => !fries.containsGluten);
    }else if(type === Intolerance.VEGETARIAN){
      this.filteredFries = this.fries.filter(fries => fries.isVegetarian);
    }else{
      this.filteredFries = [];
    }

    this.filteredSauces = [];
    this.filteredBurgers = [];
  }

  filterSauces(type: string){
    if (type === Intolerance.LACTOSE) {
      this.filteredSauces = this.sauces.filter(sauce => !sauce.containsLactose);
    }else if(type === Intolerance.GLUTEN){
      this.filteredSauces = this.sauces.filter(sauce => !sauce.containsGluten);
    }else if(type === Intolerance.VEGETARIAN){
      this.filteredSauces = this.sauces.filter(sauce => sauce.isVegetarian);
    }else{
      this.filteredSauces = [];
    }

    this.filteredBurgers = [];
    this.filteredFries = [];
  }

}
