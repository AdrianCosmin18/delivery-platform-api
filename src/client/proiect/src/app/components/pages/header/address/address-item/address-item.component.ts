import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {Address} from "../../../../../interfaces/address";
import {AddressUpdateFormComponent} from "../address-update-form/address-update-form.component";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {FormType} from "../../../../../constants/constants";
import {ConfirmationService} from "primeng/api";
import {CustomerService} from "../../../../../services/customer.service";
import {LoadingScreenService} from "../../../../../services/loading-screen.service";
import {Confirmation} from "primeng/api/confirmation";
import {Observable, Subject} from "rxjs";

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
    private loadingScreenService: LoadingScreenService
  ) {}

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
    const id = this.address.id;
    const street = this.address.street
    this.emitDeleteAddress.emit({id, street});
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
