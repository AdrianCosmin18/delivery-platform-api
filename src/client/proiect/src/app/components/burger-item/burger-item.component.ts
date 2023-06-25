import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { Product } from "../../interfaces/burger";
import {Confirmation, ConfirmationService, MessageService} from "primeng/api";
import {DialogService} from "primeng/dynamicdialog";
import {BurgerItemOptionsComponent} from "./burger-item-options/burger-item-options.component";
import {BurgerService} from "../../services/burger.service";

@Component({
  selector: '.burger-item',
  templateUrl: './burger-item.component.html',
  styleUrls: ['./burger-item.component.css'],
  providers: [MessageService]
})
export class BurgerItemComponent implements OnInit {
  @Input() burger!: Product;
  @Output() burgerEvent = new EventEmitter();

  public loggedIn: boolean = true;
  public isFavorite = false;
  public favoriteColor = 'p-button-secondary p-button-outlined';
  public productInCart: number = 0;
  public favoriteTooltipMessage = 'Adaugă la favorite';
  public imageData: string = '';

  constructor(
    private dialogService: DialogService,
    private messageService: MessageService,
    private burgerService: BurgerService
  ) {
  }

  ngOnInit(): void {
  }

  addToCart() {

    const ref = this.dialogService.open(BurgerItemOptionsComponent, {
      header: this.burger.name,
      width: '40%',
      data: {
        burger: this.burger,
      }
    });

    ref.onClose.subscribe((productInfo: any) => {
      if (productInfo !== undefined && productInfo.productName !== null) {
        this.messageService.add({
          severity: 'success',
          summary: `${productInfo.productQuantity} x ${productInfo.productName} adăugat in coș`
        });
      }
    });

    // this.productInCart++;
    this.burgerEvent.emit(this.burger);
  }


  addToFavorites() {
    this.isFavorite = !this.isFavorite;
    this.favoriteColor = this.isFavorite ? 'p-button-danger' : 'p-button-secondary p-button-outlined';
    if (this.isFavorite) {
      // Adaugă produsul la lista de favorite
      this.favoriteTooltipMessage = 'Șterge de la favorite';
    } else {
      // Elimină produsul din lista de favorite
      this.favoriteTooltipMessage = 'Adaugă la favorite';
    }
  }


  getImageUrl(imageId: number){
    return this.burgerService.getProductImageById(imageId);
  }

}
