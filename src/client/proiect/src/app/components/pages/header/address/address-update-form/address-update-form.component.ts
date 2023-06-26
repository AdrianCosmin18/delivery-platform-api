import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {City} from "../../../../../interfaces/city";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {Address} from "../../../../../interfaces/address";
import {CityService} from "../../../../../services/city.service";
import {CustomerService} from "../../../../../services/customer.service";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../../store/app.reducer";
import {Observable} from "rxjs";
import {MessageService} from "primeng/api";
import {ErrorMessages, FormType} from "../../../../../constants/constants";
import {LoadingScreenService} from "../../../../../services/loading-screen.service";

@Component({
  selector: 'app-address-update-form',
  templateUrl: './address-update-form.component.html',
  styleUrls: ['./address-update-form.component.css'],
  providers: [MessageService]
})
export class AddressUpdateFormComponent implements OnInit {
  public form!: FormGroup;
  public address!: Address;
  public cities: City[] = [];
  public citySelected: string = '';
  private auth$!: Observable<{ email: string; }>;
  public formType!: FormType;



  constructor(
    private formBuilder: FormBuilder,
    public config: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private cityService: CityService,
    private userService: CustomerService,
    private store: Store<fromApp.AppState>,
    private messageService: MessageService,
    private loadingScreenService: LoadingScreenService
  ) { }

  ngOnInit(): void {

    this.getDataFromParentComponent();
  }

  initForm(){
    this.form = this.formBuilder.group({
      cities: ['', [Validators.required]],
      street: ['', [Validators.required, Validators.minLength(4)]],
      number: ['', [Validators.required]],
      block: [''],
      staircase: [''],
      floor: [''],
      apartment: [''],
      interphone: [''],
      details: [''],
      isDefault: [false]
    });

    this.cityService.subjectCities.subscribe({
      next: value => {
        this.cities = value;
      }
    })
  }

  getDataFromParentComponent(){
    this.formType = this.config.data.formType;
    if(this.formType === FormType.ADD_FORM_ADDRESS){
      this.initForm();
    } else if(this.formType === FormType.UPDATE_FORM_ADDRESS){
      this.initForm();
      this.getCurrentAddress();
    }
  }

  getCurrentAddress(){
    this.address = this.config.data.address;
    this.putAddressInForm();
  }

  putAddressInForm(): void{
    this.form.get("cities")?.setValue(this.address.cityName);
    this.citySelected = this.address.cityName
    this.form.patchValue({
      street: this.address.street,
      number: this.address.number,
      block: this.address.block,
      staircase: this.address.staircase,
      floor: this.address.floor,
      apartment: this.address.apartment,
      interphone: this.address.interphone,
      details: this.address.details,
      isDefault: this.address.isDefault
    });
  }

  updateAddress() {

    let updatedAddress = {
      street: this.form.get("street")?.value,
      number: this.form.get("number")?.value,
      block: this.form.get("block")?.value,
      staircase: this.form.get("staircase")?.value,
      floor: this.form.get("floor")?.value,
      apartment: this.form.get("apartment")?.value,
      interphone: this.form.get("interphone")?.value,
      details: this.form.get("details")?.value,
      isDefault: this.form.get("isDefault")?.value,
      cityName: this.form.get("cities")?.value
    };

    console.log(updatedAddress);

    this.auth$ = this.store.select("auth");
    this.auth$.subscribe(value => {
      const email = value.email;
        this.loadingScreenService.setLoading(true);
        this.userService.updateAddress(email, this.address.id, updatedAddress as Address).subscribe({
          next: () => {
            this.loadingScreenService.setLoading(false);
            const message = 'Adresă actualizată cu succes';
            this.cancelDialogService(message);
          },

          error: err => {
            this.loadingScreenService.setLoading(false);
            this.messageService.add({severity: 'error', summary: `${err}`})
          }
        })
    })
  }

  addAddress(){

    let newAddress = {
      id: -1,
      street: this.form.get("street")?.value,
      number: this.form.get("number")?.value,
      block: this.form.get("block")?.value,
      staircase: this.form.get("staircase")?.value,
      floor: this.form.get("floor")?.value,
      apartment: this.form.get("apartment")?.value,
      interphone: this.form.get("interphone")?.value,
      details: this.form.get("details")?.value,
      isDefault: this.form.get("isDefault")?.value,
      cityName: this.form.get("cities")?.value
    };

    console.log(newAddress);

    this.auth$ = this.store.select("auth");
    this.auth$.subscribe(value => {
      const email = value.email;
      this.loadingScreenService.setLoading(true);
      this.userService.addAddress(email, newAddress as Address).subscribe({
        next: () => {
          this.loadingScreenService.setLoading(false);
          const message = 'Adresă nouă adaugată';
          this.cancelDialogService(message);
        },

        error: err => {
          this.loadingScreenService.setLoading(false);
          if(err === ErrorMessages.USER_ALREADY_OWN_ADDRESS_EXCEPTION){
            this.messageService.add({severity: 'error', summary: `Ai deja această adresă înregistrată`})}
        }
      })
    })
  }

  cancelDialogService(message: string){
    this.ref.close(message);
  }
}
