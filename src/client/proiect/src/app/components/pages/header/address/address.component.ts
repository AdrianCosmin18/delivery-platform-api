import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../../../interfaces/user";
import {config, Observable, Subscription} from "rxjs";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {CustomerService} from "../../../../services/customer.service";
import {FormBuilder} from "@angular/forms";
import {MessageService, PrimeNGConfig} from "primeng/api";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../store/app.reducer";
import {UserCredentials} from "../../../../interfaces/user-credentials";
import {Address} from "../../../../interfaces/address";
import {AddressUpdateFormComponent} from "./address-update-form/address-update-form.component";
import {FormType} from "../../../../constants/constants";

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css'],
  providers: [MessageService]
})
export class AddressComponent implements OnInit, OnDestroy {
  public email: string = '';
  public addresses!: Array<Address>;
  private auth$!: Observable<{ email: string; firstName: string; loggedIn: boolean }>;
  private subscription: Subscription = new Subscription();
  public lenAddresses = 0;
  public isAPlacedOrder: boolean = false;



  constructor(
    private dialogService: DialogService,
    private config: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private userService: CustomerService,
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private store: Store<fromApp.AppState>,
    private primengConfig: PrimeNGConfig

  ) { }

  ngOnInit(): void {
    this.getInfoFromParent();
    this.getInfoUser();
  }

  getInfoFromParent(){
    this.isAPlacedOrder = this.config.data.isPlaceOrder;
  }

  getInfoUser(){
    this.auth$ = this.store.select("auth");
    this.subscription = this.auth$.subscribe(value => {
      this.email = value.email;
      this.getAddresses();
    });
  }

  getAddresses(): void{
      this.userService.getUserAddresses(this.email).subscribe({
        next: value => {
          this.addresses = value;
          this.lenAddresses = this.addresses.length;
          this.primengConfig.ripple = true;
          console.log(this.addresses);
        },
        error: err => {
          this.messageService.add({severity:'error', summary: err.error.message, detail: 'Message Content'});
        }
      })
  }

  setAsMainAddress(addressId: number){
    this.userService.setAddressAsMainAddress(this.email, addressId).subscribe({
      next: () => {
        this.messageService.add({severity:'success', summary: `Ai o noua adresa principala`, detail: 'Message Content'});
        this.getAddresses();
      }
    })
  }

  updatedAddress(message: any){
    if(message){
      this.messageService.add({severity:'success', summary: `${message}`, detail: 'Message Content'});
    }
    this.getAddresses();
  }

  addNewAddress() {

    const ref = this.dialogService.open(AddressUpdateFormComponent, {
      header: 'Adauga adresa',
      width: '60%',
      data: {
        formType: FormType.ADD_FORM_ADDRESS,
      }
    });

    ref.onClose.subscribe((message) => {
      if(message){
        this.messageService.add({severity:'success', summary: `${message}`, detail: 'Message Content'});
      }
      this.getAddresses();
    });
  }

  deleteAddress({summary, detail}: any) {
    this.messageService.add({severity:'info', summary: summary, detail: detail});
    this.getAddresses();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
