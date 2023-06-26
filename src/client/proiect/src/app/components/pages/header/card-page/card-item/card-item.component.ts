import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Card} from "../../../../../interfaces/card";
import {Constant} from "../../../../../constants/constants";
import {cleanPackageJson} from "@angular/compiler-cli/ngcc/src/packages/build_marker";
import {ConfirmationService} from "primeng/api";
import {CustomerService} from "../../../../../services/customer.service";
import {DynamicDialogRef} from "primeng/dynamicdialog";
import {LoadingScreenService} from "../../../../../services/loading-screen.service";

@Component({
  selector: '.app-card-item',
  templateUrl: './card-item.component.html',
  styleUrls: ['./card-item.component.css']
})
export class CardItemComponent implements OnInit {
  @Input() card!: Card;
  @Input() email!: string;
  @Input() isAPlacedOrder!: boolean;
  @Output() emitMainCardId = new EventEmitter<number>();
  @Output() emitDeleteCard = new EventEmitter<string>();
  public src = "";
  public expiry = "";
  public textColor = '';
  public tooltipMessage = '';

  constructor(
    private confirmationService: ConfirmationService,
    private userService: CustomerService,
    public ref: DynamicDialogRef,
    private loadingScreenService: LoadingScreenService
  ) { }

  ngOnInit(): void {

    this.getCardType();
    if(this.card.isDefault){
      this.textColor = '';
      this.tooltipMessage = "Card principal";
    }else{
      this.textColor = 'p-button-outlined'
    }
  }

  getCardType(){
    if(this.card.cardType === Constant.MASTERCARD){
      this.src = './assets/Cards/mastercard.png';
    }else if (this.card.cardType === Constant.VISA){
      this.src = './assets/Cards/visa.png';
    }

    let month = this.card.fullExpiryDate[1];
    if('1' <= month && month <= '9'){
      month = `0${month}`;
    }
    // @ts-ignore
    this.expiry = `${this.card.fullExpiryDate[2]}.${month}.${this.card.fullExpiryDate[0]}`;
  }

  makeMainCard(){
    if(!this.card.isDefault){
      this.emitMainCardId.emit(this.card.id);
    }
  }

  deleteCard() {
    this.loadingScreenService.setLoading(true);

    this.userService.deleteCard(this.email, this.card.id).subscribe({
      next: () => {
        this.loadingScreenService.setLoading(false);
        const summary = `Cardul ${this.card.cardNumber} a fost șters cu succes`;
        this.emitDeleteCard.emit(summary);
      },
      error: err => {
        this.loadingScreenService.setLoading(false);
        alert(err);
      }
    })
  }

  selectCardToPlaceOrder() {
    this.ref.close(this.card);
  }
}
