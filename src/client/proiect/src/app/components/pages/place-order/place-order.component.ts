import {Component, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {MenuItem, MessageService} from "primeng/api";
import {Observable, Subscription} from "rxjs";
import {Store} from "@ngrx/store";
import * as Action from '../../../store/cart/product.action';
import * as fromApp from "../../../store/app.reducer";
import {Address} from "../../../interfaces/address";
import {CustomerService} from "../../../services/customer.service";
import {AddressComponent} from "../header/address/address.component";
import {DialogService} from "primeng/dynamicdialog";
import {Card} from "../../../interfaces/card";
import {CardPageComponent} from "../header/card-page/card-page.component";
import {CustomTipsComponent} from "./custom-tips/custom-tips.component";
import {OrderItem} from "../../../models/order-item";
import {Constants} from "../../../constants/constants";
import {Route, Router} from "@angular/router";
import {NotificationService} from "../../../services/notification.service";
import {OrderRequest} from "../../../interfaces/OrderRequest";
import {LoadingDialogComponent} from "../../loading-dialog/loading-dialog.component";
import {LoadingScreenService} from "../../../services/loading-screen.service";

@Component({
  selector: 'app-place-order',
  templateUrl: './place-order.component.html',
  styleUrls: ['./place-order.component.css']
})
export class PlaceOrderComponent implements OnInit, OnDestroy {
  private auth$!: Observable<{ email: string; }>;
  private subscriptions: Subscription = new Subscription();
  private itemList$!: Observable<{ itemList: OrderItem[]}>;

  private email: string = '';
  public hasMainAddress!: boolean;
  public hasSelectedAddress!: boolean;
  public address!: Address;
  public hasMainCard!: boolean;
  public hasSelectedCard!: boolean;

  public card!: Card;

  public toStringAddress: string = '';
  public toStringCard: string = '';

  public noAddressMessage: string = 'Vă rugăm să selectați o adresă';
  public noCardMessage: string = 'Vă rugăm să selectați un card';

  public classButtonForTips = 'p-button-raised p-button-text';
  public tip2 = '';
  public tip4 = '';
  public tip5 = '';
  public tipCustom = '';
  public productsPrice = 0;
  public transportPrice = 4.99;
  public tipPrice = 0;
  public totalPrice = 0;

  public items: OrderItem[] = [];

  public loading = false;
  public commentText: string = '';




  constructor(
    private messageService: MessageService,
    private store: Store<fromApp.AppState>,
    private userService: CustomerService,
    public dialogService: DialogService,
    private router: Router,
    private notificationService: NotificationService,
    private loadingScreenService: LoadingScreenService
  ) { }

  ngOnInit(): void {
    this.loadingScreenService.setLoading(true);
    this.getInfo();
    this.initCartList();
    this.totalSum();

    setTimeout(() => {
      this.loadingScreenService.setLoading(false);
    }, 2000);
  }

  getInfo(){
    this.auth$ = this.store.select("auth");
    this.subscriptions = this.auth$.subscribe(value => {
      this.email = value.email;
      this.hasAnyMainAddress();
      this.hasAnyMainCard();
    })
  }

  hasAnyMainAddress(){
    this.userService.hasUserMainAddress(this.email).subscribe({
      next: value => {
        if(value){
          this.hasMainAddress = true;
          this.getUserMainAddress();
        }else{
          this.hasMainAddress = false;
        }
        this.hasSelectedAddress = this.hasMainAddress;
      },
      error: err => {
        alert(err.error.message);
      }
    })
  }

  getUserMainAddress(){
    this.userService.getUserMainAddress(this.email).subscribe({
      next: value => {
        if(value){
          this.address = value;
          this.initToStringAddress();
        }
      },
      error: err => {
        alert(err.error.message);
      }
    })
  }

  hasAnyMainCard(){
    this.userService.hasUserMainCard(this.email).subscribe({
      next: value => {
        if(value){
          this.hasMainCard = true;
          this.getUserMainCard();
        }else{
          this.hasMainCard = false;
        }
        this.hasSelectedCard = this.hasMainCard;
      },
      error: err => {
        alert(err.error.message);
      }
    })
  }

  getUserMainCard(){
    this.userService.getUserMainCard(this.email).subscribe({
      next: value => {
        if(value){
          this.card = value;
          this.initToStringCard();
        }
      },
      error: err => {
        alert(err.error.message);
      }
    })
  }

  initToStringAddress(){
    this.toStringAddress =  this.address.cityName + ", " + this.address.street + ", "+ this.address.number;
  }

  initToStringCard(){
    this.toStringCard = this.card.cardNumber + " - " + this.card.cardType;
  }

  selectAddress() {
    const ref = this.dialogService.open(AddressComponent, {
      header: 'Adresele mele',
      width: '60%',
      data: {
        isPlaceOrder: true
      }
    });

    ref.onClose.subscribe((addr: Address) => {
      if(addr){
        this.address = addr;
        this.hasSelectedAddress = true;
        this.initToStringAddress();
      }
    })
  }

  selectCard(){
    const ref = this.dialogService.open(CardPageComponent, {
      header: 'Cardurile mele',
      width: '50%',
      data: {
        isPlaceOrder: true
      }
    });

    ref.onClose.subscribe((crd: Card) => {
      if(crd){
        this.card = crd;
        this.hasSelectedCard = true;
        this.initToStringCard();
      }
    })
  }

  clickTipsButton2Lei(){
    this.tip2 = this.classButtonForTips;
    this.tip4 = '';
    this.tip5 = '';
    this.tipCustom = '';

    this.tipPrice = 2;
    this.totalSum();
  }

  clickTipsButton4Lei() {
    this.tip2 = '';
    this.tip4 = this.classButtonForTips;
    this.tip5 = '';
    this.tipCustom = '';

    this.tipPrice = 4;
    this.totalSum();
  }

  clickTipsButton5Lei() {
    this.tip2 = '';
    this.tip4 = '';
    this.tip5 = this.classButtonForTips;
    this.tipCustom = '';

    this.tipPrice = 5;
    this.totalSum();
  }

  clickTipsButtonCustomeTip() {
    const ref = this.dialogService.open(CustomTipsComponent, {
      width: '420px'
    });
    ref.onClose.subscribe((value) => {
      if (value) {
        let nr = parseInt(value);
        this.tip2 = '';
        this.tip4 = '';
        this.tip5 = '';
        this.tipCustom = this.classButtonForTips;

        this.tipPrice = nr;
        this.totalSum();
      }
    });
  }

  initCartList(){

    this.itemList$ = this.store.select("items");
    this.subscriptions = this.itemList$.subscribe({
      next: value => {
        this.items = value.itemList;
        console.log(this.items);
      }
    })
    // this.items = JSON.parse(localStorage.getItem(Constants.ITEM_LIST) || "[]");
    // console.log(this.items);
  }

  modifyCart(productName: string): void{
    this.initCartList();
    this.totalSum();
  }

  totalAmountProducts(): number{
    let sum = 0;
    for(let p of this.items){
      sum += Number((p.price * p.quantity).toFixed(2));
    }
    return Number(sum.toFixed(2));
  }

  totalSum(){
    let totProd = this.totalAmountProducts();
    this.totalPrice = totProd + this.tipPrice;
    if(totProd < 100){
      this.totalPrice += this.transportPrice;
      this.totalPrice = Number(this.totalPrice.toFixed(2));
    }
  }


  load() {

    if(!this.hasSelectedCard){
      this.messageService.add({key:'notSelected', severity:'error', summary: 'Selectează cardul pentru plată !'})
    }else if(!this.hasSelectedAddress){
      this.messageService.add({key:'notSelected', severity:'error', summary: 'Selectează adresa de livrare !'})
    }else{

      this.loadingScreenService.setLoading(true);
      this.userService.placeOrder(this.createOrderRequest()).subscribe({
        next: () => {
          this.loadingScreenService.setLoading(false);
          this.loading = true;
          setTimeout(() => {
            this.loading = false;

            if(this.router.url.includes("/placeOrder")){
              this.router.navigate(['/mainPage']);
            }
            this.store.dispatch(new Action.EmptyList());
            this.notificationService.onInfo('placedOrder', 'Comandă plasată cu succes', 'Pentru mai multe detalii acesează Istoric comenzi');
          }, 3000);
        }, error: err => {
          this.loadingScreenService.setLoading(false);
          this.notificationService.onError('placedOrder', 'A apărut o eroare la plasarea comenzii');
        }
      });

    }
  }

  createOrderRequest(){
    let orderRequest: OrderRequest = {
      emailUser: this.email,
      productsInCart: this.items,
      cardId: this.card.id,
      addressId: this.address.id,
      productsAmount: this.totalAmountProducts(),
      deliveryTax: this.transportPrice,
      tipsTax: this.tipPrice,
      totalAmount: this.totalPrice,
      commentsSection: this.commentText
    };

    return orderRequest;
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

}
