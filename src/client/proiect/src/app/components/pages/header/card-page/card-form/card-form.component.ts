import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Address} from "../../../../../interfaces/address";
import {City} from "../../../../../interfaces/city";
import {Observable} from "rxjs";
import {ErrorMessages, FormType} from "../../../../../constants/constants";
import {Card} from "../../../../../interfaces/card";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {CityService} from "../../../../../services/city.service";
import {CustomerService} from "../../../../../services/customer.service";
import {Store} from "@ngrx/store";
import * as fromApp from "../../../../../store/app.reducer";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-card-form',
  templateUrl: './card-form.component.html',
  styleUrls: ['./card-form.component.css']
})
export class CardFormComponent implements OnInit {
  public form!: FormGroup;
  public card!: Card;
  public months!: any;
  public years!: any;
  private auth$!: Observable<{ email: string; }>;
  public monthSelected!: string;
  public yearSelected!: number;

  constructor(
    private formBuilder: FormBuilder,
    private config: DynamicDialogConfig,
    private ref: DynamicDialogRef,
    private userService: CustomerService,
    private store: Store<fromApp.AppState>,
    private messageService: MessageService) { }

  ngOnInit(): void {

    this.initForm();
    this.initMonthDropdown();
    this.initYearDropdown();
  }

  initForm(){
    this.form = this.formBuilder.group({
      cardType: [''],
      cardNumber: ['',
        [
          Validators.required,
          Validators.pattern(/^[245]\d{3}-\d{4}-\d{4}-\d{4}$/)
        ]
      ],
      cardHolderName: ['',
        [
          Validators.required,
          Validators.pattern(/^[A-Za-z\s]{6,50}$/)
        ]
      ],
      expiryMonth: ['', [Validators.required]],
      expiryYear: ['', [Validators.required]],
      securityCode: ['',
        [
          Validators.required,
          Validators.pattern(/^\d{3}$/)
        ]
      ],
      isDefault: [false]
    })
  }

  initMonthDropdown(){
    this.months = [
      {name: 'Ianuarie', value: 1},
      {name: 'Februarie', value: 2},
      {name: 'Martie', value: 3},
      {name: 'Aprilie', value: 4},
      {name: 'Mai', value: 5},
      {name: 'Iunie', value: 6},
      {name: 'Iulie', value: 7},
      {name: 'August', value: 8},
      {name: 'Septembrie', value: 9},
      {name: 'Octombrie', value: 10},
      {name: 'Noiembrie', value: 11},
      {name: 'Decembrie', value: 12},


    ]
  }

  initYearDropdown(){
    this.years = [
      {value: 2023},
      {value: 2024},
      {value: 2025},
      {value: 2026},
      {value: 2027},
      {value: 2028},
      {value: 2029},
      {value: 2030},
      {value: 2031},
      {value: 2032},
      {value: 2033},
      {value: 2034},
      {value: 2035},
    ]
  }

  addCard(){

    let number = this.form.get("cardNumber")?.value;
    number = number.replaceAll('-', '');
    console.log(number);

    let expMonth = this.form.get("expiryMonth")?.value;
    if( '1' <= expMonth && expMonth <= '9'){
      expMonth = `0${expMonth}`;
    }
    let expYear = this.form.get("expiryYear")?.value;
    let exp = `${expYear}-${expMonth}`;
    console.log(exp);



    let newCard = {
      id: -1,
      cardNumber: number,
      cardHolderName: this.form.get("cardHolderName")?.value,
      securityCode: this.form.get("securityCode")?.value,
      expiryDate: exp,
      isDefault: this.form.get("isDefault")?.value,
    };
    console.log(newCard);

    this.auth$ = this.store.select("auth");
    this.auth$.subscribe(value => {
      const email = value.email;
      this.userService.addCard(email, newCard as Card).subscribe({
        next: response => {
          const message = 'Card nou adăugat';
          this.cancelDialogService(message);
        },
        error: err => {
          if(err === ErrorMessages.USER_CARD_ALREADY_EXISTS_EXCEPTION){
            this.messageService.add({severity: 'error', summary: `Ai deja acest card înregistrat`});
          }
        }
      })
    })

  }

  cancelDialogService(message: string){
    this.ref.close(message);
  }
}
