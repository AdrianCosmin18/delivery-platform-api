import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Address} from "../../../../../interfaces/address";
import {AddressUpdateFormComponent} from "../address-update-form/address-update-form.component";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {FormType} from "../../../../../constants/constants";
import {ConfirmationService} from "primeng/api";
import {CustomerService} from "../../../../../services/customer.service";
import {LoadingScreenService} from "../../../../../services/loading-screen.service";

@Component({
  selector: '.address-item',
  templateUrl: './address-item.component.html',
  styleUrls: ['./address-item.component.css']
})
export class AddressItemComponent implements OnInit {
  @Input() address!: Address;
  @Input() email!: string;
  @Input() isAPlacedOrder!: boolean;
  @Output() emitMainAddressId = new EventEmitter<any>();
  @Output() emitUpdateAddress = new EventEmitter<any>();
  @Output() emitDeleteAddress = new EventEmitter<any>();

  public favoriteColor = 'p-button-secondary p-button-outlined';
  public tooltipMessage = '';

  constructor(
    private dialogService: DialogService,
    private config: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private confirmationService: ConfirmationService,
    private customerService: CustomerService,
    private loadingScreenService: LoadingScreenService
  ) { }

  ngOnInit(): void {
    if(this.address.isDefault){
      this.favoriteColor = 'p-button-danger';
      this.tooltipMessage = "adresa principala";

    }else{
      this.favoriteColor = 'p-button-secondary p-button-outlined';
    }
  }

  makeMainAddress(){
    if(!this.address.isDefault){
      this.emitMainAddressId.emit(this.address.id);
    }
  }

  deleteAddress() {
    this.confirmationService.confirm({
      message: 'Sunteți sigur că doriți să ștergeți această adresă?',
      header: 'Șterge adresa',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Da',
      rejectLabel: 'Nu',
      key: 'delAddress',
      accept: () => {
        this.loadingScreenService.setLoading(true);
        this.customerService.deleteAddress(this.email, this.address.id).subscribe({
          next: () => {
            this.loadingScreenService.setLoading(false);
            const summary = 'Adresa a fost ștearsă cu succes';
            const detail = this.address.street;
            this.emitDeleteAddress.emit({summary, detail});
          },
          error: err => {
            this.loadingScreenService.setLoading(false);
            alert(err);
          }
        })
      }
    })
  }

  openUpdateAddressForm(): void{

    const ref = this.dialogService.open(AddressUpdateFormComponent, {
      header: 'Modifică adresa',
      width: '60%',
      data: {
        formType: FormType.UPDATE_FORM_ADDRESS,
        address: this.address
      }
    });

    ref.onClose.subscribe((message) => {
        this.emitUpdateAddress.emit(message);
    });

  }

  selectAddressToPlaceOrder() {
    this.ref.close(this.address);
  }
}
