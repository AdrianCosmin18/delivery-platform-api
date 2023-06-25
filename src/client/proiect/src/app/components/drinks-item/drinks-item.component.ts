import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Product} from "../../interfaces/burger";
import {DialogService} from "primeng/dynamicdialog";
import {MessageService} from "primeng/api";
import {DrinksItemOptionsComponent} from "./drinks-item-options/drinks-item-options.component";
import {BurgerService} from "../../services/burger.service";

@Component({
  selector: '.drink-item',
  templateUrl: './drinks-item.component.html',
  styleUrls: ['./drinks-item.component.css'],
  providers: [MessageService]
})
export class DrinksItemComponent implements OnInit {
  @Input() drink!: {key: string, value: Product[]};
  // @Input() extraIngredients: Product[] = [];
  @Output() drinkEvent = new EventEmitter();
  public descriptionDrink = '';

  public loggedIn: boolean = true;
  public isFavorite = false;
  public favoriteColor = 'p-button-secondary p-button-outlined';
  public favoriteTooltipMessage = 'Adauga la favorite';

  constructor(
    private dialogService: DialogService,
    private messageService: MessageService,
    private burgerService: BurgerService
  ) { }

  ngOnInit(): void {
    this.putDrinkDescription();
  }

  addCart(){
    const ref = this.dialogService.open(DrinksItemOptionsComponent, {
      header: this.drink.key,
      width: '40%',
      data: {
        //trimitem mai departe doar lista de de sucuri care contin acelasi nume unic
        drink: this.drink.value,
        // extraIngredients: this.extraIngredients
      }
    });

    ref.onClose.subscribe((productInfo: any) => {
      if(productInfo !== undefined && productInfo.productName !== null){
        this.messageService.add({severity: 'success', summary: `${productInfo.productQuantity} x ${productInfo.productName} adăugat in coș`});
      }
    });

    this.drinkEvent.emit(this.drink);
  }

  addToFavorites() {
    this.isFavorite = !this.isFavorite;
    this.favoriteColor = this.isFavorite ? 'p-button-danger' : 'p-button-secondary p-button-outlined';
    if (this.isFavorite) {
      // Adaugă produsul la lista de favorite
      this.favoriteTooltipMessage = 'Sterge de la favorite';
    } else {
      // Elimină produsul din lista de favorite
      this.favoriteTooltipMessage = 'Adauga la favorite';
    }
  }

  //din data membra 'description' a lui Product, luam textul ce se afla dupa prima vrigula
  // => descrierea sa efectiva (ce e inainte e marimea bauturii)
  putDrinkDescription(){
    const description = this.drink.value[0].description;//descrierea unui produs
    const firstComma = description.indexOf(",");
    this.descriptionDrink = description.substring(firstComma + 1);
  }

  getImageUrl(imageId: number){
    return this.burgerService.getProductImageById(imageId);
  }

}
