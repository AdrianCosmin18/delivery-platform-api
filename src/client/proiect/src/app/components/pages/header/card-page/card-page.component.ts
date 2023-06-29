import { Component, OnInit } from '@angular/core';
import {Card} from "../../../../interfaces/card";
import {CustomerService} from "../../../../services/customer.service";
import {Observable, Subscription} from "rxjs";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../store/app.reducer";
import {MessageService} from "primeng/api";
import {AddressUpdateFormComponent} from "../address/address-update-form/address-update-form.component";
import {FormType} from "../../../../constants/constants";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {CardFormComponent} from "./card-form/card-form.component";
import {LoadingScreenService} from "../../../../services/loading-screen.service";

@Component({
  selector: 'app-card-page',
  templateUrl: './card-page.component.html',
  styleUrls: ['./card-page.component.css']
})
export class CardPageComponent implements OnInit {
  public cards: Card[] = [];
  public email: string = '';
  private auth$!: Observable<{ email: string; }>;
  private authSubscription: Subscription = new Subscription();
  public isAPlacedOrder: boolean = false;



  constructor(
    private userService: CustomerService,
    private store: Store<fromApp.AppState>,
    private messageService: MessageService,
    private dialogService: DialogService,
    private config: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private loadingScreenService: LoadingScreenService
  ) { }

  ngOnInit(): void {
    this.loadingScreenService.setLoading(true);
    this.getInfoFromParent();
    this.getCards();
  }

  getInfoFromParent(){
    this.isAPlacedOrder = this.config.data.isPlaceOrder;
  }

  getCards(): void{

    this.auth$ = this.store.select("auth");
    this.authSubscription = this.auth$.subscribe(value => {
      this.email = value.email;
      this.userService.getUserCards(this.email).subscribe({
        next: response => {
          this.cards = response;
          console.log(this.cards);
          this.loadingScreenService.setLoading(false);
        }
      })
    })
  }

  addNewCard() {

    const ref = this.dialogService.open(CardFormComponent, {
      header: 'Adaugă card',
      width: '50%',
      height: '60%'
    });

    ref.onClose.subscribe((message) => {
      if(message){
        this.messageService.add({severity:'success', summary: `${message}`});
      }
      this.getCards();
    });
  }

  setAsMainCard(cardId: number) {
    this.loadingScreenService.setLoading(true);
    this.userService.setCardAsMainCard(this.email, cardId).subscribe({
      next: () => {
        this.loadingScreenService.setLoading(false);
        this.messageService.add({severity:'success', summary: `Ai un nou card principal`});
        this.getCards();
      }
    })
  }

  onDeleteCard(summary: string){
    this.messageService.add({severity:'info', summary: summary});
    this.getCards();
  }
}
