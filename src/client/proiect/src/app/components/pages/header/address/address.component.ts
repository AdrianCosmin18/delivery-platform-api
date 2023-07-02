import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../../../interfaces/user";
import {config, Observable, Subscription} from "rxjs";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {CustomerService} from "../../../../services/customer.service";
import {FormBuilder} from "@angular/forms";
import {ConfirmationService, MessageService, PrimeNGConfig} from "primeng/api";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../store/app.reducer";
import {Address} from "../../../../interfaces/address";
import {AddressUpdateFormComponent} from "./address-update-form/address-update-form.component";
import {FormType} from "../../../../constants/constants";
import {LoadingScreenService} from "../../../../services/loading-screen.service";

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
    // private primengConfig: PrimeNGConfig,
    private loadingScreenService: LoadingScreenService,
    private confirmationService: ConfirmationService,
    private customerService: CustomerService,

  ) { }

  ngOnInit(): void {
    this.loadingScreenService.setLoading(true);
    this.getInfoFromParent();
    this.getInfoUser();

    setTimeout(() =>{
      this.loadingScreenService.setLoading(false);
    }, 1000);
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
          // this.primengConfig.ripple = true;
          // this.loadingScreenService.setLoading(false);

        },
        error: err => {
          // this.loadingScreenService.setLoading(false);
          this.messageService.add({severity:'error', summary: err.error.message});
        }
      })
  }

  setAsMainAddress(addressId: number){
    this.loadingScreenService.setLoading(true);
    this.userService.setAddressAsMainAddress(this.email, addressId).subscribe({
      next: () => {
        this.loadingScreenService.setLoading(false);
        this.messageService.add({severity:'success', summary: `Ai o nouă adresă principală`});
        this.getAddresses();
      }
    })
  }

  updatedAddress(message: any){
    if(message){
      this.messageService.add({severity:'success', summary: `${message}`});
    }
    this.getAddresses();
  }

  addNewAddress() {

    const ref = this.dialogService.open(AddressUpdateFormComponent, {
      header: 'Adaugă adresă',
      width: '60%',
      data: {
        formType: FormType.ADD_FORM_ADDRESS,
      }
    });

    ref.onClose.subscribe((message) => {
      if(message){
        this.messageService.add({severity:'success', summary: `${message}`});
      }
      this.getAddresses();
    });
  }

  deleteAddress({id, street}: any) {
    this.confirmationService.confirm({
      message: 'Sunteți sigur că doriți să ștergeți această adresă?',
      header: 'Șterge adresa',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Da',
      rejectLabel: 'Nu',
      key: 'delAddress',
      accept: () => {
        this.loadingScreenService.setLoading(true);
        this.customerService.deleteAddress(this.email, Number(id)).subscribe({

          next: () => {
            this.getAddresses();
            this.loadingScreenService.setLoading(false);
            const summary = 'Adresa a fost ștearsă cu succes';
            this.messageService.add({severity:'info', summary: summary, detail: street});

          },
          error: err => {
            this.loadingScreenService.setLoading(false);
            alert(err);
          }
        })
      }
    })

    // this.messageService.add({severity:'info', summary: summary, detail: detail});
    this.getAddresses();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
